import SwiftUI

/// "Sozlamalar" — settings. Mirrors `settings.html`.
/// Mentor tone + interruption frequency sliders, default session config.
struct SettingsView: View {
    @State private var tone: Double = 0.5        // 0 yumshoq … 1 qattiq
    @State private var blend: Double = 0.3       // sukut … faol
    @State private var defaultDuration = 45
    @State private var reminderMinutes = 5
    @State private var autoPostpone = true

    var body: some View {
        VoidBackdrop {
            VStack(spacing: 0) {
                Spacer().frame(height: 18)

                // Header
                HStack {
                    Button(action: {}) {
                        Image(systemName: "chevron.left").foregroundColor(MentorColors.gold)
                    }
                    Spacer()
                    MentorPill("SOZLAMALAR")
                    Spacer()
                    Image(systemName: "power").foregroundColor(MentorColors.gold)
                }
                .padding(.horizontal, 24)

                ScrollView {
                    VStack(spacing: 20) {
                        // Profile row
                        HStack(spacing: 14) {
                            MentorOrb(size: 44, breathing: false)
                            VStack(alignment: .leading, spacing: 3) {
                                Text("Aziz")
                                    .font(MentorFonts.mentorBold(18))
                                    .foregroundColor(MentorColors.textPrimary)
                                Text("YO'L · 71 KUN · SHAXSIY KABINET")
                                    .font(MentorFonts.mono(8))
                                    .tracking(1.5)
                                    .foregroundColor(MentorColors.goldDeep)
                            }
                            Spacer()
                            Image(systemName: "chevron.right").foregroundColor(MentorColors.textMuted)
                        }
                        .padding(14)
                        .background(MentorColors.gold.opacity(0.05))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.goldDeep.opacity(0.4), lineWidth: 1))
                        .cornerRadius(8)

                        sectionLabel("MURABBIY · MEN BILAN")

                        VStack(spacing: 20) {
                            sliderRow(title: "Ohang", value: $tone,
                                      left: "YUMSHOQ", mid: "O'RTA", right: "QATTIQ",
                                      tag: "O'RTA · DO'STONA")
                            sliderRow(title: "Aralashish", value: $blend,
                                      left: "SUKUT", mid: "KAMTAR", right: "FAOL",
                                      tag: "KAMTAR")

                            Text("Murabbiy hozir do'stona ohangda gapiradi. Kuniga 3-4 marta aralashadi: ertalab, sessiyadan oldin, kun oxirida.")
                                .font(MentorFonts.mentor(13))
                                .foregroundColor(MentorColors.textMuted)
                                .fixedSize(horizontal: false, vertical: true)
                        }
                        .padding(14)
                        .background(MentorColors.surfaceVoid2.opacity(0.6))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.goldDeep.opacity(0.3), lineWidth: 1))
                        .cornerRadius(8)

                        sectionLabel("SESSIYA · SOZLAMALAR")

                        stepperRow(icon: "timer", title: "Default davomiylik",
                                   sub: "yangi sessiya uchun", value: "\(defaultDuration) DAQ")
                        stepperRow(icon: "bell", title: "Eslatish vaqti",
                                   sub: "sessiyadan necha daq oldin", value: "\(reminderMinutes) DAQ")
                        toggleRow(icon: "arrow.clockwise", title: "Auto-postpone",
                                  sub: "javob bermasang — chaqirish davom etadi", isOn: $autoPostpone)
                    }
                    .padding(.horizontal, 24)
                    .padding(.vertical, 18)
                }
            }
        }
        .preferredColorScheme(.dark)
    }

    private func sectionLabel(_ text: String) -> some View {
        Text(text)
            .font(MentorFonts.mono(9))
            .tracking(3)
            .foregroundColor(MentorColors.goldDeep)
            .frame(maxWidth: .infinity, alignment: .leading)
    }

    private func sliderRow(title: String, value: Binding<Double>,
                           left: String, mid: String, right: String, tag: String) -> some View {
        VStack(spacing: 8) {
            HStack {
                Text(title)
                    .font(MentorFonts.ui(16, weight: .semibold))
                    .foregroundColor(MentorColors.textPrimary)
                Spacer()
                Text(tag)
                    .font(MentorFonts.mono(9))
                    .tracking(1.5)
                    .foregroundColor(MentorColors.gold)
            }
            Slider(value: value)
                .tint(MentorColors.gold)
            HStack {
                Text(left); Spacer(); Text(mid); Spacer(); Text(right)
            }
            .font(MentorFonts.mono(8))
            .foregroundColor(MentorColors.textMuted)
        }
    }

    private func stepperRow(icon: String, title: String, sub: String, value: String) -> some View {
        HStack(spacing: 12) {
            Image(systemName: icon).foregroundColor(MentorColors.gold).frame(width: 22)
            VStack(alignment: .leading, spacing: 2) {
                Text(title).font(MentorFonts.ui(15, weight: .semibold)).foregroundColor(MentorColors.textPrimary)
                Text(sub).font(MentorFonts.mentor(12)).foregroundColor(MentorColors.textMuted)
            }
            Spacer()
            Text(value).font(MentorFonts.mono(11)).foregroundColor(MentorColors.gold)
            Image(systemName: "chevron.right").font(.system(size: 12)).foregroundColor(MentorColors.textMuted)
        }
        .padding(14)
        .background(MentorColors.surfaceVoid2.opacity(0.6))
        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.goldDeep.opacity(0.3), lineWidth: 1))
        .cornerRadius(8)
    }

    private func toggleRow(icon: String, title: String, sub: String, isOn: Binding<Bool>) -> some View {
        HStack(spacing: 12) {
            Image(systemName: icon).foregroundColor(MentorColors.gold).frame(width: 22)
            VStack(alignment: .leading, spacing: 2) {
                Text(title).font(MentorFonts.ui(15, weight: .semibold)).foregroundColor(MentorColors.textPrimary)
                Text(sub).font(MentorFonts.mentor(12)).foregroundColor(MentorColors.textMuted)
            }
            Spacer()
            Toggle("", isOn: isOn).labelsHidden().tint(MentorColors.gold)
        }
        .padding(14)
        .background(MentorColors.surfaceVoid2.opacity(0.6))
        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.goldDeep.opacity(0.3), lineWidth: 1))
        .cornerRadius(8)
    }
}

#Preview {
    SettingsView()
}
