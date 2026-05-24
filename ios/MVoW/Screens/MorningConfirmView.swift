import SwiftUI

/// Morning ritual — user reviews last night's plan and confirms (or amends).
/// Mirrors Android `MorningConfirmScreen.kt` and `docs/v2/preview/morning-confirm.html`.
struct MorningConfirmView: View {
    enum TaskAction { case keep, move, cancel }
    enum Energy { case strong, medium, tired }

    struct PlannedTask: Identifiable {
        let id = UUID()
        let time: String
        let durationLabel: String
        let name: String
        let meta: String
        let severityMax: Bool
        let initial: TaskAction
    }

    @State private var actions: [TaskAction]
    @State private var pickedIndex: Int = 2
    @State private var energy: Energy = .medium

    let tasks: [PlannedTask]
    var onConfirm: (Int, Int, Energy) -> Void = { _, _, _ in }
    var onReopen: () -> Void = {}

    init(
        tasks: [PlannedTask] = MorningConfirmView.sample,
        onConfirm: @escaping (Int, Int, Energy) -> Void = { _, _, _ in },
        onReopen: @escaping () -> Void = {}
    ) {
        self.tasks = tasks
        self.onConfirm = onConfirm
        self.onReopen = onReopen
        _actions = State(initialValue: tasks.map { $0.initial })
    }

    private var changeCount: Int {
        zip(actions, tasks).filter { $0 != $1.initial }.count
    }

    var body: some View {
        ZStack(alignment: .bottom) {
            // Sunrise gradient backdrop
            LinearGradient(
                colors: [Color(red: 0.016, green: 0.024, blue: 0.043),
                         Color(red: 0.055, green: 0.043, blue: 0.063),
                         Color(red: 0.102, green: 0.086, blue: 0.063)],
                startPoint: .top, endPoint: .bottom
            )
            .ignoresSafeArea()

            ScrollView {
                VStack(spacing: 18) {
                    headerRow
                    hero
                    pivotalQuote
                    sectionTitle(label: "REJA · HAR BIRIGA BAHO", stat: "\(tasks.count) ISH")
                    VStack(spacing: 8) {
                        ForEach(Array(tasks.enumerated()), id: \.element.id) { i, t in
                            TaskRow(task: t, action: $actions[i])
                        }
                    }
                    topPriorityCard
                    energyCard
                }
                .padding(.horizontal, 20)
                .padding(.top, 28)
                .padding(.bottom, 140)
            }

            bottomBar
        }
    }

    // MARK: header

    private var headerRow: some View {
        HStack {
            MentorPill("☀  06:32 · TONG", color: MentorColors.gold)
            Spacer()
            MentorPill("TASDIQ", color: MentorColors.goldDeep)
        }
    }

    private var hero: some View {
        VStack(spacing: 6) {
            Text("XAYRLI TONG")
                .font(MentorFonts.mono(9))
                .tracking(4)
                .foregroundColor(MentorColors.goldDeep)
            HStack(spacing: 0) {
                Text("Kechagi rejang ")
                    .font(MentorFonts.mentor(24))
                    .foregroundColor(MentorColors.textPrimary)
                Text("esingdami")
                    .font(MentorFonts.mentorBold(24))
                    .foregroundColor(MentorColors.gold)
                Text("?")
                    .font(MentorFonts.mentor(24))
                    .foregroundColor(MentorColors.textPrimary)
            }
            Text("11 MAY · SESHANBA · \(tasks.count) ISH CHIZILGAN")
                .font(MentorFonts.mono(9))
                .tracking(2)
                .foregroundColor(MentorColors.goldDeep)
        }
        .frame(maxWidth: .infinity)
    }

    private var pivotalQuote: some View {
        HStack(alignment: .top, spacing: 12) {
            MentorOrb(size: 26, breathing: false)
            Text("Kecha 21:14 da bu rejani muhrlading. Endi tongda — yangi nazar bilan ko'r. Hammasi hali ham to'g'rimi? Birortasi shu kunga to'g'ri kelmasa, bemalol surib qo'y.")
                .font(MentorFonts.mentor(15))
                .foregroundColor(MentorColors.textPrimary)
                .lineSpacing(4)
        }
        .padding(14)
        .background(
            ZStack(alignment: .leading) {
                Rectangle().fill(MentorColors.gold.opacity(0.06))
                Rectangle().fill(MentorColors.gold).frame(width: 2)
            }
        )
    }

