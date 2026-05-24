# M-VoW AI v2 — 5 oylik Roadmap

**Hujjat versiyasi:** 2.0
**Sana:** 2026-05-10
**Maqsad:** Production-ready Android Beta — 22 hafta ichida
**Asos:** [SPEC.md](SPEC.md), [DESIGN.md](DESIGN.md), [DOMAIN.md](DOMAIN.md), [PLATFORMS.md](PLATFORMS.md)

---

## Umumiy timeline

```
Oy 1            Oy 2            Oy 3            Oy 4            Oy 5
├── Foundation ├── Core Loop ── ├── AI + Voice ─├── Polish ──── ├── Beta
│                                                                │
Hafta 1   4   8                  12              16              20  22
```

---

## Faza 1 — Foundation (Hafta 1-4)

### Sprint 1.1 (Hafta 1) — Re-architecture & Setup

**Maqsad:** v1 → v2 migration foundation.

**Tasklar:**
- [ ] Brand rename: `uz.mentorai.focus` → `app.vowai`
- [ ] Logo va brand assets (VOW shield)
- [ ] DB v3 → v4 migration (yangi entity'lar)
- [ ] `User`, `Vow`, `DayPlan`, `Task` entity'lari + DAOs
- [ ] `DayDriverFsm` skeleton + tests
- [ ] Hilt module reorganization
- [ ] Compose Multiplatform shared logic stub (V3'ga tayyorlik)

**Deliverables:**
- ✅ Yangi nom bilan APK build
- ✅ Yangi DB ishlaydi
- ✅ DayDriverFsm 100% test coverage

**Risk:** DB migration murakkabligi → 1 sprint qo'shimcha bo'lishi mumkin.

### Sprint 1.2 (Hafta 2) — Backend Infrastructure

**Maqsad:** Cloudflare backend launch.

**Tasklar:**
- [ ] Cloudflare Workers project setup
- [ ] D1 database schema
- [ ] Auth endpoints (anonymous + Apple/Google)
- [ ] AI proxy (Anthropic API)
- [ ] Plan CRUD endpoints
- [ ] Stats endpoints
- [ ] Push notification setup (FCM)
- [ ] Deploy: staging.api.vow.ai

**Deliverables:**
- ✅ Backend production'da
- ✅ Android client → backend integration

### Sprint 1.3 (Hafta 3) — Multi-language refactor

**Maqsad:** Mavjud i18n tizimini kengaytirish.

**Tasklar:**
- [ ] Existing UiStrings + MentorPhrases (mavjud) — VOW brand bilan yangilanadi
- [ ] All Mentor character strings → MentorPhrases (centralized)
- [ ] Permission rationale text — barcha 6 tilda
- [ ] Calendar dialog — barcha 6 tilda
- [ ] Onboarding screens — barcha 6 tilda
- [ ] Settings screen kengaytirish (wake-up time, sleep time, voice prefs)

**Deliverables:**
- ✅ Hammasi 6 tilda
- ✅ Til o'zgarganda hech qanday hardcoded matn qolmaydi

### Sprint 1.4 (Hafta 4) — Design System Implementation

**Maqsad:** Mockup'ga muvofiq UI components.

**Tasklar:**
- [ ] Theme: VOW colors (cyan #00E5D4 + deep navy)
- [ ] Custom fonts: JetBrains Mono + Söhne Breit (yoki muqobil)
- [ ] `CircularTimer` Composable (mockup'dagidek)
- [ ] `TaskPill` Composable (cyan gradient)
- [ ] `MentorMicButton` (80dp + halo)
- [ ] `BrandHeader` (VOW logo + tagline)
- [ ] All buttons: Primary, Secondary, Ghost
- [ ] Storybook/Showkase integration (component preview)

**Deliverables:**
- ✅ Hard Lock screen v2 (mockup'ga muvofiq) tayyor
- ✅ Storybook'da har komponent ko'rinadi

---

## Faza 2 — Core Loop (Hafta 5-8)

### Sprint 2.1 (Hafta 5) — Force Wake-Up

**Maqsad:** F1 funksiyasi — majburiy uyg'otish.

**Tasklar:**
- [ ] `WakeUpAlarmService` (foreground + WakeLock)
- [ ] `WakeUpActivity` (full-screen)
- [ ] Math challenge generator (random)
- [ ] Accelerometer tilt detection
- [ ] Voice commitment recording + match
- [ ] Snooze logic (max 1 marta)
- [ ] Escalation logic (60s no response → louder)
- [ ] AlarmManager integration
- [ ] Boot recovery (BootReceiver re-arms alarm)
- [ ] Doze mode handling

**Deliverables:**
- ✅ Belgilangan vaqtda 100% yoqiladi
- ✅ 3 ta signal collected → dismiss
- ✅ Telefon o'chsa, qaytadan yoqilganda alarm chiqadi

**Test:**
- 50 ta kuzatuvli wake-up
- Battery o'lik state'da test
- DND, silent mode test

### Sprint 2.2 (Hafta 6) — Always-On Persistent Overlay

**Maqsad:** F3 — har doim ko'rinadigan widget.

**Tasklar:**
- [ ] `PersistentTimerOverlayService`
- [ ] Lock screen rejimi (full screen overlay)
- [ ] Corner widget (1/6 screen)
- [ ] Drag-drop logic (4 burchakka)
- [ ] Position saqlash (DataStore)
- [ ] Screen state listener (locked/unlocked switch)
- [ ] Wall-clock timer rendering (smooth, no tick)
- [ ] Battery optimization (1Hz update only)

**Deliverables:**
- ✅ Telefon ochiq → corner widget ko'rinadi
- ✅ Telefon qulflangan → full lock screen overlay
- ✅ Battery: 1 soatlik widget ≤ 0.5%

**Risk:** Vendor restrictions (Samsung, Xiaomi) — vendor-specific testing kerak.

### Sprint 2.3 (Hafta 7) — Hard Lock Screen v2

**Maqsad:** F4 — mockup'ga muvofiq Hard Lock UI.

**Tasklar:**
- [ ] HardLock Composable (mockup-faithful)
- [ ] `CircularTimer` glow halo
- [ ] Task pill (cyan gradient)
- [ ] Blocked distractions list (strikethrough)
- [ ] Mic button (hold-to-talk)
- [ ] Cancel task ghost button
- [ ] Bottom drawer animation
- [ ] TTS rotation: 5-10 daqiqada motivatsion
- [ ] Quote rotation: "Ey inson, sen vaqtsan..."

**Deliverables:**
- ✅ Hard Lock pixel-perfect to mockup
- ✅ Mic press → negotiation flow ochiladi
- ✅ Cancel task → confirmation modal

### Sprint 2.4 (Hafta 8) — Day-Driver State Machine integration

**Maqsad:** Sprint 1.1'dagi FSM'ni live ulash.

**Tasklar:**
- [ ] `DayDriverFsm` → `DayDriverService` (foreground)
- [ ] State persistence (DataStore)
- [ ] State recovery on boot
- [ ] AlarmManager scheduling for state transitions
- [ ] Notification flow: state changes → user notif
- [ ] Compose UI binding to FSM state
- [ ] Logging + telemetry

**Deliverables:**
- ✅ FSM butun kun davomida ishlay oladi
- ✅ Telefon o'chsa ham state qaytadi
- ✅ End-to-end test: 24-soatlik kun simulyatsiya

---

## Faza 3 — AI + Voice (Hafta 9-12)

### Sprint 3.1 (Hafta 9) — Morning Plan Dialog (F2)

**Maqsad:** Ertalabki AI suhbat.

**Tasklar:**
- [ ] `MorningPlanActivity` + Compose chat UI
- [ ] Anthropic Sonnet 4.6 integration (planning prompts)
- [ ] Multi-turn dialog state
- [ ] Task extraction from conversation
- [ ] Auto-categorization (worship/study/work/etc.)
- [ ] Auto-suggest Pomodoro for study tasks
- [ ] Schedule optimization (priority, time-of-day)
- [ ] Plan confirmation UI
- [ ] Plan editing (drag-drop)
- [ ] Final task scheduling (AlarmManager)

**Deliverables:**
- ✅ 5 turn'gacha plan tuzilishi
- ✅ Foydalanuvchi rejani drag-drop bilan o'zgartira oladi
- ✅ Tasdiqlangach — barcha task'lar avtomatik schedule'lanadi

### Sprint 3.2 (Hafta 10) — Task Transitions (F5)

**Maqsad:** Vazifalar orasidagi AI dialog.

**Tasklar:**
- [ ] 5-min-before warning
- [ ] Transition dialog UI
- [ ] AI question: "Bajarding mi?"
- [ ] +Time options (10, 15, 30 min)
- [ ] Extension count tracking
- [ ] Cascading reschedule (boshqa task'lar siljiydi)
- [ ] Sleep time auto-adjust
- [ ] Skip task logic
- [ ] Streak penalty rules

**Deliverables:**
- ✅ Vazifa tugashidan oldin AI savol beradi
- ✅ Vaqt qo'shilsa, keyingi vazifalar siljiydi
- ✅ 3 marta extend → SKIP majburiy

### Sprint 3.3 (Hafta 11) — Pomodoro Mode (F6)

**Maqsad:** Aqliy ish vazifalari uchun Pomodoro.

**Tasklar:**
- [ ] `PomodoroFsm` + entity
- [ ] 4-cycle UI (mockup'dagidek)
- [ ] Phase transitions (work → break → work)
- [ ] Break suggestions (TTS: "Suv ich. Cho'zil.")
- [ ] Long break (4-cycle keyin 30min)
- [ ] Configurable cycles (25/5, 50/10, 90/20)
- [ ] Deep Focus mode (warmup 10min)

**Deliverables:**
- ✅ Pomodoro task ishlaydi
- ✅ Cycle indicators ko'rinadi
- ✅ Break paytida ekran rangi o'zgaradi

### Sprint 3.4 (Hafta 12) — Sleep Training (F7)

**Maqsad:** Uxlash tarbiyasi.

**Tasklar:**
- [ ] Wind-down scheduler (17:00, 19:00, 21:30)
- [ ] Display warm tones (gamma correction)
- [ ] Sleep lockdown UI (22:00)
- [ ] Scientific facts database (50+ fakt, manbalar bilan)
- [ ] AI-generated facts (Sonnet, kontekstual)
- [ ] Sleep mode bypass (alarm + qo'ng'iroq ruxsat)
- [ ] Wake-up integration (next day)

**Deliverables:**
- ✅ 17:00 dan boshlab wind-down ishlaydi
- ✅ 22:00 da hammasi bloklanadi
- ✅ Foydalanuvchi telefonni ochsa — fakt ko'rsatadi

---

## Faza 4 — Polish & Anti-Tamper (Hafta 13-16)

### Sprint 4.1 (Hafta 13) — AI Negotiation v2

**Maqsad:** F9'ning v1 versiyasini VOW falsafasiga moslashtirish.

**Tasklar:**
- [ ] Mentor character'ni VOW brand'ga moslash
- [ ] System prompt yangilanishi (VOW values)
- [ ] Negotiation UI (Hard Lock'dan ochilishi)
- [ ] FSM enforcement
- [ ] Server-signed grant tokens (anti-tamper)
- [ ] LLM tool use ulanishi
- [ ] Voice input/output (TTS+STT)
- [ ] Audit log to backend

**Deliverables:**
- ✅ Negotiation harakat'i muvaffaqiyatli yoki to'sib qo'yadi
- ✅ Server qarorni HMAC bilan imzolaydi
- ✅ Client'da tampering mumkin emas

### Sprint 4.2 (Hafta 14) — Streak System v2 + Insurance (F10)

**Maqsad:** Streak'ni server-canonical qilish.

**Tasklar:**
- [ ] Streak server'da hisoblanishi
- [ ] Local cache (offline support)
- [ ] Streak insurance (1/oy, cooldown 30 kun)
- [ ] Insurance UI (tugma, bir martagina)
- [ ] Streak grafigi (haftalik, oylik)
- [ ] Streak yo'qolish notification (yaxshi tone)

**Deliverables:**
- ✅ Streak'ni client'da soxtalashtirib bo'lmaydi
- ✅ Insurance — bir martagina tikrorlanadigan kechirim

### Sprint 4.3 (Hafta 15) — Anti-Tamper

**Maqsad:** Bypass'larga qarshi qatlamlar.

**Tasklar:**
- [ ] EncryptedSharedPreferences barcha sensitive data uchun
- [ ] SQLCipher Room database
- [ ] Server-anchored time clock
- [ ] AccessibilityService re-arm logic
- [ ] WorkManager validation worker (15-min check)
- [ ] APK integrity check (Play Integrity API)
- [ ] Force-stop detection + penalty

**Deliverables:**
- ✅ Top 5 vendor'larda bypass yopiq
- ✅ Quartal red-team test passed

### Sprint 4.4 (Hafta 16) — Polish & Performance

**Maqsad:** Battery + crash + ANR.

**Tasklar:**
- [ ] Crashlytics integration
- [ ] StrictMode ANR detection
- [ ] LeakCanary memory leaks
- [ ] Battery profiling (24h test)
- [ ] Cold start optimization
- [ ] Compose recomposition optimization
- [ ] Database query indexing

**Deliverables:**
- ✅ Crash-free > 99.5%
- ✅ Cold start < 1.5s P95
- ✅ Battery < 7%/day
- ✅ ANR < 0.05%

---

## Faza 5 — Beta Launch (Hafta 17-22)

### Sprint 5.1 (Hafta 17) — Onboarding Refinement

**Maqsad:** Drop-off ni minimallashtirish.

**Tasklar:**
- [ ] Onboarding A/B test infrastructure (Remote Config)
- [ ] 3 ta variant: Long form, Short form, Voice-led
- [ ] Friction reduction (skip optional steps)
- [ ] Better permission rationale UX
- [ ] Animated guides (which Settings button to press)
- [ ] Vendor-specific guides (Samsung, Xiaomi, Huawei)

**Deliverables:**
- ✅ Onboarding completion rate > 60%
- ✅ A/B test framework ishlaydi

### Sprint 5.2 (Hafta 18) — Family Link (F11)

**Maqsad:** Ota-ona ilovasi (alohida APK).

**Tasklar:**
- [ ] Family Link entity + backend
- [ ] QR code pairing flow
- [ ] Real-time event stream (Durable Objects)
- [ ] Parent dashboard UI
- [ ] Daily report generator
- [ ] Remote actions (grant time, add task)
- [ ] Multi-child support
- [ ] E2EE for child content

**Deliverables:**
- ✅ Ota-ona ilovasi shipped
- ✅ Real-time hisobot ishlaydi
- ✅ Privacy policy compliance

### Sprint 5.3 (Hafta 19) — Localization + Voice

**Maqsad:** 6 ta tilda kuchli quality.

**Tasklar:**
- [ ] Native speaker testing (har til)
- [ ] AI prompt fine-tuning per language
- [ ] TTS quality testing (vendor TTS engines)
- [ ] STT accuracy testing
- [ ] ElevenLabs Premium voice integration (optional)
- [ ] RTL support testing (Arabic)

**Deliverables:**
- ✅ Har til'da NPS > 40
- ✅ Premium voice tier ishga tushdi

### Sprint 5.4 (Hafta 20) — Closed Beta

**Maqsad:** 50-100 ta foydalanuvchi.

**Tasklar:**
- [ ] Internal track release
- [ ] Closed beta'ga 50 ta foydalanuvchi taklif
- [ ] Feedback channel (Discord yoki Telegram)
- [ ] Daily metrics dashboard
- [ ] Quick crash hotfix process
- [ ] Onboarding observation (calls bilan)

**Deliverables:**
- ✅ 50 ta foydalanuvchi sinab ko'radi
- ✅ Top 5 issue listed
- ✅ Crash-free rate confirmed

### Sprint 5.5 (Hafta 21) — Bug Fixes & Iteration

**Maqsad:** Closed beta feedback'ga ko'ra fix'lash.

**Tasklar:**
- [ ] Top 10 crash fix
- [ ] Top 5 UX issue
- [ ] Performance regression
- [ ] Vendor-specific issues
- [ ] Support documentation

**Deliverables:**
- ✅ Open beta'ga tayyor

### Sprint 5.6 (Hafta 22) — Open Beta + Marketing

**Maqsad:** 1000+ foydalanuvchi.

**Tasklar:**
- [ ] Open beta release (Play Store)
- [ ] Privacy policy + Terms of Service
- [ ] Play Store listing (5 ta screenshot, 3 ta video)
- [ ] Influencer marketing (3 ta productivity yutuber)
- [ ] Twitter/X thread (technical deep-dive)
- [ ] HackerNews launch
- [ ] ProductHunt launch (planned for V1.0)

**Deliverables:**
- ✅ 1000+ foydalanuvchi
- ✅ D1 retention > 60%
- ✅ NPS > 40

---

## V2.5 — iOS launch (Oy 6-8)

iOS to'liq port qilish — alohida 8 haftalik fazalar:
- Hafta 23-24: Project setup, FamilyControls entitlement apply
- Hafta 25-26: Core domain port (Kotlin Multiplatform shared modules)
- Hafta 27-28: SwiftUI screens + Live Activities
- Hafta 29-30: Voice (AVSpeechSynthesizer + Speech)
- Hafta 31: Beta + App Store review

---

## V3 — Cross-platform (Oy 9-12)

- Web Companion (Next.js)
- Android TV display app
- Apple Watch widget
- Smart TV display (kelajak)
- Anti-tamper Rust core (UniFFI)

---

## Kalendar — kalit milestone'lar

| Sana | Milestone |
|---|---|
| 2026-05-10 | Bugun — V2 spec yakunlandi |
| 2026-05-17 | Sprint 1.1 yakuni — DB v4 ishlaydi |
| 2026-06-07 | Faza 1 yakuni — foundation |
| 2026-07-05 | Faza 2 yakuni — Core loop ishlaydi (wake → plan → block) |
| 2026-08-02 | Faza 3 yakuni — AI + voice integration |
| 2026-08-30 | Faza 4 yakuni — Polish + anti-tamper |
| 2026-09-13 | Closed Beta start |
| 2026-10-04 | **Open Beta launch (V2.0)** |
| 2026-12-01 | iOS V2.5 |
| 2027-02-01 | V3.0 (cross-platform) |

---

## Risk regissteri

| Risk | Ehtimol | Ta'sir | Yumshatish |
|---|---|---|---|
| Sprint 2.1 (Wake-Up) battery | Yuqori | Yuqori | Doze-aware design + extensive testing |
| Sprint 2.2 (Overlay) vendor blocks | Yuqori | Yuqori | Per-vendor onboarding + diagnostics |
| Sprint 4.3 (Anti-tamper) bypass found | Yuqori | O'rta | Quarterly red-team review |
| Sprint 5.2 (Family) Apple Family policy | O'rta | Yuqori | Self-imposed parental control framing |
| Anthropic API quota | Past | O'rta | Rate limiting + cache |
| Server downtime | Past | Yuqori | Multi-region deploy + offline fallback |
| iOS V2.5 entitlement rejected | O'rta | Yuqori | Apply early (Sprint 1) + Plan B |

---

## Jamoa va resurslar (taklif)

| Rol | Yuk | Sprint'lar |
|---|---|---|
| Android Lead Engineer | 100% | 1-22 |
| Backend Engineer | 70% | 1-12, 18 |
| iOS Engineer | 100% | V2.5 (23+) |
| UX/UI Designer | 50% | 1-8, 17 |
| AI Prompt Engineer | 30% | 9-12, 18 |
| QA / Red-team | 20% | 13-16, 20-22 |
| Product Manager | 30% | All |

**Total cost (5 oy):** ~$120-150k (engineering only, excluding marketing/ops)

---

## Tasdiqlash

- [ ] Roadmap qabul qilinadigan
- [ ] Milestones realistic
- [ ] Resource talablari foydali
- [ ] Risk plan tushunarli

Tasdiqlangach — Sprint 1.1'dan boshlaymiz.
