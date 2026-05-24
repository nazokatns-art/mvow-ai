import SwiftUI

/// Daily brief — 3-question Socratic morning ritual.
/// Mirrors Android `DailyBriefScreen.kt`.
struct DailyBriefView: View {
    enum Step { case q1, q2, q3, action }

    struct Answer { let icon: String; let label: String }

    @State private var step: Step = .q1
    @State private var identity: String? = nil
    @State private var priority: String? = nil
    @State private var tinyStep: String? = nil

    var onDone: (String, String, String) -> Void = { _, _, _ in }

    var body: some View {
        ZStack(alignment: .bottom) {
            MentorColors.surfaceVoid.ignoresSafeArea()
            ScrollView {
                VStack(spacing: 18) {
                    headerRow
                    BrandSeal(text: sealText)
                    neuroStrip
                    questionBlock
                    if !answers.isEmpty {
                        VStack(spacing: 6) {
                            ForEach(Array(stride(from: 0, to: answers.count, by: 2)), id: \.self) { i in
                                HStack(spacing: 6) {
                                    AnswerChip(a: answers[i], selected: currentPick == answers[i].label) { pick(answers[i].label) }
                                    if i + 1 < answers.count {
                                        AnswerChip(a: answers[i + 1], selected: currentPick == answers[i + 1].label) { pick(answers[i + 1].label) }
                                    } else { Spacer().frame(maxWidth: .infinity) }
                                }
                            }
                        }
                    }
                    if step != .q1 || identity != nil { reframeCard }
                    if step == .action { planCard }
                }
                .padding(.horizontal, 20).padding(.top, 28).padding(.bottom, 130)
            }
            bottomBar
        }
    }

    private var sealText: String {
        switch step {
        case .q1: return "kunning niyati"
        case .q2: return "ikkinchi savol"
        case .q3: return "eng kichik qadam"
        case .action: return "kuching jam bo'ldi"
        }
    }
    private var stepIdx: Int { [.q1, .q2, .q3, .action].firstIndex(of: step)! + 1 }

    private var currentPick: String? {
        switch step {
        case .q1: return identity
        case .q2: return priority
        case .q3: return tinyStep
        case .action: return nil
        }
    }

    private func pick(_ label: String) {
        switch step {
        case .q1: identity = label
        case .q2: priority = label
        case .q3: tinyStep = label
        case .action: break
        }
    }

    private var answers: [Answer] {
        switch step {
        case .q1: return [
            Answer(icon: "⚡", label: "Kuchli, tayyor."),
            Answer(icon: "☀", label: "Yengil, umidli."),
            Answer(icon: "🌙", label: "Charchagan."),
            Answer(icon: "∼", label: "Bilmayman.")
        ]
        case .q2: return [
            Answer(icon: "◇", label: "Kod loyihasini tugatsam."),
            Answer(icon: "▥", label: "Bir bob kitob o'qisam."),
            Answer(icon: "✦", label: "Sport — yugurish."),
            Answer(icon: "☼", label: "Oila bilan ovqatlansam.")
        ]
        case .q3: return [
            Answer(icon: "▥", label: "Kitobni oldim."),
            Answer(icon: "▦", label: "Birinchi sahifani ochdim."),
            Answer(icon: "▧", label: "Quloqchin kiyaman."),
            Answer(icon: "▩", label: "Telefon uzoqqa qo'yaman.")
        ]
        case .action: return []
        }
    }

    private var questionWords: [(String, Bool)] {
        switch step {
        case .q1: return [("Bugun", false), ("kim", true), ("bo'lib", false), ("uyg'onding?", false)]
        case .q2: return [("Kun", false), ("tugagach,", false), ("qaysi", true), ("ish", false), ("uchun", false), ("xursand", false), ("bo'lasan?", false)]
        case .q3: return [("Hoziroq,", false), ("2 daqiqada", true), ("qaysi", false), ("qadamni", false), ("qila", false), ("olasan?", false)]
        case .action: return [("Mana,", false), ("sening", false), ("kuning.", true), ("Boshlaymizmi?", false)]
        }
    }

