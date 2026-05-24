import SwiftUI

/// Central state machine for one running task.
/// Mirrors Android `DayFlowScreen.kt` and `docs/v2/preview/day-flow.html`.
struct DayFlowView: View {
    enum State { case ready, running, breakRest, done, tired }
    enum Energy { case strong, medium, tired }

    @SwiftUI.State private var state: State = .ready
    @SwiftUI.State private var energy: Energy = .medium
    @SwiftUI.State private var liveSeconds: Int = 23 * 60 + 42

    let totalTasks: Int
    let currentIndex: Int
    var onAllTasksDone: () -> Void

    init(totalTasks: Int = 7, currentIndex: Int = 2, onAllTasksDone: @escaping () -> Void = {}) {
        self.totalTasks = totalTasks
        self.currentIndex = currentIndex
        self.onAllTasksDone = onAllTasksDone
    }

    private var snapshot: Snapshot { snapshotFor(state: state, liveSeconds: liveSeconds) }
    private var bg: Color {
        switch state {
        case .breakRest, .tired: return Color(red: 0.039, green: 0.102, blue: 0.078)
        case .done: return Color(red: 0.102, green: 0.086, blue: 0.063)
        default: return MentorColors.surfaceVoid
        }
    }
    private var ringColor: Color {
        switch state {
        case .breakRest: return MentorColors.emeraldBright
        case .done: return MentorColors.gold
        case .tired: return MentorColors.rose
        default: return MentorColors.gold
        }
    }

    var body: some View {
        ZStack {
            bg.ignoresSafeArea()
            VStack(spacing: 14) {
                DayProgressRow(total: totalTasks, current: currentIndex)
                taskBlock
                CircularTimer(progress: snapshot.progress, digits: snapshot.timerDigits, label: snapshot.timerLabel, accent: ringColor)
                if snapshot.pomodoroVisible {
                    PomodoroRow(current: snapshot.pomodoroCurrent)
                }
                mentorMessage
                energyRow
                Spacer()
                MentorPrimaryButton(snapshot.primaryBtn) {
                    advanceState()
                }
                secondaryRow
            }
            .padding(.horizontal, 20)
            .padding(.top, 28)
            .padding(.bottom, 24)
        }
        .onReceive(Timer.publish(every: 1, on: .main, in: .common).autoconnect()) { _ in
            guard state == .running else { return }
            if liveSeconds > 0 { liveSeconds -= 1 }
            else { state = .breakRest }
        }
    }

    private func advanceState() {
        switch state {
        case .ready: state = .running
        case .running: state = .breakRest
        case .breakRest: state = .running; liveSeconds = 25 * 60
        case .done: onAllTasksDone(); state = .ready
        case .tired: state = .breakRest
        }
    }

    // MARK: task block

    private var taskBlock: some View {
        VStack(spacing: 6) {
            Text(snapshot.pre)
                .font(MentorFonts.mono(9))
                .tracking(4)
                .foregroundColor(MentorColors.gold)
            Text(snapshot.title)
                .font(MentorFonts.mentorBold(22))
                .foregroundColor(MentorColors.textPrimary)
                .multilineTextAlignment(.center)
            Text(snapshot.goal)
                .font(MentorFonts.mentor(13))
                .foregroundColor(MentorColors.textBody)
                .multilineTextAlignment(.center)
        }
    }

    // MARK: mentor message

