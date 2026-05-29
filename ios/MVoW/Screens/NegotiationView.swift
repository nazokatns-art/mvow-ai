import SwiftUI

/// "Diqqat buzilishi" — focus negotiation. Mirrors `negotiation.html`.
/// User asks the mentor for permission to break focus; mentor listens, weighs,
/// then grants (valid reason) or denies (excuse). Warm friend, not a warden.
struct NegotiationView: View {
    enum Phase: String, CaseIterable {
        case awaiting, thinking, denied, granted
    }

    @State private var phase: Phase = .awaiting

    var onReturn: () -> Void = {}

    var body: some View {
        ZStack {
            backdrop.ignoresSafeArea()

            VStack(spacing: 0) {
                Spacer().frame(height: 24)

                MentorPill(headerStamp, color: phaseColor, leadingDot: true)

                Spacer().frame(height: 14)

                Text("ARAB GRAMMATIKASI")
                    .font(MentorFonts.mono(9))
                    .tracking(3)
                    .foregroundColor(MentorColors.textMuted)

                Text(seal)
                    .font(MentorFonts.mentor(14))
                    .foregroundColor(MentorColors.honey)
                    .padding(.top, 10)

                Spacer().frame(height: 18)

                // User reason
                VStack(spacing: 6) {
                    Text("SEN AYTDING")
                        .font(MentorFonts.mono(8))
                        .tracking(3)
                        .foregroundColor(MentorColors.goldDeep)
                    Text(reason)
                        .font(MentorFonts.mentor(15))
                        .foregroundColor(MentorColors.textBody)
                        .multilineTextAlignment(.center)
                }
                .padding(.horizontal, 30)

                Spacer().frame(height: 24)

                // Mentor orb + verdict ring
                ZStack {
                    Circle()
                        .stroke(phaseColor.opacity(0.18), lineWidth: 3)
                        .frame(width: 200, height: 200)
                    MentorOrb(size: 64, tone: orbTone)
                    if phase == .denied || phase == .granted {
                        Text("00:34:56")
                            .font(.system(size: 26, weight: .semibold, design: .rounded))
                            .foregroundColor(phaseColor)
                            .offset(y: 78)
                    }
                }

                Spacer().frame(height: 20)

                // Mentor message
                Text(mentorLine)
                    .font(MentorFonts.mentorBold(19))
                    .foregroundColor(MentorColors.textPrimary)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal, 28)
                    .fixedSize(horizontal: false, vertical: true)

                Text(wisdom)
                    .font(MentorFonts.mentor(13))
                    .foregroundColor(MentorColors.textMuted)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal, 32)
                    .padding(.top, 10)

                Spacer()

                // Action — drives the demo through phases
                MentorPrimaryButton(buttonLabel) {
                    advance()
                }
                .padding(.horizontal, 24)
                .padding(.bottom, 22)
            }
        }
        .preferredColorScheme(.dark)
    }

    private func advance() {
        switch phase {
        case .awaiting: withAnimation { phase = .thinking }
        case .thinking: withAnimation { phase = .denied }
        case .denied:   onReturn()
        case .granted:  onReturn()
        }
    }

    // MARK: - Phase content

    private var phaseColor: Color {
        switch phase {
        case .awaiting, .thinking: return MentorColors.gold
        case .denied:  return MentorColors.crimson
        case .granted: return MentorColors.emerald
        }
    }
    private var orbTone: MentorOrb.Tone {
        switch phase {
        case .denied:  return .crimson
        case .granted: return .emerald
        default:       return .gold
        }
    }
    private var backdrop: Color {
        switch phase {
        case .denied:  return Color(red: 0.03, green: 0.016, blue: 0.04)
        default:       return MentorColors.surfaceVoid
        }
    }
    private var headerStamp: String {
        switch phase {
        case .awaiting: return "TINGLOV · OVOZ KUTILMOQDA"
        case .thinking: return "MULOHAZA · O'YLAB KO'RYAPTI"
        case .denied:   return "RAD · BUGUNGI 1-TALAB"
        case .granted:  return "RUXSAT · 5 DAQIQA"
        }
    }
    private var seal: String {
        switch phase {
        case .awaiting: return "murabbiy eshityapti"
        case .thinking: return "murabbiy o'ylayapti"
        case .denied:   return "murabbiy javobi"
        case .granted:  return "murabbiy ruxsati"
        }
    }
    private var reason: String {
        phase == .granted
            ? "\"Onamga muhim qo'ng'iroq qilishim kerak\""
            : "\"Instagram'da do'stim xabar yubordi\""
    }
    private var mentorLine: String {
        switch phase {
        case .awaiting: return "Sababingni ovoz bilan ayt. Murabbiy eshitadi."
        case .thinking: return "Sababing tarozida. Bir lahza shoshilmasdan…"
        case .denied:   return "Aziz, hozir Arab grammatikasi vaqti. Do'sting kuta oladi."
        case .granted:  return "Ona haqi muhim. Besh daqiqa — so'ng yana fokusing."
        }
    }
    private var wisdom: String {
        switch phase {
        case .awaiting: return "Bir lahza to'xta, sababingni o'zingga ayt."
        case .thinking: return "To'g'ri qaror — shoshilmasdan kelgani."
        case .denied:   return "Har bir \"yo'q\" — kelajakdagi \"ha\"ning poydevori."
        case .granted:  return "Eng yaxshi qaror — eng to'g'ri vaqtda kelgani."
        }
    }
    private var buttonLabel: String {
        switch phase {
        case .awaiting: return "Yuborish"
        case .thinking: return "Kuting"
        case .denied:   return "Qaytish"
        case .granted:  return "Qo'ng'iroq qilish"
        }
    }
}

#Preview {
    NegotiationView()
}
