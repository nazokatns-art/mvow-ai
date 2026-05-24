import SwiftUI

/// Stuck — user paralysis mid-task; mentor question-driven unblocking.
/// Mirrors Android `StuckScreen.kt`.
struct StuckView: View {
    enum Step { case recognize, q1Feeling, q2Protecting, q3TinyStep, action }

    struct Answer { let icon: String; let label: String }

    @State private var step: Step = .recognize
    @State private var feeling: String? = nil
    @State private var protecting: String? = nil
    @State private var tinyStep: String? = nil

    let stuckMinutes: Int
    var onResolve: (String?, String?, String?) -> Void = { _, _, _ in }
    var onDeferTalk: () -> Void = {}

    init(stuckMinutes: Int = 14,
         onResolve: @escaping (String?, String?, String?) -> Void = { _, _, _ in },
         onDeferTalk: @escaping () -> Void = {}) {
        self.stuckMinutes = stuckMinutes
        self.onResolve = onResolve
        self.onDeferTalk = onDeferTalk
    }

    var body: some View {
        ZStack(alignment: .bottom) {
            Color(red: 0.039, green: 0.031, blue: 0.063).ignoresSafeArea()
            ScrollView {
                VStack(spacing: 18) {
                    headerRow
                    BrandSeal(text: sealText)
                    if let rec = recognition {
                        recognitionBlock(words: rec.words, emIndex: rec.emIndex)
                    }
                    if step == .recognize { actionCard }
                    if !answersForStep.isEmpty {
                        Text(promptText)
                            .font(MentorFonts.mentor(12))
                            .foregroundColor(MentorColors.textMuted)
                            .frame(maxWidth: .infinity, alignment: .leading)
                        VStack(spacing: 6) {
                            ForEach(answersForStep, id: \.label) { a in
                                AnswerRow(icon: a.icon, label: a.label, selected: a.label == currentPick) {
                                    pick(a.label)
                                }
                            }
                        }
                    }
                    if !reframeText.isEmpty { reframeCard(text: reframeText) }
                }
                .padding(.horizontal, 20)
                .padding(.top, 28)
                .padding(.bottom, 140)
            }
            bottomBar
        }
    }

    // MARK: state mapping

    private struct Recognition { let words: [String]; let emIndex: Int }

    private var sealText: String {
        switch step {
        case .recognize: return "murabbiy ko'rdi"
        case .q1Feeling: return "birinchi savol — nom ber"
        case .q2Protecting: return "ikkinchi savol — chuqurroq"
        case .q3TinyStep: return "uchinchi savol — qadam"
        case .action: return "tuman ko'tarildi"
        }
    }

    private var stepIndex: Int {
        switch step {
        case .recognize: return 0
        case .q1Feeling: return 1
        case .q2Protecting: return 2
        case .q3TinyStep, .action: return 3
        }
    }

    private var statusLabel: String {
        switch step {
        case .recognize: return "TURG'UNLIK · \(stuckMinutes) DAQ"
        case .q1Feeling: return "SAVOL · 1"
        case .q2Protecting: return "SAVOL · 2"
        case .q3TinyStep: return "SAVOL · 3"
        case .action: return "TINIQ · TAYYORMAN"
        }
    }

    private var recognition: Recognition? {
        switch step {
        case .recognize: return .init(words: ["Sen", "\(stuckMinutes)", "daqiqa", "ekranga", "qaragancha", "turibsan."], emIndex: 4)
        case .q1Feeling: return .init(words: ["Eng", "yaqin", "his", "—", "qaysi?"], emIndex: 2)
        case .q2Protecting: return .init(words: ["Bu", "his", "sendan", "nimani", "himoya", "qilyapti?"], emIndex: 1)
        case .q3TinyStep: return .init(words: ["Hoziroq,", "2 daqiqada", "qaysi", "arzimas", "qadamni", "qila", "olasan?"], emIndex: 1)
        case .action: return nil
        }
    }