    private var mentorMessage: some View {
        HStack(alignment: .top, spacing: 10) {
            MentorOrb(size: 24, breathing: false)
            HStack(alignment: .top, spacing: 0) {
                Text(snapshot.mentorPrefix)
                    .font(MentorFonts.mentorBold(13))
                    .foregroundColor(MentorColors.gold)
                Text(snapshot.mentorRest)
                    .font(MentorFonts.mentor(13))
                    .foregroundColor(MentorColors.textBody)
            }
        }
        .padding(12)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(
            RoundedRectangle(cornerRadius: 8)
                .fill(MentorColors.gold.opacity(0.05))
                .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.gold.opacity(0.35), lineWidth: 1))
        )
    }

    // MARK: energy

    private var energyRow: some View {
        VStack(alignment: .leading, spacing: 6) {
            Text("HOZIRGI ENERGIYANG?")
                .font(MentorFonts.mono(9))
                .tracking(4)
                .foregroundColor(MentorColors.textMuted)
            HStack(spacing: 6) {
                DayFlowEnergyChip(text: "⚡ KUCHLI", selected: energy == .strong, accent: MentorColors.gold) { energy = .strong }
                DayFlowEnergyChip(text: "🌥 O'RTA", selected: energy == .medium, accent: MentorColors.gold) { energy = .medium }
                DayFlowEnergyChip(text: "🌙 CHARCHADIM", selected: energy == .tired, accent: MentorColors.rose) {
                    energy = .tired
                    state = .tired
                }
            }
        }
    }

    private var secondaryRow: some View {
        HStack(spacing: 8) {
            SmallActionButton(title: "⏰ 5 daq keyin") {}
            SmallActionButton(title: "⏭ O'tkazib yub") {}
        }
    }

    // MARK: state snapshot

    private struct Snapshot {
        let pre: String, title: String, goal: String
        let timerDigits: String, timerLabel: String
        let progress: Double
        let mentorPrefix: String, mentorRest: String
        let primaryBtn: String
        let pomodoroVisible: Bool
        let pomodoroCurrent: Int
    }

    private func snapshotFor(state: State, liveSeconds: Int) -> Snapshot {
        switch state {
        case .ready:
            return Snapshot(
                pre: "4-ISH · KEYINGI", title: "Yugurish · 5 km",
                goal: "\"Tanani uyg'otish — kunni jonli boshlash\"",
                timerDigits: "30:00", timerLabel: "TAYMER · KUTYAPTI",
                progress: 0,
                mentorPrefix: "Boshlayveraymi? ",
                mentorRest: "30 daqiqalik taymer tayyor. Sen \"ha\" desang yonadi.",
                primaryBtn: "Ha, boshlayman",
                pomodoroVisible: false, pomodoroCurrent: 0
            )
        case .running:
            let m = liveSeconds / 60, s = liveSeconds % 60
            return Snapshot(
                pre: "3-ISH · CHUQUR FOKUS", title: "Kod yozish · API",
                goal: "\"API integratsiyasini tugatish va testlarni yozish\"",
                timerDigits: String(format: "%02d:%02d", m, s),
                timerLabel: "QOLDI",
                progress: 1.0 - min(1.0, Double(liveSeconds) / Double(25 * 60)),
                mentorPrefix: "2-pomodoro ",
                mentorRest: "davom etmoqda. Ko'zlaringni 20 soniya uzoq narsaga qarat — diqqat tiniqlashadi.",
                primaryBtn: "Davom etaman",
                pomodoroVisible: true, pomodoroCurrent: 2
            )
        case .breakRest:
            return Snapshot(
                pre: "POMODORO TANAFFUS · 5 DAQ", title: "Dam ol — ko'z ber",
                goal: "\"Stol oldidan tur, derazaga qara, 5 daqiqa nafas ol\"",
                timerDigits: "04:38", timerLabel: "DAM",
                progress: 0.07,
                mentorPrefix: "",
                mentorRest: "Dam — bekorchilik emas, miyangning sifatli ish vaqti. Qaytib kelganingda fokus 30% kuchayadi.",
                primaryBtn: "Keyingi pomodoroga",
                pomodoroVisible: true, pomodoroCurrent: 3
            )
        case .done:
            return Snapshot(
                pre: "3-ISH BAJARILDI ✓", title: "Kod yozish · TUGADI",
                goal: "\"45 daqiqa fokus, 4 pomodoro · API integratsiya yopildi\"",
                timerDigits: "00:00", timerLabel: "BAJARDING",
                progress: 1,
                mentorPrefix: "Bajarding. ",
                mentorRest: "Yana bir halqa daraxtingda. Endi 5 daqiqa dam, keyin 4-ish — yugurish.",
                primaryBtn: "4-ishga o'taman",
                pomodoroVisible: false, pomodoroCurrent: 0
            )
        case .tired:
            return Snapshot(
                pre: "CHARCHOQ SEZILDI", title: "To'xtaylik — sen muhim",
                goal: "\"Charchagan miya — yomon qaror chiqaradi. Hozir to'xtash — ertaga g'olib bo'lish.\"",
                timerDigits: "15:00", timerLabel: "DAM TAVSIYA",
                progress: 0.5,
                mentorPrefix: "Sen \"charchayapman\" dedding. ",
                mentorRest: "Eshityapman. Keyingi 2 ish ertaga ko'chiriladi. Hozir — chuqurroq dam ol.",
                primaryBtn: "15 daqiqa dam olaman",
                pomodoroVisible: false, pomodoroCurrent: 0
            )
        }
    }
}

