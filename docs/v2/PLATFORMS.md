# M-VoW AI v2 — Platform-spetsifik amaliyot

**Hujjat versiyasi:** 2.0
**Sana:** 2026-05-10
**Maqsad:** Har platforma'da nima ishlaydi, nima ishlamaydi, qanday yo'l bilan ishlatamiz — **ochiq ravishda**.

---

## 1. Platform matritsasi

| Funksiya | Android | iOS | iPad | Android TV | tvOS | Smart TV | Web | watchOS |
|---|---|---|---|---|---|---|---|---|
| F1 Force Wake-Up | ✅ | ⚠️ | ⚠️ | ❌ | ❌ | ❌ | ❌ | ⚠️ |
| F2 Plan Dialog | ✅ | ✅ | ✅ | ❌ | ❌ | ❌ | ✅ | ❌ |
| F3 Always-On Overlay | ✅ | ⚠️ | ⚠️ | ⚠️ | ❌ | ❌ | ❌ | ⚠️ |
| F4 Hard Lock | ✅ | ⚠️ | ⚠️ | ✅ | ❌ | ❌ | ❌ | ❌ |
| F5 Task Chains | ✅ | ✅ | ✅ | ✅ (display) | ❌ | ❌ | ✅ | ✅ |
| F6 Pomodoro | ✅ | ✅ | ✅ | ✅ (display) | ❌ | ❌ | ✅ | ✅ |
| F7 Sleep Training | ✅ | ✅ | ✅ | ❌ | ❌ | ❌ | ⚠️ | ❌ |
| F8 Hard Block | ✅ | ⚠️ | ⚠️ | ⚠️ | ❌ | ❌ | ❌ | ❌ |
| F9 AI Negotiation | ✅ | ✅ | ✅ | ⚠️ | ❌ | ❌ | ✅ | ⚠️ |
| F10 Streak | ✅ | ✅ | ✅ | ✅ (display) | ✅ (display) | ✅ (display) | ✅ | ✅ |
| F11 Family Control | ✅ | ✅ | ✅ | ✅ (display) | ❌ | ❌ | ✅ | ⚠️ |
| F12 Multi-language | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |

**Belgilar:**
- ✅ To'liq ishlaydi
- ⚠️ Qisman / cheklangan
- ❌ Mumkin emas (platform sandbox)

---

## 2. Android — birinchi va asosiy platform

### 2.1 Min talablar
- **API level:** 29 (Android 10)
- **Target:** 35 (Android 15)
- **Recommended:** API 33+ (Android 13+) — to'liq imkoniyatlar

### 2.2 Yadro permission'lar

```xml
<!-- Network -->
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>

<!-- Wake-up & alarm -->
<uses-permission android:name="android.permission.WAKE_LOCK"/>
<uses-permission android:name="android.permission.DISABLE_KEYGUARD"/>
<uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM"/>
<uses-permission android:name="android.permission.USE_EXACT_ALARM"/>
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
<uses-permission android:name="android.permission.VIBRATE"/>

<!-- Foreground & overlay -->
<uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_SPECIAL_USE"/>
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>

<!-- App monitoring -->
<uses-permission android:name="android.permission.PACKAGE_USAGE_STATS"
    tools:ignore="ProtectedPermissions"/>
<uses-permission android:name="android.permission.QUERY_ALL_PACKAGES"
    tools:ignore="QueryAllPackagesPermission"/>

<!-- Voice -->
<uses-permission android:name="android.permission.RECORD_AUDIO"/>

<!-- Notifications -->
<uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>

<!-- Sensors (force wake-up) -->
<uses-permission android:name="android.permission.HIGH_SAMPLING_RATE_SENSORS"/>

<!-- Family Link push -->
<uses-permission android:name="com.google.android.c2dm.permission.RECEIVE"/>
```

### 2.3 Force Wake-Up implementatsiyasi (Android)

