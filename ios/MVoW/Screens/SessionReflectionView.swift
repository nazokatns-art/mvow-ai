import SwiftUI

/// Post-session reflection — closes each task with 3 questions.
/// Mirrors Android `SessionReflectionScreen.kt`.
struct SessionReflectionView: View {
    enum Step { case done, q1, q2, confirmed }
    struct Answer { let icon: String; let label: String }

    @State private var step: Step = .done
    @State private var knowledge: String? = nil
    @State private var strength: String? = nil

    let taskTitle: String
    let durationLabel: String
    let timeRange: String
    let blockedApps: [String]
    let extraBlocked: Int
    let streakDays: Int
    let hoursToday: Double
    let sessionsThisWeek: Int

    init(taskTitle: String = "Qur'on darsi",
         durationLabel: String = "45 DAQ FOKUS",
         timeRange: String = "14:30 — 15:15",
         blockedApps: [String] = ["INSTAGRAM", "TIKTOK", "YOUTUBE"],
         extraBlocked: Int = 2,
         streakDays: Int = 13,
         hoursToday: Double = 4.25,
         sessionsThisWeek: Int = 39) {
        self.taskTitle = taskTitle
        self.durationLabel = durationLabel
        self.timeRange = timeRange
        self.blockedApps = blockedApps
        self.extraBlocked = extraBlocked
        self.streakDays = streakDays
        self.hoursToday = hoursToday
        self.sessionsThisWeek = sessionsThisWeek
    }

    var body: some View {
        ZStack(alignment: .bottom) {
            Color(red: 0.059, green: 0.043, blue: 0.078).ignoresSafeArea()
            ScrollView {
                VStack(spacing: 18) {
                    headerRow
                    BrandSeal(text: sealText)
                    achievementCard
                    switch step {
                    case .done: doneCelebration
                    case .q1: questionBlock(words: q1Words, emIdx: 1, prompt: "— miyaning yangi izi nimaga qoldi —", answers: q1Answers, selected: $knowledge)
                    case .q2: questionBlock(words: q2Words, emIdx: 2, prompt: "— ongli kuch qaytarib chaqirilishi mumkin —", answers: q2Answers, selected: $strength)
                    case .confirmed: confirmedBlock
                    }
                }
                .padding(.horizontal, 20).padding(.top, 28).padding(.bottom, 150)
            }
            bottomBar
        }
    }

    private var sealText: String {
        switch step {
        case .done: return "murabbiy bilan baho beramiz"
        case .q1: return "birinchi savol — bilim"
        case .q2: return "ikkinchi savol — kuch"
        case .confirmed: return "tasdiqlandi · birga davom"
        }
    }
    private var stepIdx: Int {
        switch step {
        case .done: return 1
        case .q1: return 2
        case .q2, .confirmed: return 3
        }
    }

    private let q1Words: [String] = ["Nima", "yangi", "narsani", "ko'rding", "bu", "sessiyada?"]
    private let q2Words: [String] = ["Bu", "kuching", "qaysi qismidan", "keldi?"]
    private let q1Answers: [Answer] = [
        Answer(icon: "◇", label: "Yangi tushuncha"),
        Answer(icon: "∽", label: "Eski narsa — yangicha"),
        Answer(icon: "⚡", label: "O'z chegaramni bildim"),
        Answer(icon: "✦", label: "Boshqa — aytay")
    ]
    private let q2Answers: [Answer] = [
        Answer(icon: "◉", label: "Ertalabki niyat"),
        Answer(icon: "↻", label: "Ko'p kunlik odat"),
        Answer(icon: "☾", label: "Yaxshi uyqu"),
        Answer(icon: "☼", label: "Erta turgan tongim")
    ]

