# ROADMAP — Mentor-AI MVP (Android)

**Hujjat versiyasi:** 0.2
**Sana:** 2026-05-09
**Maqsad:** 14 hafta ichida Play Store internal beta (Calendar sync qo'shildi)

---

## Umumiy timeline

```
Hafta:  1   2   3   4   5   6   7   8   9   10  11  12
        ─── ─── ─── ─── ─── ─── ─── ─── ─── ─── ─── ───
Sprint: │ S0│ S1│   S2  │   S3  │   S4  │   S5  │ S6│
        Setup Permis. Block AI/UI Polish Beta
```

---

## Sprint 0: Foundation (Hafta 1)

**Maqsad:** Loyiha skeleton, build tooling, CI/CD.

### Tasklar:
- [ ] Android Studio loyihasini yaratish (Kotlin DSL, Compose, Gradle 8.x)
- [ ] Ko'p modulli tuzilish: `app`, `feature-guard`, `feature-agent`, `core-policy`, `core-data`, `core-common`
- [ ] Hilt sozlash
- [ ] Detekt + ktlint + lint baseline
- [ ] GitHub Actions: build + test on PR
- [ ] Firebase loyihasi (Crashlytics + Remote Config)
- [ ] Rust core repo, cargo workspace, NDK toolchain
- [ ] UniFFI/JNI binding "hello world" (Rust funksiyasini Kotlin'dan chaqirish)
- [ ] Material 3 + Dark Discipline color tokens (Compose)

### Deliverables:
- `./gradlew assembleDebug` ishlaydi
- "Hello from Rust" Compose ekranida ko'rinadi
- CI yashil

### Risk:
- NDK + UniFFI sozlash birinchi marta 2-3 kun olishi mumkin

---

## Sprint 1: Permission Funnel (Hafta 2–3)

**Maqsad:** Foydalanuvchini barcha kerakli permission'lardan o'tkazish.

### Tasklar:
- [ ] Onboarding flow — 8 ekran
- [ ] Compose navigation
- [ ] Har bir permission uchun:
  - "Why?" ekrani
  - System dialog/Settings'ga yo'naltirish
  - Permission grant'ini tekshirish
  - Animatsiyali yo'riqnoma (qaysi tugmani bosish kerak)
- [ ] Maqsad yozish ekrani (min 10 so'z, ovozli yozib olish)
- [ ] Bloklanadigan ilovalarni tanlash UI
- [ ] Onboarding state'ni Room'ga saqlash (qayta ochish — qaytmaslik)

### Permission ketma-ketligi:
1. Notifications (oson)
2. Usage Stats
3. System Alert Window (Overlay)
4. Accessibility Service (eng qiyin — ko'rsatma video)
5. Device Admin
6. Audio Recording

### Deliverables:
- Onboarding'ni to'liq o'tib, "Setup complete" ekraniga yetish
- Har bir permission'ning runtime check'i

### Risk:
- Accessibility Service'ni yoqish — har bir vendor (Samsung/Xiaomi/Huawei) UI har xil. Top 5 vendor uchun alohida yo'riqnoma kerak.

---

## Sprint 2: Hard-Block Mexanizmi (Hafta 4–5)

**Maqsad:** Bloklash logikasi ishga tushishi.

### Tasklar:
- [ ] `FocusGuardService` — foreground service
- [ ] `AppMonitorAccessibility` — TYPE_WINDOW_STATE_CHANGED listener
- [ ] Rust core: `RuleEngine.evaluate(packageName)`
- [ ] `FocusOverlayManager` — WindowManager overlay
- [ ] Compose UI overlay ichida: timer + maqsad + 2 tugma
- [ ] "Back" tugmasi → launcher (CATEGORY_HOME)
- [ ] Settings'ga navigatsiya intercept (bypass-trap)
- [ ] BootReceiver + GuardValidationWorker

### Deliverables:
- Sessiya boshlanadi → Instagram ochilsa → overlay ko'tariladi → "Return" → launcher
- Reboot'dan keyin sessiya tiklanadi
- AccessibilityService o'chirilsa — yuqori bildirishnoma

### Test:
- Manual: 10 ta turli ilovani test qilish
- Stress test: 1 daqiqada 30 marta ilova almashtirish

### Risk:
- Overlay javob vaqti 50ms'dan oshsa, foydalanuvchi bloklangan ilovani ko'rib oladi → frustration

---

## Sprint 2.5: Calendar Sync (Hafta 6) — YANGI

**Maqsad:** Google Calendar'dan vazifalarni o'qib, sessiyalarga aylantirish.

### Tasklar:
- [ ] Google OAuth 2.0 sozlash (Firebase Auth + Google Sign-In)
- [ ] Calendar API v3 integration
- [ ] `CalendarSyncWorker` — har 15 daq + push webhook
- [ ] AI klassifikator (Haiku 4.5) — vazifa nomidan kategoriya aniqlash
- [ ] Vazifa → blocklist mapping logic
- [ ] `SessionStartLocker` — vaqt kelganda telefonni uyg'otish
- [ ] `AlarmManager.setExactAndAllowWhileIdle()` integration
- [ ] Calendar UI (kunlik ro'yxat ekrani)

### Deliverables:
- Google Calendar'da yaratilgan vazifa avtomatik sessiyaga aylanadi
- Vaqt kelganda telefon majburiy yoqiladi va overlay chiqadi

### Risk:
- Google Calendar API quota (kunlik 1M request — bizga yetarli)
- AlarmManager Doze mode'da kechikishi mumkin (Samsung qurilmalarida)

---

## Sprint 3: AI Mentor + UI (Hafta 7–8)

**Maqsad:** AI agent integratsiyasi va asosiy UI.

### Tasklar:
- [ ] Backend (Cloudflare Worker) — minimal
  - `/v1/auth` (anonymous device_id)
  - `/v1/sessions/:id/negotiate` (FSM + Anthropic API)
- [ ] Anthropic API integration (Claude Haiku 4.5 streaming)
- [ ] System prompt yozish va sinash (Stoik tone)
- [ ] FSM logic (server-side)
- [ ] Android: streaming response handler
- [ ] TTSEngine (Android TextToSpeech) — token kelayotgan paytda gapiradi
- [ ] STTEngine (SpeechRecognizer) — sabab yozish
- [ ] Negotiation overlay UI (alohida ekran)
- [ ] Sessiya ekrani (timer, maqsad, statistika)

### Deliverables:
- "Override request" → AI mentor bilan ovozli muloqot
- AI o'zbek tilida javob beradi
- "5 daqiqa" so'rovi: kerakli paytda berilib, kerakli paytda rad etiladi

### Risk:
- Claude'ning o'zbek tilidagi javoblari sifati — testdan o'tkazish kerak
- Server FSM bilan client orasidagi kechikish (network latency) — offline fallback kerak

---

## Sprint 4: Anti-Tamper + Polish (Hafta 9–10)

**Maqsad:** Bypass'larni yopish, UX'ni jilolash.

### Tasklar:
- [ ] DeviceAdmin integratsiyasi (uninstall block)
- [ ] Encrypted state store (Rust + Android Keystore)
- [ ] HMAC chain implementatsiyasi
- [ ] Streak penalty system
- [ ] Force-stop detection logic
- [ ] Settings-trap polish (5 ta vendor uchun)
- [ ] Notification design (high-priority style)
- [ ] Haptic feedback overlay'da
- [ ] Sound design integratsiyasi (low C2, 40Hz rumble, Tibetan bowl)
- [ ] Adversarial testing — birinchi red-team round

### Deliverables:
- 10 ta bypass scenarioning hammasi yopiq
- Polished UX (animatsiyalar, ovozlar)

### Risk:
- DeviceAdmin Play Store policy — manual review uchun video tayyorlash

---

## Sprint 5: Beta-readiness (Hafta 11–12)

**Maqsad:** Internal va closed beta launch.

### Tasklar:
- [ ] Crashlytics + analytics integratsiyasi (Mixpanel yoki PostHog)
- [ ] Onboarding A/B test infra (Remote Config)
- [ ] Localizatsiya: O'zbek (asosiy), Rus, Ingliz
- [ ] Privacy policy + Terms of Service
- [ ] Play Store listing (screenshotlar, video, description)
- [ ] Permission justification hujjati (Play Store review uchun)
- [ ] Internal track release
- [ ] 50 closed beta foydalanuvchini taklif qilish
- [ ] Feedback channel (Discord yoki Telegram)

### Deliverables:
- Play Store internal track'da APK
- 50 foydalanuvchi sinab ko'rmoqda
- Crashlytics dashboard

### Risk:
- Play Store review 1-2 hafta cho'zilishi mumkin

---

## Sprint 6: Open Beta + Iteratsiya (Hafta 13–14)

**Maqsad:** Birinchi 1,000 foydalanuvchi.

### Tasklar:
- [ ] Closed beta'dan kelgan feedback bo'yicha tuzatishlar
- [ ] Top 5 crash'larni hal qilish
- [ ] Open beta track release
- [ ] Marketing kontentini tayyorlash (Twitter/X thread, YouTube demo)
- [ ] Productivity influencer'larni topish (3 nafar)
- [ ] Retention metrics dashboard

### Deliverables:
- Open beta'da 1,000 foydalanuvchi
- D1 retention > 60% (asosiy KPI)

### Risk:
- Foydalanuvchilar AccessibilityService'dan qo'rqishadi — onboarding'ni soddalashtirish kerak

---

## V2 Roadmap (12 haftadan keyin)

| Funktsiya | Tahminiy vaqt |
|---|---|
| iOS versiya (FamilyControls) | 8 hafta |
| Premium ovozli mentor (ElevenLabs) | 2 hafta |
| Statistika dashboard | 3 hafta |
| Accountability partner | 4 hafta |
| Web blocking (browser-level) | 6 hafta |
| Hard tier (sideload) | 4 hafta |
| Pul/xayriya garovi integratsiyasi | 3 hafta |

---

## Kalendar tasvirlash

| Sana | Asosiy milestone |
|---|---|
| 2026-05-09 | **Bugun** — Reja boshi |
| 2026-05-16 | Sprint 0 tugashi (build pipeline tayyor) |
| 2026-05-30 | Sprint 1 tugashi (Onboarding ishlaydi) |
| 2026-06-13 | Sprint 2 tugashi (Hard-block ishlaydi) |
| 2026-06-27 | Sprint 3 tugashi (AI mentor ishlaydi) |
| 2026-07-11 | Sprint 4 tugashi (Polish + anti-tamper) |
| 2026-07-25 | Sprint 5 tugashi (Closed beta) |
| 2026-08-01 | **Open beta launch** |

---

## Risk regissteri (top 5)

1. **Play Store rejection (AccessibilityService policy)** — Mitigation: justification video va submission'gacha policy specialistlarga maslahat
2. **Foydalanuvchilar permission'larni bermaydi (drop-off > 50%)** — Mitigation: onboarding A/B testing, "Soft mode" downgrade
3. **AI o'zbek tilida sifatsiz** — Mitigation: prompt engineering, native speakerlar bilan testlash
4. **Bypass topiladi va tarqaydi** — Mitigation: oylik adversarial review, tezkor patch siklisi
5. **Server FSM downtime** — Mitigation: client-side fallback FSM (lokal qaror, faqat DENY)

---

## Jamoa va resurslar (taklif)

| Rol | Tahminiy vaqt | Eslatma |
|---|---|---|
| Android dasturchi (senior) | 100% | Asosiy ish — 12 hafta |
| Backend dasturchi | 30% | Faqat Sprint 3'dan boshlab |
| Rust dasturchi | 20% | Sprint 0 va Sprint 4'da kuchli |
| UX dizayner | 50% | Onboarding va overlay UI |
| AI prompt engineer | 30% | Sprint 3'da kuchli, keyin tutib turish |
| QA / red-team | 20% | Sprint 4'dan boshlab |
