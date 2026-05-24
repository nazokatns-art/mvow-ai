import SwiftUI

/// Analytical weekly review. Mirrors Android `WeeklyReviewScreen.kt`.
struct WeeklyReviewView: View {
    enum BarTag { case normal, best, rest, today }
    struct DayBar: Identifiable {
        let id = UUID()
        let day: String
        let hours: Double
        let tag: BarTag
    }

    let userName: String
    let weekNumber: Int
    let days: [DayBar]
    let totalHours: Int
    let sessions: Int
    let bypass: Int
    let completed: Int
    let prayerLine: String
    let deltaPct: Int
    let longest: String
    let earliest: String
    let streak: Int

    @State private var animatedHours: Int = 0

    init(
        userName: String = "Aziz",
        weekNumber: Int = 19,
        days: [DayBar] = WeeklyReviewView.sampleWeek,
        totalHours: Int = 47, sessions: Int = 38, bypass: Int = 2,
        completed: Int = 95, prayerLine: String = "5/5", deltaPct: Int = 12,
        longest: String = "2s 14daq", earliest: String = "04:42", streak: Int = 12
    ) {
        self.userName = userName; self.weekNumber = weekNumber; self.days = days
        self.totalHours = totalHours; self.sessions = sessions; self.bypass = bypass
        self.completed = completed; self.prayerLine = prayerLine; self.deltaPct = deltaPct
        self.longest = longest; self.earliest = earliest; self.streak = streak
    }

    var body: some View {
        ZStack(alignment: .bottom) {
            MentorColors.surfaceVoid2.ignoresSafeArea()
            ScrollView {
                VStack(spacing: 18) {
                    headerRow
                    titleBlock
                    heroStat
                    dayChart
                    recordsRow
                    mentorLetter
                    miniStats
                }
                .padding(.horizontal, 20).padding(.top, 28).padding(.bottom, 140)
            }
            bottomBar
        }
        .onAppear {
            animatedHours = 0
            withAnimation(.easeOut(duration: 1.7)) { animatedHours = totalHours }
        }
    }

    private var headerRow: some View {
        HStack {
            MentorPill("✦  HAFTA \(weekNumber) · TAHLIL", color: MentorColors.gold)
            Spacer()
            Image(systemName: "xmark")
                .font(.system(size: 14)).foregroundColor(MentorColors.textMuted)
                .frame(width: 30, height: 30)
                .background(Circle().fill(Color.white.opacity(0.04))
                                .overlay(Circle().strokeBorder(MentorColors.textGhost, lineWidth: 1)))
        }
    }

    private var titleBlock: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("DO'STING — MURABBIY").font(MentorFonts.mono(9).weight(.semibold)).tracking(4).foregroundColor(MentorColors.goldDeep)
            HStack(spacing: 0) {
                Text("\(userName), qara ").font(MentorFonts.mentorBold(22)).foregroundColor(MentorColors.textPrimary)
                Text("nima qilding").font(MentorFonts.mentorBold(22)).foregroundColor(MentorColors.gold)
                Text(" bu hafta.").font(MentorFonts.mentorBold(22)).foregroundColor(MentorColors.textPrimary)
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }

    private var heroStat: some View {
        VStack(spacing: 6) {
            Text("JAMI FOKUS · BU HAFTA").font(MentorFonts.mono(9)).tracking(4).foregroundColor(MentorColors.goldDeep)
            HStack(alignment: .bottom, spacing: 8) {
                Text("\(animatedHours)").font(.system(size: 80, weight: .light)).foregroundColor(MentorColors.textPrimary)
                VStack(alignment: .leading, spacing: 2) {
                    Text("SOAT").font(MentorFonts.mono(11).weight(.semibold)).tracking(3).foregroundColor(MentorColors.gold)
                    Text("↑ +\(deltaPct)% o'tgan hafta").font(MentorFonts.mentor(10)).foregroundColor(MentorColors.emeraldBright)
                }
                .padding(.bottom, 18)
            }
            HStack(spacing: 0) {
                Text("Bu — ").font(MentorFonts.mentor(13)).foregroundColor(MentorColors.textBody)
                Text("\(sessions) sessiya").font(MentorFonts.mentorBold(13)).foregroundColor(MentorColors.gold)
                Text(". Hech qachon shuncha qilmagansan.").font(MentorFonts.mentor(13)).foregroundColor(MentorColors.textBody)
            }
        }
        .frame(maxWidth: .infinity)
    }

