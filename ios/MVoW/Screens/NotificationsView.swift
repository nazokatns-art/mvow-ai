import SwiftUI

/// Notifications center. Mirrors Android `NotificationsScreen.kt`.
struct NotificationsView: View {
    enum Kind { case mentor, alert, celebrate, system }

    struct Item: Identifiable {
        let id = UUID()
        let kind: Kind
        let type: String
        let time: String
        let text: String
        let action: String?
        let unread: Bool
    }

    struct Group: Identifiable {
        let id = UUID()
        let dayLabel: String
        let items: [Item]
    }

    let groups: [Group]
    var onClose: () -> Void = {}

    init(groups: [Group] = NotificationsView.sample, onClose: @escaping () -> Void = {}) {
        self.groups = groups
        self.onClose = onClose
    }

    private var unreadCount: Int {
        groups.flatMap { $0.items }.filter { $0.unread }.count
    }

    var body: some View {
        ZStack {
            MentorColors.surfaceVoid2.ignoresSafeArea()
            VStack(spacing: 0) {
                topBar
                ScrollView {
                    VStack(spacing: 8) {
                        ForEach(groups) { g in
                            DayDivider(label: g.dayLabel)
                            ForEach(g.items) { item in
                                NotifCard(item: item)
                            }
                        }
                    }
                    .padding(.horizontal, 16).padding(.vertical, 14)
                }
            }
        }
    }

    private var topBar: some View {
        HStack(spacing: 16) {
            Button(action: onClose) {
                Text("←").font(.system(size: 22)).foregroundColor(MentorColors.textMuted)
            }
            VStack(alignment: .leading, spacing: 2) {
                Text("Bildirishnomalar").font(.system(size: 17, weight: .semibold)).foregroundColor(MentorColors.textPrimary)
                if unreadCount > 0 {
                    Text("\(unreadCount) o'qilmagan").font(MentorFonts.mono(10)).tracking(2).foregroundColor(MentorColors.gold)
                }
            }
            Spacer()
            Text("hammasi o'qildi").font(.system(size: 10)).tracking(1).foregroundColor(MentorColors.textMuted)
                .padding(.horizontal, 10).padding(.vertical, 5)
                .background(Capsule().fill(Color.white.opacity(0.04))
                                .overlay(Capsule().strokeBorder(MentorColors.textGhost, lineWidth: 1)))
        }
        .padding(.horizontal, 16).padding(.vertical, 14)
        .background(Color(red: 0.071, green: 0.055, blue: 0.094))
    }

    static let sample: [Group] = [
        Group(dayLabel: "BUGUN · 10 MAY", items: [
            Item(kind: .mentor, type: "MURABBIY · KUN OXIRGI", time: "22:14",
                 text: "Bugun 3.5 soat fokusda turding. Daraxtingda yana bir halqa shakllandi. Erta yot — uyqu sifati ertangi g'alabaning yarmi.",
                 action: "QISQA BAHO BERAY", unread: true),
            Item(kind: .celebrate, type: "YANGI YUTUQ", time: "17:45",
                 text: "12 kun streak — yangi yuqori darajang. O'tgan oydan 4 kun ko'p.",
                 action: nil, unread: true),
            Item(kind: .mentor, type: "MURABBIY · 5 DAQIQA OLDIN", time: "14:25",
                 text: "Qur'on darsi 5 daqiqada. Telefoning'ni qo'l ostidan uzoqroqqa qo'y — fokus shu kichik harakatdan boshlanadi.",
                 action: nil, unread: true),
            Item(kind: .alert, type: "CHEKINISH · INSTAGRAM", time: "11:42",
                 text: "Sen bugun 1-marta bloklangan ilovaga urinding. Negotiation: sabab eshitildi, mentor inkor etdi.",
                 action: nil, unread: false),
            Item(kind: .mentor, type: "MURABBIY · TONG", time: "06:42",
                 text: "Aziz, 7s 14daq uxlading — yaxshi. Ertalabki 5 daqiqa quyosh sifatli kun va sifatli uyqu beradi. Bugun kim bo'lib uyg'onding?",
                 action: "JAVOB BERAMAN", unread: false)
        ]),
        Group(dayLabel: "KECHA · 9 MAY · SHANBA", items: [
            Item(kind: .mentor, type: "MURABBIY · DAM KUNI", time: "10:00",
                 text: "Bugun shanba — dam ol. Hech qanday alarm. Telefoning seniki. Murabbiy ertaga qaytadi.",
                 action: nil, unread: false)
        ])
    ]
}

private struct DayDivider: View {
    let label: String
    var body: some View {
        HStack(spacing: 10) {
            Rectangle().fill(MentorColors.textGhost).frame(height: 1)
            Text(label).font(MentorFonts.mono(9)).tracking(3).foregroundColor(MentorColors.textMuted)
            Rectangle().fill(MentorColors.textGhost).frame(height: 1)
        }
    }
}

private struct NotifCard: View {
    let item: NotificationsView.Item

    private var accent: Color {
        switch item.kind {
        case .mentor: return MentorColors.gold
        case .alert: return MentorColors.rose
        case .celebrate: return MentorColors.emeraldBright
        case .system: return Color(red: 0.486, green: 0.659, blue: 0.788)
        }
    }
    private var icon: String {
        switch item.kind {
        case .mentor: return "✦"
        case .alert: return "!"
        case .celebrate: return "★"
        case .system: return "i"
        }
    }

    var body: some View {
        HStack(alignment: .top, spacing: 12) {
            ZStack {
                Circle().fill(accent.opacity(0.20))
                    .overlay(Circle().strokeBorder(accent, lineWidth: 1))
                Text(icon).font(.system(size: 13, weight: .bold)).foregroundColor(accent)
            }
            .frame(width: 32, height: 32)
            VStack(alignment: .leading, spacing: 6) {
                HStack {
                    Text(item.type).font(MentorFonts.mono(9).weight(.semibold)).tracking(2).foregroundColor(accent)
                    Spacer()
                    Text(item.time).font(MentorFonts.mono(9)).foregroundColor(MentorColors.textMuted)
                }
                Text(item.text).font(MentorFonts.mentor(13))
                    .foregroundColor(item.unread ? MentorColors.textPrimary : MentorColors.textBody)
                    .lineSpacing(3)
                if let action = item.action {
                    HStack(spacing: 6) {
                        Text(action).font(.system(size: 10, weight: .semibold)).tracking(2).foregroundColor(accent)
                        Text("→").foregroundColor(accent)
                    }
                    .padding(.horizontal, 10).padding(.vertical, 6)
                    .background(RoundedRectangle(cornerRadius: 6).fill(accent.opacity(0.15))
                                    .overlay(RoundedRectangle(cornerRadius: 6).strokeBorder(accent, lineWidth: 1)))
                }
            }
        }
        .padding(12)
        .background(RoundedRectangle(cornerRadius: 10)
                        .fill(item.unread ? accent.opacity(0.07) : Color.white.opacity(0.02))
                        .overlay(RoundedRectangle(cornerRadius: 10)
                                    .strokeBorder(item.unread ? accent.opacity(0.4) : MentorColors.textGhost, lineWidth: 1)))
    }
}

#Preview {
    NotificationsView()
}