    private var prompt: String {
        switch step {
        case .q1: return "— ko'nglingdan kelgan birini tanla —"
        case .q2: return "— miyangga aniq manzara ber —"
        case .q3: return "— eng ahmoqona kichik narsa ham bo'ladi —"
        case .action: return "— uchta savol, uchta javob: harakat tug'ildi —"
        }
    }

    private var primaryLabel: String {
        switch step {
        case .q1, .q2: return "Keyingi savol"
        case .q3: return "Boshlayman"
        case .action: return "Boshlayman"
        }
    }

    private var canAdvance: Bool {
        switch step {
        case .q1: return identity != nil
        case .q2: return priority != nil
        case .q3: return tinyStep != nil
        case .action: return true
        }
    }

    private var reframeText: String {
        switch step {
        case .q1: return "Yaxshi. Kuch — his emas, yo'nalish. Sen aytding \"\(identity ?? "...")\" — endi miyangda dofamin shu so'zga moslashadi."
        case .q2: return "Aniq manzara — miya uchun ozuqa. Endi bu manzara senning prefrontalingda yondi."
        case .q3: return "2 daqiqalik harakat — eng katta to'siqni yengadi. Boshlash — yarmi."
        case .action: return "Sen 3 ta savolga javob berding. Miyangda 3 ta neyron yo'l yondi: identitet, vizualizatsiya, harakat. Qila olishing osonroq."
        }
    }

    // MARK: subviews

    private var headerRow: some View {
        HStack {
            MentorPill("☀  06:42 · TONG", color: MentorColors.gold)
            Spacer()
            HStack(spacing: 6) {
                Text("\(stepIdx)/3").font(MentorFonts.mono(10)).tracking(3).foregroundColor(MentorColors.gold)
                ForEach(1...3, id: \.self) { i in
                    Circle().fill(i <= stepIdx ? MentorColors.gold : MentorColors.textGhost).frame(width: 5, height: 5)
                }
            }
            .padding(.horizontal, 12).padding(.vertical, 5)
            .background(Capsule().fill(MentorColors.gold.opacity(0.06))
                            .overlay(Capsule().strokeBorder(MentorColors.gold.opacity(0.4), lineWidth: 1)))
        }
    }

    private var neuroStrip: some View {
        HStack(spacing: 8) {
            NeuroCard(icon: "☾", value: "7s 14d", label: "UYQU")
            NeuroCard(icon: "☀", value: "5 DAQ", label: "TONG QUYOSHI")
            NeuroCard(icon: "⚡", value: "82%", label: "ENERGIYA")
        }
    }

