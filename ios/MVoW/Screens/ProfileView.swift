import SwiftUI

/// "SEN" profile page. Mirrors Android `ProfileScreen.kt`.
struct ProfileView: View {
    enum GrowthStage {
        case seed, sapling, tree, oak
        var label: String { ["URUG'", "KURTAK", "DARAXT", "ORZUDAGI"][rawIndex] }
        var meta: String {
            ["birinchi halqa endi shakllanyapti", "ildizing chuqurroq tushyapti", "har hafta yangi halqa", "12 hafta — chuqur ildiz"][rawIndex]
        }
        var rings: Int { [1, 3, 5, 8][rawIndex] }
        var rawIndex: Int {
            switch self {
            case .seed: return 0
            case .sapling: return 1
            case .tree: return 2
            case .oak: return 3
            }
        }
    }

    struct HabitChip { let icon: String; let name: String; let daysOld: Int }

    let userName: String
    let sinceLabel: String
    let becomingText: String
    let becomingEm: String
    let streakNow: Int
    let bestStreak: Int
    let totalSessions: Int
    let totalHours: Int
    let stage: GrowthStage
    let habits: [HabitChip]
    let relationDepth: String
    let relationText: String
    let relationEm: String

    init(
        userName: String = "Aziz",
        sinceLabel: String = "12 KUN YO'L · 2026-MAY-3 DAN",
        becomingText: String = "O'z so'zida turadigan odam — bu sen bo'lyapsan.",
        becomingEm: String = "O'z so'zida turadigan",
        streakNow: Int = 12,
        bestStreak: Int = 12,
        totalSessions: Int = 38,
        totalHours: Int = 47,
        stage: GrowthStage = .tree,
        habits: [HabitChip] = [
            HabitChip(icon: "☾", name: "Tonggi namoz", daysOld: 12),
            HabitChip(icon: "⊙", name: "Yugurish · 5 km", daysOld: 9),
            HabitChip(icon: "▥", name: "Mutolaa · 30 daq", daysOld: 7)
        ],
        relationDepth: String = "CHUQUR TANISHUV",
        relationText: String = "Sening ritmingni bilaman. Tongda kuchli, tushga charchaysan, kechqurun yana yumshaysan.",
        relationEm: String = "Sening ritmingni"
    ) {
        self.userName = userName
        self.sinceLabel = sinceLabel
        self.becomingText = becomingText
        self.becomingEm = becomingEm
        self.streakNow = streakNow
        self.bestStreak = bestStreak
        self.totalSessions = totalSessions
        self.totalHours = totalHours
        self.stage = stage
        self.habits = habits
        self.relationDepth = relationDepth
        self.relationText = relationText
        self.relationEm = relationEm
    }

    var body: some View {
        ZStack {
            MentorColors.surfaceVoid2.ignoresSafeArea()
            ScrollView {
                VStack(spacing: 18) {
                    headerRow
                    identityHero
                    becomingCard
                    statsGrid
                    growthBlock
                    habitsBlock
                    relationBlock
                }
                .padding(.horizontal, 20).padding(.top, 28).padding(.bottom, 40)
            }
        }
    }

    private var headerRow: some View {
        HStack {
            IconCircle(symbol: "chevron.left")
            Spacer()
            Text("SEN").font(.system(size: 11, weight: .bold)).tracking(6).foregroundColor(MentorColors.gold)
                .padding(.horizontal, 14).padding(.vertical, 6)
                .background(Capsule().fill(MentorColors.gold.opacity(0.10))
                                .overlay(Capsule().strokeBorder(MentorColors.gold, lineWidth: 1)))
            Spacer()
            IconCircle(symbol: "gearshape.fill")
        }
    }

    private var identityHero: some View {
        VStack(spacing: 16) {
            ZStack {
                Circle().fill(MentorColors.gold.opacity(0.15)).frame(width: 120, height: 120)
                Circle().strokeBorder(MentorColors.gold.opacity(0.3), lineWidth: 1).frame(width: 104, height: 104)
                Circle().fill(MentorColors.gold.opacity(0.20)).frame(width: 86, height: 86)
                    .overlay(Circle().strokeBorder(MentorColors.gold, lineWidth: 2))
                Text("✦").font(.system(size: 32)).foregroundColor(MentorColors.goldFlash)
            }
            Text(userName).font(MentorFonts.mentorBold(26)).foregroundColor(MentorColors.textPrimary)
            Text(sinceLabel)
                .font(MentorFonts.mono(9).weight(.semibold)).tracking(4)
                .foregroundColor(MentorColors.goldDeep)
        }
    }