    private func sectionTitle(label: String, stat: String) -> some View {
        HStack(spacing: 10) {
            Text(label)
                .font(MentorFonts.mono(9))
                .tracking(4)
                .foregroundColor(MentorColors.goldDeep)
            Rectangle()
                .fill(LinearGradient(colors: [.clear, MentorColors.goldDeep, .clear],
                                     startPoint: .leading, endPoint: .trailing))
                .frame(height: 1)
            Text(stat)
                .font(MentorFonts.mono(9))
                .foregroundColor(MentorColors.gold)
        }
    }

    // MARK: top priority

    private var topPriorityCard: some View {
        VStack(alignment: .leading, spacing: 10) {
            Text("BITTA SAVOL · ENG MUHIM")
                .font(MentorFonts.mono(9))
                .tracking(4)
                .foregroundColor(MentorColors.goldDeep)
            HStack(spacing: 0) {
                Text("Bu kun tugagach, ")
                    .font(MentorFonts.mentor(14))
                    .foregroundColor(MentorColors.textPrimary)
                Text("qaysi bittasi")
                    .font(MentorFonts.mentorBold(14))
                    .foregroundColor(MentorColors.gold)
                Text(" bajarilmasa, achinasan?")
                    .font(MentorFonts.mentor(14))
                    .foregroundColor(MentorColors.textPrimary)
            }
            // Pick chips
            VStack(spacing: 6) {
                ForEach(Array(stride(from: 0, to: tasks.count, by: 3)), id: \.self) { row in
                    HStack(spacing: 6) {
                        ForEach(row..<min(row + 3, tasks.count), id: \.self) { i in
                            PickChip(text: shortName(tasks[i].name), selected: i == pickedIndex) {
                                pickedIndex = i
                            }
                        }
                    }
                }
            }
        }
        .padding(14)
        .background(
            RoundedRectangle(cornerRadius: 8)
                .fill(MentorColors.gold.opacity(0.05))
                .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.goldDeep.opacity(0.4), lineWidth: 1))
        )
    }

    private func shortName(_ s: String) -> String {
        s.split(separator: "·").first.map(String.init)?.trimmingCharacters(in: .whitespaces) ?? s
    }

    // MARK: energy

    private var energyCard: some View {
        VStack(alignment: .leading, spacing: 10) {
            Text("HOZIRGI ENERGIYANG?")
                .font(MentorFonts.mono(9))
                .tracking(4)
                .foregroundColor(MentorColors.goldDeep)
            HStack(spacing: 6) {
                EnergyChip(text: "⚡ KUCHLI", selected: energy == .strong) { energy = .strong }
                EnergyChip(text: "🌥 O'RTA", selected: energy == .medium) { energy = .medium }
                EnergyChip(text: "🌙 CHARCHADIM", selected: energy == .tired) { energy = .tired }
            }
        }
        .padding(14)
        .background(
            RoundedRectangle(cornerRadius: 8)
                .fill(Color.white.opacity(0.02))
                .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.textGhost, lineWidth: 1))
        )
    }

    // MARK: bottom bar

    private var bottomBar: some View {
        VStack(spacing: 6) {
            MentorPrimaryButton(
                changeCount > 0 ? "O'zgartirdim (\(changeCount)) — qayta tasdiqla"
                                : "Tasdiqladim — birinchi ishga"
            ) {
                onConfirm(changeCount, pickedIndex, energy)
            }
            Button {
                onReopen()
            } label: {
                Text("— rejani qaytadan ko'raman —")
                    .font(MentorFonts.mentor(11))
                    .foregroundColor(MentorColors.textMuted)
            }
        }
        .padding(.horizontal, 20)
        .padding(.vertical, 16)
        .background(Color(red: 0.102, green: 0.086, blue: 0.063))
    }

    // MARK: sample data

    static let sample: [PlannedTask] = [
        .init(time: "06:45", durationLabel: "15 daq", name: "Tonggi yengillik", meta: "⊙ STRETCH + QUYOSH · 15 DAQ", severityMax: false, initial: .keep),
        .init(time: "07:00", durationLabel: "45 daq", name: "Yugurish · 5 km", meta: "⊙ SPORT · 45 DAQ", severityMax: false, initial: .keep),
        .init(time: "09:00", durationLabel: "2 soat", name: "Chuqur kod — API", meta: "⊙ POMODORO · 4 SIKL · 2S", severityMax: true, initial: .keep),
        .init(time: "13:00", durationLabel: "→ 15:00", name: "Ingliz tili · B2 darsi", meta: "↗ ZOOM · 14:00 → 15:00 ga ko'chdi", severityMax: false, initial: .move),
        .init(time: "19:00", durationLabel: "60 daq", name: "Kitob o'qish · Atomic Habits", meta: "⊙ FOKUS · 3 BOB · 60 DAQ", severityMax: false, initial: .keep)
    ]
}

