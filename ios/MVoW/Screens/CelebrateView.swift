import SwiftUI

/// Weekly celebration — 8 varied creative variants.
/// Mirrors Android `CelebrateScreen.kt`.
struct CelebrateView: View {
    enum Variant: CaseIterable {
        case mountain, stars, garden, tree, river, dawn, phoenix, key

        var label: String {
            switch self {
            case .mountain: return "CHO'QQI"
            case .stars: return "YULDUZLAR"
            case .garden: return "BOG'"
            case .tree: return "DARAXT"
            case .river: return "DARYO"
            case .dawn: return "TONG"
            case .phoenix: return "QAYTA TUG'ILDIM"
            case .key: return "KALIT"
            }
        }
        var pre: String {
            switch self {
            case .mountain: return "CHO'QQIGA YETDING"
            case .stars: return "YULDUZLARING YONDI"
            case .garden: return "BOG'ING GULLADI"
            case .tree: return "DARAXTING O'SDI"
            case .river: return "DARYODAN O'TDING"
            case .dawn: return "TONGING OTDI"
            case .phoenix: return "QAYTA TUG'ILDING"
            case .key: return "KALIT QO'LIDA"
            }
        }
        var sub: String {
            switch self {
            case .mountain: return "birinchi cho'qqi · senga aytaman"
            case .stars: return "7 yulduz · har biri sening"
            case .garden: return "har gul · bir g'alaba"
            case .tree: return "yana bir halqa · ichkarida"
            case .river: return "ikkinchi qirg'oq · seni kutardi"
            case .dawn: return "qorong'idan keyin · yorug'lik"
            case .phoenix: return "kuldan · yana ko'tarilding"
            case .key: return "ichkari eshik · ochildi"
            }
        }
        var accent: Color {
            switch self {
            case .mountain: return MentorColors.gold
            case .stars: return Color(red: 0.772, green: 0.859, blue: 0.929)
            case .garden: return MentorColors.rose
            case .tree: return MentorColors.emeraldBright
            case .river: return Color(red: 0.486, green: 0.659, blue: 0.788)
            case .dawn: return Color(red: 1.0, green: 0.831, blue: 0.639)
            case .phoenix: return Color(red: 0.902, green: 0.224, blue: 0.275)
            case .key: return MentorColors.gold2
            }
        }
        var deep: Color {
            switch self {
            case .mountain: return Color(red: 0.102, green: 0.078, blue: 0.063)
            case .stars: return Color(red: 0.039, green: 0.055, blue: 0.125)
            case .garden: return Color(red: 0.122, green: 0.078, blue: 0.086)
            case .tree: return Color(red: 0.059, green: 0.122, blue: 0.071)
            case .river: return Color(red: 0.047, green: 0.086, blue: 0.125)
            case .dawn: return Color(red: 0.102, green: 0.063, blue: 0.024)
            case .phoenix: return Color(red: 0.122, green: 0.024, blue: 0.024)
            case .key: return Color(red: 0.078, green: 0.063, blue: 0.039)
            }
        }
        var letter: String {
            switch self {
            case .mountain: return "Bir hafta. 7 kun sen tepaga qarading va qadam tashladding. Ko'pchilik birinchi yon bag'irda to'xtaydi. Sen — yo'lda."
            case .stars: return "Har bajarilgan kun — bir yulduz. Bu hafta sen 7 ta yondirding. Bir-biriga ulansa — yo'l ko'rinadi."
            case .garden: return "Bu hafta ekkanlaring gulladi. Birorta urug' shu kun gullashini bilmasdi. Sen sabr qilding — bog' ochildi."
            case .tree: return "Daraxt halqasi har yili bir bor o'sadi. Sening ichingda — har hafta bir bor. Bu hafta yana bir halqa qo'shildi."
            case .river: return "Suv yengilmi? Yo'q. Lekin sen kechib o'tding. Boshqa qirg'oq endi seniki."
            case .dawn: return "Eng qorong'i payt — tongdan oldin. Sen sabrlik bilan kutding. Quyosh chiqdi."
            case .phoenix: return "Eski sen kuyib ketdi. Yangi sen ko'tarildi — kuchliroq, ravshanroq. Bu o'zgarish og'ir edi, lekin sen omon qolding."
            case .key: return "Yopiq eshik — kun sayin kichikroq edi. Bir hafta sen bir kalit yasading: intizom. Endi eshik ochildi."
            }
        }
        var signature: String {
            switch self {
            case .mountain: return "— men sen bilan birgaman"
            case .stars: return "— sening osmonda izing bor"
            case .garden: return "— bog'ingda bahor bor"
            case .tree: return "— ichingda o'rmon bor"
            case .river: return "— oqim seni olib o'tdi"
            case .dawn: return "— tongning birinchi nuri"
            case .phoenix: return "— olovingda noyob hayot bor"
            case .key: return "— kalit doim qo'lingda edi"
            }
        }
        var stats: String {
            switch self {
            case .mountain: return "47 SOAT · 38 SESSIYA · +12%"
            case .stars: return "7/7 KUN · 95% BAJARILDI"
            case .garden: return "5 GUL · 2 KURTAK"
            case .tree: return "12 HAFTA · ILDIZ CHUQUR"
            case .river: return "DARYO YENGILDI"
            case .dawn: return "QORONG'I YENGILDI"
            case .phoenix: return "QAYTA TUG'ILISH · YANGI YOSH"
            case .key: return "ESHIK OCHILDI"
            }
        }
        var next: String {
            switch self {
            case .mountain: return "Yangi hafta"
            case .stars: return "Yangi yulduz tutamiz"
            case .garden: return "Yangi urug'lar"
            case .tree: return "Yangi halqa"
            case .river: return "Yangi qirg'oqqa"
            case .dawn: return "Yangi tong"
            case .phoenix: return "Yangi qanot"
            case .key: return "Yangi xona"
            }
        }
    }

