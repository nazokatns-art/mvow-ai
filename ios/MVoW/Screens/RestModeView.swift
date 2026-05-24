import SwiftUI

/// Rest mode — mentor invites user to rest. 4 variants.
/// Mirrors Android `RestModeScreen.kt`.
struct RestModeView: View {
    enum Kind { case burnout, restDay, soft, celebrate }

    let kind: Kind
    let userName: String
    var onAccept: () -> Void = {}
    var onOverride: () -> Void = {}

    init(kind: Kind = .burnout, userName: String = "Aziz",
         onAccept: @escaping () -> Void = {}, onOverride: @escaping () -> Void = {}) {
        self.kind = kind
        self.userName = userName
        self.onAccept = onAccept
        self.onOverride = onOverride
    }

    var body: some View {
        ZStack(alignment: .bottom) {
            MentorColors.surfaceVoid2.ignoresSafeArea()
            ScrollView {
                VStack(spacing: 18) {
                    headerRow
                    BrandSeal(text: content.seal)
                    statsStrip
                    hero
                    changesGrid
                    wisdomCard
                    energyRow
                }
                .padding(.horizontal, 20).padding(.top, 28).padding(.bottom, 140)
            }
            if !content.acceptLabel.isEmpty { bottomBar }
        }
    }

    // MARK: content

    private struct Content {
        let modePill: String, seal: String, heroPrelude: String
        let heroTitle: [String]
        let messageLead: String, messageEm: String, messageTail: String
        let wisdomLead: String, wisdomEm: String, wisdomTail: String
        let streak: Int, hours: Int, sessions: Int, bypass: Int
        let acceptLabel: String
        let showOverride: Bool
    }

    private var content: Content {
        switch kind {
        case .burnout:
            return Content(
                modePill: "DAM TAKLIFI", seal: "murabbiy g'amxo'rligida",
                heroPrelude: "CHARCHAGANSAN",
                heroTitle: ["\(userName),", "dam", "ol."],
                messageLead: "Sen ", messageEm: "12 kun ketma-ket",
                messageTail: " fokusda turding. Bugun nafas ol — ertaga yana kuchli qaytasan.",
                wisdomLead: "Egilmagan kamon ", wisdomEm: "ko'p o'q ottiradi",
                wisdomTail: ". Egilmas — sinadi.",
                streak: 12, hours: 47, sessions: 38, bypass: 2,
                acceptLabel: "Dam olaman", showOverride: true
            )
        case .restDay:
            return Content(
                modePill: "DAM KUNI", seal: "erkin kun",
                heroPrelude: "BUGUN — DAM",
                heroTitle: ["Sen", "erkin", "san."],
                messageLead: "Hech qanday alarm. Hech qanday block. Telefoning ", messageEm: "to'liq seniki",
                messageTail: ". Mentor faqat ertaga qaytadi.",
                wisdomLead: "Daraxt qishda dam oladi — bahorda ", wisdomEm: "guldek otiladi", wisdomTail: ".",
                streak: 12, hours: 47, sessions: 38, bypass: 2,
                acceptLabel: "Dam kuni — tasdiqlayman", showOverride: true
            )
        case .soft:
            return Content(
                modePill: "CHARCHOQ · -50%", seal: "mentor yumshoq ohangda",
                heroPrelude: "OG'IRROQ KUNDASAN",
                heroTitle: ["Bugun", "osonroq", "qil."],
                messageLead: "Sessiyalaring ", messageEm: "yarmiga qisqartirildi",
                messageTail: ". Block kuchsiz. Negotiation'da mentor jim. Bu ham fokus turi — ham g'amxo'rlik.",
                wisdomLead: "Kuch — siljishda. ", wisdomEm: "Mayda qadam", wisdomTail: " ham qadam.",
                streak: 12, hours: 47, sessions: 38, bypass: 2,
                acceptLabel: "", showOverride: false
            )
        case .celebrate:
            return Content(
                modePill: "7-KUN MUZAFFAR", seal: "mentor sevinishida",
                heroPrelude: "BAYRAM · BIR HAFTA",
                heroTitle: ["Tabriklayman.", "Sen", "qila", "olding."],
                messageLead: "Bir hafta ketma-ket reja bo'yicha yashading. ", messageEm: "Ko'pchilik 3 kunda taslim",
                messageTail: " bo'ladi. Sen — boshqa.",
                wisdomLead: "Har ", wisdomEm: "7-kun", wisdomTail: " — bayram. Bayram — kuchning takror tug'ilishi.",
                streak: 7, hours: 32, sessions: 24, bypass: 1,
                acceptLabel: "Bayram — ko'rib chiqaman", showOverride: false
            )
        }
    }

    // MARK: subviews

    private var headerRow: some View {
        HStack {
            MentorPill("MURABBIY YONDA", color: MentorColors.emeraldBright, leadingDot: true)
            Spacer()
            MentorPill("☾ \(content.modePill)", color: Color(red: 0.420, green: 0.361, blue: 0.557))
        }
    }

