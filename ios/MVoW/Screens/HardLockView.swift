import SwiftUI

/// "Ekran qulflash" — focus lock screen. Mirrors `hard-lock.html`.
/// Calm teal "locked" state; turns crimson "alert" when user tries a blocked app,
/// offering to stay focused or request permission (-> Negotiation).
struct HardLockView: View {
    enum LockState { case locked, alert }

    @State private var state: LockState = .locked
    @State private var alertApp: String = "Instagram"

    var onRequestPermission: () -> Void = {}
    var onTalkToMentor: () -> Void = {}

    private let blocked = ["Instagram", "TikTok", "YouTube"]

    private var accent: Color {
        state == .alert ? MentorColors.crimson : Color(red: 0.0, green: 0.9, blue: 0.83)
    }

    var body: some View {
        ZStack {
            (state == .alert ? Color(red: 0.11, green: 0.04, blue: 0.05) : MentorColors.surfaceVoid)
                .ignoresSafeArea()

            VStack(spacing: 0) {
                Spacer().frame(height: 30)

                // Brand
                HStack(spacing: 8) {
                    Image(systemName: "shield.fill")
                        .foregroundColor(accent)
                    Text("M·VoW")
                        .font(.system(size: 18, weight: .semibold))
                        .foregroundColor(accent)
                }

                Spacer().frame(height: 24)

                MentorPill(state == .alert ? "FOKUS BUZILMOQDA" : "QULFLANGAN · FOKUSDA",
                           color: accent, leadingDot: true)

                Spacer().frame(height: 30)

                // Timer ring
                ZStack {
                    Circle()
                        .stroke(accent.opacity(0.15), lineWidth: 4)
                    Circle()
                        .trim(from: 0, to: 0.62)
                        .stroke(accent, style: StrokeStyle(lineWidth: 4, lineCap: .round))
                        .rotationEffect(.degrees(-90))
                    VStack(spacing: 6) {
                        Text("34:56")
                            .font(.system(size: 46, weight: .semibold, design: .rounded))
                            .foregroundColor(accent)
                        Text("KEYINGI DAM'GACHA QOLDI")
                            .font(MentorFonts.mono(8))
                            .tracking(2)
                            .foregroundColor(MentorColors.textMuted)
                    }
                }
                .frame(width: 230, height: 230)

                Spacer().frame(height: 26)

                // Task pill
                Text("ARAB GRAMMATIKASI · 06:20 — 07:20")
                    .font(MentorFonts.mono(11))
                    .tracking(1)
                    .foregroundColor(accent)
                    .padding(.vertical, 10)
                    .padding(.horizontal, 18)
                    .frame(maxWidth: .infinity)
                    .background(accent.opacity(0.08))
                    .overlay(Capsule().strokeBorder(accent, lineWidth: 1))
                    .clipShape(Capsule())
                    .padding(.horizontal, 24)

                Spacer().frame(height: 22)

                if state == .alert {
                    alertBanner
                        .padding(.horizontal, 24)
                        .transition(.scale.combined(with: .opacity))
                } else {
                    blockedList
                        .padding(.horizontal, 24)
                }

                Spacer()

                if state == .locked {
                    Button(action: onTalkToMentor) {
                        VStack(spacing: 8) {
                            Image(systemName: "mic.fill")
                                .font(.system(size: 24))
                                .foregroundColor(MentorColors.surfaceVoid)
                                .frame(width: 64, height: 64)
                                .background(
                                    LinearGradient(colors: [accent.opacity(0.9), accent],
                                                   startPoint: .top, endPoint: .bottom)
                                )
                                .clipShape(Circle())
                                .shadow(color: accent.opacity(0.5), radius: 16)
                            Text("MURABBIY BILAN SUHBAT")
                                .font(MentorFonts.mono(8.5))
                                .tracking(2)
                                .foregroundColor(MentorColors.textMuted)
                        }
                    }
                    .buttonStyle(.plain)

                    Text("FOKUSDA QOL, AZIZ. CHALG'IMA.")
                        .font(.system(size: 13, weight: .semibold))
                        .foregroundColor(accent)
                        .padding(.top, 14)
                }

                Spacer().frame(height: 30)
            }
        }
        .preferredColorScheme(.dark)
    }

    private var blockedList: some View {
        VStack(alignment: .leading, spacing: 10) {
            Text("BLOKLANGAN · DIQQATNI O'G'IRLAGUVCHILAR")
                .font(MentorFonts.mono(8.5))
                .tracking(2)
                .foregroundColor(MentorColors.textMuted)
            ForEach(blocked, id: \.self) { app in
                Button {
                    alertApp = app
                    withAnimation(.easeInOut(duration: 0.2)) { state = .alert }
                } label: {
                    HStack(spacing: 8) {
                        Image(systemName: "nosign")
                            .font(.system(size: 13))
                            .foregroundColor(MentorColors.textMuted)
                        Text(app.uppercased())
                            .font(MentorFonts.mono(11))
                            .tracking(1)
                            .foregroundColor(MentorColors.textMuted)
                            .strikethrough()
                        Spacer()
                    }
                }
                .buttonStyle(.plain)
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }

    private var alertBanner: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("⚠ FOKUS BUZILMOQDA")
                .font(MentorFonts.mono(9))
                .tracking(3)
                .foregroundColor(MentorColors.crimson)

            (Text(alertApp)
                .foregroundColor(MentorColors.crimson)
                .fontWeight(.semibold)
             + Text(" ochmoqchimisan? Hozir ")
                .foregroundColor(MentorColors.textPrimary)
             + Text("Arab grammatikasi")
                .foregroundColor(MentorColors.crimson)
                .fontWeight(.semibold)
             + Text(" vaqti.")
                .foregroundColor(MentorColors.textPrimary))
            .font(MentorFonts.mentor(15))
            .fixedSize(horizontal: false, vertical: true)

            HStack(spacing: 8) {
                Button {
                    withAnimation(.easeInOut(duration: 0.2)) { state = .locked }
                } label: {
                    Text("YO'Q · FOKUSDA QOLAMAN")
                        .font(.system(size: 10, weight: .bold))
                        .tracking(1)
                        .foregroundColor(MentorColors.surfaceVoid)
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, 11)
                        .background(Color(red: 0.0, green: 0.9, blue: 0.83))
                        .cornerRadius(4)
                }
                .buttonStyle(.plain)

                Button(action: onRequestPermission) {
                    Text("RUXSAT SO'RAYMAN ›")
                        .font(.system(size: 10, weight: .bold))
                        .tracking(1)
                        .foregroundColor(MentorColors.crimson)
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, 11)
                        .overlay(
                            RoundedRectangle(cornerRadius: 4)
                                .strokeBorder(MentorColors.crimson, lineWidth: 1.5)
                        )
                }
                .buttonStyle(.plain)
            }
        }
        .padding(14)
        .background(MentorColors.crimson.opacity(0.10))
        .overlay(
            RoundedRectangle(cornerRadius: 6)
                .strokeBorder(MentorColors.crimson, lineWidth: 1.5)
        )
        .cornerRadius(6)
    }
}

#Preview {
    HardLockView()
}
