# M-VoW AI v2 — To'liq Texnik Spetsifikatsiya

**Hujjat versiyasi:** 2.0
**Sana:** 2026-05-10
**Holat:** Master spec — barcha qarorlar shu hujjatdan kelib chiqadi
**Avvalgisi:** v1 (`docs/PRD.md`, `docs/ARCHITECTURE.md`) — endi **bekor qilingan**

---

## 1. Vizyon

> **VOW** = qasamyod, va'da, ahd.
> Sen o'zingga *"intizomli bo'laman"* deb va'da bering. **M-VoW AI** — uni saqlaydi.

> *"You made a vow. We keep it."*

M-VoW AI — bu **kun-haydovchi (Day-Driver)**. Foydalanuvchi unga *"qachon xohlasam ishlataman"* deydigan ilova emas. **Mentor butun kunni boshqaradi** — uyg'otadi, reja tuzdiradi, bajartiradi, dam berdiradi, uxlatadi.

Bu — **operatsion tizim** darajasidagi intizom platformasi. Foydalanuvchi tomonidan **ishlatilmaydi**, balki foydalanuvchini **shakllantirib oladi**.

### 1.1 Bir gapda

> M-VoW AI sening ertalabki o'zingni yollab, kechqurungi zaifligingdan himoya qiladigan **dasturiy mentor**. Brand — VOW. Ovoz — Mentor.

### 1.2 Uchta yadro printsipi

| Printsip | Ma'nosi |
|---|---|
| **Asimmetrik kuch** | Sen nimani xohlaysan deganda — kuchsiz. Sen oldingi sen — kuchli. Mentor o'sha kuch farqini saqlaydi. |
| **Vaqt = umr** | Har bir ekran "vaqt sening umringdir" deb eslatadi. Ekran rangi, ovozi, harakati — hammasi bu falsafani yetkazadi. |
| **AI matn yozadi, tizim qaror qiladi** | LLM gapiradi (his-tuyg'u, uslub). FSM (qoidalar) qaror qiladi (ruxsat/yo'q). Aldab bo'lmaydi. |

---

## 2. Foydalanuvchi personalari

### 2.1 Asosiy persona — *"O'z hayotini qaytarib olmoqchi"*
- **Yoshi:** 18–40
- **Holati:** Aqlli, niyatli, lekin iroda yetishmaydi
- **Joriy tajriba:** Pomodoro, Forest, Cold Turkey — sinaganini, ishlamaganligini biladi
- **To'lashga tayyor:** $5-10/oy real natija uchun
- **Sinab ko'rdi:** *"O'zim intizomli bo'laman"* — 30 kunda charchaydi

### 2.2 Ikkinchi persona — *"Diniy intizom istovchi"*
- **Holat:** Namozni vaqtida o'qish, Qur'on darsi, ramazon intizomi
- **Maxsus ehtiyoj:** "Worship" kategoriyasi — eng kuchli bloklash
- **Bozor:** Markaziy Osiyo, Yaqin Sharq, Janubiy Osiyo musulmon foydalanuvchilari

### 2.3 Uchinchi persona — *"Ota-ona, bola uchun"*
- **Maqsad:** Bola telefondan oqilona foydalanishini istash
- **Cheklov:** Bola qarshilik qiladi, ota-ona qattiqqo'l bo'lolmaydi
- **Yechim:** AI ota-ona o'rniga *"yomon politsiyachi"* rolini o'ynaydi

### 2.4 To'rtinchi persona — *"Talaba/imtihonchi"*
- **Maqsad:** Imtihonga tayyorgarlik, dars davomida diqqat
- **Maxsus ehtiyoj:** Pomodoro, dars vaqti rejasi, uxlashni rejaga qo'yish

---

## 3. Asosiy funksiyalar (12 ta yadro)

### 3.1 F1 — Force Wake-Up (Majburiy uyg'otish)
**Tasvir:** Foydalanuvchi belgilagan vaqtda telefon majburan yoqiladi va to'liq ekran yoqilmaydigan alarm boshlanadi.

**Talablar:**
- Ekranni majburan yoqish (`FULL_WAKE_LOCK` + `setTurnScreenOn`)
- Volume DND (Do Not Disturb)'ga e'tibor bermaydi (alarm stream)
- Vibratsiya pattern: doimiy
- AI ovoz: "Nega turmayapsan? Bekorga yashashga rozimisan?"
- Foydalanuvchi alarm'ni faqat **3 ta signalni** birgalikda bajarib to'xtata oladi:
  1. Ekranni *"TURDIM"* tugmasi orqali ochish
  2. Math challenge yechish (masalan: 9 × 7 = ?)
  3. Telefonni **fizik harakat** qilish (accelerometer 5°+ burilish)
