import SwiftUI

/// Onboarding step 1 — first cinematic introduction to MNSM brand & M-VoW app.
/// Mirrors `welcome.html` design.
struct WelcomeView: View {
    @State private var logoVisible = false
    @State private var titleVisible = false
    @State private var taglineVisible = false
    @State private var promiseVisible = false
    @State private var ctaVisible = false
    @State private var selectedLang: Language = .uz

    enum Language: String, CaseIterable { case uz = "UZ", ru = "RU", en = "EN" }

    var onStart: () -> Void = {}

    var body: some View {
        VoidBackdrop(goldAmbient: true) {
            VStack(spacing: 0) {
                Spacer().frame(height: 50)

                // Tiny version
                Text("M · VoW · v 2 · 0")
                    .font(MentorFonts.mono(8))
                    .tracking(6)
                    .foregroundColor(MentorColors.goldDeep)
                    .opacity(logoVisible ? 1 : 0)

                Spacer().frame(height: 40)

                // MNSM logo (bundle image — add 'mnsm-logo.png' to Xcode assets)
                ZStack {
                    Circle()
                        .fill(MentorColors.gold.opacity(0.35))
                        .frame(width: 200, height: 200)
                        .blur(radius: 36)
                        .opacity(logoVisible ? 0.55 : 0)

                    Image("mnsm-logo")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 220, height: 160)
                        .opacity(logoVisible ? 1 : 0)
                        .scaleEffect(logoVisible ? 1.0 : 0.85)
                        .blur(radius: logoVisible ? 0 : 8)
                }

                Spacer().frame(height: 16)

                // App name M·VoW
                Text("M · VoW")
                    .font(MentorFonts.display(28))
                    .tracking(10)
                    .foregroundColor(MentorColors.gold)
                    .shadow(color: MentorColors.gold.opacity(0.5), radius: 12)
                    .opacity(titleVisible ? 1 : 0)
                    .offset(y: titleVisible ? 0 : 8)

                // Subtitle
                Text("sening intizom do'sting")
                    .font(MentorFonts.mentor(15))
                    .foregroundColor(MentorColors.honey)
                    .tracking(1)
                    .opacity(taglineVisible ? 1 : 0)
                    .padding(.top, 6)

                // Attribution
                Text("by MNSM · AI Specialist")
                    .font(MentorFonts.mono(8.5))
                    .tracking(4)
                    .foregroundColor(MentorColors.goldDeep)
                    .padding(.top, 10)
                    .opacity(taglineVisible ? 1 : 0)

                Spacer().frame(height: 32)

                // Promise
                VStack(spacing: 4) {
                    Text("Sen aytasan — men ")
                        .foregroundColor(MentorColors.textBody) +
                    Text("eshitaman")
                        .foregroundColor(MentorColors.gold)
                        .fontWeight(.semibold) +
                    Text(".")
                        .foregroundColor(MentorColors.textBody)

                    Text("Birga ")
                        .foregroundColor(MentorColors.textBody) +
                    Text("g'olib")
                        .foregroundColor(MentorColors.gold)
                        .fontWeight(.semibold) +
                    Text(" bo'lamiz.")
                        .foregroundColor(MentorColors.textBody)
                }
                .font(MentorFonts.mentor(17))
                .multilineTextAlignment(.center)
                .padding(.horizontal, 32)
                .opacity(promiseVisible ? 1 : 0)
                .offset(y: promiseVisible ? 0 : 8)

                Spacer().frame(height: 24)

                // Three pillars
                HStack(spacing: 6) {
                    PillarChip(symbol: "○", title: "DO'ST", sub: "qattiq nazoratchi emas")
                    PillarChip(symbol: "◇", title: "PSIXOLOG", sub: "savol bilan anglatadi")
                    PillarChip(symbol: "⌖", title: "NEYRO-\nBIOLOG", sub: "miyangni o'rgangan")
                }
                .padding(.horizontal, 24)
                .opacity(ctaVisible ? 1 : 0)

                Spacer().frame(height: 18)

                // Language picker
                HStack(spacing: 6) {
                    ForEach(Language.allCases, id: \.self) { lang in
                        LangChip(text: lang.rawValue, active: selectedLang == lang) {
                            withAnimation(.easeInOut(duration: 0.2)) {
                                selectedLang = lang
                            }
                        }
                    }
                }
                .opacity(ctaVisible ? 1 : 0)

                Spacer()

                // CTA
                VStack(spacing: 10) {
                    Button(action: onStart) {
                        Text("Boshlaymiz")
                            .font(.system(size: 13, weight: .bold))
                            .tracking(8)
                            .foregroundColor(MentorColors.surfaceVoid)
                            .frame(maxWidth: .infinity)
                            .padding(.vertical, 16)
                            .background(
                                LinearGradient(
                                    colors: [MentorColors.cream, MentorColors.gold, MentorColors.goldDeep],
                                    startPoint: .top, endPoint: .bottom
                                )
                            )
                            .cornerRadius(4)
                            .shadow(color: MentorColors.gold.opacity(0.45), radius: 18)
                    }

                    Text("OFFLINE · SHAXSIY MA'LUMOT — TELEFONIDA · MNSM")
                        .font(MentorFonts.mono(7.5))
                        .tracking(2)
                        .foregroundColor(MentorColors.textGhost)
                }
                .padding(.horizontal, 24)
                .opacity(ctaVisible ? 1 : 0)
                .offset(y: ctaVisible ? 0 : 12)

                Spacer().frame(height: 30)
            }
            .padding(.top, 14)
            .padding(.bottom, 14)
        }
        .preferredColorScheme(.dark)
        .onAppear { runIntro() }
    }

    private func runIntro() {
        withAnimation(.easeOut(duration: 1.0).delay(0.2))  { logoVisible = true }
        withAnimation(.easeOut(duration: 0.9).delay(1.2))  { titleVisible = true }
        withAnimation(.easeOut(duration: 0.8).delay(1.7))  { taglineVisible = true }
        withAnimation(.easeOut(duration: 0.8).delay(2.4))  { promiseVisible = true }
        withAnimation(.easeOut(duration: 0.8).delay(3.3))  { ctaVisible = true }
    }
}

