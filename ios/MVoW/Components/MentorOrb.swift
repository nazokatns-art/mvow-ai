import SwiftUI

/// The mentor's "presence" — a breathing gold orb with halo.
/// Used across all screens as the mentor's visual identity.
struct MentorOrb: View {
    enum Tone { case gold, crimson, emerald }

    let size: CGFloat
    let tone: Tone
    let breathing: Bool

    @State private var scale: CGFloat = 0.95

    init(size: CGFloat = 32, tone: Tone = .gold, breathing: Bool = true) {
        self.size = size
        self.tone = tone
        self.breathing = breathing
    }

    var body: some View {
        ZStack {
            // Halo glow
            Circle()
                .fill(haloColor.opacity(0.45))
                .frame(width: size * 1.6, height: size * 1.6)
                .blur(radius: size * 0.45)

            // Core
            Circle()
                .fill(
                    RadialGradient(
                        colors: [coreLight, coreMid, coreDark],
                        center: UnitPoint(x: 0.35, y: 0.30),
                        startRadius: 0,
                        endRadius: size
                    )
                )
                .frame(width: size, height: size)
                .shadow(color: haloColor.opacity(0.55), radius: size * 0.4)
        }
        .scaleEffect(breathing ? scale : 1.0)
        .onAppear {
            guard breathing else { return }
            withAnimation(.easeInOut(duration: 3).repeatForever(autoreverses: true)) {
                scale = 1.08
            }
        }
    }

    private var coreLight: Color {
        switch tone {
        case .gold: return MentorColors.goldFlash
        case .crimson: return Color(red: 1.0, green: 0.85, blue: 0.86)
        case .emerald: return Color(red: 0.86, green: 0.95, blue: 0.88)
        }
    }
    private var coreMid: Color {
        switch tone {
        case .gold: return MentorColors.gold
        case .crimson: return MentorColors.crimson
        case .emerald: return MentorColors.emerald
        }
    }
    private var coreDark: Color {
        switch tone {
        case .gold: return MentorColors.goldDeep
        case .crimson: return MentorColors.crimsonDeep
        case .emerald: return MentorColors.emeraldDeep
        }
    }
    private var haloColor: Color { coreMid }
}

/// Decorative ornament line — gold gradient with a star/glyph in the middle.
struct OrnamentDivider: View {
    let glyph: String
    let lineWidth: CGFloat

    init(glyph: String = "✦", lineWidth: CGFloat = 40) {
        self.glyph = glyph
        self.lineWidth = lineWidth
    }

    var body: some View {
        HStack(spacing: 12) {
            Rectangle()
                .fill(LinearGradient(
                    colors: [.clear, MentorColors.honey, .clear],
                    startPoint: .leading, endPoint: .trailing
                ))
                .frame(width: lineWidth, height: 1)
            Text(glyph)
                .font(.system(size: 9))
                .foregroundColor(MentorColors.honey)
            Rectangle()
                .fill(LinearGradient(
                    colors: [.clear, MentorColors.honey, .clear],
                    startPoint: .leading, endPoint: .trailing
                ))
                .frame(width: lineWidth, height: 1)
        }
    }
}

/// Brand seal — ornament + italic tagline.
struct BrandSeal: View {
    let text: String

    var body: some View {
        VStack(spacing: 4) {
            OrnamentDivider()
            Text(text)
                .font(MentorFonts.mentor(14))
                .foregroundColor(MentorColors.honey)
                .tracking(2)
        }
    }
}

/// The cinematic "phone frame" backdrop used on every preview.
/// Renders the dark void with optional gold ambient glow + gilt frame.
struct VoidBackdrop<Content: View>: View {
    let goldAmbient: Bool
    let content: Content

    init(goldAmbient: Bool = false, @ViewBuilder content: () -> Content) {
        self.goldAmbient = goldAmbient
        self.content = content()
    }

    var body: some View {
        ZStack {
            // Base void
            LinearGradient(
                colors: [MentorColors.surfaceVoid, MentorColors.surfaceVoid2, MentorColors.surfaceVoid],
                startPoint: .top, endPoint: .bottom
            )
            .ignoresSafeArea()

            // Gold radial ambient
            if goldAmbient {
                RadialGradient(
                    colors: [MentorColors.gold.opacity(0.32), .clear],
                    center: UnitPoint(x: 0.5, y: 0.35),
                    startRadius: 50,
                    endRadius: 280
                )
                .ignoresSafeArea()
            } else {
                RadialGradient(
                    colors: [MentorColors.gold.opacity(0.10), .clear],
                    center: UnitPoint(x: 0.5, y: 0.25),
                    startRadius: 50,
                    endRadius: 240
                )
                .ignoresSafeArea()
            }

            // Gilt frame
            RoundedRectangle(cornerRadius: 26, style: .continuous)
                .strokeBorder(MentorColors.gold.opacity(0.18), lineWidth: 1)
                .padding(14)
                .ignoresSafeArea()

            // Content
            content
        }
    }
}