    private var becomingCard: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("SEN — KIM BO'LIB BORMOQDASAN").font(MentorFonts.mono(9).weight(.semibold)).tracking(4).foregroundColor(MentorColors.goldDeep)
            HStack(spacing: 0) {
                Text(becomingEm).font(MentorFonts.mentorBold(14)).foregroundColor(MentorColors.gold)
                Text(becomingText.replacingOccurrences(of: becomingEm, with: ""))
                    .font(MentorFonts.mentor(14)).foregroundColor(MentorColors.textBody)
            }
        }
        .padding(14).frame(maxWidth: .infinity, alignment: .leading)
        .background(RoundedRectangle(cornerRadius: 8).fill(MentorColors.gold.opacity(0.05))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.gold.opacity(0.3), lineWidth: 1)))
    }

    private var statsGrid: some View {
        HStack(spacing: 8) {
            StatCard(value: "\(streakNow)", unit: "K", label: "STREAK\nHOZIR", accent: MentorColors.gold)
            StatCard(value: "\(bestStreak)", unit: "K", label: "ENG\nUZUN", accent: MentorColors.goldFlash)
            StatCard(value: "\(totalSessions)", unit: "", label: "JAMI\nSESSIYA", accent: MentorColors.emeraldBright)
            StatCard(value: "\(totalHours)", unit: "S", label: "JAMI\nFOKUS", accent: Color(red: 0.486, green: 0.659, blue: 0.788))
        }
    }

    private var growthBlock: some View {
        VStack(spacing: 14) {
            HStack {
                Text("DARAXTING · \(stage.label)").font(MentorFonts.mono(9).weight(.semibold)).tracking(3).foregroundColor(MentorColors.emeraldBright)
                Spacer()
                Text(stage.meta).font(MentorFonts.mentor(9)).foregroundColor(MentorColors.textMuted)
            }
            ZStack {
                ForEach(1...stage.rings, id: \.self) { i in
                    Circle()
                        .strokeBorder(MentorColors.emeraldBright.opacity(0.15 + 0.4 * Double(i) / Double(stage.rings)), lineWidth: 1.5)
                        .frame(width: CGFloat(28 + i * 14), height: CGFloat(28 + i * 14))
                }
                Circle().fill(MentorColors.emeraldBright).frame(width: 18, height: 18)
            }
            .frame(height: 140)
        }
        .padding(14)
        .background(RoundedRectangle(cornerRadius: 8).fill(MentorColors.emeraldBright.opacity(0.06))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.emeraldBright.opacity(0.3), lineWidth: 1)))
    }

    private var habitsBlock: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("SHAKLANGAN ODATLAR · \(habits.count)")
                .font(MentorFonts.mono(9).weight(.semibold)).tracking(3).foregroundColor(MentorColors.goldDeep)
            if habits.isEmpty {
                Text("·  HALI ODAT YO'Q — BIRINCHISINI QO'SH")
                    .font(MentorFonts.mono(10)).tracking(2).foregroundColor(MentorColors.textMuted)
                    .padding(14)
                    .frame(maxWidth: .infinity)
                    .background(RoundedRectangle(cornerRadius: 8).fill(Color.white.opacity(0.02))
                                    .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.textGhost, lineWidth: 1)))
            } else {
                VStack(spacing: 6) {
                    ForEach(Array(habits.enumerated()), id: \.offset) { _, h in
                        HStack(spacing: 10) {
                            Text(h.icon).font(.system(size: 16)).foregroundColor(MentorColors.gold)
                            Text(h.name).font(.system(size: 13)).foregroundColor(MentorColors.textPrimary)
                            Spacer()
                            Text("\(h.daysOld) kun").font(MentorFonts.mono(10).weight(.semibold)).tracking(1).foregroundColor(MentorColors.gold)
                        }
                        .padding(.horizontal, 12).padding(.vertical, 10)
                        .background(RoundedRectangle(cornerRadius: 8).fill(MentorColors.gold.opacity(0.05))
                                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.gold.opacity(0.3), lineWidth: 1)))
                    }
                }
            }
        }
    }

    private var relationBlock: some View {
        VStack(alignment: .leading, spacing: 8) {
            HStack {
                Text("MURABBIY · MEN BILAN").font(MentorFonts.mono(9).weight(.semibold)).tracking(3).foregroundColor(MentorColors.goldDeep)
                Spacer()
                Text(relationDepth).font(MentorFonts.mono(9)).tracking(2).foregroundColor(MentorColors.gold)
            }
            HStack(spacing: 0) {
                Text(relationEm).font(MentorFonts.mentorBold(13)).foregroundColor(MentorColors.gold)
                Text(relationText.replacingOccurrences(of: relationEm, with: ""))
                    .font(MentorFonts.mentor(13)).foregroundColor(MentorColors.textBody)
            }
        }
        .padding(14).frame(maxWidth: .infinity, alignment: .leading)
        .background(RoundedRectangle(cornerRadius: 8).fill(MentorColors.gold.opacity(0.05))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.gold.opacity(0.3), lineWidth: 1)))
    }
}

private struct StatCard: View {
    let value: String, unit: String, label: String, accent: Color
    var body: some View {
        VStack(spacing: 4) {
            HStack(alignment: .bottom, spacing: 2) {
                Text(value).font(.system(size: 24, weight: .bold)).foregroundColor(accent)
                if !unit.isEmpty {
                    Text(unit).font(.system(size: 11)).foregroundColor(accent.opacity(0.6))
                        .padding(.bottom, 3)
                }
            }
            Text(label).font(MentorFonts.mono(8)).tracking(1.5).foregroundColor(MentorColors.textMuted)
                .multilineTextAlignment(.center).lineLimit(2)
        }
        .frame(maxWidth: .infinity).padding(.vertical, 12).padding(.horizontal, 6)
        .background(RoundedRectangle(cornerRadius: 8).fill(Color.white.opacity(0.03))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.textGhost, lineWidth: 1)))
    }
}

private struct IconCircle: View {
    let symbol: String
    var body: some View {
        Image(systemName: symbol)
            .font(.system(size: 14))
            .foregroundColor(MentorColors.textMuted)
            .frame(width: 34, height: 34)
            .background(Circle().fill(Color.white.opacity(0.04))
                            .overlay(Circle().strokeBorder(MentorColors.textGhost, lineWidth: 1)))
    }
}

#Preview {
    ProfileView()
}