**Strategiya: maksimal qarshilik:**

```kotlin
class WakeUpAlarmService : LifecycleService() {

    override fun onStartCommand(...): Int {
        // 1. Foreground notification (alarm category)
        startForeground(
            NOTIF_ID,
            NotificationCompat.Builder(this, CHANNEL_ALARM)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setFullScreenIntent(wakeUpActivityPi, true)  // Critical
                .setOngoing(true)
                .setSound(null)  // Sound managed by AudioManager
                .build()
        )

        // 2. Wake lock (FULL — display + CPU + screen on)
        wakeLock = pm.newWakeLock(
            PowerManager.FULL_WAKE_LOCK or
            PowerManager.ACQUIRE_CAUSES_WAKEUP or
            PowerManager.ON_AFTER_RELEASE,
            "VOW:WakeUp"
        )
        wakeLock.acquire(30 * 60_000L)

        // 3. Audio (alarm stream — bypasses DND)
        audioManager.setStreamVolume(
            AudioManager.STREAM_ALARM,
            audioManager.getStreamMaxVolume(STREAM_ALARM),
            0
        )
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build())
            setDataSource(this@WakeUpAlarmService, alarmSoundUri)
            isLooping = true
            prepare()
            start()
        }

        // 4. Vibration (continuous waveform)
        vibrator.vibrate(VibrationEffect.createWaveform(
            longArrayOf(0, 500, 200, 500, 200, 1000),
            0  // Loop indefinitely
        ))

        // 5. Launch full-screen activity
        startActivity(Intent(this, WakeUpActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })

        // 6. AI voice prompt
        scope.launch {
            ttsEngine.awaitReady()
            ttsEngine.speak(MentorPhrases.wakeUpQuestion(language))
        }

        // 7. Sensor monitoring (tilt detection)
        sensorManager.registerListener(tiltListener, accelerometer, SENSOR_DELAY_GAME)

        return START_STICKY
    }
}
```

**Bypass himoyalari:**
- Foydalanuvchi telefonni o'chirsa: BootReceiver alarm'ni qayta sozlaydi
- Foydalanuvchi force-stop qilsa: WorkManager re-schedules
- Foydalanuvchi DND yoqsa: alarm stream e'tibor bermaydi
- Foydalanuvchi volume tushirsa: setStreamVolume har 5 soniyada qayta o'rnatadi

**Cheklov:**
- Power button bilan ekranni o'chirishni to'sib bo'lmaydi (Android cheklovi)
- "Dismiss" foydalanuvchi tomonidan har doim mumkin (lekin biz signals talab qilamiz)

### 2.4 Always-On Overlay implementatsiyasi

**Ikki variant:**

```kotlin
class PersistentTimerOverlayService : LifecycleService() {

    private val screenStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                Intent.ACTION_USER_PRESENT -> showCornerWidget()
                Intent.ACTION_SCREEN_OFF -> showLockScreenWidget()
                Intent.ACTION_SCREEN_ON -> {
                    // Lock screen ham ko'rinadi, foydalanuvchi unlock qilmaguncha
                    if (keyguardManager.isKeyguardLocked) {
                        showLockScreenWidget()
                    }
                }
            }
        }
    }
}
```

**Lock screen widget:**
```kotlin
// LockScreenWidgetParams
WindowManager.LayoutParams(
    MATCH_PARENT, MATCH_PARENT,
    TYPE_APPLICATION_OVERLAY,
    FLAG_NOT_FOCUSABLE or
    FLAG_LAYOUT_IN_SCREEN or
    FLAG_SHOW_WHEN_LOCKED or
    FLAG_DISMISS_KEYGUARD,
    PixelFormat.TRANSLUCENT
)
```