    @State private var variant: Variant
    var onNextWeek: (Variant) -> Void = { _ in }

    init(initial: Variant = .mountain, onNextWeek: @escaping (Variant) -> Void = { _ in }) {
        _variant = State(initialValue: initial)
        self.onNextWeek = onNextWeek
    }

    var body: some View {
        VStack(spacing: 0) {
            ZStack {
                variant.deep.ignoresSafeArea()
                VariantArt(variant: variant)
                VStack {
                    headline
                    Spacer()
                    letterCard
                }
                .padding(.horizontal, 24).padding(.vertical, 28)
            }
            tabs
            Spacer().frame(height: 8)
            bottomBar
        }
        .background(variant.deep)
    }

    private var headline: some View {
        VStack(spacing: 10) {
            Text(variant.pre).font(.system(size: 10, weight: .semibold)).tracking(5).foregroundColor(variant.accent)
            Text("TABRIKLAYMAN").font(.system(size: 32, weight: .bold)).tracking(6).foregroundColor(MentorColors.textPrimary)
            Text(variant.sub).font(MentorFonts.mentor(13)).foregroundColor(variant.accent)
                .multilineTextAlignment(.center)
        }
    }

    private var letterCard: some View {
        VStack(alignment: .leading, spacing: 10) {
            Text(variant.letter)
                .font(MentorFonts.mentor(14))
                .foregroundColor(MentorColors.textBody)
                .lineSpacing(5)
            Text(variant.signature)
                .font(MentorFonts.mentor(12))
                .foregroundColor(variant.accent)
                .frame(maxWidth: .infinity, alignment: .trailing)
        }
        .padding(16)
        .background(RoundedRectangle(cornerRadius: 8)
                        .fill(variant.accent.opacity(0.06))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(variant.accent.opacity(0.3), lineWidth: 1)))
    }

    private var tabs: some View {
        HStack {
            ForEach(Variant.allCases, id: \.self) { v in
                Button { variant = v } label: {
                    VStack(spacing: 4) {
                        Rectangle().fill(v == variant ? v.accent : MentorColors.textGhost).frame(height: 6)
                        Text(v.label)
                            .font(.system(size: 7.5, weight: v == variant ? .bold : .regular))
                            .tracking(1)
                            .foregroundColor(v == variant ? v.accent : MentorColors.textMuted)
                    }
                    .frame(maxWidth: .infinity).padding(.vertical, 4)
                }
            }
        }
        .padding(.horizontal, 12)
    }

    private var bottomBar: some View {
        VStack(spacing: 8) {
            MentorPrimaryButton(variant.next) { onNextWeek(variant) }
            Text(variant.stats).font(MentorFonts.mono(10)).tracking(2).foregroundColor(variant.accent)
        }
        .padding(.horizontal, 24).padding(.vertical, 14)
    }
}

