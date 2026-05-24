import SwiftUI

/// Evening planning — user drafts tomorrow's tasks. Mirrors Android `TomorrowPlanScreen.kt`.
struct TomorrowPlanView: View {
    struct DraftTask: Identifiable {
        let id = UUID()
        let time: String
        let durationLabel: String
        let name: String
        let meta: String
        let severityMax: Bool
    }

    @State private var tasks: [DraftTask] = TomorrowPlanView.sample
    var onSeal: ([DraftTask]) -> Void = { _ in }
    var onSkip: () -> Void = {}

    var body: some View {
        ZStack(alignment: .bottom) {
            Color(red: 0.024, green: 0.027, blue: 0.059).ignoresSafeArea()
            ScrollView {
                VStack(spacing: 16) {
                    headerRow
                    invite
                    SectionTitle(label: "ERTANGI ISHLAR · 11-MAY", stat: "\(tasks.count)")
                    VStack(spacing: 8) {
                        ForEach(tasks) { t in
                            TaskRow(task: t) { delete(t) }
                        }
                    }
                    SectionTitle(label: "TEZ QO'SH", stat: nil)
                    quickAdd
                    voiceRow
                    overviewCard
                    closingNote
                }
                .padding(.horizontal, 20).padding(.top, 28).padding(.bottom, 140)
            }
            bottomBar
        }
    }

    private func delete(_ t: DraftTask) {
        tasks.removeAll { $0.id == t.id }
    }

    private var headerRow: some View {
        HStack {
            MentorPill("☾  21:14 · ERTANGI REJA", color: Color(red: 0.420, green: 0.361, blue: 0.557))
            Spacer()
            MentorPill("KECHQURUN", color: MentorColors.goldDeep)
        }
    }

    private var invite: some View {
        HStack(alignment: .top, spacing: 12) {
            MentorOrb(size: 28, breathing: false)
            HStack(spacing: 0) {
                Text("Ertangi yo'lni hozir chizamiz. Tongda ")
                    .font(MentorFonts.mentor(14)).foregroundColor(MentorColors.textPrimary)
                Text("tasdiqlaysan").font(MentorFonts.mentorBold(14)).foregroundColor(MentorColors.gold)
                Text(" — kerak bo'lsa o'zgartirib qaytadan.")
                    .font(MentorFonts.mentor(14)).foregroundColor(MentorColors.textPrimary)
            }
        }
        .padding(14)
        .background(RoundedRectangle(cornerRadius: 4)
                        .fill(MentorColors.gold.opacity(0.05))
                        .overlay(RoundedRectangle(cornerRadius: 4).strokeBorder(MentorColors.gold.opacity(0.5), lineWidth: 1)))
    }

    private var quickAdd: some View {
        let kinds = ["⊙ Sport", "◇ Kod", "▥ Kitob", "⌖ Til", "∼ Pomodoro", "☼ Oila", "+ Boshqa"]
        return FlowLayout(spacing: 6) {
            ForEach(kinds, id: \.self) { k in
                Button {
                    tasks.append(DraftTask(time: "—:—", durationLabel: "30 daq",
                                           name: k.split(separator: " ").dropFirst().joined(separator: " "),
                                           meta: "⊙ YANGI · 30 DAQ", severityMax: false))
                } label: {
                    Text(k).font(.system(size: 11)).tracking(1).foregroundColor(MentorColors.textBody)
                        .padding(.horizontal, 12).padding(.vertical, 8)
                        .background(Capsule().fill(Color.white.opacity(0.03))
                                        .overlay(Capsule().strokeBorder(MentorColors.textGhost, lineWidth: 1)))
                }
            }
        }
    }