    private var headerRow: some View {
        HStack {
            MentorPill("✓  \(stampLabel)", color: MentorColors.emeraldBright)
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

    private var stampLabel: String {
        switch step {
        case .done: return "SESSIYA TUGADI"
        case .q1: return "BAHO · SAVOL 1/2"
        case .q2: return "BAHO · SAVOL 2/2"
        case .confirmed: return "TASDIQLANDI"
        }
    }

    private var achievementCard: some View {
        VStack(alignment: .leading, spacing: 10) {
            Text(step == .confirmed ? "BAJARILDI VA MUSTAHKAMLANDI" : "BAJARILDI · MUQADDAS")
                .font(MentorFonts.mono(9).weight(.semibold)).tracking(4).foregroundColor(MentorColors.emeraldBright)
            Text(taskTitle).font(MentorFonts.mentorBold(22)).foregroundColor(MentorColors.textPrimary)
            HStack(spacing: 16) {
                Text("✓ \(durationLabel)").font(.system(size: 11)).tracking(2).foregroundColor(MentorColors.gold)
                Text("∼ \(timeRange)").font(.system(size: 11)).tracking(2).foregroundColor(MentorColors.textMuted)
            }
            HStack(spacing: 6) {
                ForEach(blockedApps, id: \.self) { Pill(text: "✓ \($0)") }
                if extraBlocked > 0 { Pill(text: "+ \(extraBlocked)") }
            }
        }
        .padding(14)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(RoundedRectangle(cornerRadius: 8).fill(MentorColors.emeraldBright.opacity(0.06))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.emeraldBright.opacity(0.4), lineWidth: 1)))
    }

    private var doneCelebration: some View {
        VStack(spacing: 10) {
            Text("Bajarding.").font(MentorFonts.mentorBold(30)).foregroundColor(MentorColors.gold)
            HStack(spacing: 0) {
                Text("Yana 45 daqiqa ").font(MentorFonts.mentor(14)).foregroundColor(MentorColors.textBody)
                Text("o'zingdan ozgina kuchliroq").font(MentorFonts.mentorBold(14)).foregroundColor(MentorColors.gold)
                Text(" bo'lding.").font(MentorFonts.mentor(14)).foregroundColor(MentorColors.textBody)
            }
            Text("Endi birga baho beramiz — miyangda nima qoldi?")
                .font(MentorFonts.mentor(14)).foregroundColor(MentorColors.textBody).multilineTextAlignment(.center)
        }
    }

    private func questionBlock(words: [String], emIdx: Int, prompt: String, answers: [Answer], selected: Binding<String?>) -> some View {
        VStack(spacing: 12) {
            HStack(alignment: .top, spacing: 12) {
                MentorOrb(size: 28, breathing: false)
                VStack(alignment: .leading, spacing: 6) {
                    HStack(spacing: 4) {
                        ForEach(Array(words.enumerated()), id: \.offset) { i, w in
                            Text(w).font(i == emIdx ? MentorFonts.mentorBold(18) : MentorFonts.mentor(18))
                                .foregroundColor(i == emIdx ? MentorColors.gold : MentorColors.textPrimary)
                        }
                    }
                    Text(prompt).font(MentorFonts.mentor(11)).foregroundColor(MentorColors.textMuted)
                }
            }
            .padding(14)
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(RoundedRectangle(cornerRadius: 8).fill(MentorColors.gold.opacity(0.05))
                            .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.gold.opacity(0.4), lineWidth: 1)))