private struct VariantArt: View {
    let variant: CelebrateView.Variant

    var body: some View {
        GeometryReader { geo in
            ZStack {
                switch variant {
                case .mountain: mountain(geo: geo)
                case .stars: stars(geo: geo)
                case .garden: garden(geo: geo)
                case .tree: tree(geo: geo)
                case .river: river(geo: geo)
                case .dawn: dawn(geo: geo)
                case .phoenix: phoenix(geo: geo)
                case .key: key(geo: geo)
                }
            }
        }
    }

    private func mountain(geo: GeometryProxy) -> some View {
        let w = geo.size.width, h = geo.size.height
        return ZStack {
            Path { p in
                p.move(to: .init(x: 0, y: h))
                p.addLine(to: .init(x: 0.21 * w, y: h * 0.6))
                p.addLine(to: .init(x: 0.5 * w, y: h * 0.28))
                p.addLine(to: .init(x: 0.74 * w, y: h * 0.71))
                p.addLine(to: .init(x: w, y: h))
            }
            .fill(LinearGradient(colors: [Color(red: 0.102, green: 0.078, blue: 0.063), Color(red: 0.016, green: 0.024, blue: 0.043)], startPoint: .top, endPoint: .bottom))
            Path { p in
                p.move(to: .init(x: 0, y: h))
                p.addLine(to: .init(x: 0.21 * w, y: h * 0.6))
                p.addLine(to: .init(x: 0.5 * w, y: h * 0.28))
                p.addLine(to: .init(x: 0.74 * w, y: h * 0.71))
            }
            .stroke(variant.accent, lineWidth: 1.5)
            Circle().fill(variant.accent).frame(width: 12, height: 12).position(x: w * 0.5, y: h * 0.28)
        }
    }

    private func stars(geo: GeometryProxy) -> some View {
        let points: [CGPoint] = [
            CGPoint(x: 0.13, y: 0.21), CGPoint(x: 0.37, y: 0.38), CGPoint(x: 0.30, y: 0.67),
            CGPoint(x: 0.60, y: 0.54), CGPoint(x: 0.77, y: 0.25), CGPoint(x: 0.87, y: 0.75),
            CGPoint(x: 0.50, y: 0.83)
        ]
        let w = geo.size.width, h = geo.size.height
        return ZStack {
            Path { p in
                for (i, pt) in points.enumerated() {
                    let xp = pt.x * w, yp = pt.y * h
                    if i == 0 { p.move(to: .init(x: xp, y: yp)) } else { p.addLine(to: .init(x: xp, y: yp)) }
                }
            }
            .stroke(variant.accent.opacity(0.5), lineWidth: 1.2)
            ForEach(0..<points.count, id: \.self) { i in
                Circle().fill(variant.accent).frame(width: 6, height: 6)
                    .position(x: points[i].x * w, y: points[i].y * h)
            }
        }
    }

