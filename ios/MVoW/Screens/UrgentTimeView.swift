import SwiftUI

/// Urgent time — user requests session pause for emergency.
/// Mirrors Android `UrgentTimeScreen.kt`.
struct UrgentTimeView: View {
    @State private var minutes: Int = 15
    var onGrant: (Int) -> Void = { _ in }
    var onCancel: () -> Void = {}

    private let chips = [5, 10, 15, 30, 45, 60]

    var body: some View {
        ZStack(alignment: .bottom) {
            Color(red: 0.078, green: 0.024, blue: 0.043).ignoresSafeArea()
            ScrollView {
                VStack(spacing: 18) {
                    headerRow
                    BrandSeal(text: "murabbiy so'raydi")
                    question
                    TimeDial(minutes: minutes)
                    quickChips
                    reasonInput
                    resumeAt
                }
                .padding(.horizontal, 20).padding(.top, 28).padding(.bottom, 140)
            }
            bottomBar
        }
    }

    private var headerRow: some View {
        HStack {
            MentorPill("SHOSHILINCH", color: MentorColors.crimson, leadingDot: true)
            Spacer()
            HStack(spacing: 6) {
                Rectangle().fill(MentorColors.textMuted).frame(width: 3, height: 8)
                Rectangle().fill(MentorColors.textMuted).frame(width: 3, height: 8)
                Text("SESSIYA PAUZA").font(MentorFonts.mono(9)).tracking(3).foregroundColor(MentorColors.textMuted)
            }
            .padding(.horizontal, 10).padding(.vertical, 5)
            .background(Capsule().fill(Color.white.opacity(0.03))
                            .overlay(Capsule().strokeBorder(MentorColors.textGhost, lineWidth: 1)))
        }
    }

    private var question: some View {
        VStack(spacing: 8) {
            Text("SEN AYTDING — \"SHOSHILINCH ISHIM CHIQDI\"")
                .font(MentorFonts.mono(9)).tracking(3).foregroundColor(MentorColors.goldDeep)
            HStack(spacing: 0) {
                Text("Yaxshi. ").font(MentorFonts.mentor(20)).foregroundColor(MentorColors.textPrimary)
                Text("Qancha vaqt").font(MentorFonts.mentorBold(20)).foregroundColor(MentorColors.gold)
                Text(" kerak?").font(MentorFonts.mentor(20)).foregroundColor(MentorColors.textPrimary)
            }
        }
    }

