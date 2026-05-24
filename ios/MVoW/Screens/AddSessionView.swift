import SwiftUI

/// Manual session creation. Mirrors Android `AddSessionScreen.kt`.
struct AddSessionView: View {
    enum Severity: CaseIterable {
        case muqaddas, high, medium, low
        var label: String { ["MUQADDAS", "YUQORI", "REJADAGI", "OPTSIYONAL"][idx] }
        var color: Color {
            switch self {
            case .muqaddas: return MentorColors.crimson
            case .high: return MentorColors.gold
            case .medium: return MentorColors.emeraldBright
            case .low: return Color(red: 0.486, green: 0.659, blue: 0.788)
            }
        }
        var idx: Int { ["muqaddas", "high", "medium", "low"].firstIndex(of: String(describing: self))! }
    }
    enum SType: CaseIterable {
        case solo, online
        var label: String { self == .solo ? "SOLO FOKUS" : "ONLINE DARS" }
        var icon: String { self == .solo ? "🛡" : "↗" }
        var sub: String { self == .solo ? "hammasi bloklanadi" : "whitelist · 2 ilova" }
    }

    let userName: String
    @State private var title: String
    @State private var goal: String
    @State private var time: String
    @State private var severity: Severity = .muqaddas
    @State private var sessionType: SType = .solo

    var onSave: (String, String, String, Severity, SType) -> Void = { _, _, _, _, _ in }
    var onCancel: () -> Void = {}

    init(userName: String = "Aziz",
         initialTitle: String = "",
         initialGoal: String = "",
         initialTime: String = "",
         onSave: @escaping (String, String, String, Severity, SType) -> Void = { _, _, _, _, _ in },
         onCancel: @escaping () -> Void = {}) {
        self.userName = userName
        _title = State(initialValue: initialTitle)
        _goal = State(initialValue: initialGoal)
        _time = State(initialValue: initialTime)
        self.onSave = onSave
        self.onCancel = onCancel
    }

    private var ready: Bool { !title.isEmpty && !goal.isEmpty && !time.isEmpty }

    var body: some View {
        ZStack(alignment: .bottom) {
            MentorColors.surfaceVoid2.ignoresSafeArea()
            ScrollView {
                VStack(spacing: 16) {
                    headerRow
                    mentorAsk
                    previewCard
                    field(label: "NOMI", value: $title, placeholder: "Sessiya nomi…")
                    field(label: "MAQSAD", value: $goal, placeholder: "Nimaga erishasan…")
                    field(label: "VAQT", value: $time, placeholder: "14:30 — 15:15 · har kuni")
                    severityBlock
                    typeBlock
                    if ready { commentary }
                }
                .padding(.horizontal, 20).padding(.top, 28).padding(.bottom, 160)
            }
            bottomBar
        }
    }

    private var headerRow: some View {
        HStack {
            MentorPill("✦  YANGI SESSIYA", color: MentorColors.gold)
            Spacer()
            Button(action: onCancel) {
                Text("×").font(.system(size: 16)).foregroundColor(MentorColors.textMuted)
                    .frame(width: 30, height: 30)
                    .background(Circle().fill(Color.white.opacity(0.04))
                                    .overlay(Circle().strokeBorder(MentorColors.textGhost, lineWidth: 1)))
            }
        }
    }