    private var answersForStep: [Answer] {
        switch step {
        case .recognize: return [
            Answer(icon: "∼", label: "Tushunmadim, men shu yerdaman."),
            Answer(icon: "◇", label: "Boshlay olmayapman."),
            Answer(icon: "◯", label: "Charchadim, energiya yo'q."),
            Answer(icon: "⌖", label: "Ahamiyatsiz his qilyapman.")
        ]
        case .q1Feeling: return [
            Answer(icon: "◐", label: "Qo'rquv — boshlamaslik qo'rqinchli."),
            Answer(icon: "◑", label: "Charchoq — bu og'ir."),
            Answer(icon: "◒", label: "Ma'nosizlik — nimaga kerakligini sezmayman."),
            Answer(icon: "◓", label: "Boshqa — aytaman.")
        ]
        case .q2Protecting: return [
            Answer(icon: "✺", label: "Mag'lubiyatdan."),
            Answer(icon: "✻", label: "Charchoqdan."),
            Answer(icon: "✼", label: "Mas'uliyatdan."),
            Answer(icon: "✽", label: "Notanishdan.")
        ]
        case .q3TinyStep: return [
            Answer(icon: "▥", label: "Kitobni qo'lga olish."),
            Answer(icon: "▦", label: "Birinchi sahifani ochish."),
            Answer(icon: "▧", label: "Stol oldida o'tirish."),
            Answer(icon: "▩", label: "Telefonni boshqa xonaga qo'yish.")
        ]
        case .action: return []
        }
    }

    private var promptText: String {
        switch step {
        case .recognize: return "— men hukm qilmayman, savol beraman —"
        case .q1Feeling: return "— his-ni nomlash uni kichraytiradi —"
        case .q2Protecting: return "— har his bir ehtiyojni ifoda qiladi —"
        case .q3TinyStep: return "— eng kichik harakat ham harakat —"
        case .action: return ""
        }
    }

    private var currentPick: String? {
        switch step {
        case .recognize, .q1Feeling: return feeling
        case .q2Protecting: return protecting
        case .q3TinyStep: return tinyStep
        case .action: return nil
        }
    }

    private func pick(_ label: String) {
        switch step {
        case .recognize, .q1Feeling: feeling = label
        case .q2Protecting: protecting = label
        case .q3TinyStep: tinyStep = label
        case .action: break
        }
    }

    private var reframeText: String {
        switch step {
        case .q2Protecting:
            return "Yaxshi. Tan oldim. \(feeling ?? "His") — dushman emas, signal. Endi miyangda amigdala tinch bo'ldi — savol bilan System 2 yondi."
        case .q3TinyStep:
            return "Sen ko'rding — qo'rquv mag'lubiyatdan emas, notanishlikdan kelyapti. Notanish — bilim bilan ochiladi. Bilim — kichik qadamdan."
        case .action:
            return "Sen 3 ta savolga javob berding. Miyangda 3 ta neyron yo'l yondi: tan olish, ma'no, harakat. Tuman ko'tarildi."
        default: return ""
        }
    }

    private var canAdvance: Bool {
        switch step {
        case .action: return true
        default: return currentPick != nil || (step == .recognize && feeling != nil)
        }
    }

    private var primaryLabel: String {
        switch step {
        case .recognize: return "Tayyorman, boshlaymiz"
        case .q1Feeling, .q2Protecting: return "Davom etamiz"
        case .q3TinyStep: return "Boshlayman"
        case .action: return "Birinchi qadamni qilaman"
        }
    }

    // MARK: subviews

    private var headerRow: some View {
        HStack {
            MentorPill(statusLabel, color: MentorColors.rose, leadingDot: true)
            Spacer()
            HStack(spacing: 8) {
                Text("\(stepIndex)/3")
                    .font(MentorFonts.mono(10))
                    .tracking(3)
                    .foregroundColor(MentorColors.gold)
                ForEach(1...3, id: \.self) { i in
                    Circle().fill(i <= stepIndex ? MentorColors.gold : MentorColors.textGhost).frame(width: 5, height: 5)
                }
            }
            .padding(.horizontal, 12).padding(.vertical, 5)
            .background(Capsule().fill(MentorColors.gold.opacity(0.06))
                            .overlay(Capsule().strokeBorder(MentorColors.gold.opacity(0.4), lineWidth: 1)))
        }
    }

