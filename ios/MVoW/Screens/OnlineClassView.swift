import SwiftUI

/// Online class — contextual whitelist per platform. Mirrors Android `OnlineClassScreen.kt`.
struct OnlineClassView: View {
    enum ClassMode: CaseIterable {
        case telegram, zoom, meet, teams, discord, masterclass, coursera

        var tab: String {
            switch self {
            case .telegram: return "TELEGRAM"
            case .zoom: return "ZOOM"
            case .meet: return "G·MEET"
            case .teams: return "TEAMS"
            case .discord: return "DISCORD"
            case .masterclass: return "MASTERKLASS"
            case .coursera: return "COURSERA"
            }
        }
        var modeName: String { tab + " DARS" }
        var source: String {
            switch self {
            case .telegram: return "TELEGRAM · @algoritm_dars_2026"
            case .zoom: return "ZOOM · MEETING 845-237-1102"
            case .meet: return "GOOGLE MEET · meet/abc-defg-hij"
            case .teams: return "TEAMS · Loyiha guruhi"
            case .discord: return "DISCORD · Coding Voice"
            case .masterclass: return "YOUTUBE · ALI ABDAAL — DEEP WORK"
            case .coursera: return "COURSERA · Machine Learning · Hafta 4"
            }
        }
        var sourceColor: Color {
            switch self {
            case .telegram: return Color(red: 0.165, green: 0.671, blue: 0.933)
            case .zoom: return Color(red: 0.176, green: 0.549, blue: 1.0)
            case .meet: return Color(red: 0.0, green: 0.537, blue: 0.482)
            case .teams: return Color(red: 0.275, green: 0.306, blue: 0.722)
            case .discord: return Color(red: 0.345, green: 0.396, blue: 0.949)
            case .masterclass: return Color(red: 1.0, green: 0.0, blue: 0.0)
            case .coursera: return Color(red: 0.0, green: 0.337, blue: 0.824)
            }
        }
        var title: String {
            switch self {
            case .telegram: return "Algoritm darsi · 12-mavzu"
            case .zoom: return "Ingliz tili · B2 darsi"
            case .meet: return "Loyiha kengashi"
            case .teams: return "Stand-up meeting"
            case .discord: return "Birga ishlash · code session"
            case .masterclass: return "Deep Work — masterklass"
            case .coursera: return "Coursera · ML kursi"
            }
        }
        var schedule: String {
            switch self {
            case .telegram: return "19:00 — 20:30"
            case .zoom: return "20:00 — 21:30"
            case .meet: return "15:00 — 16:00"
            case .teams: return "09:00 — 09:30"
            case .discord: return "21:00 — 23:00"
            case .masterclass: return "18:30 — 20:00"
            case .coursera: return "14:00 — 15:30"
            }
        }
        var duration: String {
            switch self {
            case .telegram, .zoom, .masterclass, .coursera: return "90 DAQ"
            case .meet: return "60 DAQ"
            case .teams: return "30 DAQ"
            case .discord: return "120 DAQ"
            }
        }
        var allowed: [(String, String, Color)] {
            switch self {
            case .telegram: return [("T", "TELEGRAM", Color(red: 0.165, green: 0.671, blue: 0.933)),
                                    ("Z", "ZOOM", Color(red: 0.176, green: 0.549, blue: 1.0))]
            case .zoom: return [("Z", "ZOOM", Color(red: 0.176, green: 0.549, blue: 1.0)),
                                ("T", "TELEGRAM", Color(red: 0.165, green: 0.671, blue: 0.933))]
            case .meet: return [("M", "G·MEET", Color(red: 0.0, green: 0.537, blue: 0.482)),
                                ("T", "TELEGRAM", Color(red: 0.165, green: 0.671, blue: 0.933))]
            case .teams: return [("T", "TEAMS", Color(red: 0.275, green: 0.306, blue: 0.722))]
            case .discord: return [("D", "DISCORD", Color(red: 0.345, green: 0.396, blue: 0.949)),
                                   ("G", "GITHUB", Color(red: 0.051, green: 0.067, blue: 0.090))]
            case .masterclass: return [("Y", "YOUTUBE", Color(red: 1.0, green: 0.0, blue: 0.0)),
                                       ("N", "NOTION", Color(red: 0.710, green: 0.710, blue: 0.710))]
            case .coursera: return [("C", "COURSERA", Color(red: 0.0, green: 0.337, blue: 0.824)),
                                    ("N", "NOTION", Color(red: 0.710, green: 0.710, blue: 0.710))]
            }
        }
        var noteLead: String {
            switch self {
            case .telegram: return "Telegram'da "
            case .zoom: return ""
            case .meet: return "Kengashda "
            case .teams: return "Stand-up qisqa — "
            case .discord: return "Discord'da "
            case .masterclass: return "Faqat "
            case .coursera: return "Kurs videosini ko'rgach — "
            }
        }
        var noteEm: String {
            switch self {
            case .telegram: return "faqat dars guruhida"
            case .zoom: return "Kamerang yoqilgan bo'lsin"
            case .meet: return "aktiv ishtirok et"
            case .teams: return "bitta jumla bilan ayt"
            case .discord: return "faqat coding voice'da bo'l"
            case .masterclass: return "shu video'ga e'tibor"
            case .coursera: return "darhol amaliyot"
            }
        }
        var launchLabel: String {
            switch self {
            case .telegram: return "Telegram'ni och"
            case .zoom: return "Zoom'ga qo'shilish"
            case .meet: return "Meet'ga qo'shilish"
            case .teams: return "Teams'ga qo'shilish"
            case .discord: return "Discord'ni och"
            case .masterclass: return "YouTube'ni och"
            case .coursera: return "Kursni och"
            }
        }
    }