    private var dayChart: some View {
        let maxH = days.map { $0.hours }.max() ?? 1
        return VStack(spacing: 14) {
            HStack {
                Text("KUNLAR · SOATDA").font(MentorFonts.mono(9)).tracking(3).foregroundColor(MentorColors.goldDeep)
                Spacer()
                HStack(spacing: 10) {
                    LegendDot(color: MentorColors.gold, text: "FOKUS")
                    LegendDot(color: MentorColors.goldFlash, text: "EN YAXSHI")
                }
            }
            HStack(alignment: .bottom, spacing: 4) {
                ForEach(days) { d in
                    DayBarCol(day: d, maxH: maxH)
                }
            }
            .frame(height: 140)
        }
        .padding(14)
        .background(RoundedRectangle(cornerRadius: 8).fill(Color.white.opacity(0.03))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.textGhost, lineWidth: 1)))
    }

    private var recordsRow: some View {
        HStack(spacing: 8) {
            RecordCard(icon: "⏱", value: longest, label: "EN UZUN\nSESSIYA")
            RecordCard(icon: "☀", value: earliest, label: "EN ERTA\nUYG'ONISH")
            RecordCard(icon: "∞", value: "\(streak) KUN", label: "YANGI\nSTREAK")
        }
    }

    private var mentorLetter: some View {
        VStack(alignment: .leading, spacing: 6) {
            HStack {
                Text("MURABBIY HAQ-QO'NI").font(MentorFonts.mono(9).weight(.semibold)).tracking(3).foregroundColor(MentorColors.goldDeep)
                Spacer()
                Text("— \(userName) ning do'sti").font(MentorFonts.mentor(9)).foregroundColor(MentorColors.textMuted)
            }
            HStack(alignment: .top, spacing: 0) {
                Text("Sen ").font(MentorFonts.mentor(14)).foregroundColor(MentorColors.textBody)
                Text("payshanba").font(MentorFonts.mentorBold(14)).foregroundColor(MentorColors.gold)
                Text(" kuni ").font(MentorFonts.mentor(14)).foregroundColor(MentorColors.textBody)
                Text("8s 18daq").font(MentorFonts.mentorBold(14)).foregroundColor(MentorColors.gold)
                Text(" fokusda turding.").font(MentorFonts.mentor(14)).foregroundColor(MentorColors.textBody)
            }
            Text("Shanbada dam olding — to'g'ri qilding. Daraxtning kuchi shoxchadan emas, ildizdan keladi.")
                .font(MentorFonts.mentor(14)).foregroundColor(MentorColors.textBody).lineSpacing(3)
            Text("— birgaman, ertaga ham")
                .font(MentorFonts.mentor(11)).foregroundColor(MentorColors.gold)
                .frame(maxWidth: .infinity, alignment: .trailing)
        }
        .padding(14)
        .background(RoundedRectangle(cornerRadius: 8).fill(MentorColors.gold.opacity(0.06))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.gold.opacity(0.4), lineWidth: 1)))
    }

    private var miniStats: some View {
        HStack(spacing: 6) {
            MiniCard(value: "\(sessions)", label: "SESSIYA", accent: MentorColors.gold)
            MiniCard(value: "\(bypass)", label: "CHEKINISH", accent: MentorColors.rose)
            MiniCard(value: "\(completed)%", label: "BAJARILDI", accent: MentorColors.emeraldBright)
            MiniCard(value: prayerLine, label: "NAMOZ · O'RTA", accent: Color(red: 0.486, green: 0.659, blue: 0.788))
        }
    }

    private var bottomBar: some View {
        VStack(spacing: 8) {
            MentorPrimaryButton("Yangi haftani boshlayman") {}
            HStack(spacing: 8) {
                SecondaryChip(text: "Ulashish")
                SecondaryChip(text: "To'liq tahlil")
            }
        }
        .padding(.horizontal, 20).padding(.vertical, 14)
        .background(MentorColors.surfaceVoid2)
    }

    static let sampleWeek: [DayBar] = [
        .init(day: "DU", hours: 5.5, tag: .normal),
        .init(day: "SE", hours: 7.2, tag: .normal),
        .init(day: "CH", hours: 6.0, tag: .normal),
        .init(day: "PA", hours: 8.3, tag: .best),
        .init(day: "JU", hours: 7.0, tag: .normal),
        .init(day: "SH", hours: 2.0, tag: .rest),
        .init(day: "YA", hours: 3.5, tag: .today)
    ]
}