private struct PillarChip: View {
    let symbol: String
    let title: String
    let sub: String

    var body: some View {
        VStack(spacing: 4) {
            Text(symbol)
                .font(.system(size: 18))
                .foregroundColor(MentorColors.gold)
            Text(title)
                .font(MentorFonts.mono(8.5))
                .tracking(1.5)
                .foregroundColor(MentorColors.gold)
                .multilineTextAlignment(.center)
            Text(sub)
                .font(MentorFonts.mentor(11))
                .foregroundColor(MentorColors.textMuted)
                .multilineTextAlignment(.center)
                .lineLimit(2)
        }
        .frame(maxWidth: .infinity)
        .padding(10)
        .background(MentorColors.gold.opacity(0.06))
        .overlay(
            RoundedRectangle(cornerRadius: 4)
                .strokeBorder(MentorColors.goldDeep.opacity(0.4), lineWidth: 1)
        )
        .cornerRadius(4)
    }
}

private struct LangChip: View {
    let text: String
    let active: Bool
    let onTap: () -> Void

    var body: some View {
        Button(action: onTap) {
            Text(text)
                .font(MentorFonts.mono(9))
                .tracking(2)
                .foregroundColor(active ? MentorColors.gold : MentorColors.goldDeep)
                .padding(.vertical, 6)
                .padding(.horizontal, 14)
                .background(active ? MentorColors.gold.opacity(0.14) : MentorColors.gold.opacity(0.04))
                .overlay(
                    Capsule()
                        .strokeBorder(active ? MentorColors.gold : MentorColors.goldDeep, lineWidth: 1)
                )
                .clipShape(Capsule())
                .shadow(color: active ? MentorColors.gold.opacity(0.3) : .clear, radius: 6)
        }
        .buttonStyle(.plain)
    }
}

#Preview {
    WelcomeView()
}