private struct DayProgressRow: View {
    let total: Int
    let current: Int

    var body: some View {
        VStack(spacing: 8) {
            HStack(spacing: 0) {
                Text("BUGUNGI YO'L · ")
                    .font(MentorFonts.mono(10))
                    .tracking(3)
                    .foregroundColor(MentorColors.textMuted)
                Text("\(current + 1)")
                    .font(MentorFonts.mono(10).weight(.bold))
                    .tracking(3)
                    .foregroundColor(MentorColors.gold)
                Text(" / \(total)")
                    .font(MentorFonts.mono(10))
                    .tracking(3)
                    .foregroundColor(MentorColors.textMuted)
            }
            HStack(spacing: 6) {
                ForEach(0..<total, id: \.self) { i in
                    Circle()
                        .fill(
                            i < current ? MentorColors.emeraldBright :
                            i == current ? MentorColors.gold : MentorColors.textGhost
                        )
                        .frame(width: i == current ? 10 : 8, height: i == current ? 10 : 8)
                }
            }
        }
    }
}

private struct CircularTimer: View {
    let progress: Double
    let digits: String
    let label: String
    let accent: Color

    var body: some View {
        ZStack {
            Circle()
                .stroke(accent.opacity(0.15), lineWidth: 6)
                .frame(width: 220, height: 220)
            Circle()
                .trim(from: 0, to: progress)
                .stroke(accent, style: StrokeStyle(lineWidth: 6, lineCap: .round))
                .rotationEffect(.degrees(-90))
                .frame(width: 220, height: 220)
                .animation(.easeInOut(duration: 0.6), value: progress)
            VStack(spacing: 8) {
                Circle()
                    .fill(accent.opacity(0.25))
                    .frame(width: 32, height: 32)
                    .overlay(Circle().strokeBorder(accent.opacity(0.6), lineWidth: 1))
                Text(digits)
                    .font(.system(size: 44, weight: .light, design: .monospaced))
                    .tracking(2)
                    .foregroundColor(MentorColors.textPrimary)
                Text(label)
                    .font(MentorFonts.mono(9).weight(.semibold))
                    .tracking(4)
                    .foregroundColor(accent)
            }
        }
    }
}

private struct PomodoroRow: View {
    let current: Int

    var body: some View {
        HStack(spacing: 6) {
            Text("POMODORO")
                .font(MentorFonts.mono(9))
                .tracking(3)
                .foregroundColor(MentorColors.textMuted)
            ForEach(0..<4, id: \.self) { i in
                Circle()
                    .fill(
                        i < current - 1 ? MentorColors.emeraldBright :
                        i == current - 1 ? MentorColors.gold : MentorColors.textGhost
                    )
                    .frame(
                        width: i == current - 1 ? 10 : 7,
                        height: i == current - 1 ? 10 : 7
                    )
            }
        }
    }
}

private struct DayFlowEnergyChip: View {
    let text: String
    let selected: Bool
    let accent: Color
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            Text(text)
                .font(.system(size: 10, weight: .semibold))
                .foregroundColor(selected ? accent : MentorColors.textBody)
                .frame(maxWidth: .infinity)
                .frame(height: 36)
                .background(
                    RoundedRectangle(cornerRadius: 4)
                        .fill(selected ? accent.opacity(0.16) : Color.white.opacity(0.02))
                        .overlay(RoundedRectangle(cornerRadius: 4)
                                    .strokeBorder(selected ? accent : MentorColors.textGhost, lineWidth: 1))
                )
        }
    }
}

private struct SmallActionButton: View {
    let title: String
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            Text(title)
                .font(.system(size: 11))
                .foregroundColor(MentorColors.textMuted)
                .frame(maxWidth: .infinity)
                .frame(height: 40)
                .background(
                    RoundedRectangle(cornerRadius: 4)
                        .fill(Color.white.opacity(0.03))
                        .overlay(RoundedRectangle(cornerRadius: 4).strokeBorder(MentorColors.textGhost, lineWidth: 1))
                )
        }
    }
}

#Preview {
    DayFlowView()
}
