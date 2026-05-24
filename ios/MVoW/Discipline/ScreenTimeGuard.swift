import Foundation
#if canImport(FamilyControls)
import FamilyControls
import ManagedSettings
import DeviceActivity
#endif

/// iOS analog of Android's `FocusGuardService`.
/// Uses Apple's Screen Time / FamilyControls APIs to enforce focus sessions.
///
/// IMPORTANT iOS limitations vs. Android:
///   - We CANNOT detect "user opened Instagram" in real-time and overlay our UI.
///   - We CAN ask iOS to block selected apps for a time window.
///   - The user must approve FamilyControls authorization once per device.
///   - Custom Negotiation overlay (Android Hard Lock) is impossible at OS level —
///     we use the system "Screen Time blocked" sheet instead, with deep-link
///     back to our app for the negotiation flow.
///
/// Requires entitlement: com.apple.developer.family-controls
@available(iOS 16.0, *)
final class ScreenTimeGuard: ObservableObject {

    @Published var isAuthorized: Bool = false
    @Published var activeShield: Bool = false

    /// Apply Family Controls authorization. Triggers a system sheet on first run.
    func requestAuthorization() async {
        #if canImport(FamilyControls)
        do {
            try await AuthorizationCenter.shared.requestAuthorization(for: .individual)
            await MainActor.run { self.isAuthorized = true }
        } catch {
            print("FamilyControls authorization failed: \(error)")
        }
        #endif
    }

    /// Begin a hard-lock session: block the user-picked apps until `endAt`.
    /// `selection` comes from FamilyActivityPicker (user picks which apps to block).
    @MainActor
    func startSession(
        selection: FamilyActivitySelectionStub,
        endAt: Date
    ) {
        #if canImport(FamilyControls)
        let store = ManagedSettingsStore(named: .init("MVoW.session"))

        // Block applications & categories the user selected
        store.shield.applications = selection.applicationTokens
        store.shield.applicationCategories = .specific(selection.categoryTokens)
        store.shield.webDomainCategories = .specific(selection.categoryTokens)
        activeShield = true

        // Schedule end via DeviceActivityCenter
        let center = DeviceActivityCenter()
        let schedule = DeviceActivitySchedule(
            intervalStart: Calendar.current.dateComponents([.hour, .minute], from: Date()),
            intervalEnd: Calendar.current.dateComponents([.hour, .minute], from: endAt),
            repeats: false
        )
        do {
            try center.startMonitoring(.init("MVoW.activeSession"), during: schedule)
        } catch {
            print("DeviceActivity scheduling failed: \(error)")
        }
        #endif
    }

    /// End the session and lift the shield.
    @MainActor
    func endSession() {
        #if canImport(FamilyControls)
        let store = ManagedSettingsStore(named: .init("MVoW.session"))
        store.clearAllSettings()
        activeShield = false

        let center = DeviceActivityCenter()
        center.stopMonitoring([.init("MVoW.activeSession")])
        #endif
    }
}

/// Wrapper around FamilyActivitySelection so non-iOS builds compile.
struct FamilyActivitySelectionStub {
    #if canImport(FamilyControls)
    let applicationTokens: Set<ApplicationToken>
    let categoryTokens: Set<ActivityCategoryToken>
    #else
    let applicationTokens: Set<String> = []
    let categoryTokens: Set<String> = []
    #endif
}
