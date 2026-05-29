import SwiftUI

/// Onboarding — "Va'da" (the vow). Mirrors `voice-commitment.html`.
/// Mentor introduces itself; user records a spoken promise to stay disciplined.
struct VoiceCommitmentView: View {
    @State private var recording = false
    @State private var pulse = false

    var onContinue: () -> Void = {}

    var body: some View {
        VoidBackdrop(goldAmbient: false) {
            VStack(spacing: 0) {
                Spacer().frame(height: 18)

                MentorPill("✦  VA'DA LAHZASI")

                Spacer().frame(height: 18)

                BrandSeal(text: "tanishuv lahzasi")

                Spacer().frame(height: 26)

                Text("MURABBIY · BIRINCHI SO'Z")
                    .font(MentorFonts.mono(8.5))
                    .tracking(3)
                    .foregroundColor(MentorColors.goldDeep)

                // Mentor invite
                (Text("Salom, ")
                    .foregroundColor(MentorColors.textPrimary)
                 + Text("Aziz")
                    .foregroundColor(MentorColors.gold)
                 + Text(". Endi va'da ber.")
                    .foregroundColor(MentorColors.textPrimary))
                .font(MentorFonts.mentorBold(24))
                .multilineTextAlignment(.center)
                .padding(.horizontal, 28)
                .padding(.top, 8)

                Text("intizomli bo'lishga o'z ovozing bilan va'da ber — bu lahza qiyin kunda qaytarib eshitiladi.")
                    .font(MentorFonts.mentor(14))
                    .foregroundColor(MentorColors.textBody)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal, 32)
                    .padding(.top, 10)

                Spacer()

                // Record button
                ZStack {
                    Circle()
                        .fill(MentorColors.gold.opacity(recording ? 0.30 : 0.18))
                        .frame(width: 130, height: 130)
                        .blur(radius: 24)
                        .scaleEffect(pulse ? 1.15 : 0.9)

                    Button {
                        withAnimation(.easeInOut(duration: 0.2)) { recording.toggle() }
                    } label: {
                        Image(systemName: recording ? "stop.fill" : "mic.fill")
                            .font(.system(size: 30, weight: .medium))
                            .foregroundColor(MentorColors.surfaceVoid)
                            .frame(width: 84, height: 84)
                            .background(
                                LinearGradient(colors: [MentorColors.goldFlash, MentorColors.gold, MentorColors.gold2],
                                               startPoint: .top, endPoint: .bottom)
                            )
                            .clipShape(Circle())
                            .shadow(color: MentorColors.gold.opacity(0.5), radius: 18)
                    }
                    .buttonStyle(.plain)
                }
                .onAppear {
                    withAnimation(.easeInOut(duration: 2).repeatForever(autoreverses: true)) { pulse = true }
                }

                Text(recording ? "TINGLAYAPMAN…" : "OVOZ BILAN AYT")
                    .font(MentorFonts.mono(9))
                    .tracking(3)
                    .foregroundColor(recording ? MentorColors.gold : MentorColors.textMuted)
                    .padding(.top, 18)

                Spacer()

                // Vow capture box
                VStack(alignment: .leading, spacing: 6) {
                    Text("SENING VA'DANG")
                        .font(MentorFonts.mono(8.5))
                        .tracking(3)
                        .foregroundColor(MentorColors.goldDeep)
                    Text("— mikrofonni bos va aytib ber —")
                        .font(MentorFonts.mentor(13))
                        .foregroundColor(MentorColors.textGhost)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(14)
                .overlay(
                    RoundedRectangle(cornerRadius: 4)
                        .strokeBorder(style: StrokeStyle(lineWidth: 1, dash: [4, 4]))
                        .foregroundColor(MentorColors.goldDeep)
                )
                .padding(.horizontal, 24)

                Spacer().frame(height: 16)

                MentorPrimaryButton("Ovozni yozib olish", action: onContinue)
                    .padding(.horizontal, 24)
                    .padding(.bottom, 22)
            }
        }
        .preferredColorScheme(.dark)
    }
}

#Preview {
    VoiceCommitmentView()
}