    private var quickChips: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: 6) {
                ForEach(chips, id: \.self) { m in
                    let selected = m == minutes
                    Button { minutes = m } label: {
                        Text(m < 60 ? "\(m) daq" : "1 soat")
                            .font(.system(size: 11, weight: selected ? .semibold : .regular))
                            .foregroundColor(selected ? MentorColors.gold : MentorColors.textBody)
                            .padding(.horizontal, 14).padding(.vertical, 8)
                            .background(Capsule().fill(selected ? MentorColors.gold.opacity(0.18) : Color.white.opacity(0.03))
                                            .overlay(Capsule().strokeBorder(selected ? MentorColors.gold : MentorColors.textGhost, lineWidth: 1)))
                    }
                }
            }
        }
    }

    private var reasonInput: some View {
        HStack(spacing: 12) {
            Circle().fill(MentorColors.gold.opacity(0.12)).frame(width: 32, height: 32)
                .overlay(Circle().strokeBorder(MentorColors.gold.opacity(0.5), lineWidth: 1))
                .overlay(Image(systemName: "mic.fill").font(.system(size: 12)).foregroundColor(MentorColors.gold))
            Text("Sabab ayt (ixtiyoriy)…")
                .font(MentorFonts.mentor(13)).foregroundColor(MentorColors.textMuted)
            Spacer()
        }
        .padding(.horizontal, 12).padding(.vertical, 10)
        .background(RoundedRectangle(cornerRadius: 8)
                        .fill(Color.white.opacity(0.03))
                        .overlay(RoundedRectangle(cornerRadius: 8).strokeBorder(MentorColors.textGhost, lineWidth: 1)))
    }

    private var resumeAt: some View {
        let resumeDate = Calendar.current.date(byAdding: .minute, value: minutes, to: Date()) ?? Date()
        let f = DateFormatter(); f.dateFormat = "HH:mm"
        return HStack {
            Text("QAYTADAN BOSHLANADI")
                .font(MentorFonts.mono(9)).tracking(3).foregroundColor(MentorColors.goldDeep)
            Spacer()
            Text(f.string(from: resumeDate))
                .font(.system(size: 15, weight: .semibold, design: .monospaced))
                .tracking(2)
                .foregroundColor(MentorColors.gold)
        }
        .padding(.horizontal, 14).padding(.vertical, 10)
        .background(RoundedRectangle(cornerRadius: 6)
                        .fill(MentorColors.gold.opacity(0.05))
                        .overlay(RoundedRectangle(cornerRadius: 6).strokeBorder(MentorColors.goldDeep.opacity(0.4), lineWidth: 1)))
    }

    private var bottomBar: some View {
        let label: String = {
            if minutes < 60 { return "\(minutes) daqiqaga ruxsat ber" }
            let h = minutes / 60; let m = minutes % 60
            return m == 0 ? "\(h) soat ruxsat ber" : "\(h)s \(m) daq ruxsat ber"
        }()
        return VStack(spacing: 6) {
            MentorPrimaryButton(label) { onGrant(minutes) }
            Button(action: onCancel) {
                Text("Bekor qil — sessiyaga qaytaman")
                    .font(MentorFonts.mentor(12))
                    .foregroundColor(MentorColors.textMuted)
            }
        }
        .padding(.horizontal, 20).padding(.vertical, 16)
        .background(Color(red: 0.078, green: 0.024, blue: 0.043))
    }
}

private struct TimeDial: View {
    let minutes: Int

    private var fraction: Double { Double(min(minutes, 60)) / 60.0 }

    var body: some View {
        ZStack {
            Circle().stroke(MentorColors.gold.opacity(0.12), lineWidth: 6).frame(width: 240, height: 240)
            Circle().trim(from: 0, to: fraction)
                .stroke(MentorColors.gold, style: StrokeStyle(lineWidth: 6, lineCap: .round))
                .rotationEffect(.degrees(-90))
                .frame(width: 240, height: 240)
                .animation(.easeInOut(duration: 0.4), value: fraction)

            ForEach(0...11, id: \.self) { i in
                Rectangle()
                    .fill(i % 3 == 0 ? MentorColors.gold.opacity(0.6) : MentorColors.textGhost)
                    .frame(width: i % 3 == 0 ? 1.5 : 1, height: i % 3 == 0 ? 10 : 6)
                    .offset(y: -106)
                    .rotationEffect(.degrees(Double(i) * 30))
            }

            VStack(spacing: 2) {
                Text("\(minutes)")
                    .font(.system(size: 64, weight: .light, design: .monospaced))
                    .foregroundColor(MentorColors.textPrimary)
                Text("DAQIQA")
                    .font(MentorFonts.mono(11).weight(.semibold)).tracking(4)
                    .foregroundColor(MentorColors.gold)
            }

            Text("60").font(MentorFonts.mono(10)).foregroundColor(MentorColors.textMuted).offset(y: -100)
            Text("15").font(MentorFonts.mono(10)).foregroundColor(MentorColors.textMuted).offset(x: 100)
            Text("30").font(MentorFonts.mono(10)).foregroundColor(MentorColors.textMuted).offset(y: 100)
            Text("45").font(MentorFonts.mono(10)).foregroundColor(MentorColors.textMuted).offset(x: -100)
        }
    }
}

#Preview {
    UrgentTimeView()
}