**Corner widget (telefon ochiq):**
```kotlin
WindowManager.LayoutParams(
    120.dp, 64.dp,  // 1/6 screen width
    TYPE_APPLICATION_OVERLAY,
    FLAG_NOT_FOCUSABLE or
    FLAG_LAYOUT_IN_SCREEN or
    FLAG_LAYOUT_NO_LIMITS,
    PixelFormat.TRANSLUCENT
).apply {
    gravity = Gravity.TOP or Gravity.END
    x = 8.dp
    y = 64.dp  // Status bar pastida
}
```

### 2.5 Play Store talablari

**Permission justification (manual review):**
- AccessibilityService: "Real-time blocking of user-selected distracting apps during focus sessions, with explicit user consent."
- USE_EXACT_ALARM: "Exact wake-up alarm at user-configured time, equivalent to system clock."
- SYSTEM_ALERT_WINDOW: "Persistent timer widget visible during focus sessions."
- FOREGROUND_SERVICE_SPECIAL_USE: "Maintaining session state and timer accuracy during background."

**Submission checklist:**
- ✅ Privacy policy (auto-link)
- ✅ Permission rationale screen
- ✅ Demo video (3 min, demonstrating each permission's use)
- ✅ Crashlytics integrated
- ✅ Family Link policy compliance (if shipping family tier)

### 2.6 Vendor-specific (Top 5 muammoli)

| Vendor | Muammo | Yechim |
|---|---|---|
| **Samsung** | Aggressive battery optimization kills services | Settings: "Mentor: Don't optimize" guide |
| **Xiaomi/MIUI** | Background activity disabled by default | Onboarding step: enable "Auto-start" |
| **Huawei/EMUI** | "Protected apps" feature | Onboarding guide for Protected Apps list |
| **OPPO/ColorOS** | App freeze after 5 min idle | "Recent apps lock" + "Auto-launch" |
| **OnePlus** | Battery saver kills foreground services | "Battery → Don't optimize" |

M-VoW AI bunday qurilmalarda **diagnostic** sahifa ko'rsatadi — masalan: *"Samsung qurilmangiz Mentor'ni 4 soatdan keyin to'xtatadi. Sozlash uchun..."*

---

## 3. iOS — ikkinchi platform (V2.5)

### 3.1 iOS sandbox haqiqati

iOS — Android'dan **kuchsizroq** bizning vizyonimiz uchun. Apple foydalanuvchilarni tizim darajasidagi enforcement'dan himoya qiladi. Bizning yondashuv: **Apple imkoniyatlaridan** maksimal foydalanish.

### 3.2 Asosiy frameworklar

| Framework | Vazifa |
|---|---|
| `FamilyControls` | Authorization for shielding |
| `ManagedSettings` | Apply shields (block apps) |
| `DeviceActivity` | Schedule + monitor extensions |
| `ActivityKit` | Live Activities (Lock Screen) |
| `WidgetKit` | Home screen widgets |
| `AVSpeechSynthesizer` | TTS |
| `Speech` | STT |
| `Intents`/`AppIntents` | Siri integration |

### 3.3 Force Wake-Up — iOS cheklovi

iOS'da **bizning kabi force-wake mumkin emas**. Apple alarm ishlash mexanizmini sandbox'lab qo'ygan.

**Eng yaxshi muqobil:**

```swift
// 1. Time-sensitive notification
let content = UNMutableNotificationContent()
content.title = "VOW: Vaqt keldi"
content.body = "Nega turmayapsan? Bekorga yashashga rozimisan?"
content.sound = .defaultCritical  // Bypass silent mode (foydalanuvchi roziligi bilan)
content.interruptionLevel = .timeSensitive
content.categoryIdentifier = "WAKE_UP"

// 2. Critical alert (faqat health/safety apps — Apple ruxsati kerak)
content.sound = UNNotificationSound.defaultCritical(volume: 1.0)

// 3. Live Activity (Lock Screen takeover)
let attributes = WakeUpActivityAttributes()
let activity = try Activity.request(
    attributes: attributes,
    content: .init(state: state, staleDate: nil),
    pushType: .token
)

// 4. Trigger schedule with DeviceActivity
let schedule = DeviceActivitySchedule(
    intervalStart: wakeTimeComponents,
    intervalEnd: wakeTimeComponents + 30min,
    repeats: true
)
```

**Real natija:** Critical Alert + Live Activity + DeviceActivity bir vaqtda ishlasa, foydalanuvchi notification'ni dismiss qila olmaydi (notification interruption level "time-sensitive"), lekin **boshqa app'larga o'tib ketsa bo'ladi**. Bu — Apple cheklovi.

**VOW yondashuv:** iOS'da "wake-up" mahsulot funksiyasi sifatida **kuchsizroq** ekanligini ochiq aytamiz va boshqa Mentor funksiyalari bilan to'ldiramiz.

### 3.4 Always-On Overlay — iOS cheklovi

iOS'da **boshqa app ustida widget chiqarib bo'lmaydi**. Yagona yo'l:

- **Live Activity (iOS 16.1+)** — Lock Screen + Dynamic Island
- **Home Screen Widget (iOS 14+)** — home screen'da ko'rinadi (lekin app ustida emas)

Bu — *"chetdagi 1/6 widget"* falsafamizga **mos kelmaydi**. Lekin **Lock Screen** + **Dynamic Island** orqali foydalanuvchi har 1-2 daqiqada ko'radi.

```swift
struct VowSessionAttributes: ActivityAttributes {
    public struct ContentState: Codable, Hashable {
        var taskTitle: String
        var startAt: Date
        var endAt: Date
        var blockedApps: [String]
        var interceptCount: Int
    }
    var sessionId: String
}

// Widget'da:
LiveActivityWidget(for: VowSessionAttributes.self) { context in
    // Lock Screen view
    VStack {
        HStack {
            Image(systemName: "shield.fill").foregroundColor(.cyan)
            Text("VOW")
                .font(.system(.caption, design: .monospaced))
                .tracking(4)
            Spacer()
            Text(timerInterval: context.state.startAt...context.state.endAt,
                 countsDown: true)
                .font(.system(.title, design: .monospaced))
                .monospacedDigit()
        }
        Text(context.state.taskTitle.uppercased())
            .font(.headline)
            .foregroundColor(.cyan)
    }
}
```

### 3.5 Hard Block — iOS implementatsiyasi

**Ishlaydi**, lekin Apple-controlled tarzda:

```swift
// 1. Authorize (FamilyControls)
let center = AuthorizationCenter.shared
try await center.requestAuthorization(for: .individual)

// 2. Apply shield via ManagedSettingsStore
let store = ManagedSettingsStore(named: .vowSession)
store.shield.applications = blockedApps  // Set<ApplicationToken>
store.shield.applicationCategories = .specific(blockedCategories)
store.shield.webDomains = blockedDomains

// Shield rendered by SYSTEM, not our app — un-bypassable!
```

**Bypass mumkin emas chunki:**
- Shield UI — Apple system UI
- Killing VOW app dismiss qilmaydi shield'ni
- Foydalanuvchi VOW'ni uninstall qilsa, shield qoladi (Screen Time passcode bilan)

**Lekin:**
- Foydalanuvchi Screen Time passcode'ni biladi → shield bekor qila oladi
- Apple Family bo'limidan child mode'ni o'chirish mumkin

### 3.6 Apple App Store talablari

**FamilyControls entitlement:**
- Apple manual review: 4-8 hafta
- Justification: "Self-imposed parental controls" pozitsiyasi
- Yo'q: "We block apps you can't disable"
- Ha: "We help users implement self-control they configure themselves"

**Submission checklist:**
- ✅ Privacy policy (FamilyControls'ning special requirements)
- ✅ Demo video showing self-imposed nature
- ✅ Beta testing via TestFlight (50+ users, 14 days)
- ✅ App Store screenshots (5 ta minimum)
- ✅ App Privacy Details (declared data collection)

### 3.7 iOS development priorities

V2.5'da iOS — **read+write minimum viable**:
- ✅ Onboarding + plan dialog
- ✅ Live Activity (Lock Screen + Dynamic Island)
- ✅ FamilyControls shield
- ✅ AI negotiation (text + voice)
- ✅ Sleep training (notifications)
- ⚠️ Wake-up (kuchsizroq — Critical Alert)
- ❌ Always-on corner widget (mumkin emas)
- ❌ Force-stop prevention (mumkin emas)

---

## 4. iPad — iOS bilan bir xil + planshet UX

iPad ilovasi — iOS'ning bir xil kodbase'i, **iPad-spesifik UI**:
- Stage Manager qo'llab-quvvatlash
- Split View (VOW + boshqa app yonma-yon)
- Apple Pencil — drawing in plan dialog (kelajak)
- Larger Live Activity layout

---

## 5. Android TV — read-only display

### 5.1 Imkoniyatlar
- ✅ Kompozitor UI (TV layout)
- ✅ Timer ko'rsatish
- ✅ Streak dashboard
- ✅ Family Link parent dashboard
- ⚠️ Hard block (cheklangan)

### 5.2 Cheklovlar
- ❌ AccessibilityService cheklangan (TV apps)
- ❌ Overlay TV apps ustida ishlamaydi
- ❌ User input (mic) — faqat TV pultidagi mic
- ❌ Ovoz cheklangan — kelajak

### 5.3 TV use case

```
Smart TV ekrani:

┌──────────────────────────────────────────┐
│                                          │
│           VOW — Aziz                     │
│                                          │
│         ╭─────────────╮                  │
│        ╱   ╭──────╮    ╲                 │
│       │   │ 00:34:57 │   │              │
│        ╲   ╰──────╯    ╱                 │
│         ╰─────────────╯                  │
│                                          │
│       MATEMATIKA — POMODORO              │
│                                          │
│  ⬤ ⬤ ⬤ ○                                 │
│                                          │
│  Bugungi vazifalar:                      │
│  ✅ Yugurish                             │
│  ✅ Uborka                               │
│  ⏳ Matematika (joriy)                   │
│  ◯ Ovqat (15:00)                         │
│                                          │
└──────────────────────────────────────────┘
```

TV — **motivatsion display**, hech narsa bloklanmaydi.

---

## 6. tvOS (Apple TV) — mumkin emas

tvOS sandbox ushbu funksionallikni qo'llab-quvvatlamaydi:
- ❌ AccessibilityService yo'q
- ❌ FamilyControls yo'q
- ❌ Overlay API yo'q

tvOS faqat *games + media* uchun. **VOW tvOS app yo'q.**

---

## 7. Smart TV (Samsung Tizen, LG webOS)

### 7.1 Holat
- Tizen / webOS — web-based dasturlash
- ❌ Background process yo'q
- ❌ Boshqa app'lar bilan integratsiya yo'q
- ⚠️ Faqat: Samsung Health-style display app

### 7.2 Yechim
**VOW Web Display** — browser ilovasi (next section).

---

## 8. Web Companion (browser dashboard)

### 8.1 Maqsad
Foydalanuvchi computer/laptop oldida o'tirgan paytda, telefondagi VOW sessiyasi bilan **sinxron** ko'rinadigan dashboard.

### 8.2 Texnik

**Stack:**
- Compose Multiplatform Web (Jetpack Compose'ning brauzer porti) — kelajakda
- Hozircha: Next.js + React + Tailwind (tezroq prototype)

**Hosting:**
- Cloudflare Pages
- URL: `app.vow.ai` (foydalanuvchi panel)

**Real-time sync:**
- WebSocket → backend
- Telefon → server → web (har taymer tick'i, lekin throttled)

### 8.3 Imkoniyatlar
- ✅ Joriy taymer ko'rinishi
- ✅ Bugungi reja
- ✅ Streak grafiki
- ✅ AI bilan suhbat (text only)
- ✅ Family Dashboard (ota-ona uchun)
- ❌ Bloklash (browser'da mumkin emas)
- ❌ Wake-up

**Asosiy use case:** ish kuni davomida ish stoli ekranida fokus widget.

---

## 9. Apple Watch / Wear OS (V3)

### 9.1 Apple Watch
- ✅ Live Activity ko'rsatish (kelajakdagi watchOS bilan)
- ✅ Mini taymer complication (clock face'da)
- ✅ Tap → telefon'da ochish
- ⚠️ Wake-up: haptic + vibration (yumshoqroq)

### 9.2 Wear OS
- ✅ Mini complication
- ✅ Heart rate'ga asoslangan stress detection (kelajak)
- ❌ Bloklash mumkin emas

---

## 10. Cross-platform code sharing strategy

### 10.1 V2 yondashuvi

```
android/                          # Native Kotlin (asosiy)
├── app/
└── feature-*/

ios/                              # Native Swift (V2.5)
├── VowApp/
└── VowExtensions/

shared/                           # Kotlin Multiplatform (V3)
├── domain/                       # Pure logic — FSMs, models
├── network/                      # API client
└── data/                         # Repos (DB through expect/actual)

web/                              # Next.js (V3)
└── ...
```

### 10.2 Hozir KMP ishlatmaymiz, sabab:
- Android'ni tezroq shippeing qilamiz
- iOS uchun native Swift (FamilyControls — Kotlin'da yo'q)
- KMP'ga V3'da migration

### 10.3 Kelajakdagi shared logic:
- FSM domain logic (Pure Kotlin)
- Network layer (Ktor)
- Data models
- Validation rules
- Streak math

---

## 11. Backend deployment

### 11.1 Stack
- **Workers:** Cloudflare Workers (TypeScript)
- **DB:** Cloudflare D1 (SQLite)
- **Real-time:** Durable Objects + WebSockets
- **Cache:** Cloudflare KV
- **Push:** FCM (Android), APNs (iOS)

### 11.2 Deployment topologiyasi

```
[Mobile/Web client]
       │
       ├─ HTTPS ──▶ [Cloudflare Worker]
       │              │
       │              ├─ AI proxy ──▶ Anthropic API
       │              ├─ Auth ──▶ JWT
       │              ├─ DB ──▶ D1
       │              └─ Push ──▶ FCM/APNs
       │
       └─ WSS ──▶ [Durable Object]
                     │
                     └─ Family events broadcast
```

### 11.3 Region strategy
- Primary: Frankfurt (EU users)
- Secondary: Virginia (US users)
- Tashkent users: nearest EU edge (~30ms latency)

### 11.4 Cost estimate

| Component | Cost / month / user |
|---|---|
| Workers requests | $0.01 |
| D1 storage + queries | $0.005 |
| Anthropic API (avg 60 calls/oy) | $0.15 |
| Cloudflare R2 (voice files, optional) | $0.01 |
| **Total** | **~$0.18 / user / month** |

Premium narxi $4.99 → **gross margin ~95%**.

---

## 12. Tasdiqlash

Platform decisions:
- [ ] Android: native Kotlin (asosiy V2)
- [ ] iOS: native Swift (V2.5, FamilyControls bilan)
- [ ] iPad: iOS code'ning iPad UI
- [ ] Android TV: read-only display ilovasi
- [ ] tvOS: shippamaymiz
- [ ] Smart TV: shippamaymiz
- [ ] Web: V3'da
- [ ] Watch: V3'da

Backend:
- [ ] Cloudflare Workers + D1
- [ ] Anthropic API proxy
- [ ] Real-time via Durable Objects

Tasdiqlangach, [ROADMAP.md](ROADMAP.md) bo'yicha sprint planning.