    private var questionBlock: some View {
        VStack(alignment: .leading, spacing: 8) {
            HStack(alignment: .top, spacing: 12) {
                MentorOrb(size: 28, breathing: false)
                HStack(spacing: 4) {
                    ForEach(Array(questionWords.enumerated()), id: \.offset) { _, item in
                        Text(item.0)
                            .font(item.1 ? MentorFonts.mentorBold(18) : MentorFonts.mentor(18))
                            .foregroundColor(item.1 ? MentorColors.gold : MentorColors.textPrimary)
                    }
                }
            }
            Text(prompt).font(MentorFonts.mentor(11)).foregroundColor(MentorColors.textMuted)
        }
        .padding(14)
        .background(RoundedRectangle(cornerRadius: 8).fill(MentorColors.gold.opacity(0.05))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.gold.opacity(0.4), lineWidth: 1)))
    }

    private var reframeCard: some View {
        VStack(alignment: .leading, spacing: 6) {
            Text("MURABBIY").font(MentorFonts.mono(9).weight(.semibold)).tracking(4).foregroundColor(MentorColors.emeraldBright)
            Text(reframeText).font(MentorFonts.mentor(13)).foregroundColor(MentorColors.textBody).lineSpacing(3)
        }
        .padding(14).frame(maxWidth: .infinity, alignment: .leading)
        .background(RoundedRectangle(cornerRadius: 8).fill(MentorColors.emeraldBright.opacity(0.06))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.emeraldBright.opacity(0.3), lineWidth: 1)))
    }

    private var planCard: some View {
        VStack(alignment: .leading, spacing: 10) {
            HStack {
                Text("BUGUNGI ENG MUHIM").font(MentorFonts.mono(9)).tracking(4).foregroundColor(MentorColors.goldDeep)
                Spacer()
                Text("07:00 — 07:30").font(MentorFonts.mono(9)).tracking(1).foregroundColor(MentorColors.gold)
            }
            HStack(spacing: 12) {
                ZStack {
                    Circle().fill(MentorColors.gold.opacity(0.2)).overlay(Circle().strokeBorder(MentorColors.gold, lineWidth: 1))
                    Text("1").font(.system(size: 16, weight: .bold)).foregroundColor(MentorColors.gold)
                }
                .frame(width: 34, height: 34)
                VStack(alignment: .leading, spacing: 2) {
                    Text(priority ?? "Birinchi ish").font(.system(size: 16, weight: .semibold)).foregroundColor(MentorColors.textPrimary)
                    Text("Bugun yana bir bobni o'zlashtirish")
                        .font(MentorFonts.mentor(11)).foregroundColor(MentorColors.textMuted)
                }
            }
            HStack(spacing: 8) {
                Text("⚡").font(.system(size: 14)).foregroundColor(MentorColors.gold)
                Text("2 DAQIQADA: KITOBNI OL · BIRINCHI OYAT")
                    .font(.system(size: 10, weight: .semibold)).tracking(2)
                    .foregroundColor(MentorColors.gold)
            }
            .padding(.horizontal, 10).padding(.vertical, 8)
            .background(RoundedRectangle(cornerRadius: 4).fill(Color.white.opacity(0.02))
                            .overlay(RoundedRectangle(cornerRadius: 4).strokeBorder(MentorColors.goldDeep, lineWidth: 1)))
        }
        .padding(14)
        .background(RoundedRectangle(cornerRadius: 8).fill(MentorColors.gold.opacity(0.06))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.gold.opacity(0.4), lineWidth: 1)))
    }

    private var bottomBar: some View {
        VStack(spacing: 6) {
            MentorPrimaryButton(primaryLabel, enabled: canAdvance) {
                switch step {
                case .q1: step = .q2
                case .q2: step = .q3
                case .q3: step = .action
                case .action: onDone(identity ?? "", priority ?? "", tinyStep ?? "")
                }
            }
            Button {} label: {
                Text("— keyin javob beraman —")
                    .font(MentorFonts.mentor(11)).foregroundColor(MentorColors.textMuted)
            }
        }
        .padding(.horizontal, 20).padding(.vertical, 16)
        .background(MentorColors.surfaceVoid)
    }
}

private struct NeuroCard: View {
    let icon: String, value: String, label: String
    var body: some View {
        VStack(spacing: 4) {
            Text(icon).font(.system(size: 16)).foregroundColor(MentorColors.gold)
            Text(value).font(.system(size: 13, weight: .semibold)).foregroundColor(MentorColors.textPrimary)
            Text(label).font(MentorFonts.mono(8)).tracking(2).foregroundColor(MentorColors.textMuted)
        }
        .padding(.horizontal, 10).padding(.vertical, 10)
        .frame(maxWidth: .infinity)
        .background(RoundedRectangle(cornerRadius: 8).fill(Color.white.opacity(0.03))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.textGhost, lineWidth: 1)))
    }
}

private struct AnswerChip: View {
    let a: DailyBriefView.Answer
    let selected: Bool
    let action: () -> Void
    var body: some View {
        Button(action: action) {
            VStack(alignment: .leading, spacing: 6) {
                Text(a.icon).font(.system(size: 18))
                    .foregroundColor(selected ? MentorColors.gold : MentorColors.textBody)
                Text(a.label).font(.system(size: 12, weight: selected ? .semibold : .regular))
                    .foregroundColor(selected ? MentorColors.textPrimary : MentorColors.textBody)
                    .multilineTextAlignment(.leading)
            }
            .padding(12).frame(maxWidth: .infinity, alignment: .leading)
            .background(RoundedRectangle(cornerRadius: 8)
                .fill(selected ? MentorColors.gold.opacity(0.15) : Color.white.opacity(0.03))
                .overlay(RoundedRectangle(cornerRadius: 8)
                    .strokeBorder(selected ? MentorColors.gold : MentorColors.textGhost, lineWidth: 1)))
        }
    }
}

#Preview {
    DailyBriefView()
}