    private var mentorAsk: some View {
        HStack(alignment: .top, spacing: 12) {
            MentorOrb(size: 28, breathing: false)
            HStack(spacing: 0) {
                if ready {
                    Text("Mana sening yo'ling. ").font(MentorFonts.mentor(18)).foregroundColor(MentorColors.textPrimary)
                    Text("Tasdiqlasakmi?").font(MentorFonts.mentorBold(18)).foregroundColor(MentorColors.gold)
                } else {
                    Text("\(userName), qaysi ").font(MentorFonts.mentor(18)).foregroundColor(MentorColors.textPrimary)
                    Text("yo'lni").font(MentorFonts.mentorBold(18)).foregroundColor(MentorColors.gold)
                    Text(" quramiz?").font(MentorFonts.mentor(18)).foregroundColor(MentorColors.textPrimary)
                }
            }
        }
        .padding(14).frame(maxWidth: .infinity, alignment: .leading)
        .background(RoundedRectangle(cornerRadius: 8).fill(MentorColors.gold.opacity(0.05))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.gold.opacity(0.4), lineWidth: 1)))
    }

    private var previewCard: some View {
        VStack(alignment: .leading, spacing: 8) {
            HStack {
                HStack(spacing: 6) {
                    Circle().fill(severity.color).frame(width: 6, height: 6)
                    Text(severity.label).font(MentorFonts.mono(9).weight(.semibold)).tracking(3).foregroundColor(severity.color)
                }
                Spacer()
                Text(time.isEmpty ? "— vaqt belgilanmagan —" : time)
                    .font(MentorFonts.mono(11)).tracking(1).foregroundColor(time.isEmpty ? MentorColors.textMuted : MentorColors.gold)
            }
            Text(title.isEmpty ? "— nom yo'q —" : title)
                .font(MentorFonts.mentorBold(20))
                .foregroundColor(title.isEmpty ? MentorColors.textMuted : MentorColors.textPrimary)
            Text(goal.isEmpty ? "— maqsad yo'q —" : goal)
                .font(MentorFonts.mentor(13))
                .foregroundColor(goal.isEmpty ? MentorColors.textMuted : MentorColors.textBody)
            HStack(spacing: 10) {
                MetaTag(text: "∼ \(deriveDuration())")
                MetaTag(text: "↻ HAR KUNI")
                MetaTag(text: "\(sessionType.icon) \(sessionType.label.split(separator: " ").first.map(String.init) ?? "")")
            }
        }
        .padding(14)
        .background(RoundedRectangle(cornerRadius: 10).fill(severity.color.opacity(0.06))
                        .overlay(RoundedRectangle(cornerRadius: 10).strokeBorder(severity.color.opacity(0.5), lineWidth: 1)))
    }

    private func deriveDuration() -> String {
        guard let m = try? NSRegularExpression(pattern: "(\\d{1,2}):(\\d{2})\\s*[—-]\\s*(\\d{1,2}):(\\d{2})", options: []) else { return "— DAQ" }
        let range = NSRange(time.startIndex..., in: time)
        guard let match = m.firstMatch(in: time, range: range), match.numberOfRanges == 5 else { return "— DAQ" }
        let h1 = Int((time as NSString).substring(with: match.range(at: 1))) ?? 0
        let m1 = Int((time as NSString).substring(with: match.range(at: 2))) ?? 0
        let h2 = Int((time as NSString).substring(with: match.range(at: 3))) ?? 0
        let m2 = Int((time as NSString).substring(with: match.range(at: 4))) ?? 0
        let diff = max(0, (h2 * 60 + m2) - (h1 * 60 + m1))
        return diff >= 60 ? "\(diff/60)S \(diff%60)D" : "\(diff) DAQ"
    }

    private func field(label: String, value: Binding<String>, placeholder: String) -> some View {
        HStack(spacing: 10) {
            Text(label).font(MentorFonts.mono(9).weight(.semibold)).tracking(3).foregroundColor(MentorColors.goldDeep)
                .frame(width: 56, alignment: .leading)
            ZStack(alignment: .leading) {
                if value.wrappedValue.isEmpty {
                    Text(placeholder).font(MentorFonts.mentor(14)).foregroundColor(MentorColors.textMuted)
                }
                TextField("", text: value)
                    .font(.system(size: 14)).foregroundColor(MentorColors.textPrimary)
            }
            ZStack {
                Circle().fill(MentorColors.gold.opacity(0.10)).overlay(Circle().strokeBorder(MentorColors.gold.opacity(0.5), lineWidth: 1))
                Image(systemName: "mic.fill").font(.system(size: 11)).foregroundColor(MentorColors.gold)
            }
            .frame(width: 28, height: 28)
        }
        .padding(.horizontal, 12).padding(.vertical, 12)
        .background(RoundedRectangle(cornerRadius: 8).fill(Color.white.opacity(0.03))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.textGhost, lineWidth: 1)))
    }

    private var severityBlock: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("QANCHA MUHIM?").font(MentorFonts.mono(9).weight(.semibold)).tracking(4).foregroundColor(MentorColors.goldDeep)
            HStack(spacing: 6) {
                ForEach(Severity.allCases, id: \.label) { s in
                    Button { severity = s } label: {
                        Text(s.label).font(.system(size: 9, weight: severity == s ? .bold : .regular))
                            .tracking(1)
                            .foregroundColor(severity == s ? s.color : MentorColors.textBody)
                            .frame(maxWidth: .infinity).frame(height: 36)
                            .background(RoundedRectangle(cornerRadius: 4)
                                .fill(severity == s ? s.color.opacity(0.18) : Color.white.opacity(0.03))
                                .overlay(RoundedRectangle(cornerRadius: 4).strokeBorder(severity == s ? s.color : MentorColors.textGhost, lineWidth: 1)))
                    }
                }
            }
        }
    }

    private var typeBlock: some View {
        HStack(spacing: 8) {
            ForEach(SType.allCases, id: \.label) { t in
                let selected = sessionType == t
                Button { sessionType = t } label: {
                    HStack(spacing: 10) {
                        Text(t.icon).font(.system(size: 18))
                            .foregroundColor(selected ? MentorColors.gold : MentorColors.textBody)
                        VStack(alignment: .leading, spacing: 2) {
                            Text(t.label).font(.system(size: 11, weight: .semibold)).tracking(1)
                                .foregroundColor(selected ? MentorColors.textPrimary : MentorColors.textBody)
                            Text(t.sub).font(.system(size: 9)).foregroundColor(MentorColors.textMuted)
                        }
                        Spacer()
                    }
                    .padding(.horizontal, 12).padding(.vertical, 12)
                    .background(RoundedRectangle(cornerRadius: 8)
                        .fill(selected ? MentorColors.gold.opacity(0.12) : Color.white.opacity(0.03))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(selected ? MentorColors.gold : MentorColors.textGhost, lineWidth: 1)))
                }
            }
        }
    }

    private var commentary: some View {
        let text: String = {
            if sessionType == .online {
                return "Online dars — faqat shu guruhda bo'lasan. Boshqa chatlar bloklangan. Qatnashish — eshitish."
            } else if severity == .muqaddas {
                return "Yaxshi reja. \(time.prefix(5)) — kuchli vaqt, charchamagan paytda. Eslataman, sen tayyorlanasan."
            } else {
                return "Yaxshi. Mentor seni bu sessiyaga eslatadi."
            }
        }()
        return HStack(alignment: .top, spacing: 10) {
            MentorOrb(size: 22, breathing: false)
            Text(text).font(MentorFonts.mentor(13)).foregroundColor(MentorColors.textBody).lineSpacing(3)
        }
        .padding(12)
        .background(RoundedRectangle(cornerRadius: 8).fill(MentorColors.gold.opacity(0.05))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.gold.opacity(0.3), lineWidth: 1)))
    }

    private var bottomBar: some View {
        VStack(spacing: 8) {
            MentorPrimaryButton("Sessiyani muhrlash", enabled: ready) {
                onSave(title, goal, time, severity, sessionType)
            }
            HStack(spacing: 8) {
                Text("Ovoz va'adi qo'sh").font(.system(size: 11)).foregroundColor(MentorColors.textMuted)
                    .frame(maxWidth: .infinity).frame(height: 40)
                    .background(RoundedRectangle(cornerRadius: 4).fill(Color.white.opacity(0.03))
                                    .overlay(RoundedRectangle(cornerRadius: 4).strokeBorder(MentorColors.textGhost, lineWidth: 1)))
                Button(action: onCancel) {
                    Text("Bekor qilish").font(.system(size: 11)).foregroundColor(MentorColors.textMuted)
                        .frame(maxWidth: .infinity).frame(height: 40)
                        .background(RoundedRectangle(cornerRadius: 4).fill(Color.white.opacity(0.03))
                                        .overlay(RoundedRectangle(cornerRadius: 4).strokeBorder(MentorColors.textGhost, lineWidth: 1)))
                }
            }
        }
        .padding(.horizontal, 20).padding(.vertical, 14)
        .background(MentorColors.surfaceVoid2)
    }
}

private struct MetaTag: View {
    let text: String
    var body: some View {
        Text(text).font(.system(size: 9)).tracking(1).foregroundColor(MentorColors.textBody)
            .padding(.horizontal, 8).padding(.vertical, 4)
            .background(Capsule().fill(Color.white.opacity(0.03))
                            .overlay(Capsule().strokeBorder(MentorColors.textGhost, lineWidth: 1)))
    }
}

#Preview {
    AddSessionView(initialTitle: "Qur'on darsi",
                   initialGoal: "Bugun yana bir bobni o'zlashtirish",
                   initialTime: "14:30 — 15:15 · har kuni")
}