private struct TaskRow: View {
    let task: MorningConfirmView.PlannedTask
    @Binding var action: MorningConfirmView.TaskAction

    private var border: Color {
        switch action {
        case .keep where task.severityMax: return MentorColors.crimson.opacity(0.6)
        case .keep: return MentorColors.emeraldBright
        case .move: return MentorColors.goldDeep
        case .cancel: return MentorColors.textGhost
        }
    }
    private var bg: Color {
        switch action {
        case .keep: return MentorColors.emeraldBright.opacity(0.08)
        case .move: return MentorColors.gold.opacity(0.04)
        case .cancel: return .clear
        }
    }
    private var timeColor: Color {
        switch action {
        case .keep where task.severityMax: return MentorColors.crimson
        case .keep: return MentorColors.emeraldBright
        default: return MentorColors.gold
        }
    }

    var body: some View {
        HStack(alignment: .center, spacing: 10) {
            VStack(spacing: 2) {
                Text(task.time)
                    .font(MentorFonts.mono(11))
                    .tracking(1)
                    .foregroundColor(timeColor)
                Text(task.durationLabel)
                    .font(.system(size: 7.5))
                    .foregroundColor(MentorColors.goldDeep)
            }
            .frame(width: 54)

            VStack(alignment: .leading, spacing: 6) {
                Text(task.name)
                    .font(MentorFonts.mentorBold(13.5))
                    .foregroundColor(MentorColors.textPrimary)
                Text(task.meta)
                    .font(MentorFonts.mono(7.5))
                    .tracking(1)
                    .foregroundColor(MentorColors.goldDeep)
                HStack(spacing: 5) {
                    ActionChip(text: "✓ QOLDIR", selected: action == .keep, color: MentorColors.emeraldBright) { action = .keep }
                    ActionChip(text: "→ SUR", selected: action == .move, color: MentorColors.gold2) { action = .move }
                    ActionChip(text: "× O'CHIR", selected: action == .cancel, color: MentorColors.crimson) { action = .cancel }
                }
            }
        }
        .padding(.horizontal, 12)
        .padding(.vertical, 10)
        .background(
            RoundedRectangle(cornerRadius: 8)
                .fill(bg)
                .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(border, lineWidth: 1))
        )
    }
}

private struct ActionChip: View {
    let text: String
    let selected: Bool
    let color: Color
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            Text(text)
                .font(.system(size: 9, weight: .semibold))
                .tracking(1)
                .foregroundColor(selected ? color : MentorColors.textMuted)
                .frame(maxWidth: .infinity)
                .frame(height: 28)
                .background(
                    RoundedRectangle(cornerRadius: 4)
                        .fill(selected ? color.opacity(0.20) : Color.white.opacity(0.03))
                        .overlay(RoundedRectangle(cornerRadius: 4)
                                    .strokeBorder(selected ? color : MentorColors.textGhost, lineWidth: 1))
                )
        }
    }
}

private struct PickChip: View {
    let text: String
    let selected: Bool
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            Text(text)
                .font(.system(size: 10, weight: selected ? .semibold : .regular))
                .foregroundColor(selected ? MentorColors.gold : MentorColors.textBody)
                .lineLimit(1)
                .frame(maxWidth: .infinity)
                .frame(height: 32)
                .background(
                    RoundedRectangle(cornerRadius: 4)
                        .fill(selected ? MentorColors.gold.opacity(0.18) : Color.white.opacity(0.02))
                        .overlay(RoundedRectangle(cornerRadius: 4)
                                    .strokeBorder(selected ? MentorColors.gold : MentorColors.textGhost, lineWidth: 1))
                )
        }
    }
}

private struct EnergyChip: View {
    let text: String
    let selected: Bool
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            Text(text)
                .font(.system(size: 10, weight: .semibold))
                .foregroundColor(selected ? MentorColors.gold : MentorColors.textBody)
                .frame(maxWidth: .infinity)
                .frame(height: 40)
                .background(
                    RoundedRectangle(cornerRadius: 4)
                        .fill(selected ? MentorColors.gold.opacity(0.16) : Color.white.opacity(0.02))
                        .overlay(RoundedRectangle(cornerRadius: 4)
                                    .strokeBorder(selected ? MentorColors.gold : MentorColors.textGhost, lineWidth: 1))
                )
        }
    }
}

#Preview {
    MorningConfirmView()
}