    @State private var mode: ClassMode
    let blockedApps: [String]
    let remainingTime: String
    let elapsedMin: Int
    let totalMin: Int
    var onLaunch: (ClassMode) -> Void = { _ in }
    var onEnd: () -> Void = {}

    init(initial: ClassMode = .telegram,
         blockedApps: [String] = ["INSTAGRAM", "TIKTOK", "YOUTUBE", "TWITTER", "FACEBOOK"],
         remainingTime: String = "01:07:14",
         elapsedMin: Int = 23,
         totalMin: Int = 90,
         onLaunch: @escaping (ClassMode) -> Void = { _ in },
         onEnd: @escaping () -> Void = {}) {
        _mode = State(initialValue: initial)
        self.blockedApps = blockedApps
        self.remainingTime = remainingTime
        self.elapsedMin = elapsedMin
        self.totalMin = totalMin
        self.onLaunch = onLaunch
        self.onEnd = onEnd
    }

    var body: some View {
        ZStack(alignment: .bottom) {
            MentorColors.surfaceVoid2.ignoresSafeArea()
            ScrollView {
                VStack(spacing: 16) {
                    headerRow
                    modeTabs
                    BrandSeal(text: "murabbiy nazoratida")
                    sessionBlock
                    appsPanels
                    mentorNote
                    timerMini
                }
                .padding(.horizontal, 20).padding(.top, 28).padding(.bottom, 140)
            }
            bottomBar
        }
    }

    private var headerRow: some View {
        HStack {
            HStack(spacing: 6) {
                Image(systemName: "lock.fill").font(.system(size: 10)).foregroundColor(MentorColors.textBody)
                Text("KONTEKSTLI BLOK").font(MentorFonts.mono(9)).tracking(3).foregroundColor(MentorColors.textBody)
            }
            .padding(.horizontal, 12).padding(.vertical, 5)
            .background(Capsule().fill(Color.white.opacity(0.04))
                            .overlay(Capsule().strokeBorder(MentorColors.textGhost, lineWidth: 1)))
            Spacer()
            MentorPill(mode.modeName, color: MentorColors.emeraldBright, leadingDot: true)
        }
    }