            VStack(spacing: 6) {
                ForEach(Array(stride(from: 0, to: answers.count, by: 2)), id: \.self) { i in
                    HStack(spacing: 6) {
                        ReflectChip(a: answers[i], selected: selected.wrappedValue == answers[i].label) {
                            selected.wrappedValue = answers[i].label
                        }
                        if i + 1 < answers.count {
                            ReflectChip(a: answers[i + 1], selected: selected.wrappedValue == answers[i + 1].label) {
                                selected.wrappedValue = answers[i + 1].label
                            }
                        } else { Spacer().frame(maxWidth: .infinity) }
                    }
                }
            }
        }
    }

    private var confirmedBlock: some View {
        VStack(spacing: 14) {
            // Identity
            VStack(alignment: .leading, spacing: 8) {
                Text("SEN — KIM BO'LDING BU LAHZADA").font(MentorFonts.mono(9)).tracking(4).foregroundColor(MentorColors.goldDeep)
                HStack(spacing: 0) {
                    Text("Sen — ").font(MentorFonts.mentor(15)).foregroundColor(MentorColors.textBody)
                    Text("o'z so'zida turgan odam").font(MentorFonts.mentorBold(15)).foregroundColor(MentorColors.gold)
                    Text(".").font(MentorFonts.mentor(15)).foregroundColor(MentorColors.textBody)
                }
                Text("Reja qilding, qilding. Bu — yangi sen.")
                    .font(MentorFonts.mentor(14)).foregroundColor(MentorColors.textBody)
            }
            .padding(14)
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(RoundedRectangle(cornerRadius: 8).fill(MentorColors.gold.opacity(0.06))
                            .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.gold.opacity(0.4), lineWidth: 1)))

            HStack(spacing: 8) {
                StatMini(change: "+1", value: "\(streakDays)", label: "KUN STREAK")
                StatMini(change: "+0.75", value: String(format: "%.2f", hoursToday), label: "SOAT BUGUN")
                StatMini(change: "+1", value: "\(sessionsThisWeek)", label: "SESSIYA HAFT")
            }
        }
    }

    private var bottomBar: some View {
        VStack(spacing: 6) {
            MentorPrimaryButton(primaryLabel, enabled: canAdvance) {
                switch step {
                case .done: step = .q1
                case .q1: step = .q2
                case .q2: step = .confirmed
                case .confirmed: break
                }
            }
        }
        .padding(.horizontal, 20).padding(.vertical, 16)
        .background(Color(red: 0.059, green: 0.043, blue: 0.078))
    }

    private var primaryLabel: String {
        switch step {
        case .done: return "Davom etamiz"
        case .q1: return "Keyingi savol"
        case .q2: return "Tasdiqlash"
        case .confirmed: return "Bosh sahifa"
        }
    }
    private var canAdvance: Bool {
        switch step {
        case .q1: return knowledge != nil
        case .q2: return strength != nil
        default: return true
        }
    }
}

private struct Pill: View {
    let text: String
    var body: some View {
        Text(text).font(.system(size: 8)).tracking(1).foregroundColor(MentorColors.textMuted)
            .padding(.horizontal, 8).padding(.vertical, 3)
            .background(Capsule().fill(Color.white.opacity(0.03))
                            .overlay(Capsule().strokeBorder(MentorColors.textGhost, lineWidth: 1)))
    }
}

private struct ReflectChip: View {
    let a: SessionReflectionView.Answer
    let selected: Bool
    let action: () -> Void
    var body: some View {
        Button(action: action) {
            VStack(alignment: .leading, spacing: 6) {
                Text(a.icon).font(.system(size: 16))
                    .foregroundColor(selected ? MentorColors.gold : MentorColors.textBody)
                Text(a.label).font(.system(size: 12, weight: selected ? .semibold : .regular))
                    .foregroundColor(selected ? MentorColors.textPrimary : MentorColors.textBody)
            }
            .padding(12).frame(maxWidth: .infinity, alignment: .leading)
            .background(RoundedRectangle(cornerRadius: 8)
                .fill(selected ? MentorColors.gold.opacity(0.15) : Color.white.opacity(0.03))
                .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(selected ? MentorColors.gold : MentorColors.textGhost, lineWidth: 1)))
        }
    }
}

private struct StatMini: View {
    let change: String, value: String, label: String
    var body: some View {
        VStack(spacing: 4) {
            Text(change).font(.system(size: 11, weight: .bold)).foregroundColor(MentorColors.emeraldBright)
            Text(value).font(.system(size: 22, weight: .semibold)).foregroundColor(MentorColors.textPrimary)
            Text(label).font(MentorFonts.mono(8)).tracking(2).foregroundColor(MentorColors.textMuted)
                .multilineTextAlignment(.center)
        }
        .frame(maxWidth: .infinity).padding(10)
        .background(RoundedRectangle(cornerRadius: 8).fill(MentorColors.emeraldBright.opacity(0.05))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.emeraldBright.opacity(0.3), lineWidth: 1)))
    }
}

#Preview {
    SessionReflectionView()
}
