# ARCHITECTURE — Mentor-AI Android

**Hujjat versiyasi:** 0.2
**Sana:** 2026-05-09
**Qamrov:** Asosan Android (iOS V2 — qisqa qo'shimcha)
**Stack qarori:** **Native (Kotlin + Swift)** — Flutter/RN qo'llab-quvvatlanmaydi (sabab pastda)

---

## 1. Yuqori darajali arxitektura

```
┌──────────────────────────────────────────────────────────────┐
│                    ANDROID DEVICE                            │
│                                                              │
│  ┌────────────────┐   ┌─────────────────────────────────┐   │
│  │  Mentor App    │   │   System (Android OS)            │   │
│  │   (UI + Logic) │   │                                  │   │
│  └────────┬───────┘   │  ┌─────────────────────────┐    │   │
│           │           │  │  AccessibilityService    │    │   │
│           │           │  │  (Foreground monitoring) │    │   │
│           │           │  └────────────┬─────────────┘    │   │
│           │           │               │                  │   │
│           │           │  ┌────────────▼─────────────┐    │   │
│           │ ◄─────────┼──┤  FocusGuardService       │    │   │
│           │           │  │  (Foreground service)    │    │   │
│           │           │  └────────────┬─────────────┘    │   │
│           │           │               │                  │   │
│           │           │  ┌────────────▼─────────────┐    │   │
│           │           │  │  WindowManager Overlay   │    │   │
│           │           │  │  (TYPE_APPLICATION_OVERLAY)│   │   │
│           │           │  └──────────────────────────┘    │   │
│           │           │                                  │   │
│           │           │  ┌──────────────────────────┐    │   │
│           │           │  │  DevicePolicyManager     │    │   │
│           │           │  │  (Uninstall protection)  │    │   │
│           │           │  └──────────────────────────┘    │   │
│           │           └──────────────────────────────────┘   │
│           │                                                  │
│  ┌────────▼────────────────────┐                             │
│  │  Rust Core (libmentor.so)   │                             │
│  │  - RuleEngine               │                             │
│  │  - PolicyValidator          │                             │
│  │  - StreakManager            │                             │
│  │  - EncryptedStateStore      │                             │
│  └─────────────────────────────┘                             │
│                                                              │
└──────────────────────────────────────────────────────────────┘
                            │
                            │ HTTPS (signed requests)
                            ▼
┌──────────────────────────────────────────────────────────────┐
│                  BACKEND (Cloudflare Workers)                 │
│                                                              │
│  ┌──────────────┐  ┌──────────────┐  ┌────────────────┐     │
│  │  AI Gateway  │  │  FSM Engine  │  │  Sync Service  │     │
│  │ (Anthropic)  │  │ (Decisions)  │  │  (D1 storage)  │     │
│  └──────────────┘  └──────────────┘  └────────────────┘     │
└──────────────────────────────────────────────────────────────┘
```

---

## 2. Modullar va paketlar

```
android/
├── app/                              # Asosiy ilova UI'si (Compose)
│   ├── ui/
│   │   ├── onboarding/              # Permission funnel
│   │   ├── session/                 # Sessiya ekrani (timer)
│   │   ├── settings/                # Sozlamalar
│   │   └── overlay/                 # Hard-block overlay
│   └── di/                          # Hilt modullar
│
├── feature-guard/                    # Bloklash mexanizmi
│   ├── service/
│   │   ├── FocusGuardService.kt     # Foreground service
│   │   ├── AppMonitorAccessibility.kt # AccessibilityService
│   │   └── BootReceiver.kt          # Reboot recovery
│   ├── overlay/
│   │   └── FocusOverlayManager.kt   # WindowManager overlay
│   └── admin/
│       └── MentorDeviceAdmin.kt     # DeviceAdmin receiver
│
├── feature-agent/                    # AI mentor qatlam
│   ├── client/
│   │   └── AnthropicClient.kt       # Streaming HTTP client
│   ├── voice/
│   │   ├── TTSEngine.kt             # Android TextToSpeech
│   │   └── STTEngine.kt             # SpeechRecognizer
│   └── fsm/
│       └── NegotiationStateMachine.kt # Local FSM (server'ga signed)
│
├── core-policy/                      # Rust binding
│   ├── PolicyEngine.kt              # JNI orqali Rust'ga ko'prik
│   └── jniLibs/
│       ├── arm64-v8a/libmentor.so
│       └── armeabi-v7a/libmentor.so
│
├── core-data/                        # Maʼlumotlar qatlami
│   ├── local/
│   │   ├── EncryptedPrefs.kt
│   │   └── SessionDao.kt (Room)
│   └── remote/
│       └── BackendApi.kt            # Retrofit
│
└── core-common/                      # Umumiy utilitlar
    └── ...
```

---

## 3. Asosiy komponentlar — chuqur tahlil

### 3.1 `FocusGuardService` (Foreground Service)

**Vazifa:** Sessiya davomida hayotda qolish, AccessibilityService'ni nazorat qilish, overlay'ni boshqarish.

**Texnik xususiyatlar:**
- `FOREGROUND_SERVICE_TYPE_SPECIAL_USE` (Android 14+ majburiy)
- Persistent notification — foydalanuvchi service ishlayotganini ko'radi
- Partial WakeLock — faqat sessiya davomida (battery'ni saqlash uchun)
- Hilt orqali boshqa qatlamlarga ulanadi

**Hayot davri:**
```
Sessiya boshlash → startForegroundService() → ishlaydi → Sessiya tugaydi → stopSelf()
                                                ↑
                                                └── BootReceiver tomonidan ham qayta ishga tushiriladi
```

### 3.2 `AppMonitorAccessibility` (AccessibilityService)

**Vazifa:** Foydalanuvchi qaysi ilovani ochayotganini bilish (real-time).

**Nima uchun AccessibilityService?**
- `UsageStatsManager` — kechikadi (5+ soniya), real-time emas
- `ActivityManager.getRunningTasks()` — Android 5'dan beri ishlamaydi
- `AccessibilityService` — yagona qonuniy real-time yo'l

**Hodisa turi:** `AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED`
**Javob vaqti:** ~50ms (notificationTimeout)

**Xavfsizlik bypass-trap:**
Foydalanuvchi `Settings > Apps > Mentor-AI > Disable` ga borsa, AccessibilityService'ning o'zi buni intercept qiladi va overlay chiqaradi. Foydalanuvchi sozlama sahifasini ko'ra olmaydi.

### 3.3 `FocusOverlayManager` (Hard-block UI)

**Vazifa:** Bloklangan ilova ustiga to'liq ekranli "to'siq" qo'yish.

**Texnik xususiyatlar:**
- `WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY` — boshqa ilovalar ustiga chiqadi
- `LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS` — status bar ustidan ham
- `FLAG_NOT_FOCUSABLE` — IME (klaviatura) ni bloklamaydi, lekin touch'larni o'ziga oladi
- ComposeView ichida — Jetpack Compose'dan foydalana olamiz

**Bypass himoyasi:**
- "Back" tugmasi bosilsa, foydalanuvchi launcher'ga yuboriladi (Intent.CATEGORY_HOME), bloklangan ilovaga emas
- Recent apps'dan tanlasa ham overlay qaytadan ko'tariladi

### 3.4 `MentorDeviceAdmin` (DeviceAdmin Receiver)

**Vazifa:** Ilovani o'chirishni qiyinlashtirish.

**Imkoniyatlar:**
- `setUninstallBlocked(true)` — Settings'dan uninstall'ni bloklash
- `lockNow()` — ekranni qulflash (extreme tier)
- `setPasswordQuality()` — qattiq parol talabini o'rnatish

**Cheklovlar:**
- DeviceAdmin Play Store ilovasi bo'lishi mumkin, lekin Google bunday ilovalarga chuqur skeptik
- Foydalanuvchi DeviceAdmin'ni o'chirishi mumkin, lekin AccessibilityService bu sahifani trap qiladi

### 3.5 `Rust Core` (libmentor.so)

**Nima uchun Rust?**
1. **Tahrirga chidamli** — JS/Kotlin'da yozilgan logikani foydalanuvchi modify qila oladi
2. **Tezkor** — overlay tetikleyish 50ms ichida bo'lishi kerak
3. **Bir xil** — Android va iOS'da bir xil qoidalar dvigateli (UniFFI orqali)

**Asosiy modullar:**
```rust
pub mod engine;        // Asosiy qoidalar dvigateli
pub mod policy;        // Server tomonidan imzolangan qoidalar
pub mod state_store;   // Encrypted state (HMAC-chained)
pub mod streak;        // Streak hisoblash
pub mod clock;         // Trusted monotonic clock
```

**JNI binding:**
```kotlin
// Kotlin tomon
class PolicyEngine {
    private val nativeHandle: Long = createEngine()

    external fun createEngine(): Long
    external fun evaluate(handle: Long, packageName: String): VerdictNative
    external fun isInActiveSession(handle: Long): Boolean

    companion object {
        init { System.loadLibrary("mentor") }
    }
}
```

---

## 4. Kerakli ruxsatlar (Permissions)

### Onboarding'da ketma-ket so'raladi:

| # | Permission | Manifest | Foydalanuvchi sozlamadan beradi | Tushuntirish |
|---|---|---|---|---|
| 1 | `INTERNET` | Avtomatik | — | AI bilan muloqot |
| 2 | `RECEIVE_BOOT_COMPLETED` | Avtomatik | — | Reboot'dan keyin tiklanish |
| 3 | `FOREGROUND_SERVICE` | Avtomatik | — | Asosiy service |
| 4 | `FOREGROUND_SERVICE_SPECIAL_USE` | Manifest declaration | — | Android 14+ majburiy |
| 5 | `POST_NOTIFICATIONS` | Runtime | Ha (popup) | Mentor xabarlari |
| 6 | `PACKAGE_USAGE_STATS` | Special | Ha (Settings) | Ilovalarni aniqlash |
| 7 | `SYSTEM_ALERT_WINDOW` | Special | Ha (Settings) | Overlay |
| 8 | `BIND_ACCESSIBILITY_SERVICE` | Special | Ha (Settings) | Real-time monitoring |
| 9 | `BIND_DEVICE_ADMIN` | Special | Ha (Settings) | Uninstall protection |
| 10 | `RECORD_AUDIO` | Runtime | Ha (popup) | Ovozli muloqot |

### Permission funnel UX:
- Har bir permission alohida ekranda
- Permission bermay turib oldinga o'tib bo'lmaydi (yoki "Soft mode"ga downgrade)
- "Why?" tugmasi har bir permission'ning sababini batafsil ko'rsatadi
- Settings ochilganda — "Mentor-AI" qatorini topish bo'yicha animatsiyali yo'riqnoma

---

## 5. Maʼlumotlar oqimi (Data Flow)

### 5.1 Sessiya boshlash oqimi
```
Foydalanuvchi "Start Session" → Compose UI
    ↓
SessionViewModel.startSession()
    ↓
1. Maqsadni Room DB'ga saqlash
2. Ovozli yozuv'ni shifrlangan storage'ga saqlash
3. Backend'ga commitment yuborish (signed)
4. PolicyEngine.activateSession() — Rust core
5. FocusGuardService startForegroundService()
6. FocusGuardService AccessibilityService bilan bog'lanish (binder)
7. Notification: "Sessiya faol — 90:00 qoldi"
```

### 5.2 Bloklash oqimi
```
Foydalanuvchi Instagram'ni ochadi
    ↓
AppMonitorAccessibility.onAccessibilityEvent()
    ↓
PolicyEngine.evaluate("com.instagram.android") → Rust
    ↓
Verdict::Block { mentor_prompt_id: PROMPT_SOCIAL }
    ↓
FocusOverlayManager.show(blockedApp, message)
    ↓
TTSEngine.speak("Stop. Sen 90 daqiqaga so'z bergan eding.")
```

### 5.3 "5 daqiqa" so'rovi oqimi
```
Foydalanuvchi "Override request" tugmasini bosadi
    ↓
AnthropicClient.startNegotiation(context)
    ↓
[server] FSM.evaluate(request_count, time_elapsed, app_category)
    ↓
[server] LLM.generate(prompt, context) → streaming
    ↓
[client] TTSEngine.streamSpeak(tokens) — javob kelayotgan paytda gapiradi
    ↓
[client] STTEngine.startListening() — foydalanuvchi sababini aytadi
    ↓
[server] LLM.classify(reason, schema) → ReasonType
    ↓
[server] FSM.decide() → GRANT(2min) | DENY
    ↓
[client] PolicyEngine.applyDecision(signed_token)
```

---

## 6. Persistence & Anti-Tamper Strategiya

### 6.1 Reboot recovery
```
BootReceiver (RECEIVE_BOOT_COMPLETED)
    ↓
1. PolicyEngine.loadState() — encrypted storage'dan
2. Agar faol sessiya bor bo'lsa: FocusGuardService restart
3. WorkManager: GuardValidationWorker schedule (15 daq)
```

### 6.2 Force-stop himoyasi
- Foydalanuvchi `Settings > Apps > Force Stop` qilsa, foreground service to'xtaydi
- Yechim: ilova qayta ochilganda **streak penalty** qo'llaniladi
- Foydalanuvchi buni biladi (onboarding'da ko'rsatilgan)

### 6.3 GuardValidationWorker
Har 15 daqiqada tekshiradi:
1. AccessibilityService hali yoqilganmi?
2. Permission'lar bekor qilinmadimi?
3. Faol sessiya bor bo'lsa, FocusGuardService ishlayaptimi?

Agar nimadir buzilgan bo'lsa:
- Yuqori darajali bildirishnoma
- Foydalanuvchi ilovani ochmaguncha streak to'xtatib turiladi

### 6.4 Encrypted State Store (Rust)
```rust
struct StateStore {
    data: EncryptedBlob,        // AES-GCM with Android Keystore key
    hmac_chain: Vec<Hmac>,      // Har bir o'zgarish HMAC bilan zanjirlanadi
}
```
Foydalanuvchi `clear app data` qilsa, streak yo'qoladi va serverda ham log qilinadi.

---

## 7. Backend integratsiyasi

### 7.1 API endpoints

| Endpoint | Method | Maqsad |
|---|---|---|
| `/v1/auth` | POST | Anonim hisob yaratish (device_id) |
| `/v1/sessions` | POST | Sessiya boshlash (commitment yozib qo'yish) |
| `/v1/sessions/:id/negotiate` | POST | "5 daq" so'rovi (FSM + LLM) |
| `/v1/sessions/:id/end` | POST | Sessiya tugashi |
| `/v1/policies/sync` | GET | Qoidalar (signed JSON) |
| `/v1/streak` | GET | Foydalanuvchi streak holati |

### 7.2 Server FSM (Cloudflare Workers + D1)
```typescript
// server/src/fsm/negotiation.ts
export async function negotiate(req: NegotiationRequest): Promise<Decision> {
    const ctx = await loadContext(req.userId, req.sessionId);

    if (ctx.requestsToday >= 3) return { decision: 'DENY_HARD', ... };
    if (ctx.minutesElapsed < 25) return { decision: 'DENY_SOFT', ... };

    // LLM ChatStream uchun
    const reasonClassification = await classifyReason(req.spokenReason);

    if (reasonClassification.is_genuine_emergency) {
        return { decision: 'GRANT', minutes: 15, signedToken: sign(...) };
    }

    return { decision: 'DENY_HARD', message: ... };
}
```

### 7.3 Signed decision token
Har bir GRANT qarori serverda HMAC-SHA256 bilan imzolanadi:
```
{
  "decision": "GRANT",
  "minutes": 5,
  "expires_at": "2026-05-09T15:30:00Z",
  "session_id": "...",
  "sig": "hmac-sha256-..."
}
```
Client bu token'ni Rust core'ga beradi. Rust signaturni serverning public key bilan tekshiradi. Foydalanuvchi token'ni qalbakilashtira olmaydi.

---

## 8. Texnik qarorlar — Trade-off jadvali

### 8.1 Cross-platform stack qarori (yakunlangan)

**Tanlangan:** Native Kotlin (Android) + Native Swift (iOS) + umumiy Rust core.
**Rad etilgan:** Flutter, React Native.

**Sabab — funksiyalarning 70%'i platform-spetsifik:**

| Talab | Flutter/RN qila oladimi? |
|---|---|
| `AccessibilityService` (Android) | Faqat platform channel orqali — baribir Kotlin yozasiz |
| `SYSTEM_ALERT_WINDOW` overlay | Plugin yo'q, qo'lda bridge |
| `DeviceAdmin` | Plugin yo'q |
| `FamilyControls` (iOS) | Plugin yo'q (2026), Swift Extension target talab |
| `DeviceActivityMonitor` extension | Mumkin emas — Swift kerak |
| `ShieldAction` extension | Mumkin emas — Swift kerak |
| `Live Activities` (iOS) | ActivityKit + WidgetKit — Swift kerak |
| Bypass-tahdid | JS bundle'ni patch qilish oson |

**Xulosa:** Cross-platform "afzalligi" yo'q — UI'ning 80%'i deklarativ (Compose ↔ SwiftUI mental model bir xil), copy/paste tezkor.

| Qaror | Tanladim | Sabab | Trade-off |
|---|---|---|---|
| Native Kotlin vs React Native | Native Kotlin | RN'da AccessibilityService va overlay'ga past darajali kirish yo'q | Ko'proq vaqt sarflanadi (lekin sifat muhim) |
| Hilt vs Koin | Hilt | Compose bilan birinchi-darajali integratsiya, compile-time DI | Boilerplate ko'p |
| Room vs SQLDelight | Room | Hilt bilan oson, Compose StateFlow uchun yaxshi | SQL flexibility kamroq |
| Retrofit vs Ktor | Retrofit | Sodda, zo'r OkHttp interceptor ekosistemasi | Ktor multiplatform-ready |
| Rust core vs to'liq Kotlin | Rust core | Tahrirga chidamliroq, iOS bilan kod ulashish | Build pipeline murakkab |
| Compose vs XML Views | Compose | Overlay UI'sini Compose bilan oddiy yozish, declarative | Eskirroq qurilmalar uchun salbiy |
| AccessibilityService vs UsageStats | AccessibilityService | Real-time, 50ms response | Play Store skeptik, justification kerak |

---

## 9. Test strategiyasi

### Unit tests (JUnit + MockK):
- Rust core — `cargo test`
- ViewModel'lar — Turbine + Coroutines test
- FSM logic — alohida modulda

### Integration tests:
- AccessibilityService — emulator + UI Automator
- Overlay — UI Automator screenshot test
- Permission flow — Espresso scenario tests

### Adversarial tests (oylik):
- "Bypass" scenariolarini avtomatlashtirish:
  - Force stop'dan keyin ilova qayta ishladimi?
  - Settings'dan AccessibilityService o'chirilganda intercept qildimi?
  - Reboot'dan keyin sessiya tiklandimi?
- Har bir release'dan oldin manual red-team o'tkazish

---

## 10. Calendar & Social Sync (yangi modul)

### 10.1 Provayder ustuvorligi

| Provayder | Faza | API | Auth |
|---|---|---|---|
| Google Calendar | Faza 1 | Calendar API v3 | OAuth 2.0 |
| Outlook | Faza 3 | Microsoft Graph API | OAuth 2.0 |
| Apple Calendar | Faza 4 (iOS) | EventKit | Local |

### 10.2 Sinxronizatsiya oqimi

```
Google Calendar → CalendarSyncWorker (har 15 daq + push webhook)
                       ↓
                  AI klassifikator (Haiku 4.5)
                       ↓
              ScheduledSession (Room DB)
                       ↓
              Vaqt kelganda → SessionStartLocker
                       ↓
                  Lock Screen overlay
```

### 10.3 Vazifa → blocklist mapping

| Vazifa kategoriyasi | Bloklash darajasi | Misol |
|---|---|---|
| `worship` (Qur'on, Namoz) | MAX — barcha social, video, oyin | "05:00 Qur'on darsi" |
| `study` (dars, imtihon) | Social + video, Spotify ruxsat | "Imtihonga tayyorgarlik" |
| `physical` (sport) | Hammasi, musiqa ruxsat | "Yugurish" |
| `work` | Social + video, Slack/Email ruxsat | "Migratsiya kodi" |
| `family` (oilaviy) | Hammasi, faqat qo'ng'iroq | "Oilaviy ovqat" |
| `sleep` | Hammasi, faqat alarm | "Uxlash" |

Klassifikatsiya AI orqali avtomatik bo'ladi, foydalanuvchi qo'lda ham o'zgartira oladi.

### 10.4 OAuth scope'lari

```kotlin
// Google
val SCOPES = listOf(
    "https://www.googleapis.com/auth/calendar.readonly",
    "https://www.googleapis.com/auth/calendar.events.readonly"
)
// Faqat o'qish — Mentor calendar'ga yozmaydi (V2'da yozish ham bo'lishi mumkin)
```

---

## 11. Lock Screen & Always-on Timer

### 11.1 Android: persistent timer notification

- `MediaStyle` notification + `setOngoing(true)` + `lockscreenVisibility = VISIBILITY_PUBLIC`
- Foydalanuvchi swipe qila olmaydi (`FLAG_NO_CLEAR`)
- Tizim chronometer bilan o'zi yangilaydi (`setUsesChronometer(true)`)
- Ovozsiz update'lar (`setOnlyAlertOnce(true)`) — battery saqlash

### 11.2 Android: sessiya boshlanish vaqtida majburiy uyg'otish

```
ScheduledSession.startAt vaqti → AlarmManager.setExactAndAllowWhileIdle()
                                       ↓
                              SessionStartReceiver
                                       ↓
                          PowerManager.WakeLock (FULL_WAKE_LOCK)
                                       ↓
                  SessionStartActivity (setShowWhenLocked + setTurnScreenOn)
                                       ↓
                            TTSEngine.speak("Vaqt keldi.")
```

### 11.3 iOS: Live Activities

- `ActivityKit` — Lock Screen va Dynamic Island'da timer
- `pushType: .token` — server tomonidan update'lash
- `staleDate` orqali avtomatik tugatish
- Sessiya tugagandan keyin `.dismissalPolicy: .immediate`

### 11.4 iOS: sessiya boshlanish notification

iOS'da Android'dagi WakeLock/Activity-from-background imkoniyati yo'q. Yagona yo'l:
- **Time-sensitive notification** (`UNNotificationInterruptionLevel.timeSensitive`)
- Foydalanuvchi notification'ni ko'rib, ilovani o'zi ochadi
- Shielding `DeviceActivitySchedule` orqali avtomatik yoqiladi (foydalanuvchi aralashuvisiz)

---

## 12. Build & deployment

### Build pipeline:
```
1. Rust core build (cargo + cargo-ndk) → libmentor.so (4 ABI)
2. Android build (Gradle 8.x + Kotlin 2.x)
3. Lint + Detekt
4. Unit tests
5. Instrumented tests (Firebase Test Lab)
6. AAB → Play Console (internal track)
```

### Release tracks:
- **Internal** — jamoa testlari
- **Closed beta** — 50–100 ishonchli foydalanuvchi (red-team)
- **Open beta** — 1,000 foydalanuvchi
- **Production** — global

### Versiyalash:
- SemVer: `MAJOR.MINOR.PATCH`
- Har bir release'da `CHANGELOG.md` yangilanadi
- Feature flag'lar (Firebase Remote Config) — yangi xususiyatlarni bosqichma-bosqich ochish