    private func recognitionBlock(words: [String], emIndex: Int) -> some View {
        HStack(alignment: .top, spacing: 12) {
            MentorOrb(size: 28, breathing: false)
            HStack(spacing: 4) {
                ForEach(Array(words.enumerated()), id: \.offset) { i, w in
                    Text(w)
                        .font(i == emIndex ? MentorFonts.mentorBold(18) : MentorFonts.mentor(18))
                        .foregroundColor(i == emIndex ? MentorColors.rose : MentorColors.textPrimary)
                }
            }
            Spacer()
        }
        .padding(14)
        .background(
            RoundedRectangle(cornerRadius: 8)
                .fill(Color(red: 0.420, green: 0.361, blue: 0.557).opacity(0.10))
                .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(Color(red: 0.420, green: 0.361, blue: 0.557), lineWidth: 1))
        )
    }

    private var actionCard: some View {
        VStack(alignment: .leading, spacing: 10) {
            Text("SEN — KIM")
                .font(MentorFonts.mono(9)).tracking(4).foregroundColor(MentorColors.goldDeep)
            HStack(spacing: 0) {
                Text("Sen — ").font(MentorFonts.mentor(14)).foregroundColor(MentorColors.textBody)
                Text("turg'unlikdan chiqishni").font(MentorFonts.mentorBold(14)).foregroundColor(MentorColors.gold)
                Text(" biladigan odam.").font(MentorFonts.mentor(14)).foregroundColor(MentorColors.textBody)
            }
            HStack(spacing: 12) {
                ZStack {
                    Circle().fill(MentorColors.gold.opacity(0.2)).overlay(Circle().strokeBorder(MentorColors.gold, lineWidth: 1))
                    Text("1").font(.system(size: 16, weight: .bold)).foregroundColor(MentorColors.gold)
                }
                .frame(width: 34, height: 34)
                VStack(alignment: .leading, spacing: 2) {
                    Text("2 daqiqalik qadam").font(.system(size: 14, weight: .semibold)).foregroundColor(MentorColors.textPrimary)
                    Text("Kitobni qo'lga ol — birinchi sahifa.")
                        .font(MentorFonts.mentor(11)).foregroundColor(MentorColors.textMuted)
                }
            }
        }
        .padding(14)
        .background(
            RoundedRectangle(cornerRadius: 8)
                .fill(MentorColors.gold.opacity(0.06))
                .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.gold.opacity(0.4), lineWidth: 1))
        )
    }

    private func reframeCard(text: String) -> some View {
        VStack(alignment: .leading, spacing: 6) {
            Text("MURABBIY · QAYTAR").font(MentorFonts.mono(9).weight(.semibold)).tracking(4)
                .foregroundColor(Color(red: 0.420, green: 0.361, blue: 0.557))
            Text(text).font(MentorFonts.mentor(13)).foregroundColor(MentorColors.textBody).lineSpacing(3)
        }
        .padding(14)
        .background(
            RoundedRectangle(cornerRadius: 8)
                .fill(Color(red: 0.420, green: 0.361, blue: 0.557).opacity(0.10))
                .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(Color(red: 0.420, green: 0.361, blue: 0.557).opacity(0.4), lineWidth: 1))
        )
    }

    private var bottomBar: some View {
        VStack(spacing: 6) {
            MentorPrimaryButton(primaryLabel, enabled: canAdvance) {
                advance()
            }
            if step != .action {
                Button(action: onDeferTalk) {
                    Text("— hozir gapirgim yo'q —")
                        .font(MentorFonts.mentor(11))
                        .foregroundColor(MentorColors.textMuted)
                }
            }
        }
        .padding(.horizontal, 20)
        .padding(.vertical, 16)
        .background(Color(red: 0.039, green: 0.031, blue: 0.063))
    }

    private func advance() {
        switch step {
        case .recognize: step = .q1Feeling
        case .q1Feeling: step = .q2Protecting
        case .q2Protecting: step = .q3TinyStep
        case .q3TinyStep: step = .action
        case .action: onResolve(feeling, protecting, tinyStep)
        }
    }
}

private struct AnswerRow: View {
    let icon: String
    let label: String
    let selected: Bool
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            HStack(spacing: 12) {
                Text(icon)
                    .font(.system(size: 18))
                    .foregroundColor(selected ? MentorColors.gold : MentorColors.textBody)
                Text(label)
                    .font(MentorFonts.mentor(13))
                    .foregroundColor(selected ? MentorColors.textPrimary : MentorColors.textBody)
                Spacer()
            }
            .padding(.horizontal, 14).padding(.vertical, 12)
            .background(
                RoundedRectangle(cornerRadius: 8)
                    .fill(selected ? MentorColors.gold.opacity(0.15) : Color.white.opacity(0.03))
                    .overlay(RoundedRectangle(cornerRadius: 8)
                                .strokeBorder(selected ? MentorColors.gold : MentorColors.textGhost, lineWidth: 1))
            )
        }
    }
}

#Preview {
    StuckView()
}