    private var voiceRow: some View {
        HStack(spacing: 12) {
            ZStack {
                Circle().fill(MentorColors.gold.opacity(0.15))
                    .overlay(Circle().strokeBorder(MentorColors.gold, lineWidth: 1))
                Image(systemName: "mic.fill").font(.system(size: 12)).foregroundColor(MentorColors.gold)
            }
            .frame(width: 36, height: 36)
            HStack(spacing: 0) {
                Text("Yoki ").font(MentorFonts.mentor(13)).foregroundColor(MentorColors.textMuted)
                Text("ovoz bilan ayt").font(MentorFonts.mentorBold(13)).foregroundColor(MentorColors.gold)
                Text(" — \"ertaga ertalab yugurish...\"").font(MentorFonts.mentor(13)).foregroundColor(MentorColors.textMuted)
                    .lineLimit(1)
            }
            Spacer()
        }
        .padding(12)
        .background(RoundedRectangle(cornerRadius: 8).fill(Color.white.opacity(0.02))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.textGhost, lineWidth: 1)))
    }

    private var overviewCard: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("ERTANGI MANZARA").font(MentorFonts.mono(8.5)).tracking(4)
                .foregroundColor(Color(red: 0.420, green: 0.361, blue: 0.557))
            OverviewLine(label: "Jami fokus", value: "4 soat 45 daq")
            OverviewLine(label: "Sessiya soni", value: "\(tasks.count) ta")
            OverviewLine(label: "Birinchi uyg'onish", value: "06:15")
            OverviewLine(label: "Erkin vaqt", value: "3 soat 30 daq")
        }
        .padding(14)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(RoundedRectangle(cornerRadius: 8)
                        .fill(Color(red: 0.420, green: 0.361, blue: 0.557).opacity(0.08))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(Color(red: 0.420, green: 0.361, blue: 0.557).opacity(0.4), lineWidth: 1)))
    }

    private var closingNote: some View {
        HStack(spacing: 0) {
            Text("Yaxshi reja. ").font(MentorFonts.mentorBold(13)).foregroundColor(MentorColors.gold)
            Text("Erta uyg'onish bilan kun kuchli boshlanadi. Tushga til darsi — miya hali toza paytda.")
                .font(MentorFonts.mentor(13)).foregroundColor(MentorColors.textBody)
        }
        .padding(14)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(RoundedRectangle(cornerRadius: 4).fill(MentorColors.gold.opacity(0.05))
                        .overlay(RoundedRectangle(cornerRadius: 4).strokeBorder(MentorColors.gold.opacity(0.4), lineWidth: 1)))
    }

    private var bottomBar: some View {
        VStack(spacing: 6) {
            MentorPrimaryButton("✓  Rejani muhrlayman") { onSeal(tasks) }
            Button(action: onSkip) {
                Text("— ertalab tuzataman —")
                    .font(MentorFonts.mentor(12)).foregroundColor(MentorColors.textMuted)
            }
        }
        .padding(.horizontal, 20).padding(.vertical, 16)
        .background(Color(red: 0.024, green: 0.027, blue: 0.059))
    }

    static let sample: [DraftTask] = [
        .init(time: "06:30", durationLabel: "15 daq", name: "Tonggi yengillik", meta: "⊙ STRETCH + QUYOSH · 15 DAQ", severityMax: false),
        .init(time: "07:00", durationLabel: "45 daq", name: "Yugurish · 5 km", meta: "⊙ SPORT · 45 DAQ", severityMax: false),
        .init(time: "09:00", durationLabel: "2 soat", name: "Chuqur kod — API", meta: "⊙ POMODORO · 4 SIKL · 2S", severityMax: true),
        .init(time: "13:00", durationLabel: "45 daq", name: "Ingliz tili · B2 darsi", meta: "↗ ZOOM · MEETING · 45 DAQ", severityMax: false),
        .init(time: "19:00", durationLabel: "60 daq", name: "Kitob o'qish · Atomic Habits", meta: "⊙ FOKUS · 3 BOB · 60 DAQ", severityMax: false)
    ]
}

