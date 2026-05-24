# M·VoW · iOS

> SwiftUI implementation matching the Android Compose app.
> Discipline enforcement via Apple's **FamilyControls / Screen Time** APIs.

---

## Status

**Skeleton scaffolded.** No `.xcodeproj` yet — you create one in Xcode and drop these files in.

| Done | Screen / Component |
|---|---|
| ✅ | Theme system (`MentorColors`, `MentorFonts`) — matches Android exactly |
| ✅ | `MentorOrb`, `OrnamentDivider`, `BrandSeal`, `VoidBackdrop` |
| ✅ | `WelcomeView` — onboarding cinematic intro with MNSM logo |
| ✅ | `HomeView` — daily dashboard (greeting, streak, next session, stats, nav) |
| ✅ | `ScreenTimeGuard` — FamilyControls/ManagedSettings hard-lock plumbing |
| ⏳ | Goal, Voice Commitment, Permissions, Done — onboarding rest |
| ⏳ | Hard Lock view, Negotiation, Daily Brief, Reflection |
| ⏳ | Calendar, Profile, Notifications, Settings, Chat |

---

## Setup (one-time, in Xcode)

1. **Open Xcode** (15+).
2. **File → New → Project → iOS → App**
   - Product name: `MVoW`
   - Interface: `SwiftUI`
   - Language: `Swift`
   - Storage: `None`
   - Save in: `c:\Users\User\Desktop\Focus AI\ios\` (it will create `MVoW.xcodeproj` next to the existing `MVoW/` folder)
3. **Delete** Xcode's auto-generated `ContentView.swift` and `MVoWApp.swift` (we already have them).
4. **Drag** the existing `ios/MVoW/` folder structure into the Xcode project navigator. Choose **"Create groups"** (not folder references). Make sure all `.swift` files are added to the `MVoW` target.
5. **Add the MNSM logo** to Assets:
   - Open `Assets.xcassets` in Xcode.
   - Drag `docs/v2/preview/assets/mnsm-logo.png` in. Name it `mnsm-logo`.
6. **Add fonts** to the project:
   - Download from Google Fonts: **Cinzel** (Bold), **Cormorant Garamond** (Medium, Medium Italic, SemiBold Italic), **JetBrains Mono** (Medium).
   - Drag the `.ttf` files into a `Fonts/` group. Check "Add to target".
   - In `Info.plist`, add **`Fonts provided by application`** array with each filename:
     - `Cinzel-Bold.ttf`
     - `CormorantGaramond-Medium.ttf`
     - `CormorantGaramond-MediumItalic.ttf`
     - `CormorantGaramond-SemiBoldItalic.ttf`
     - `JetBrainsMono-Medium.ttf`
7. **Enable FamilyControls** entitlement:
   - Project settings → Signing & Capabilities → **+ Capability** → **Family Controls**.
   - You'll need a paid Apple Developer account; FamilyControls requires Apple's approval for App Store distribution but works on your own device with provisioning.
8. **Build & Run** on simulator or device (iOS 16+).

---

## Architecture vs. Android

| Layer | Android | iOS |
|---|---|---|
| UI | Jetpack Compose | SwiftUI |
| Theme | `MentorColors.kt` | `MentorColors.swift` (identical hex values) |
| State | Hilt + ViewModel | `@StateObject` + `ObservableObject` |
| Persistence | Room | SwiftData (TODO) or Core Data |
| App blocking | Accessibility + WindowManager overlay | **FamilyControls + ManagedSettings** |
| Always-on | Foreground Service | DeviceActivityMonitor extension |
| Voice | Android STT/TTS | `Speech` + `AVSpeechSynthesizer` |

---

## iOS limitations — be honest with the user

The app **cannot** do these things on iOS the way it does on Android:

- **Cannot** detect "user opened Instagram" in real-time and overlay our custom UI.
- **Cannot** block apps without going through Apple's Screen Time sheet.
- **Cannot** prevent uninstallation (Device Admin doesn't exist on iOS).

What we **can** do:
- Block a user-selected set of apps for a chosen window via `ManagedSettingsStore.shield.applications`.
- Tell `DeviceActivityCenter` to monitor a window and notify us at start/end.
- Provide a deep link back from the Screen Time blocked sheet → our Negotiation flow (via `ShieldConfiguration` extension — TODO).
- All "soft" UX screens (Daily Brief, Goal, Voice Commitment, Profile, Settings, Calendar, Notifications, Chat, Reflection) work identically to Android.

---

## Next steps

1. Port remaining onboarding screens: `GoalView`, `VoiceCommitmentView`, `PermissionsView`, `DoneView`.
2. Port mentor screens: `NegotiationView`, `ChatView`, `DailyBriefView`, `ReflectionView`, `StuckView`, `WeeklyReviewView`, `RestModeView`.
3. Port utility screens: `CalendarView`, `ProfileView`, `NotificationsView`, `SettingsView`, `AddSessionView`, `UrgentTimeView`, `OnlineClassView`.
4. Build a `DeviceActivityMonitor` app extension for shield-on / shield-off events.
5. Build a `ShieldConfiguration` app extension for custom Negotiation entry from blocked-app sheet.
6. Wire shared models (Session, ScheduledSession, etc.) — keep parity with Android's Room entities.

---

🛡 **MNSM · AI Specialist**
