import SwiftUI

/// M·VoW design system — matches Android `MentorColors` exactly.
/// "Vault meets cathedral" — deep navy + gold glow.
enum MentorColors {
    // Surfaces — deep void
    static let surfaceVoid   = Color(red: 0.016, green: 0.024, blue: 0.043)  // #04060B
    static let surfaceVoid2  = Color(red: 0.039, green: 0.055, blue: 0.086)  // #0A0E16
    static let surfaceShadow = Color(red: 0.067, green: 0.082, blue: 0.114)  // #11151D
    static let surfaceSteel  = Color(red: 0.102, green: 0.122, blue: 0.165)  // #1A1F2A
    static let surfaceIron   = Color(red: 0.137, green: 0.161, blue: 0.212)  // #232936

    // Text
    static let textPrimary = Color(red: 0.961, green: 0.949, blue: 0.925)    // #F5F2EC
    static let textBody    = Color(red: 0.722, green: 0.733, blue: 0.761)    // #B8BBC2
    static let textMuted   = Color(red: 0.420, green: 0.431, blue: 0.463)    // #6B6E76
    static let textGhost   = Color(red: 0.227, green: 0.239, blue: 0.267)    // #3A3D44

    // Gold accent family — the mentor's voice
    static let gold      = Color(red: 0.910, green: 0.780, blue: 0.494)      // #E8C77E
    static let gold2     = Color(red: 0.780, green: 0.639, blue: 0.420)      // #C7A36B
    static let goldDeep  = Color(red: 0.541, green: 0.435, blue: 0.267)      // #8a6f44
    static let goldFlash = Color(red: 1.000, green: 0.914, blue: 0.710)      // #FFE9B5
    static let honey     = Color(red: 0.831, green: 0.647, blue: 0.455)      // #D4A574
    static let cream     = Color(red: 0.941, green: 0.898, blue: 0.824)      // #F0E5D2

    // Signal palette
    static let crimson      = Color(red: 0.722, green: 0.200, blue: 0.290)   // #B8334A
    static let crimsonDeep  = Color(red: 0.420, green: 0.122, blue: 0.122)   // #6B1F1F
    static let emerald      = Color(red: 0.290, green: 0.541, blue: 0.361)   // #4A8A5C
    static let emeraldBright = Color(red: 0.420, green: 0.686, blue: 0.486)  // #6BAF7C
    static let emeraldDeep  = Color(red: 0.122, green: 0.239, blue: 0.165)   // #1F3D2A
    static let sage         = Color(red: 0.541, green: 0.659, blue: 0.463)   // #8AA876
    static let rose         = Color(red: 0.761, green: 0.545, blue: 0.545)   // #C28B8B
}

/// Typography — Cinzel (display), Cormorant Garamond (mentor voice),
/// JetBrains Mono (data), Inter (UI). Add fonts to Xcode project.
enum MentorFonts {
    static func display(_ size: CGFloat) -> Font {
        .custom("Cinzel-Bold", size: size).weight(.bold)
    }
    static func mentor(_ size: CGFloat, italic: Bool = true) -> Font {
        let name = italic ? "CormorantGaramond-MediumItalic" : "CormorantGaramond-Medium"
        return .custom(name, size: size)
    }
    static func mentorBold(_ size: CGFloat) -> Font {
        .custom("CormorantGaramond-SemiBoldItalic", size: size)
    }
    static func mono(_ size: CGFloat) -> Font {
        .custom("JetBrainsMono-Medium", size: size)
    }
    static func ui(_ size: CGFloat, weight: Font.Weight = .medium) -> Font {
        .system(size: size, weight: weight, design: .default)
    }
}
