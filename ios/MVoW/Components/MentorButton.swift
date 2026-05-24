import SwiftUI

/// Primary action button — matches Android `MentorPrimaryButton`.
/// Dark Discipline aesthetic: 4dp corner, hard not soft, full-width by default.
struct MentorPrimaryButton: View {
    let title: String
    let enabled: Bool
    let action: () -> Void

    init(_ title: String, enabled: Bool = true, action: @escaping () -> Void) {
        self.title = title
        self.enabled = enabled
        self.action = action
    }

    var body: some View {
        Button(action: action) {
            Text(title.uppercased())
                .font(.system(size: 14, weight: .bold))
                .tracking(2)
                .foregroundColor(enabled ? MentorColors.surfaceVoid : MentorColors.textMuted)
                .frame(maxWidth: .infinity)
                .frame(height: 56)
                .background(
                    RoundedRectangle(cornerRadius: 4)
                        .fill(enabled ? MentorColors.cream : MentorColors.surfaceIron)
                )
        }
        .disabled(!enabled)
    }
}

/// Secondary outlined button.
struct MentorSecondaryButton: View {
    let title: String
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            Text(title)
                .font(.system(size: 14, weight: .medium))
                .tracking(1)
                .foregroundColor(MentorColors.textBody)
                .frame(maxWidth: .infinity)
                .frame(height: 56)
                .overlay(
                    RoundedRectangle(cornerRadius: 4)
                        .strokeBorder(MentorColors.textMuted, lineWidth: 1)
                )
        }
    }
}

/// Pill — small status badge with optional leading dot.
struct MentorPill: View {
    let text: String
    let color: Color
    let leadingDot: Bool

    init(_ text: String, color: Color = MentorColors.gold, leadingDot: Bool = false) {
        self.text = text
        self.color = color
        self.leadingDot = leadingDot
    }

    var body: some View {
        HStack(spacing: 6) {
            if leadingDot {
                Circle().fill(color).frame(width: 6, height: 6)
            }
            Text(text)
                .font(MentorFonts.mono(9))
                .tracking(3)
                .foregroundColor(color)
        }
        .padding(.horizontal, 12)
        .padding(.vertical, 5)
        .background(
            Capsule().fill(color.opacity(0.10))
                .overlay(Capsule().strokeBorder(color, lineWidth: 1))
        )
    }
}