private struct LegendDot: View {
    let color: Color
    let text: String
    var body: some View {
        HStack(spacing: 4) {
            Circle().fill(color).frame(width: 8, height: 8)
            Text(text).font(MentorFonts.mono(8)).tracking(1).foregroundColor(MentorColors.textMuted)
        }
    }
}

private struct DayBarCol: View {
    let day: WeeklyReviewView.DayBar
    let maxH: Double
    var color: Color {
        switch day.tag {
        case .best: return MentorColors.goldFlash
        case .rest: return Color(red: 0.486, green: 0.659, blue: 0.788)
        case .today: return MentorColors.emeraldBright
        case .normal: return MentorColors.gold
        }
    }
    var body: some View {
        VStack(spacing: 4) {
            Text(String(format: "%.1f", day.hours)).font(.system(size: 9, weight: .semibold)).foregroundColor(color)
            Spacer()
            RoundedRectangle(cornerRadius: 3)
                .fill(color)
                .frame(width: 20, height: CGFloat(day.hours / maxH * 100).max(4))
            Text(day.day)
                .font(MentorFonts.mono(9).weight(day.tag == .today ? .bold : .regular)).tracking(1)
                .foregroundColor(day.tag == .today ? MentorColors.emeraldBright : MentorColors.textMuted)
        }
        .frame(maxWidth: .infinity)
    }
}

private extension CGFloat {
    func max(_ other: CGFloat) -> CGFloat { Swift.max(self, other) }
}

private struct RecordCard: View {
    let icon: String, value: String, label: String
    var body: some View {
        VStack(spacing: 4) {
            Text(icon).font(.system(size: 18)).foregroundColor(MentorColors.gold)
            Text(value).font(.system(size: 14, weight: .semibold)).foregroundColor(MentorColors.textPrimary)
            Text(label).font(MentorFonts.mono(8)).tracking(1.5).foregroundColor(MentorColors.textMuted)
                .multilineTextAlignment(.center)
        }
        .frame(maxWidth: .infinity).padding(.vertical, 12)
        .background(RoundedRectangle(cornerRadius: 8).fill(MentorColors.gold.opacity(0.05))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.gold.opacity(0.3), lineWidth: 1)))
    }
}

private struct MiniCard: View {
    let value: String, label: String, accent: Color
    var body: some View {
        VStack(spacing: 3) {
            Text(value).font(.system(size: 17, weight: .semibold)).foregroundColor(accent)
            Text(label).font(MentorFonts.mono(7.5)).tracking(1.5).foregroundColor(MentorColors.textMuted)
                .multilineTextAlignment(.center)
        }
        .frame(maxWidth: .infinity).padding(.vertical, 10)
        .background(RoundedRectangle(cornerRadius: 6).fill(Color.white.opacity(0.03))
                        .overlay(RoundedRectangle(cornerRadius: 6).strokeBorder(MentorColors.textGhost, lineWidth: 1)))
    }
}

private struct SecondaryChip: View {
    let text: String
    var body: some View {
        Text(text).font(.system(size: 11)).foregroundColor(MentorColors.textMuted)
            .frame(maxWidth: .infinity).frame(height: 40)
            .background(RoundedRectangle(cornerRadius: 4).fill(Color.white.opacity(0.03))
                            .overlay(RoundedRectangle(cornerRadius: 4).strokeBorder(MentorColors.textGhost, lineWidth: 1)))
    }
}

#Preview {
    WeeklyReviewView()
}