    private var statsStrip: some View {
        HStack(spacing: 0) {
            StatCell(value: "\(content.streak)", label: "KUN\nSTREAK", accent: MentorColors.gold)
            Divider().background(MentorColors.textGhost).frame(width: 1, height: 28)
            StatCell(value: "\(content.hours)", label: "SOAT\nFOKUS · HAFT", accent: MentorColors.gold)
            Divider().background(MentorColors.textGhost).frame(width: 1, height: 28)
            StatCell(value: "\(content.sessions)", label: "SESSIYA\nHAFT", accent: MentorColors.gold)
            Divider().background(MentorColors.textGhost).frame(width: 1, height: 28)
            StatCell(value: "\(content.bypass)", label: "CHEKINISH\nHAFT", accent: MentorColors.rose)
        }
        .padding(.vertical, 12)
        .background(RoundedRectangle(cornerRadius: 8).fill(Color.white.opacity(0.03))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.textGhost, lineWidth: 1)))
    }

    private var hero: some View {
        VStack(spacing: 12) {
            Text(content.heroPrelude)
                .font(MentorFonts.mono(9)).tracking(4).foregroundColor(MentorColors.goldDeep)
            HStack(spacing: 4) {
                ForEach(content.heroTitle, id: \.self) { w in
                    Text(w).font(MentorFonts.mentorBold(32)).foregroundColor(MentorColors.textPrimary)
                }
            }
            HStack(spacing: 0) {
                Text(content.messageLead).font(MentorFonts.mentor(14)).foregroundColor(MentorColors.textBody)
                Text(content.messageEm).font(MentorFonts.mentorBold(14)).foregroundColor(MentorColors.gold)
                Text(content.messageTail).font(MentorFonts.mentor(14)).foregroundColor(MentorColors.textBody)
            }
            .multilineTextAlignment(.center)
        }
    }

    private var changesGrid: some View {
        HStack(spacing: 8) {
            ChangeCard(icon: "🔓", label: "ILOVALAR\nOCHIQ")
            ChangeCard(icon: "🔕", label: "ALARMSIZ\nKUN")
            ChangeCard(icon: "∅", label: "SESSIYA\nYO'Q")
            ChangeCard(icon: "∞", label: "STREAK\nSAQLANADI")
        }
    }

    private var wisdomCard: some View {
        HStack(spacing: 0) {
            Text(content.wisdomLead).font(MentorFonts.mentor(14)).foregroundColor(MentorColors.textBody)
            Text(content.wisdomEm).font(MentorFonts.mentorBold(14)).foregroundColor(MentorColors.gold)
            Text(content.wisdomTail).font(MentorFonts.mentor(14)).foregroundColor(MentorColors.textBody)
        }
        .padding(14)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(RoundedRectangle(cornerRadius: 8).fill(MentorColors.gold.opacity(0.05))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.gold.opacity(0.3), lineWidth: 1)))
    }

    private var energyRow: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("BUGUNGI ENERGIYANG")
                .font(MentorFonts.mono(9)).tracking(4).foregroundColor(MentorColors.goldDeep)
            HStack(spacing: 6) {
                EnergyMini(icon: "⚡", name: "KUCHLI")
                EnergyMini(icon: "🌥", name: "O'RTA")
                EnergyMini(icon: "🌙", name: "CHARCHADIM")
                EnergyMini(icon: "☾", name: "DAM KUNI")
            }
        }
    }

    private var bottomBar: some View {
        VStack(spacing: 6) {
            MentorPrimaryButton(content.acceptLabel, action: onAccept)
            if content.showOverride {
                Button(action: onOverride) {
                    Text("yo'q, davom etaman")
                        .font(MentorFonts.mentor(11))
                        .foregroundColor(MentorColors.textMuted)
                }
            }
        }
        .padding(.horizontal, 20).padding(.vertical, 16)
        .background(MentorColors.surfaceVoid2)
    }
}

private struct StatCell: View {
    let value: String
    let label: String
    let accent: Color
    var body: some View {
        VStack(spacing: 4) {
            Text(value).font(.system(size: 20, weight: .semibold)).foregroundColor(accent)
            Text(label).font(MentorFonts.mono(8)).tracking(1.5).foregroundColor(MentorColors.textMuted)
                .multilineTextAlignment(.center).lineLimit(2)
        }
        .frame(maxWidth: .infinity)
    }
}

private struct ChangeCard: View {
    let icon: String
    let label: String
    var body: some View {
        VStack(spacing: 6) {
            Text(icon).font(.system(size: 20))
            Text(label).font(MentorFonts.mono(8)).tracking(1.5).foregroundColor(MentorColors.textBody)
                .multilineTextAlignment(.center)
        }
        .frame(maxWidth: .infinity).padding(.vertical, 12)
        .background(RoundedRectangle(cornerRadius: 8).fill(MentorColors.emeraldBright.opacity(0.06))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.emeraldBright.opacity(0.3), lineWidth: 1)))
    }
}

private struct EnergyMini: View {
    let icon: String
    let name: String
    var body: some View {
        VStack(spacing: 4) {
            Text(icon).font(.system(size: 18))
            Text(name).font(MentorFonts.mono(8.5).weight(.semibold)).tracking(1)
                .foregroundColor(MentorColors.textBody).multilineTextAlignment(.center)
        }
        .frame(maxWidth: .infinity).padding(.vertical, 10)
        .background(RoundedRectangle(cornerRadius: 8).fill(Color.white.opacity(0.03))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.textGhost, lineWidth: 1)))
    }
}

#Preview {
    RestModeView(kind: .burnout)
}
