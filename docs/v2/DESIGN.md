# M-VoW AI v2 — Design System & Screen Specs

**Hujjat versiyasi:** 2.0
**Sana:** 2026-05-10
**Asos:** User mockup (Hard Lock screen) — *deep navy + cyan glow + circular ring*

---

## 0. Brand Identity

### 0.1 Nom

**M-VoW** — *Mentor* + *Vow* (qasamyod / va'da / ahd).

Stylization (qattiq qoida):
```
M - V o W
```
- "M" — kapital, Mentor merosi
- "VoW" — V kapital, o kichkina, W kapital (assertive accent)
- Brand'da har doim shunday yozilishi kerak: **M-VoW AI**

To'liq nomi: **M-VoW AI**.

### 0.2 Tagline

| Til | Tagline |
|---|---|
| English | "You made a vow. We keep it." |
| O'zbek | "Sen va'da berding. Biz uni saqlaymiz." |
| Русский | "Ты дал клятву. Мы её храним." |
| Español | "Tú hiciste un voto. Nosotros lo cumplimos." |
| العربية | "أنت قطعت عهداً. نحن نحفظه." |
| Türkçe | "Sen söz verdin. Biz tutuyoruz." |

### 0.3 Logo concept

```
   ╱╲
  ╱  ╲
 │  V │   ← qalqon shakli ichida "V" harfi
  ╲  ╱       (intizom = himoya falsafasi)
   ╲╱
```

- Qalqon (shield): himoyalovchi
- "V" harfi: vow (qasamyod) + ko'tarilgan ovoz
- Glow halo: cyan ({accent.glow})

### 0.4 Brand vs AI character

| Qatlam | Nom |
|---|---|
| Brand / app nomi | **VOW** |
| Mahsulot to'liq nomi | **M-VoW AI** |
| AI personasi (ovoz) | **Mentor** |

Foydalanuvchi *"VOW menga gapiryapti"* demaydi. **"Mentor menga gapiryapti"** deydi.
VOW — bu *tizim*. Mentor — bu *ovoz*.

### 0.5 Voice & tone

| Element | Tavsif |
|---|---|
| Brand | Qattiq, asketik, "monastery + military" |
| Mentor character | Stoic, vaznli, hech qachon kechirim so'ramaydi |
| Marketing copy | Aniq, qisqa, ortiqcha so'zsiz |
| Iltimos so'zlari | Yo'q ("please" so'zi ishlatilmaydi) |
| Undov belgilari | Yo'q (qasddan, energiya tarqatmaslik uchun) |

---

## 1. Design Philosophy

### 1.1 Asosiy hissi
> **"Vault meets cathedral."**
> Banking-grade qattiqlik + ma'naviy ahamiyat.

Ekran qiziq emas. Ekran **ahamiyatli**. Foydalanuvchi har quchoqlanganda *"bu mening vaqtim"* deb his qiladi — tezkor scroll'lash uchun emas.

### 1.2 Ko'rsatuvchi printsiplar

| Printsip | Amaliyotda |
|---|---|
| **Less is heavier** | Bo'sh joy ko'p. Element kam. Har bir element vazn bilan. |
| **Glow as importance** | Cyan halo faqat eng muhim element atrofida (timer, mic) |
| **Time as hero** | Taymer hech qachon ikkinchi darajali emas — har ekranda eng katta element |
| **Cold geometry** | Aylanalar, to'g'ri burchaklar. Yumaloqlik max 8dp. Bouncing yo'q. |
| **No dopamine cues** | Pulsator yo'q, gradient yo'q, success particle'lar yo'q |

---

## 2. Color System

### 2.1 Asosiy palette

```
PRIMARY SURFACES
─────────────────────────────────────
surface.void        #0A0E14   (asosiy fon — qattiq qora-ko'k)
surface.shadow      #11151D   (yuqori darajadagi panel)
surface.steel       #1A1F2A   (kartochkalar)
surface.iron        #232936   (interactive ham)

ACCENT (mockupdan)
─────────────────────────────────────
accent.glow         #00E5D4   (asosiy cyan — timer, mic, faol element)
accent.glow.dim     #007D75   (passiv cyan — strikethrough, label)
accent.brass        #C7A36B   (negotiation, warn — kam ishlatiladi)

TEXT
─────────────────────────────────────
text.primary        #F5F2EC   (asosiy)
text.body           #B8BBC2   (oddiy)
text.muted          #6B6E76   (label, metadata)
text.ghost          #3A3D44   (disabled)

SIGNALS
─────────────────────────────────────
signal.success      #4A8A5C   (streak, tugatildi — kam)
signal.warn         #C7A36B   (negotiation)
signal.fail         #8C2A2A   (streak yo'qoldi, force-stop)
signal.alarm        #E63946   (force wake-up paytida)
```

### 2.2 Gradient'lar (cheklangan)

Faqat **2 ta joyda** ruxsat etilgan:

#### Glow ring (timer atrofida)
```
Inner: accent.glow (full opacity)
Outer: accent.glow @ 20% opacity, blur 24dp
```

#### Cyan pill (task name)
```
Background: linear gradient
  start: #00E5D4 @ 100%
  end:   #00B5A8 @ 100%
Direction: 135deg
```

### 2.3 Foydalanish qoidalari

| Yo'q ⛔ | Ha ✅ |
|---|---|
| Rang-barang gradient'lar | Faqat 2 ta cyan gradient |
| Bouncing animatsiyalar | Linear easing |
| Confetti / particle | Hech qachon |
| Yorqin yashil "tugatildi" | Ko'milgan sariq #4A8A5C |
| Light theme | Faqat dark, qasddan |
| Heart, star, fire emoji | Hech qachon UI'da emoji yo'q |

---

## 3. Typography

### 3.1 Font stack

| Element | Font | Weight |
|---|---|---|
| **Display (timer digits)** | `JetBrains Mono` yoki `IBM Plex Mono` | 500 |
| **Headers** | `Söhne Breit` yoki `Inter Display` | 700 |
| **Body** | `Inter` | 400 |
| **Labels (UPPERCASE)** | `Inter` | 600 (letter-spacing 2sp) |

Fallback: Android default (Roboto) + tabular numerals.

### 3.2 Type scale

| Token | Size | Use |
|---|---|---|
| `display.timer` | 64sp / mono / tabular | Hard Lock big timer |
| `display.title` | 48sp / 700 | Welcome, Sleep mode |
| `headline` | 28sp / 700 | Screen title |
| `title` | 20sp / 600 | Card title |
| `body.lg` | 18sp / 400 | Goal text, mentor message |
| `body` | 16sp / 400 | Default |
| `caption` | 13sp / 400 | Metadata |
| `label` | 11sp / 600 / tracking 2sp | "BLOCKED DISTRACTIONS" |

### 3.3 Multilingual considerations

- Arabic, Hebrew (RTL): mirror layouts. Font: `IBM Plex Sans Arabic`
- CJK (kelajakda): `IBM Plex Sans JP/KR/SC`
- Cyrillic: Inter qo'llab-quvvatlanadi

---

## 4. Spacing & Sizing

### 4.1 Grid

```
xs    — 4dp    (icon padding)
sm    — 8dp    (compact)
md    — 16dp   (default)
lg    — 24dp   (screen edge)
xl    — 32dp   (section break)
xxl   — 48dp   (header bottom)
xxxl  — 64dp   (hero element top)
```

### 4.2 Touch targets

- Min: 48dp × 48dp
- Mic button (Hard Lock): 80dp × 80dp
- Timer center: 240dp circular ring

### 4.3 Radii

```
none    — 0dp     (qat'iy elementlar)
sm      — 4dp     (default — kartochkalar, tugmalar)
md      — 8dp     (FAB)
pill    — 999dp   (task pill)
```

**Yumaloqlik max 8dp.** Aylana — pill yoki to'liq dumaloq.

---

## 5. Iconography

### 5.1 Style
- **Outline only** (filled emas)
- Stroke: 1.5dp
- Source: Material Symbols (Outlined) yoki custom

### 5.2 Custom icons (kerak bo'ladi)
- `mentor_shield` — header logo (mockupdagidek)
- `hard_lock` — Hard Lock badge
- `pomodoro_cycle` — 4 ta dot
- `sleep_moon` — uxlash rejimi

---

## 6. Motion

### 6.1 Asosiy easing

```
standard:    cubic-bezier(0.4, 0.0, 0.2, 1)
deliberate:  cubic-bezier(0.7, 0.0, 0.3, 1)  ← biz ko'p ishlatamiz
sharp:       cubic-bezier(0.4, 0.0, 0.6, 1)
```

### 6.2 Davomiyligi

| Hodisa | Davomiyligi |
|---|---|
| Tugma bosish feedback | 100ms |
| Sahifa o'tishi | 320ms (deliberate) |
| Modal ochilish | 280ms |
| Negotiation transitions | 600ms (qasddan sekin) |
| Wake-up alarm pulse | 1500ms |

### 6.3 Ta'qiqlangan animatsiyalar

- Bouncing / spring overshoot
- Particle effects
- Confetti
- Background motion / parallax
- Lottie complex animations

### 6.4 Talab qilinadigan animatsiyalar

- Timer ring **silliq** kamayadi (1s tick yo'q, smooth)
- Task transition: cross-fade + slide 16dp
- Wake-up alarm: pulse glow 0.85 → 1.0 → 0.85

---

## 7. Sound Design

### 7.1 Mavjud ovoz signal'lari

| Hodisa | Tovush |
|---|---|
| Sessiya boshi | C2 piano (low), 1.2s, reverb tail |
| Bloklash | 40Hz subaudible rumble + AI TTS |
| Pomodoro tanaffus | E4 chime, 800ms |
| Sessiya tugadi | Tibetan bowl sustain (3s) |
| Force wake-up | Ascending tone + AI voice |
| Sleep dialog | Low ambient drone |

### 7.2 Volume strategiyasi

- Lock screen: USE_FULL_SCREEN_INTENT, alarm volume
- Foreground: media volume
- DND respect: faqat alarm va wake-up bypass qiladi

---

## 8. Ekran spetsifikatsiyalari (Sahifa-sahifa)

### 8.1 Hard Lock screen (mockupdan)

#### Layout
```
┌────────────────────────────────────────┐
│ ← (back yo'q)     🛡 M-VoW              │  64dp top
├────────────────────────────────────────┤
│                                        │
│           [Hard Lock badge]            │  16dp gap
│                                        │  24dp section
│           ╭─────────────╮              │
│          ╱   ╭──────╮    ╲             │
│         │   │ 00:34:57│   │            │  240dp ring
│          ╲   ╰──────╯    ╱             │
│           ╰─────────────╯              │
│      UNTIL NEXT PLANNED BREAK          │  caption
│                                        │  32dp gap
│       ╭── QUR'ON DARSI ──╮             │  pill
│       ╰──────────────────╯             │
│                                        │  32dp gap
│       BLOCKED DISTRACTIONS             │  label
│       ─ INSTAGRAM    ─                 │  strikethrough
│       ─ TIKTOK       ─                 │
│       ─ YOUTUBE      ─                 │
│                                        │
│                                        │  flex grow
├────────────────────────────────────────┤
│              ●                         │  mic 80dp
│              🎙                         │  cyan glow
│       HOLD TO COMMUNICATE              │  caption
│       [   CANCEL TASK   ]              │  ghost btn
│   FOCUS, AZIZ. NO DISTRACTIONS.        │  body sm
│   STAY DISCIPLINED (TTS)               │  caption muted
└────────────────────────────────────────┘
   144dp safe area bottom
```

#### Component spec

**Header**
- Height: 64dp
- Background: surface.void
- Logo: mvow_shield 24dp + "M-VoW" / Söhne Breit / 16sp / tracking 4sp
- No back button (qulflangan)

**Hard Lock badge**
- Pill shape
- BG: surface.iron
- Border: 1dp accent.glow.dim
- Text: "Hard Lock" / 12sp / tracking 2sp / accent.glow

**Timer ring (eng muhim)**
- Outer diameter: 240dp
- Stroke width: 4dp
- Background ring: surface.iron
- Progress arc: accent.glow with halo glow (24dp blur, 30% opacity)
- Animation: smooth countdown, no stepping
- Center text: timer digits / mono / 56sp / tabular

**Task pill (cyan)**
- Min width: content + 32dp horizontal padding
- Height: 48dp
- BG: linear gradient (cyan)
- Text color: surface.void
- Font: 16sp / 700 / tracking 2sp / UPPERCASE

**Blocked distractions list**
- Each item: 24dp height
- Icon: 16dp + name + line-through decoration
- Color: text.muted with strikethrough
- Spacing: 8dp between items

**Mic button**
- Size: 80dp circle
- BG: accent.glow
- Glow halo: 32dp radius, accent.glow @ 40%
- Icon: mic 32dp / surface.void color
- Hold-to-talk: visual feedback (ring expands to 100dp)
- Tap (no hold): "Hold the button to speak" tooltip

**Footer area**
- HOLD TO COMMUNICATE — caption
- CANCEL TASK — outlined ghost button (height 40dp)
- "FOCUS, [name]. NO DISTRACTIONS." — body sm, italic
- "STAY DISCIPLINED (TTS)" — caption muted

---

### 8.2 Force Wake-Up screen

#### Layout
```
┌────────────────────────────────────────┐
│                                        │
│                                        │
│           06 : 00                      │  display.title (96sp)
│        Sun, May 11                     │  body
│                                        │
│                                        │
│                                        │  flex
│      "Nega turmayapsan?                │  body.lg italic
│       Bekorga yashashga                │  appears letter-by-letter
│       rozimisan?"                      │
│                                        │
│                                        │
│                                        │  flex
│      ┌─────────────────────┐           │
│      │  9 × 7 = ?          │           │  challenge
│      │  ┌─────────────┐    │           │
│      │  │   [_____]   │    │           │  numeric input
│      │  └─────────────┘    │           │
│      └─────────────────────┘           │
│                                        │
│                                        │  flex
├────────────────────────────────────────┤
│         [   I'M AWAKE   ]              │  primary, disabled
│                                        │  until challenge solved
└────────────────────────────────────────┘
```

**Animatsiya:**
- Background: pulsing gradient (signal.alarm 5% → 15% → 5%)
- Time digits: pulse with alarm beat
- Quote text: typewriter effect (50ms/char)

**Behavior:**
- Vibration: continuous waveform pattern
- Alarm sound: rising tone + AI voice loop
- Math challenge: random — tongue twister'lar (uyqudan tushgan miya yecha olmaydi)
- Accelerometer monitoring: 5°+ tilt → boshqa signal sifatida
- "I'M AWAKE" tugmasi yoqilishi:
  - Math correct ✅
  - Tilt detected (last 30s) ✅

---

### 8.3 Morning Plan Dialog

#### Layout (chat-style)
```
┌────────────────────────────────────────┐
│  ← BUGUN — 2026-05-11                  │  header
├────────────────────────────────────────┤
│                                        │
│  ╭─────────────────────────╮           │
│  │ Assalomu alaykum.       │           │  AI bubble
│  │ Soat 06:10.             │           │  (left, surface.steel)
│  │ Bugun nima qilmoqchisan?│           │
│  ╰─────────────────────────╯           │
│                                        │
│           ╭─────────────────────────╮  │
│           │ Uy uborka, ovqat,       │  user bubble
│           │ matematika, sport       │  (right, accent.glow)
│           ╰─────────────────────────╯  │
│                                        │
│  ╭─────────────────────────╮           │
│  │ 4 ta narsa. Aniqlik     │           │
│  │ kerak. Uborka — qancha? │           │
│  ╰─────────────────────────╯           │
│                                        │
│  [streaming token-by-token...]         │
│                                        │
├────────────────────────────────────────┤
│  ┌──────────────────────┐  🎙          │
│  │ Yozish yoki gapirish │  mic         │
│  └──────────────────────┘              │
└────────────────────────────────────────┘
```

#### Plan confirmation view
```
┌────────────────────────────────────────┐
│  PLAN TASDIQLAYMIZMI?                  │  headline
├────────────────────────────────────────┤
│                                        │
│  06:30 — 07:00  ☀️ Nonushta            │
│                  30 min                │
│  ─────                                 │
│  07:00 — 09:00  🏃 Yugurish            │
│                  2 soat                │
│  ─────                                 │
│  09:00 — 11:00  🧹 Uy uborka           │
│                  2 soat                │
│  ─────                                 │
│  11:00 — 12:30  🍳 Osh                 │
│                  1.5 soat              │
│  ─────                                 │
│  13:30 — 15:30  📚 Matematika         │
│                  Pomodoro 25/5         │
│                  4 sikl                │
│  ─────                                 │
│  22:00          🌙 Uxlash              │
│                                        │
├────────────────────────────────────────┤
│  [ TAHRIRLASH ]  [ TASDIQLAYMAN ]      │
└────────────────────────────────────────┘
```

---

### 8.4 Persistent Edge Widget (telefon ochiq)

#### Bo'lak (top-right corner)
```
                     ┌─────────────┐
                     │ ⏱ 34:57     │  120dp wide
                     │ Qur'on darsi │  56dp tall
                     └─────────────┘
```

**Behavior:**
- Drag-drop: foydalanuvchi 4 ta burchakka sura oladi
- Position saqlanadi DataStore'da
- Tap → Hard Lock screen ochiladi
- Long-press → menu: "Show full" / "Mute TTS"
- Color: surface.shadow with cyan border 1dp

**Throttled:**
- Update har 1 soniyada (mins:secs)
- Background paint cycle = 1Hz only (battery)

---

### 8.5 Pomodoro mode

```
┌────────────────────────────────────────┐
│  🛡 M-VoW — POMODORO                    │
├────────────────────────────────────────┤
│                                        │
│         ⬤ ⬤ ⬤ ○                       │
│         CYCLE 3 / 4                    │
│                                        │
│           ╭─────────────╮              │
│          ╱   ╭──────╮    ╲             │
│         │   │ 24:35 │    │             │
│          ╲   ╰──────╯    ╱             │
│           ╰─────────────╯              │
│         WORK PHASE                     │
│                                        │
│       ╭── MATEMATIKA ──╮               │
│       ╰────────────────╯               │
│                                        │
│       Keyingi: 5 daq dam (15:30)       │
│                                        │
├────────────────────────────────────────┤
│              🎙                         │
└────────────────────────────────────────┘
```

**Phase indicators:**
- Work: cyan ring
- Break: green ring (signal.success)
- Long break (4-cycle): warm yellow

---

### 8.6 Sleep mode (22:00 dan keyin)

```
┌────────────────────────────────────────┐
│                                        │
│            🌙                           │
│                                        │
│       UXLASH VAQTI                     │  display.title
│                                        │
│       22:15                            │  body, dim
│                                        │
│                                        │
│  Foydalanuvchi telefon ochsa:          │
│                                        │
│  "Harvard School of Public Health      │
│   2024 tadqiqotiga ko'ra:              │
│                                        │
│   6 soatdan kam uxlash → 12% qisqa     │
│   umr xavfini oshiradi."               │
│                                        │
│                                        │
│  [ ANGLAYAPMAN, UXLAYMAN ]             │
│  [ qor qor uxlay olmayman ]            │
└────────────────────────────────────────┘
```

**Tone:**
- BG: surface.void with subtle warm tint (5% red)
- Text: text.body
- No glow, no animation — qat'iy

---

### 8.7 Family Dashboard (ota-ona ilovasi)

```
┌────────────────────────────────────────┐
│  🛡 MENTOR FAMILY                       │
├────────────────────────────────────────┤
│                                        │
│  BUGUN — 2026-05-11                    │
│                                        │
│  ╭─────────────────────────╮           │
│  │ AZIZ, 12                │           │  child card
│  │ Discipline: 87 / 100    │           │
│  │ ─────                   │           │
│  │ ✅ 06:00 Wake (1st try)│           │
│  │ ✅ Maktab               │           │
│  │ ⚠ Dars: 2/4 cycle done  │           │
│  │ ❌ TikTok: 4 urinish    │           │
│  ╰─────────────────────────╯           │
│                                        │
│  [ +15 daq berish ]                    │
│  [ Vazifa qo'shish ]                   │
│  [ Suhbat ]                            │
│                                        │
└────────────────────────────────────────┘
```

---

## 9. Component Library

### 9.1 Buttons

```kotlin
// Primary — eng muhim harakat
@Composable
fun MentorPrimaryButton(...)
// BG: accent.glow, text: surface.void, glow halo

// Secondary
@Composable
fun MentorSecondaryButton(...)
// Border: text.muted, transparent BG

// Ghost (intentionally weak)
@Composable
fun MentorGhostButton(...)
// Text only, lowercase, text.muted

// Mic (Hard Lock)
@Composable
fun MentorMicButton(...)
// Circular, 80dp, accent.glow, hold-to-talk
```

### 9.2 Timer

```kotlin
@Composable
fun CircularTimer(
    remainingMs: Long,
    totalMs: Long,
    glowColor: Color = MentorColors.AccentGlow
)
// Mockup'dagidek — 240dp ring + 56sp digits
```

### 9.3 Task Pill

```kotlin
@Composable
fun TaskPill(
    title: String,
    color: PillColor = PillColor.CYAN
)
// Cyan gradient, uppercase, 700 weight
```

### 9.4 Card surfaces

```kotlin
@Composable
fun MentorCard(...)
// surface.steel, 4dp radius, 16dp padding

@Composable
fun MentorElevatedCard(...)
// surface.iron, 4dp radius, 20dp padding
```

### 9.5 Input

```kotlin
@Composable
fun MentorTextField(...)
// Outline, 4dp radius, accent.glow on focus
```

---

## 10. Accessibility

### 10.1 TalkBack support
- Har timer: "X minutes remaining"
- Har button: aniq label
- Har screen: structured headings

### 10.2 Dynamic font size
- 100% — 200% scale
- Layout adapts (column'lar, scrolling)

### 10.3 Color contrast
- Text on BG: AAA (7:1)
- Cyan on void: AAA (8.2:1) ✅

### 10.4 Reduce motion
- System preference respect qilinadi
- Pulsator → static glow
- Smooth countdown → second tick

---

## 11. Variant'lar (kelajak)

### 11.1 Light mode (V3+)
**Hozir yo'q.** Dark Discipline — qasddan. Light mode v3'da, lekin kuchsizroq qilingan (cyan o'rniga deeper teal).

### 11.2 Brand variants
- Mentor Pro (oltinli aksent — premium tier)
- Mentor Family (yashil aksent — oilaviy)

### 11.3 Theme customization (V3)
- Foydalanuvchi 5 ta predefined palette tanlay oladi
- Lekin tashqi mexanizmga ta'sir qilmaydi (faqat ranglar)

---

## 12. Tasdiqlash

Dizayn:
- [ ] Color palette tasdiqlangan
- [ ] Hard Lock screen mockup'ga mos
- [ ] Tipografiya tanlovlari
- [ ] Motion principles
- [ ] Komponent ro'yxati to'liq

Tasdiqlangach, Compose'da implementation boshlanadi.