private struct SectionTitle: View {
    let label: String
    let stat: String?
    var body: some View {
        HStack(spacing: 10) {
            Text(label).font(MentorFonts.mono(9)).tracking(4).foregroundColor(MentorColors.goldDeep)
            Rectangle()
                .fill(LinearGradient(colors: [.clear, MentorColors.goldDeep, .clear],
                                     startPoint: .leading, endPoint: .trailing))
                .frame(height: 1)
            if let s = stat {
                Text(s).font(MentorFonts.mono(9).weight(.semibold)).tracking(1).foregroundColor(MentorColors.gold)
            }
        }
    }
}

private struct TaskRow: View {
    let task: TomorrowPlanView.DraftTask
    let onDelete: () -> Void
    var body: some View {
        let timeColor = task.severityMax ? MentorColors.crimson : MentorColors.gold
        let border = task.severityMax ? MentorColors.crimson.opacity(0.4) : MentorColors.goldDeep.opacity(0.4)
        let bg = task.severityMax ? MentorColors.crimson.opacity(0.04) : MentorColors.gold.opacity(0.04)
        HStack(spacing: 10) {
            VStack(spacing: 2) {
                Text(task.time).font(MentorFonts.mono(11).weight(.semibold)).tracking(1).foregroundColor(timeColor)
                Text(task.durationLabel).font(.system(size: 8)).foregroundColor(MentorColors.goldDeep)
            }
            .frame(width: 54)
            VStack(alignment: .leading, spacing: 2) {
                Text(task.name).font(.system(size: 14, weight: .semibold)).foregroundColor(MentorColors.textPrimary)
                Text(task.meta).font(MentorFonts.mono(8)).tracking(1).foregroundColor(MentorColors.goldDeep)
            }
            Spacer()
            Button(action: onDelete) {
                Text("×").font(.system(size: 16)).foregroundColor(MentorColors.textMuted)
                    .frame(width: 28, height: 28)
                    .background(Circle().fill(Color.white.opacity(0.04)))
            }
        }
        .padding(.horizontal, 12).padding(.vertical, 10)
        .background(RoundedRectangle(cornerRadius: 8).fill(bg)
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(border, lineWidth: 1)))
    }
}

private struct OverviewLine: View {
    let label: String, value: String
    var body: some View {
        HStack {
            Text(label).font(MentorFonts.mentor(13)).foregroundColor(MentorColors.textBody)
            Spacer()
            Text(value).font(.system(size: 13, weight: .semibold)).foregroundColor(MentorColors.textPrimary)
        }
        .padding(.vertical, 3)
    }
}

/// Minimal FlowLayout for chip wrapping.
struct FlowLayout: Layout {
    var spacing: CGFloat = 6
    func sizeThatFits(proposal: ProposedViewSize, subviews: Subviews, cache: inout ()) -> CGSize {
        let width = proposal.width ?? .infinity
        var x: CGFloat = 0, y: CGFloat = 0, rowHeight: CGFloat = 0
        for sv in subviews {
            let size = sv.sizeThatFits(.unspecified)
            if x + size.width > width {
                x = 0
                y += rowHeight + spacing
                rowHeight = 0
            }
            x += size.width + spacing
            rowHeight = max(rowHeight, size.height)
        }
        return CGSize(width: width, height: y + rowHeight)
    }
    func placeSubviews(in bounds: CGRect, proposal: ProposedViewSize, subviews: Subviews, cache: inout ()) {
        var x: CGFloat = bounds.minX, y: CGFloat = bounds.minY, rowHeight: CGFloat = 0
        for sv in subviews {
            let size = sv.sizeThatFits(.unspecified)
            if x + size.width > bounds.maxX {
                x = bounds.minX
                y += rowHeight + spacing
                rowHeight = 0
            }
            sv.place(at: CGPoint(x: x, y: y), proposal: .unspecified)
            x += size.width + spacing
            rowHeight = max(rowHeight, size.height)
        }
    }
}

#Preview {
    TomorrowPlanView()
}
