import SwiftUI

/// "Kalendar" — weekly schedule. Mirrors `calendar.html`.
/// Shows the week's planned sessions; gold = pending, green = done, faded = missed.
struct CalendarView: View {
    enum Period: String, CaseIterable { case day = "KUN", week = "HAFTA", month = "OY" }
    enum Status { case pending, done, missed }

    @State private var period: Period = .week

    private let days = ["DU", "SE", "CH", "PA", "JU", "SH", "YA"]
    private let dates = [4, 5, 6, 7, 8, 9, 10]
    private let today = 6  // index → YA(10) is today in the PWA; keep simple

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
                    Text("May 2026")
                        .font(MentorFonts.mentorBold(20))
                        .foregroundColor(MentorColors.textPrimary)
                    Spacer()
                    Button(action: {}) {
                        Image(systemName: "plus").foregroundColor(MentorColors.gold)
                    }
                }
                .padding(.horizontal, 24)

                Spacer().frame(height: 16)

                // Period tabs
                HStack(spacing: 0) {
                    ForEach(Period.allCases, id: \.self) { p in
                        Button { withAnimation(.easeInOut(duration: 0.2)) { period = p } } label: {
                            Text(p.rawValue)
                                .font(MentorFonts.mono(10))
                                .tracking(2)
                                .foregroundColor(period == p ? MentorColors.surfaceVoid : MentorColors.textMuted)
                                .frame(maxWidth: .infinity)
                                .padding(.vertical, 9)
                                .background(period == p ? AnyView(goldFill) : AnyView(Color.clear))
                        }
                        .buttonStyle(.plain)
                    }
                }
                .background(MentorColors.gold.opacity(0.05))
                .overlay(Capsule().strokeBorder(MentorColors.goldDeep.opacity(0.5), lineWidth: 1))
                .clipShape(Capsule())
                .padding(.horizontal, 24)

                Spacer().frame(height: 18)

                // Day strip
                HStack(spacing: 4) {
                    ForEach(0..<days.count, id: \.self) { i in
                        VStack(spacing: 4) {
                            Text(days[i])
                                .font(MentorFonts.mono(8))
                                .foregroundColor(MentorColors.textMuted)
                            Text("\(dates[i])")
                                .font(MentorFonts.mono(13))
                                .foregroundColor(i == today ? MentorColors.surfaceVoid : MentorColors.textBody)
                                .frame(width: 28, height: 28)
                                .background(i == today ? AnyView(Circle().fill(MentorColors.gold)) : AnyView(Color.clear))
                        }
                        .frame(maxWidth: .infinity)
                    }
                }
                .padding(.horizontal, 20)

                Spacer().frame(height: 14)

                // Sessions list
                ScrollView {
                    VStack(spacing: 8) {
                        SessionBar(time: "04:00", name: "Qur'on darsi", dur: "2 soat", status: .done)
                        SessionBar(time: "06:20", name: "Arab grammatikasi", dur: "1 soat", status: .pending)
                        SessionBar(time: "10:00", name: "SMM darslari", dur: "2 soat", status: .pending)
                        SessionBar(time: "13:00", name: "Loyiha · kod", dur: "1.5 soat", status: .pending)
                        SessionBar(time: "15:00", name: "Kitob · mutolaa", dur: "45 daq", status: .pending)
                        SessionBar(time: "—", name: "Kechagi sport", dur: "45 daq", status: .missed)
                    }
                    .padding(.horizontal, 24)
                    .padding(.vertical, 4)
                }

                // Legend
                HStack(spacing: 14) {
                    LegendDot(color: MentorColors.gold, label: "KUTILMOQDA")
                    LegendDot(color: MentorColors.emerald, label: "BAJARILDI")
                    LegendDot(color: MentorColors.textGhost, label: "BAJARILMAGAN")
                }
                .padding(.vertical, 12)
            }
        }
        .preferredColorScheme(.dark)
    }

    private var goldFill: some View {
        LinearGradient(colors: [MentorColors.goldFlash, MentorColors.gold],
                       startPoint: .top, endPoint: .bottom)
    }
}

private struct SessionBar: View {
    let time: String
    let name: String
    let dur: String
    let status: CalendarView.Status

    private var color: Color {
        switch status {
        case .pending: return MentorColors.gold
        case .done:    return MentorColors.emerald
        case .missed:  return MentorColors.textGhost
        }
    }

    var body: some View {
        HStack(spacing: 12) {
            Text(time)
                .font(MentorFonts.mono(11))
                .foregroundColor(color)
                .frame(width: 44, alignment: .leading)
            VStack(alignment: .leading, spacing: 2) {
                Text(name)
                    .font(MentorFonts.mentorBold(14))
                    .foregroundColor(status == .missed ? MentorColors.textMuted : MentorColors.textPrimary)
                    .strikethrough(status == .missed)
                Text(dur)
                    .font(MentorFonts.mono(8))
                    .tracking(1)
                    .foregroundColor(MentorColors.textMuted)
            }
            Spacer()
            if status == .done {
                Image(systemName: "checkmark").font(.system(size: 12)).foregroundColor(MentorColors.emerald)
            }
        }
        .padding(.horizontal, 12)
        .padding(.vertical, 10)
        .background(color.opacity(status == .missed ? 0.02 : 0.05))
        .overlay(
            RoundedRectangle(cornerRadius: 6)
                .strokeBorder(color.opacity(status == .missed ? 0.25 : 0.4),
                              style: StrokeStyle(lineWidth: 1, dash: status == .missed ? [3, 3] : []))
        )
        .cornerRadius(6)
    }
}

private struct LegendDot: View {
    let color: Color
    let label: String
    var body: some View {
        HStack(spacing: 5) {
            Circle().fill(color).frame(width: 7, height: 7)
            Text(label)
                .font(MentorFonts.mono(7.5))
                .tracking(1)
                .foregroundColor(MentorColors.textMuted)
        }
    }
}

#Preview {
    CalendarView()
}