- Snooze: maksimum 1 marta (5 daqiqa). Keyin streak yo'qoladi.

**Acceptance criteria:**
- ✅ Belgilangan vaqtda 100% yoqiladi (test: 100 ta sample)
- ✅ Telefon o'chirilgan bo'lsa, qayta yoqilganda alarm chiqadi
- ✅ DND yoqilgan bo'lsa ham alarm eshitiladi
- ✅ 60 soniya ichida foydalanuvchi javob bermasa, intensivlik oshadi
- ❌ iOS'da to'liq ishlamaydi — yumshoqroq yechim (notification + Live Activity)

### 3.2 F2 — Morning AI Plan Dialog
**Tasvir:** Uyg'ongandan keyin AI bilan suhbat orqali bugungi kun rejasi tuziladi.

**Oqim:**
1. AI: "Bugun nima qilmoqchisan?"
2. Foydalanuvchi: matn yoki ovoz orqali vazifalar ro'yxati
3. AI har vazifa uchun aniqlashtiradi:
   - Qancha vaqt?
   - Qaysi turdagi ish? (jismoniy / aqliy / muqaddas)
   - Maxsus texnika kerakmi? (Pomodoro)
4. AI optimal jadval taklif qiladi (vaqt slot'lari + prioritet)
5. Foydalanuvchi ko'radi va tasdiqlaydi yoki tahrirlaydi
6. Tasdiqlangach — barcha vazifalar **AlarmManager**'ga yoziladi

**Acceptance criteria:**
- ✅ Maksimum 5 dialog turn'ida reja tuziladi
- ✅ Sonnet 4.6 ishlatiladi (chuqur kontekst kerak)
- ✅ Foydalanuvchi rejani drag-drop bilan o'zgartira oladi
- ✅ Hech bo'lmaganda 1 ta vazifa kiritilmasa, "Ozgina taqdiri yuk" deb ogohlantiradi

### 3.3 F3 — Always-On Persistent Timer
**Tasvir:** Vazifa davomida har joyda — har vaqtda ko'rinadigan jonli widget.

**Ikki ko'rinish:**

#### 3.3.1 Lock Screen rejimi (telefon qulflangan)
- **To'liq ekran egallaydi**
- Katta circular progress ring (mockup'dagidek)
- Big timer: `00:34:57`
- Joriy vazifa nomi (cyan pill): `QUR'ON DARSI`
- Bloklangan ilovalar ro'yxati (chizilgan ikonkalar)
- Pastda push-to-talk mic
- Quote rotation: *"Ey inson, sen vaqtsan..."* (10 soniyada bir o'zgaradi)

#### 3.3.2 Telefon ochiq rejimi (1/6 screen widget)
- Drag-drop: foydalanuvchi chetga sura oladi
- Default joy: yuqori-o'ng burchak
- Mini timer: `34:57`
- Tap → to'liq ekran (Hard Lock)
- Autohide YO'Q — har doim ko'rinadi

**Acceptance criteria:**
- ✅ Telefon o'chib yonsa ham, taymer to'g'ri davom etadi (wall-clock)
- ✅ Battery: 1 soatlik widget = 0.5% dan kam
- ✅ Performance: FPS 60 har doim
- ✅ Foydalanuvchi widget'ni o'chira olmaydi (sessiya davomida)

### 3.4 F4 — Hard Lock screen (Asosiy ekran)

Bu — **birinchi mockupingiz**. Eng muhim ekran.

```
┌────────────────────────────────────┐
│  🛡 M-VoW                          │  ← Header (brand)
├────────────────────────────────────┤
│         Hard Lock                  │  ← Status badge
│                                    │
│         ╭─────────╮                │
│        ╱           ╲               │
│       │   00:34:57  │              │  ← Circular progress
│        ╲           ╱               │     ring (cyan glow)
│         ╰─────────╯                │
│      UNTIL NEXT PLANNED BREAK      │
│                                    │
│  ╭──── QUR'ON DARSI ────╮          │  ← Task pill (cyan)
│  ╰─────────────────────╯           │
│                                    │
│  BLOCKED DISTRACTIONS              │
│  📵 INSTAGRAM                      │  ← Strikethrough
│  📵 TIKTOK                         │
│  📵 YOUTUBE                        │
│                                    │
├────────────────────────────────────┤
│         🎙                          │  ← Mic button (cyan halo)
│   HOLD TO COMMUNICATE              │
│   [CANCEL TASK]                    │
│   FOCUS, AZIZ. NO DISTRACTIONS.    │
│   STAY DISCIPLINED (TTS)           │
└────────────────────────────────────┘
```

**Interaktiv:**
- Mic'ni **bosib turish** → AI bilan ovozli muloqot ochiladi
- "CANCEL TASK" → negotiation flow ochiladi (faqat "I have a real reason" tugmasi)
- TTS rejimida AI har 5-10 daqiqada motivatsion eslatadi (lekin throttled)

### 3.5 F5 — Task Chain & Transitions
**Tasvir:** Vazifalar zanjir kabi bog'langan — har birining oxirida AI bilan o'tish dialogi.

**Oqim:**
```
Vazifa A tugashidan 5 daqiqa oldin:
  → Push notification: "5 daq qoldi"
  → Vibratsiya
  
Vazifa A tugaganda (timer = 0):
  → AI ovoz: "[Vazifa A] tugadi. Bajarding?"
  → Modal: [HA, BAJARDIM] [YO'Q, VAQT KERAK]
  
Agar HA:
  → Streak +1 (har vazifa)
  → AI: "Mukammal. Keyingi: [Vazifa B]. Tayyormisan?"
  → Vazifa B avtomatik boshlanadi
  
Agar YO'Q:
  → AI: "Necha daqiqa kerak?" [+10] [+15] [+30]
  → Vazifa cho'ziladi
  → Birinchi marta — penalty yo'q
  → Ikkinchi marta — keyin barcha vazifalar siljiydi, uxlash vaqti kechroq
  → Uchinchi marta — vazifa SKIP majburiy, streak −1
```

### 3.6 F6 — Pomodoro Mode
**Tasvir:** Aqliy ish vazifalari uchun (dars, kod yozish) avtomatik Pomodoro tartibi.

**Konfiguratsiya:**
- Default: 25 min ish + 5 min dam
- Foydalanuvchi sozlay oladi: 50/10, 90/20 (Deep Work)
- 4 sikl keyin 30 min katta tanaffus

**UI farqi (Hard Lock'dan):**
```
┌────────────────────────────────────┐
│  🛡 M-VoW — POMODORO                │
├────────────────────────────────────┤
│  ⬤ ⬤ ⬤ ○                          │  ← 4 cycle progress
│  Cycle 3 / 4                       │
│                                    │
│         ╭─────────╮                │
│        ╱           ╲               │
│       │   24 : 35   │              │  ← Bigger timer
│        ╲           ╱               │
│         ╰─────────╯                │
│      WORK PHASE                    │
│                                    │
│  Keyingi: 5 daq dam (15:30)        │
└────────────────────────────────────┘
```

5 daqiqali tanaffuslarda ekran rangi: cyan → yashil. AI taklifi: *"Suv ich. Cho'zil."*

### 3.7 F7 — Sleep Training (Uxlash tarbiyasi)

**Bosqichma-bosqich oqim:**

| Vaqt | Hodisa |
|---|---|
| 17:00 | Push: "5 soat qoldi uxlashga. Kofeinni tugat." |
| 19:00 | Display warm tones'ga o'tadi (`GammaCorrection` API) |
| 20:30 | Auto-brightness pasayadi |
| 21:30 | Push: "30 daq qoldi. Hozirdan tayyorlan." |
| 22:00 | **Hard Lock** ekran (full): "UXLASH VAQTI" |
| 22:00 | Hamma social media bloklangan |
| 22:00 | Faqat ruxsat: alarm, qo'ng'iroqlar, SMS |
| 22:15 | Foydalanuvchi telefon ochsa → AI: ilmiy fakt ko'rsatadi |
| 06:00 | Force Wake-Up (F1) bilan tugaydi |

**AI ilmiy fakt'lar bazasi:**
- Static (ilovada bundled): 50+ ta fakt manba bilan
- Dynamic (Anthropic API'dan): har xil kontekstda har xil fakt
- Manbalar: PubMed, Harvard, Stanford Sleep Lab, WHO

### 3.8 F8 — Hard Block Mechanism (mavjud, kuchaytirilgan)
- AccessibilityService + WindowManager Overlay
- Bypass-trap: Settings, Accessibility ekranlari ushlanadi
- TTS: bloklash paytida AI gapiradi (tildagi)
- **Yangi:** Aggressive penalty mode — 3 marta intercept = 1 soat penalty block

### 3.9 F9 — AI Negotiation (mavjud)
- "HOLD TO COMMUNICATE" mic'i Hard Lock'da
- FSM pre-check'lar
- Anthropic Haiku 4.5 streaming
- Tool use: `recommend_verdict`
- 6 tilda javob

### 3.10 F10 — Streak System (mavjud, kengaytirilgan)
- Kunlik snapshot (DailyStats)
- Streak buzilish sabablari:
  - Sessiya tashlanishi
  - Override granted (penalty mode)
  - Wake-up snooze ko'p marta
  - 22:00 dan keyin telefon ochish
- **Yangi:** Streak'ga qarab AI tone'i o'zgaradi (mavjud)
- **Yangi:** "Streak insurance" — 1 kunda 1 marta xatolikni "kechirish" mumkin (cooldown 30 kun)

### 3.11 F11 — Parental Control (Family Link)
**Tasvir:** Ota-ona telefonida alohida ilova, bola telefonidagi Mentor'ga ulanadi.

**Imkoniyatlar:**
- Real-time hisobot (FCM): bola TikTok ochsa, 5 soniya ichida ota-onaga push
- Kunlik dashboard: bola intizom darajasi (0-100)
- Remote actions:
  - Bolaga "+15 daq" qo'shimcha vaqt berish
  - Yangi vazifa qo'shish (masalan "Kitob o'qish, 30 min")
  - Bolaga yumshoqroq tarbiyaviy xabar yuborish
- Cheklovlar (privacy uchun):
  - ❌ Bola yozishmalarini ko'rib bo'lmaydi
  - ❌ Lokatsiya cheklangan (faqat ota-ona ruxsati bilan)
  - ❌ Mikrofondan eshitib bo'lmaydi

**Texnik:**
- Backend: Cloudflare Workers + D1 + Durable Objects (real-time)
- Bola ↔ ota-ona pairing: QR kod orqali
- E2EE: bola maxfiy ma'lumotlari ota-onaga ham shifrlangan
- Multi-child: bir ota-ona 5 ta bolagacha boshqara oladi

### 3.12 F12 — Multi-Language (mavjud, Sprint 4.5)
- 6 til: EN, UZ, RU, ES, AR, TR
- Til o'zgarganda — UI, AI, TTS, STT — hammasi yangilanadi
- Onboarding birinchi qadami
- Settings'da o'zgartirish mumkin

---

## 4. Ikkinchi darajali funksiyalar (V2.5+)

| Funksiya | Tasvir | Faza |
|---|---|---|
| F13 — Daily Review | Kechqurun AI bilan kun yakuni | V2.5 |
| F14 — Weekly Review | Yakshanba pattern recognition | V2.5 |
| F15 — Custom voice (ElevenLabs) | Premium chuqur ovoz | V2.5 |
| F16 — Calendar sync | Google/Outlook/Apple | V2.5 |
| F17 — Web Companion | Browser dashboard | V3 |
| F18 — Smart TV display | Read-only timer ko'rsatish | V3 |
| F19 — Apple Watch widget | Lock screen mini timer | V3 |
| F20 — Public accountability | Social challenges | V3.5 |

---

## 5. Tilning ahamiyati va kontekstual moslik

| Madaniyat | Maxsus ehtiyoj |
|---|---|
| Markaziy Osiyo | Namoz, Qur'on darsi, ramazon |
| Yaqin Sharq | Salat, Iftar timing |
| Lotin Amerika | Familycal commitments, late dinner |
| Janubiy Osiyo | Family obligations |
| G'arbiy Yevropa | Productivity culture, deep work |

AI system prompt'da kontekst aniqlash:
- Foydalanuvchi maqsadi: "Qur'on darsi" → AI "worship" rejimida (ehtirom bilan)
- Foydalanuvchi maqsadi: "Yoga" → AI "physical" rejimida (energiya bilan)
- Foydalanuvchi tili + maqsad turi → tone moslashadi

---

## 6. Funksional bo'lmagan talablar (NFR)

### 6.1 Ishlash darajasi (Performance)

| Metric | Target |
|---|---|
| App cold start | < 1.5s (P95) |
| Hard Lock overlay show | < 50ms |
| AI first token (Haiku 4.5) | < 400ms (P95) |
| TTS first audio | < 600ms |
| STT first partial | < 800ms |
| Database query | < 16ms (UI thread blocking yo'q) |
| Animatsiya FPS | 60 har doim |

### 6.2 Battery

| Holat | Maksimum |
|---|---|
| Idle (sessiya yo'q) | 1% / kun |
| Aktiv sessiya (1 soat) | 0.5% / soat |
| AI suhbat (5 daqiqa) | 0.3% |
| Tunlik standby | 0.2% / 8 soat |
| **Jami kunlik** | **< 7%** |

### 6.3 Ishonchlilik (Reliability)

- **Crash-free sessions:** > 99.5% (Crashlytics)
- **ANR rate:** < 0.05%
- **Sessiya recovery:** Telefon o'chsa, qayta yoqilganda 100% tiklanadi
- **AlarmManager exact:** vaqtni 30 soniya darajasida saqlash
- **Network retry:** 3 marta exponential backoff (1s, 2s, 4s)

### 6.4 Xavfsizlik

- **API key:** local.properties + production'da server proxy (RELEASE build'da APK'da yo'q)
- **HTTPS only:** barcha network calls
- **Database:** EncryptedSharedPreferences + SQLCipher (V2.5)
- **Voice recordings:** Local storage, server'ga faqat foydalanuvchi rozi bo'lsa
- **Family Link:** E2EE, ota-ona ham hech narsani ko'ra olmaydi (faqat metric'lar)
- **GDPR:** Foydalanuvchi barcha ma'lumotlarini eksport / o'chirish huquqiga ega

### 6.5 Foydalanuvchanlik (Usability)

- **Onboarding:** < 5 daqiqa (10 ekran, har biri 30 soniyada)
- **Daily plan:** < 3 daqiqa AI bilan
- **Hard Lock interaktsiya:** 1 tap masofa (mic, cancel)
- **Accessibility:** TalkBack qo'llab-quvvatlash, dynamic font size
- **Offline:** Asosiy funksiyalar internet'siz ishlaydi (faqat AI suhbatlar talab qiladi)

### 6.6 Maqsadli platformalar

| Platform | Min versiya | Asosiy funksionallik |
|---|---|---|
| Android | 10 (API 29) | To'liq |
| iOS | 16.0 | 70% — force wake-up cheklangan |
| iPad | iPadOS 16.0 | 70% |
| Android TV | API 30 | Read-only display |
| Web (browser) | Chrome 100+ | Dashboard only |
| Apple Watch | watchOS 9 | Mini widget |

---

## 7. Texnologiyalar to'plami (Stack)

### 7.1 Mobile
- **Android:** Kotlin 2.1, Jetpack Compose, Hilt, Room, WorkManager
- **iOS (V2.5):** Swift 5.9, SwiftUI, FamilyControls, ActivityKit

### 7.2 Cross-platform
- **Rust core (V3):** UniFFI/JNI orqali — qoidalar dvigateli, anti-tamper
- **Compose Multiplatform Web (V3):** browser companion uchun

### 7.3 Backend
- **Hosting:** Cloudflare Workers
- **Database:** D1 (SQLite) + Durable Objects (real-time)
- **Auth:** Custom JWT + Apple Sign-In + Google Sign-In
- **AI:** Anthropic Claude API (Haiku 4.5 streaming, Sonnet 4.6 planning)
- **Voice:** ElevenLabs (Premium tier)
- **Notifications:** Firebase Cloud Messaging (Android), APNs (iOS)
- **Analytics:** PostHog (open source, self-hosted)
- **Crashes:** Firebase Crashlytics
- **Feature flags:** Firebase Remote Config

### 7.4 DevOps
- **CI/CD:** GitHub Actions
- **Testing:** JUnit5, MockK, Turbine, Maestro (E2E)
- **Code quality:** Detekt, ktlint, SwiftLint
- **Monitoring:** Grafana + Prometheus

---

## 8. Bozor pozitsiyasi

### 8.1 Raqobatchi tahlili

| Ilova | Kuchli tomon | Zaif tomon | Bizning afzalligimiz |
|---|---|---|---|
| **Forest** | Chiroyli UX | Yumshoq | Qattiq blok + AI |
| **Opal** | iOS yaxshi | Faqat iOS | Cross-platform + AI |
| **Cold Turkey** | Desktop kuchli | Mobile yo'q | Mobile-first |
| **Freedom** | Multi-device | Statik | Dinamik AI mentor |
| **Jomo** | Gamification | Zaif blok | Stoic tone |
| **Bog'liqlik** | Hech qaysi | — | **Kun-haydovchi** model |

### 8.2 Kategoriya farqi

Boshqa ilovalar — **tool**'lar (sen ishlatasan).
M-VoW AI — **system** (sen yashashingni boshqaradi).

Bu darajada hech kim qurmagan.

### 8.3 Monetizatsiya

| Tier | Narx | Imkoniyatlar |
|---|---|---|
| **Free** | $0 | 1 sessiya/kun, 5 ilova bloklash, asosiy AI |
| **Pro** | $4.99/oy yoki $39/yil | Cheksiz, ovozli AI, Pomodoro, Sleep training |
| **Family** | $9.99/oy | 5 ta family member, parental control, oilaviy dashboard |
| **Lifetime** | $129 | Hammasi cheksiz, kelajakdagi yangiliklar |

Bozor target:
- Yil 1: 100k registered, 10k paid (10% conversion)
- Yil 2: 1M registered, 80k paid

---

## 9. Muvaffaqiyat metrikalari

### 9.1 Mahsulot metrikalari

| Metric | Target (3 oy) | Target (1 yil) |
|---|---|---|
| D1 retention | > 60% | > 70% |
| D7 retention | > 35% | > 50% |
| D30 retention | > 20% | > 35% |
| Sessions/day | > 2 | > 3 |
| Wake-up dismissed first try | > 80% | > 90% |
| Streak ≥ 7 kun | > 30% users | > 50% users |
| NPS | > 40 | > 60 |

### 9.2 Texnik metrikalari

| Metric | Target |
|---|---|
| Crash-free | > 99.5% |
| ANR | < 0.05% |
| Cold start | < 1.5s |
| API latency P95 | < 500ms |

---

## 10. Risklar va yumshatish

| Risk | Ehtimol | Ta'sir | Yumshatish |
|---|---|---|---|
| Apple Family Controls rad etiladi | O'rta | Yuqori | Erta apply (Sprint 1), parental control sifatida pozitsiyalash |
| Play Store Accessibility policy | O'rta | Yuqori | Justification video, manual review tayyor |
| Foydalanuvchilar qo'rqib o'chiradi | Yuqori | O'rta | Onboarding A/B test, Soft mode option |
| Force wake-up battery zarari | O'rta | O'rta | Doze-friendly arxitektura |
| AI o'zbek/turk tillarida sifatsiz | O'rta | Yuqori | Fallback ru/en, native speaker testlash |
| Server downtime → AI ishlamaydi | Past | O'rta | Client-side fallback FSM |
| Bypass topiladi | Yuqori | O'rta | Quartal red-team review |
| iOS limit'lari user'ni xafa qiladi | Yuqori | O'rta | Aniq messaging, Android-first marketing |
| Streak buzilishi → user uninstalled | O'rta | O'rta | Streak insurance (1 free recovery/oy) |

---

## 11. Hujjat strukturasi

Bu spec'ning qo'shimcha qismlari:

- [DESIGN.md](DESIGN.md) — Design system, Compose specs, ekran chizmalari
- [DOMAIN.md](DOMAIN.md) — Data models, Room sxemalari, state machines
- [PLATFORMS.md](PLATFORMS.md) — Android/iOS/TV/Web platform-spetsifik
- [ROADMAP.md](ROADMAP.md) — 5 oylik bosqichma-bosqich plan

---

## 12. Tasdiqlash

Bu spec'ni amalga oshirish uchun **tasdiqlash kerak:**

- [ ] Vizyon (Section 1) — to'g'ri
- [ ] Personalar (Section 2) — to'g'ri
- [ ] 12 ta yadro funksiya (Section 3) — to'g'ri
- [ ] NFR'lar (Section 6) — qabul qilinadigan
- [ ] Stack (Section 7) — Native + Anthropic + Cloudflare
- [ ] Monetizatsiya (Section 8) — qabul qilinadigan
- [ ] Risk plan (Section 10) — qabul qilinadigan

Tasdiqlangach, [ROADMAP.md](ROADMAP.md) bo'yicha 5 oylik build boshlanadi.