    private var modeTabs: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: 6) {
                ForEach(ClassMode.allCases, id: \.tab) { m in
                    Button { mode = m } label: {
                        Text(m.tab).font(.system(size: 10, weight: mode == m ? .semibold : .regular)).tracking(1)
                            .foregroundColor(mode == m ? MentorColors.gold : MentorColors.textBody)
                            .padding(.horizontal, 12).padding(.vertical, 7)
                            .background(Capsule().fill(mode == m ? MentorColors.gold.opacity(0.16) : Color.white.opacity(0.03))
                                            .overlay(Capsule().strokeBorder(mode == m ? MentorColors.gold : MentorColors.textGhost, lineWidth: 1)))
                    }
                }
            }
        }
    }

    private var sessionBlock: some View {
        VStack(spacing: 10) {
            HStack(spacing: 8) {
                Circle().fill(mode.sourceColor).frame(width: 8, height: 8)
                Text(mode.source).font(MentorFonts.mono(9)).tracking(2).foregroundColor(mode.sourceColor)
            }
            .padding(.horizontal, 12).padding(.vertical, 5)
            .background(Capsule().fill(mode.sourceColor.opacity(0.10))
                            .overlay(Capsule().strokeBorder(mode.sourceColor.opacity(0.5), lineWidth: 1)))
            Text(mode.title).font(MentorFonts.mentorBold(22)).foregroundColor(MentorColors.textPrimary).multilineTextAlignment(.center)
            HStack(spacing: 6) {
                Text(mode.schedule).font(MentorFonts.mono(11)).tracking(2).foregroundColor(MentorColors.gold)
                Text("·").foregroundColor(MentorColors.textMuted)
                Text(mode.duration).font(MentorFonts.mono(11)).tracking(2).foregroundColor(MentorColors.textMuted)
            }
        }
    }

    private var appsPanels: some View {
        VStack(spacing: 8) {
            AppsPanel(label: "DARSGA RUXSAT ETILGAN", count: "\(mode.allowed.count) ILOVA",
                      accent: MentorColors.emeraldBright,
                      entries: mode.allowed.map { (icon: $0.0, name: $0.1, color: $0.2, allowed: true) })
            AppsPanel(label: "BLOKLANGAN — DIQQATSIZLIK", count: "\(blockedApps.count) ILOVA",
                      accent: MentorColors.rose,
                      entries: blockedApps.map { (icon: String($0.first ?? "X"), name: $0, color: MentorColors.rose, allowed: false) })
        }
    }

    private var mentorNote: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("MURABBIY ESLATMASI").font(MentorFonts.mono(9).weight(.semibold)).tracking(4).foregroundColor(MentorColors.goldDeep)
            HStack(spacing: 0) {
                if !mode.noteLead.isEmpty {
                    Text(mode.noteLead).font(MentorFonts.mentor(13)).foregroundColor(MentorColors.textBody)
                }
                Text(mode.noteEm).font(MentorFonts.mentorBold(13)).foregroundColor(MentorColors.gold)
                Text(".").font(MentorFonts.mentor(13)).foregroundColor(MentorColors.textBody)
            }
        }
        .padding(14).frame(maxWidth: .infinity, alignment: .leading)
        .background(RoundedRectangle(cornerRadius: 8).fill(MentorColors.gold.opacity(0.06))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.gold.opacity(0.4), lineWidth: 1)))
    }

    private var timerMini: some View {
        HStack {
            VStack(alignment: .leading, spacing: 4) {
                Text("SESSIYA QOLDI").font(MentorFonts.mono(9)).tracking(3).foregroundColor(MentorColors.goldDeep)
                Text("\(elapsedMin) / \(totalMin) DAQ").font(MentorFonts.mono(10)).tracking(1).foregroundColor(MentorColors.textMuted)
            }
            Spacer()
            Text(remainingTime).font(.system(size: 22, weight: .semibold, design: .monospaced)).tracking(2).foregroundColor(MentorColors.textPrimary)
        }
        .padding(.horizontal, 14).padding(.vertical, 10)
        .background(RoundedRectangle(cornerRadius: 8).fill(Color.white.opacity(0.03))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.textGhost, lineWidth: 1)))
    }

    private var bottomBar: some View {
        VStack(spacing: 6) {
            MentorPrimaryButton(mode.launchLabel) { onLaunch(mode) }
            Button(action: onEnd) {
                Text("Sessiyani tugatish").font(MentorFonts.mentor(12)).foregroundColor(MentorColors.textMuted)
            }
        }
        .padding(.horizontal, 20).padding(.vertical, 14)
        .background(MentorColors.surfaceVoid2)
    }
}

private struct AppsPanel: View {
    let label: String, count: String, accent: Color
    let entries: [(icon: String, name: String, color: Color, allowed: Bool)]

    var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            HStack {
                Text(label).font(MentorFonts.mono(9).weight(.semibold)).tracking(3).foregroundColor(accent)
                Spacer()
                Text(count).font(MentorFonts.mono(9)).tracking(1).foregroundColor(accent.opacity(0.7))
            }
            FlowLayout(spacing: 6) {
                ForEach(0..<entries.count, id: \.self) { i in
                    AppPill(entry: entries[i], accent: accent)
                }
            }
        }
        .padding(12)
        .background(RoundedRectangle(cornerRadius: 8).fill(accent.opacity(0.05))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(accent.opacity(0.3), lineWidth: 1)))
    }
}

private struct AppPill: View {
    let entry: (icon: String, name: String, color: Color, allowed: Bool)
    let accent: Color
    var body: some View {
        HStack(spacing: 6) {
            ZStack {
                RoundedRectangle(cornerRadius: 4).fill(entry.color).frame(width: 18, height: 18)
                Text(entry.icon).font(.system(size: 10, weight: .bold)).foregroundColor(.white)
            }
            Text(entry.allowed ? "✓" : "✕").font(.system(size: 10, weight: .bold)).foregroundColor(accent)
            Text(entry.name).font(.system(size: 10)).tracking(1).foregroundColor(MentorColors.textBody)
        }
        .padding(.horizontal, 8).padding(.vertical, 5)
        .background(Capsule().fill(Color.white.opacity(0.03))
                        .overlay(Capsule().strokeBorder(accent.opacity(0.4), lineWidth: 1)))
    }
}

#Preview {
    OnlineClassView()
}