    private func garden(geo: GeometryProxy) -> some View {
        let flowers: [(CGFloat, CGFloat, Color)] = [
            (0.18, 0.78, variant.accent),
            (0.42, 0.86, MentorColors.gold),
            (0.65, 0.74, variant.accent),
            (0.83, 0.82, MentorColors.goldFlash)
        ]
        let w = geo.size.width, h = geo.size.height
        return ZStack {
            ForEach(0..<flowers.count, id: \.self) { i in
                let f = flowers[i]
                ZStack {
                    ForEach(0..<5, id: \.self) { k in
                        Circle().fill(f.2.opacity(0.85)).frame(width: 18, height: 18)
                            .offset(x: cos(Double(k) * 1.2566) * 14, y: sin(Double(k) * 1.2566) * 8)
                    }
                    Circle().fill(MentorColors.goldFlash).frame(width: 8, height: 8)
                }
                .position(x: f.0 * w, y: f.1 * h)
            }
        }
    }

    private func tree(geo: GeometryProxy) -> some View {
        ZStack {
            ForEach(1...8, id: \.self) { i in
                Circle()
                    .strokeBorder(variant.accent.opacity(0.10 + Double(i) / 8.0 * 0.4), lineWidth: 1.4)
                    .frame(width: CGFloat(i * 28), height: CGFloat(i * 28))
            }
            Circle().fill(variant.accent.opacity(0.7)).frame(width: 22, height: 22)
        }
        .position(x: geo.size.width / 2, y: geo.size.height * 0.5)
    }

    private func river(geo: GeometryProxy) -> some View {
        let w = geo.size.width, h = geo.size.height
        return Path { p in
            p.move(to: .init(x: 0, y: h * 0.55))
            p.addCurve(to: .init(x: w * 0.75, y: h * 0.55),
                       control1: .init(x: w * 0.25, y: h * 0.45),
                       control2: .init(x: w * 0.5, y: h * 0.7))
            p.addLine(to: .init(x: w, y: h * 0.6))
            p.addLine(to: .init(x: w, y: h))
            p.addLine(to: .init(x: 0, y: h))
            p.closeSubpath()
        }
        .fill(LinearGradient(colors: [variant.accent.opacity(0.3), variant.accent.opacity(0.05)],
                              startPoint: .top, endPoint: .bottom))
    }

    private func dawn(geo: GeometryProxy) -> some View {
        ZStack {
            RadialGradient(colors: [variant.accent.opacity(0.7), variant.accent.opacity(0.1), .clear],
                           center: UnitPoint(x: 0.5, y: 0.85), startRadius: 0, endRadius: 280)
            Circle().fill(variant.accent.opacity(0.7)).frame(width: 60, height: 60)
                .position(x: geo.size.width / 2, y: geo.size.height * 0.85)
        }
    }

    private func phoenix(geo: GeometryProxy) -> some View {
        let cx = geo.size.width / 2, cy = geo.size.height * 0.5
        return ZStack {
            ForEach(0..<14, id: \.self) { i in
                let angle = Double(i) * 25.7 * .pi / 180
                let r: CGFloat = CGFloat(40 + (i % 3) * 22)
                Circle().fill(variant.accent.opacity(0.6))
                    .frame(width: 10, height: 10)
                    .position(x: cx + cos(angle) * r, y: cy + sin(angle) * r * 0.7)
            }
            Circle().fill(variant.accent).frame(width: 18, height: 18).position(x: cx, y: cy)
        }
    }

    private func key(geo: GeometryProxy) -> some View {
        let cx = geo.size.width / 2, cy = geo.size.height * 0.5
        return ZStack {
            Circle().strokeBorder(variant.accent, lineWidth: 3).frame(width: 36, height: 36).position(x: cx, y: cy)
            Rectangle().fill(variant.accent).frame(width: 80, height: 4).position(x: cx + 40, y: cy)
            Rectangle().fill(variant.accent).frame(width: 4, height: 16).position(x: cx + 70, y: cy + 8)
            Rectangle().fill(variant.accent).frame(width: 4, height: 12).position(x: cx + 60, y: cy + 6)
        }
    }
}

#Preview {
    CelebrateView()
}
