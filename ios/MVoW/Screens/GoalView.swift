import SwiftUI

/// Onboarding — "Niyat" (intention). Mirrors `goal.html`.
/// User states who they want to become in 3 months, picks a direction.
struct GoalView: View {
    @State private var goalText: String = "Intizomli, ilmda ko'tarilgan, yaqinlariga suyanchiq odam."
    @State private var selected: Direction? = nil
    @FocusState private var editing: Bool

    var onContinue: () -> Void = {}

    enum Direction: String, CaseIterable, Identifiable {
        case ilm, soglik, munosabat
        var id: String { rawValue }

        var symbol: String {
            switch self {
            case .ilm: return "○"
            case .soglik: return "⌖"
            case .munosabat: return "∞"
            }
        }
        var title: String {
            switch self {
            case .ilm: return "ILM"
            case .soglik: return "SOG'LIK"
            case .munosabat: return "MUNOSABAT"
            }
        }
        var sub: String {
            switch self {
            case .ilm: return "o'rganaman, o'rgataman"
            case .soglik: return "tana — ruh muvozanati"
            case .munosabat: return "oila, do'stlar, jamiyat"
            }
        }
    }

    var body: some View {
        VoidBackdrop {
            VStack(spacing: 0) {
                Spacer().frame(height: 18)

                MentorPill("★  NIYAT", color: MentorColors.honey)

                Spacer().frame(height: 28)

                // Mentor question
                HStack(alignment: .top, spacing: 12) {
                    MentorOrb(size: 30)
                    (Text("3 oydan keyin sen ")
                        .foregroundColor(MentorColors.textPrimary)
                     + Text("kim")
                        .foregroundColor(MentorColors.gold)
                     + Text(" bo'lishni xohlaysan?")
                        .foregroundColor(MentorColors.textPrimary))
                    .font(MentorFonts.mentorBold(24))
                    .fixedSize(horizontal: false, vertical: true)
                }
                .padding(.horizontal, 24)

                Text("— ICHINGGA QARA, SEKIN JAVOB BER —")
                    .font(MentorFonts.mono(8.5))
                    .tracking(3)
                    .foregroundColor(MentorColors.goldDeep)
                    .padding(.top, 10)
                    .padding(.leading, 42)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.horizontal, 24)

                Spacer().frame(height: 22)

                // Answer field
                VStack(alignment: .leading, spacing: 0) {
                    HStack(alignment: .top) {
                        TextField("", text: $goalText, axis: .vertical)
                            .focused($editing)
                            .font(MentorFonts.mentor(16))
                            .foregroundColor(MentorColors.textPrimary)
                            .lineLimit(3...5)
                        Spacer(minLength: 8)
                        Image(systemName: "mic.fill")
                            .font(.system(size: 14))
                            .foregroundColor(MentorColors.surfaceVoid)
                            .frame(width: 30, height: 30)
                            .background(
                                LinearGradient(colors: [MentorColors.goldFlash, MentorColors.gold, MentorColors.gold2],
                                               startPoint: .top, endPoint: .bottom)
                            )
                            .clipShape(Circle())
                    }
                    .padding(14)
                }
                .background(MentorColors.gold.opacity(0.04))
                .overlay(
                    RoundedRectangle(cornerRadius: 4)
                        .strokeBorder(editing ? MentorColors.gold : MentorColors.goldDeep.opacity(0.5), lineWidth: 1)
                )
                .cornerRadius(4)
                .padding(.horizontal, 24)

                Spacer().frame(height: 22)

                // Direction chips
                Text("YO'NALISH TANLA")
                    .font(MentorFonts.mono(8.5))
                    .tracking(3)
                    .foregroundColor(MentorColors.goldDeep)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.horizontal, 24)
                    .padding(.bottom, 8)

                ForEach(Direction.allCases) { dir in
                    DirectionChip(dir: dir, selected: selected == dir) {
                        withAnimation(.easeInOut(duration: 0.15)) {
                            selected = (selected == dir) ? nil : dir
                        }
                    }
                    .padding(.horizontal, 24)
                    .padding(.bottom, 8)
                }

                Spacer()

                MentorPrimaryButton("Davom etamiz", action: onContinue)
                    .padding(.horizontal, 24)
                    .padding(.bottom, 22)
            }
        }
        .preferredColorScheme(.dark)
    }
}

private struct DirectionChip: View {
    let dir: GoalView.Direction
    let selected: Bool
    let onTap: () -> Void

    var body: some View {
        Button(action: onTap) {
            HStack(spacing: 12) {
                Text(dir.symbol)
                    .font(.system(size: 18))
                    .foregroundColor(MentorColors.gold)
                    .frame(width: 22)
                VStack(alignment: .leading, spacing: 2) {
                    Text(dir.title)
                        .font(MentorFonts.mono(9))
                        .tracking(2)
                        .foregroundColor(MentorColors.gold)
                    Text(dir.sub)
                        .font(MentorFonts.mentor(11))
                        .foregroundColor(MentorColors.textMuted)
                }
                Spacer()
            }
            .padding(.horizontal, 12)
            .padding(.vertical, 10)
            .background(selected ? MentorColors.gold.opacity(0.14) : MentorColors.gold.opacity(0.04))
            .overlay(
                RoundedRectangle(cornerRadius: 4)
                    .strokeBorder(selected ? MentorColors.gold : MentorColors.goldDeep, lineWidth: 1)
            )
            .cornerRadius(4)
        }
        .buttonStyle(.plain)
    }
}

#Preview {
    GoalView()
}
