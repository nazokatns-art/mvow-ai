# M-VoW AI v2 — Domen modeli va state machines

**Hujjat versiyasi:** 2.0
**Sana:** 2026-05-10

---

## 1. Domen tahlili

M-VoW AI — bu **vaqt-va'da** tizimi. Asosiy domen tushunchalari:

```
User ── makes ──▶ Vow (commitment)
                    │
                    ▼
              DayPlan (tizim qura oladi)
                    │
                    ├──▶ Task (bajariladigan birlik)
                    │      │
                    │      ├── Pomodoro cycles (ixtiyoriy)
                    │      ├── BlockedApps (vazifa davomida)
                    │      └── TransitionDialog (oxirida)
                    │
                    └──▶ SleepWindow (uxlash oynasi)
```

---

## 2. Asosiy entity'lar (Room sxemasi)

### 2.1 `User` — ilova foydalanuvchisi

```kotlin
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,                  // UUID
    val displayName: String,
    val createdAtMs: Long,
    val languageCode: String,                    // "en", "uz", "ru"...
    val timezoneId: String,                      // "Asia/Tashkent"
    val tier: String,                            // FREE | PRO | FAMILY | LIFETIME
    val parentLinkId: String? = null,            // Family Link uchun
    val streakInsuranceAvailableAt: Long? = null // Cooldown timestamp
)
```

### 2.2 `Vow` — bosh va'da

```kotlin
@Entity(tableName = "vows")
data class VowEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val text: String,                            // "Imtihonga tayyorgarlik"
    val voiceRecordingPath: String?,             // O'z ovozi
    val createdAtMs: Long,
    val isActive: Boolean = true,
    val totalDurationMs: Long? = null            // Belgilangan vaqt (ixtiyoriy)
)
```

### 2.3 `DayPlan` — kunning rejasi

```kotlin
@Entity(tableName = "day_plans")
data class DayPlanEntity(
    @PrimaryKey val date: String,                // "2026-05-11"
    val userId: String,
    val state: DayPlanState,                     // PLANNING | ACTIVE | COMPLETED | ABANDONED
    val wakeUpAtEpochMs: Long,                   // 06:00
    val sleepAtEpochMs: Long,                    // 22:00
    val planConfirmedAtMs: Long?,
    val firstTaskStartedAtMs: Long?,
    val lastTaskEndedAtMs: Long?,
    val totalFocusMs: Long = 0,
    val totalInterceptCount: Int = 0,
    val totalOverrideCount: Int = 0,
    val totalPostponementCount: Int = 0,
    val disciplineScore: Int = 0                 // 0-100
)

enum class DayPlanState {
    PLANNING,        // AI bilan suhbat davom etmoqda
    ACTIVE,          // Vazifalar bajarilmoqda
    COMPLETED,       // Hammasi tugadi, uxlash vaqti
    ABANDONED        // Foydalanuvchi tashlab ketdi
}
```

### 2.4 `Task` — bajariladigan birlik

```kotlin
@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val dayPlanDate: String,                     // FK → DayPlan.date
    val orderIndex: Int,                         // Ketma-ketlik
    val title: String,                           // "Uy uborka"
    val description: String?,                    // AI'dan kengaytirilgan
    val category: String,                        // worship | study | physical | work | family | sleep | generic
    val severity: String,                        // MAX | HIGH | MEDIUM | LOW

    val plannedStartAtMs: Long,
    val plannedDurationMs: Long,
    val actualStartedAtMs: Long? = null,
    val actualEndedAtMs: Long? = null,

    val technique: String,                       // NONE | POMODORO_25_5 | DEEP_FOCUS | CUSTOM
    val techniqueConfigJson: String? = null,     // Pomodoro cycle config

    val blockedPackagesCsv: String,              // ',' bilan ajratilgan
    val whitelistedPackagesCsv: String,          // Kategoriyaga ko'ra ruxsat

    val state: TaskState,
    val extensionCount: Int = 0,
    val extensionMinutes: Int = 0,
    val transitionDialogResult: String? = null,  // DONE | EXTENDED | SKIPPED
    val skipReason: String? = null
)

enum class TaskState {
    PENDING,         // Hali boshlanmagan
    ACTIVE,          // Hozir bajarilmoqda
    WARNING,         // 5 daq qoldi
    TRANSITIONING,   // Tugash dialogi
    COMPLETED,       // Bajarildi
    EXTENDED,        // Vaqt qo'shildi
    SKIPPED          // O'tkazib yuborildi
}
```

### 2.5 `PomodoroCycle` — Pomodoro siklasi

```kotlin
@Entity(tableName = "pomodoro_cycles")
data class PomodoroCycleEntity(
    @PrimaryKey val id: String,
    val taskId: String,                          // FK → Task
    val cycleIndex: Int,                         // 1, 2, 3, 4
    val phase: String,                           // WORK | BREAK | LONG_BREAK
    val startedAtMs: Long,
    val endedAtMs: Long?,
    val plannedDurationMs: Long,
    val state: String                            // ACTIVE | COMPLETED | INTERRUPTED
)
```

### 2.6 `WakeUpAttempt` — uyg'otish urinishi

```kotlin
@Entity(tableName = "wake_up_attempts")
data class WakeUpAttemptEntity(
    @PrimaryKey val id: String,
    val dayPlanDate: String,
    val triggeredAtMs: Long,
    val resolvedAtMs: Long?,
    val resolutionType: String,                  // DISMISSED | SNOOZED | TIMEOUT
    val signalsRequired: Int,                    // Default 3
    val signalsCompleted: Int,
    val mathChallengeAttempts: Int,
    val tiltDetected: Boolean,
    val voiceCommitmentMatched: Boolean,
    val durationMs: Long                         // Qancha tinmadi
)
```

### 2.7 `NegotiationLog` — negotiation tarixi

```kotlin
@Entity(tableName = "negotiation_logs")
data class NegotiationLogEntity(
    @PrimaryKey val id: String,
    val taskId: String,
    val triggeredAtMs: Long,
    val spokenReasonText: String?,               // Sanitized
    val llmCategoryClassified: String?,
    val llmConfidence: Double?,
    val fsmDecision: String,                     // GRANT | DENY_HARD | DENY_SOFT
    val grantedMinutes: Int? = null,
    val streakPenalty: Boolean = false,
    val mentorResponseText: String?              // AI gapdi
)
```

### 2.8 `DailyStats` — kunlik snapshot (mavjud, kengaytirilgan)

```kotlin
@Entity(tableName = "daily_stats")
data class DailyStatsEntity(
    @PrimaryKey val date: String,
    val userId: String,
    val sessionsCompleted: Int = 0,
    val sessionsAbandoned: Int = 0,
    val tasksCompleted: Int = 0,
    val tasksSkipped: Int = 0,
    val totalIntercepts: Int = 0,
    val totalOverridesGranted: Int = 0,
    val totalFocusMinutes: Int = 0,
    val wakeUpAttempts: Int = 0,
    val wakeUpFirstTrySuccess: Boolean = false,
    val sleepReachedOnTime: Boolean = false,
    val streakBroken: Boolean = false,
    val disciplineScore: Int = 0                 // Daily aggregate
)
```

### 2.9 `FamilyLink` — ota-ona pairing

```kotlin
@Entity(tableName = "family_links")
data class FamilyLinkEntity(
    @PrimaryKey val id: String,
    val parentUserId: String,
    val childUserId: String,
    val pairedAtMs: Long,
    val pairingCode: String,                     // QR kod
    val isActive: Boolean = true,
    val parentPermissions: String                // JSON: array of permission keys
)
```

### 2.10 `RemoteEvent` — Family Link real-time event'lar

```kotlin
@Entity(tableName = "remote_events")
data class RemoteEventEntity(
    @PrimaryKey val id: String,
    val familyLinkId: String,
    val type: String,                            // INTERCEPT | OVERRIDE | TASK_DONE | WAKE_UP | SLEEP
    val payloadJson: String,
    val createdAtMs: Long,
    val deliveredToParent: Boolean = false
)
```

---

## 3. State machines

### 3.1 `DayDriverFsm` — kunning to'liq state machine'i

Bu — **M-VoW AI'ning yuragi**. Butun kun shu FSM ichida boshqariladi.

```
                   ┌───────────────────┐
                   │       IDLE        │ ◄────── App start, yo'q reja
                   └─────────┬─────────┘
                             │ wakeUpTime - 1 day
                             ▼
                   ┌───────────────────┐
                   │     SCHEDULED     │
                   └─────────┬─────────┘
                             │ wakeUpTime hit
                             ▼
                   ┌───────────────────┐
                   │  WAKE_UP_RINGING  │ ←── Force alarm
                   └─────────┬─────────┘
                             │
              ┌──────────────┼──────────────┐
              │ dismissed    │ snoozed      │ timeout 30min
              ▼              ▼              ▼
         ┌────────┐    ┌──────────┐   ┌─────────────┐
         │PLANNING│    │SNOOZE_5  │   │ ESCALATING  │
         └────┬───┘    └────┬─────┘   └──────┬──────┘
              │             │ time             │
              │             ▼                  │
              │       (back to RINGING)        │
              │                                │
              │ AI dialog complete             │
              ▼                                │
         ┌────────┐                            │
         │CONFIRM │                            │
         └────┬───┘                            │
              │                                │
              ▼                                │
         ┌────────┐                            │
         │ ACTIVE │ ◄──────────────────────────┘ (from escalating)
         └────┬───┘
              │ (loops through tasks)
              │
              ▼
         ┌──────────────────────┐
         │ EVENING_WIND_DOWN    │ ◄── 17:00 onwards
         └──────────┬───────────┘
                    │ 22:00
                    ▼
         ┌──────────────────────┐
         │ SLEEP_LOCKDOWN       │
         └──────────┬───────────┘
                    │ next day 06:00
                    ▼
              (back to WAKE_UP_RINGING)
```

#### Transition jadvali

| Joriy state | Hodisa | Yangi state | Side effect |
|---|---|---|---|
| `IDLE` | Onboarding tugadi | `SCHEDULED` | AlarmManager: wake-up sched |
| `SCHEDULED` | wakeUpTime hit | `WAKE_UP_RINGING` | WakeUpAlarmService start |
| `WAKE_UP_RINGING` | User dismissed (signals✓) | `PLANNING` | Open MorningPlanActivity |
| `WAKE_UP_RINGING` | Snooze pressed | `SNOOZE_5` | 5min timer, max 1 |
| `WAKE_UP_RINGING` | 30min no response | `ESCALATING` | Increase volume + extra signals |
| `SNOOZE_5` | 5min elapsed | `WAKE_UP_RINGING` | Re-trigger alarm |
| `PLANNING` | AI plan confirmed | `CONFIRM` | Save DayPlan + sched all tasks |
| `CONFIRM` | First task time hit | `ACTIVE` | TaskFsm starts |
| `ACTIVE` | All tasks done | `EVENING_WIND_DOWN` | Display warm tones |
| `EVENING_WIND_DOWN` | sleepTime hit | `SLEEP_LOCKDOWN` | Hard block all social |
| `SLEEP_LOCKDOWN` | next wakeUpTime | `WAKE_UP_RINGING` | Loop |

### 3.2 `TaskFsm` — har bir vazifa state machine'i

```
[PENDING] ──start_time──▶ [ACTIVE]
                            │
                  ┌─────────┼─────────┐
                  │         │         │
            5min  │   user  │  timer  │
            before│ override│  ends   │
                  ▼         ▼         ▼
            [WARNING]  [NEGOTIATION] [TRANSITIONING]
                  │         │              │
                  │  decision│              │
                  │  ▼       ▼              │
                  │ [ACTIVE]  [HARD_DENY]   │
                  │ extended  │              │
                  │           ▼              │
                  └────►  [ACTIVE]          │
                                            │
                          ┌─────────┼─────────┐
                          │         │         │
                       DONE      EXTEND    SKIP
                          │         │         │
                          ▼         ▼         ▼
                    [COMPLETED] [ACTIVE]  [SKIPPED]
                                  │
                                  └──▶ next task
```

### 3.3 `PomodoroFsm` — Pomodoro siklasining state'i

```
[CYCLE_WORK_25] ──25min──▶ [CYCLE_BREAK_5] ──5min──▶
                                                    │
                          (cycle 1 of 4)            │
                                                    │
                                ▼                   │
[CYCLE_WORK_25] ──25min──▶ [CYCLE_BREAK_5] ──5min──▶
                                                    │
                          (cycle 2 of 4)            │
                                                    │
                                ▼                   │
[CYCLE_WORK_25] ──25min──▶ [CYCLE_BREAK_5] ──5min──▶
                                                    │
                          (cycle 3 of 4)            │
                                                    │
                                ▼                   │
[CYCLE_WORK_25] ──25min──▶ [CYCLE_LONG_BREAK_30]
                                                    │
                          (cycle 4 of 4)            │
                                                    │
                                ▼
                          [TASK_DONE] (parent TaskFsm'ga)
```

### 3.4 `NegotiationFsm` — mavjud, kengaytirilgan

```
[OPENED]
   │
   ├─ pre_check_quota_exceeded ──▶ [HARD_DENY] (quota)
   ├─ pre_check_too_early ──▶ [SOFT_DENY] (time)
   ├─ pre_check_high_risk ──▶ [REQUIRE_VOCAL_COMMITMENT]
   │                                    │
   │                              ┌─────┴─────┐
   │                              │ matched   │ failed
   │                              ▼           ▼
   │                          [GRANT]    [HARD_DENY]
   │
   └─ allow_llm ──▶ [LISTENING] (STT)
                       │
                       ▼
                   [LLM_CONSULTING]
                       │
                       ▼
                   [LLM_RECOMMENDED]
                       │
                  ┌────┴────┬────────┐
                  │         │        │
              GRANT      DENY     EXTEND
                  │         │        │
                  ▼         ▼        ▼
             [APPLYING]  [SPOKEN]  [APPLYING]
                  │                  │
                  └────┬─────────────┘
                       ▼
                    [DONE] → close screen
```

### 3.5 `WakeUpFsm` — uyg'otish ichidagi state

```
[ALARM_TRIGGERED]
       │
       │ start ringing + TTS + vibrate
       ▼
[WAITING_SIGNALS]
       │
       │ signals collected:
       │  - tilt_detected (accelerometer)
       │  - math_solved
       │  - awake_pressed
       │
       ├─ all 3 met ────▶ [DISMISSED]
       │
       ├─ snooze pressed ─▶ [SNOOZED] ──5min──▶ [ALARM_TRIGGERED]
       │
       └─ 60s no input ──▶ [ESCALATED]
                            │
                            │ +10dB volume
                            │ extra: shake required
                            │
                            ▼
                       [WAITING_SIGNALS] (round 2)
```

---

## 4. Backend API kontrakti

### 4.1 Auth endpoints

```
POST   /v1/auth/register       (anonymous device-based)
POST   /v1/auth/login          (Apple/Google sign-in)
POST   /v1/auth/refresh
POST   /v1/auth/logout
DELETE /v1/auth/account        (GDPR delete)
```

### 4.2 Plan endpoints

```
GET    /v1/plans/today
POST   /v1/plans                       (create from AI dialog)
PATCH  /v1/plans/{date}                (edit)
DELETE /v1/plans/{date}                (rare)

POST   /v1/plans/{date}/tasks/{id}/start
POST   /v1/plans/{date}/tasks/{id}/complete
POST   /v1/plans/{date}/tasks/{id}/extend
POST   /v1/plans/{date}/tasks/{id}/skip
```

### 4.3 AI endpoints

```
POST   /v1/ai/morning-plan-dialog      (Sonnet 4.6 streaming)
POST   /v1/ai/negotiate                (Haiku 4.5 + tool use)
POST   /v1/ai/categorize-task          (Haiku 4.5)
POST   /v1/ai/sleep-fact               (cached static + Sonnet)
GET    /v1/ai/daily-review             (Sonnet 4.6)
```

### 4.4 Family Link endpoints

```
POST   /v1/family/pair                 (parent → QR)
POST   /v1/family/accept-pair          (child enters code)
GET    /v1/family/children             (list of paired children)
GET    /v1/family/children/{id}/dashboard
POST   /v1/family/children/{id}/grant-extra-time
POST   /v1/family/children/{id}/add-task

WS     /v1/family/events               (real-time event stream)
```

### 4.5 Stats endpoints

```
GET    /v1/stats/streak
GET    /v1/stats/daily/{date}
GET    /v1/stats/weekly
GET    /v1/stats/monthly
POST   /v1/stats/insurance-recover     (1/month)
```

---

## 5. Sync strategy (offline-first)

### 5.1 Local-first architecture

```
[Compose UI]
     │
     ▼
[ViewModel]
     │
     ▼
[Repository] ──reads──▶ [Local Room]
     │
     ├─ writes ──▶ [Local Room] (immediate)
     │              │
     │              ▼
     │        [SyncWorker] (delayed, retry)
     │              │
     │              ▼
     │        [Backend API]
```

### 5.2 Conflict resolution

| Conflict | Resolution |
|---|---|
| Local task done, server says active | Local wins (user authority) |
| Server has new task (from family parent) | Server wins, push to local |
| Streak count mismatch | Server wins (anti-cheat) |
| Plan modified offline + online | Last-write-wins, log conflict |

### 5.3 Critical events (immediate sync)

- Wake-up triggered/dismissed
- Task completed/abandoned
- Override granted (security audit)
- Streak broken
- Family events

These events use `Priority.HIGH` in WorkManager + retry with exponential backoff.

---

## 6. Security & integrity

### 6.1 Anti-tampering layers

| Layer | Mechanism |
|---|---|
| API key | Server proxy in production (BuildConfig only for dev) |
| Token grants | Server-signed HMAC tokens (Sprint anti-tamper) |
| Streak | Server-canonical, local is cache |
| Time clock | Server-anchored offset (prevent local clock cheats) |
| Database | EncryptedSharedPreferences + SQLCipher (V2.5) |
| Voice recordings | Local only, optional encrypted upload |

### 6.2 Threat model

**Attacker = the user themselves** in a moment of weakness.

| Threat | Mitigation |
|---|---|
| Disable Mentor in Settings | AccessibilityService bypass-trap |
| Force-stop the app | WorkManager re-arms; streak penalty applied |
| Uninstall and reinstall | Account-bound streak; loss preserved |
| Clock manipulation | Server time anchor |
| ADB / root manipulation | Out of scope (acknowledge limit) |
| APK modification | Future: signature verification, attestation |
| Prompt injection via STT | Sanitization layer + tool-use only LLM |

### 6.3 Privacy

- Voice recordings: local-only by default
- Goal text: encrypted at rest
- Family Link: zero-knowledge — even VOW server can't see child's content
- Analytics: PostHog self-hosted, no PII
- GDPR compliance: full export + delete

---

## 7. Performance budgets

### 7.1 Hot paths

| Path | Budget |
|---|---|
| App cold start → Hard Lock visible | 1.5s P95 |
| AccessibilityEvent → overlay show | 50ms P95 |
| Mic press → STT first partial | 800ms P95 |
| LLM stream first token | 400ms P95 |
| Database query (Room) | 16ms (no UI block) |
| Compose recomposition | 16ms (60fps) |

### 7.2 Memory budget

| Component | Target |
|---|---|
| App heap (idle) | < 80MB |
| App heap (active session) | < 150MB |
| App heap (negotiation) | < 200MB |
| Native (Rust core) | < 20MB |
| TTS engine | < 30MB |

### 7.3 Battery budget (per day)

| Component | Cost |
|---|---|
| Always-on overlay (16h active) | 4% |
| AI calls (avg 5-10/day) | 1% |
| Foreground service | 1% |
| TTS playback | 0.5% |
| Wake-up alarm + planning | 0.5% |
| **Total budget** | **< 7%** |

---

## 8. Database migration strategy

### 8.1 Versioning

```
v1: Initial (v1 days — sessions only)
v2: + scheduled_sessions
v3: + daily_stats
v4: VOW v2 migration — add day_plans, tasks, pomodoro_cycles, vows, users
v5: + wake_up_attempts, family_links, remote_events
```

### 8.2 Migration policy

- Development phase (now): `fallbackToDestructiveMigration(true)` — quick iteration
- Beta phase: explicit `Migration` classes for v3 → v4
- Production: never destructive, schema export enabled

### 8.3 v3 → v4 migration

```kotlin
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Create users
        db.execSQL("""
            CREATE TABLE users (
                id TEXT PRIMARY KEY NOT NULL,
                displayName TEXT NOT NULL,
                createdAtMs INTEGER NOT NULL,
                languageCode TEXT NOT NULL,
                timezoneId TEXT NOT NULL,
                tier TEXT NOT NULL,
                parentLinkId TEXT,
                streakInsuranceAvailableAt INTEGER
            )
        """.trimIndent())

        // Create vows
        db.execSQL(...)

        // Create day_plans
        db.execSQL(...)

        // Create tasks (replace scheduled_sessions semantics)
        db.execSQL(...)

        // Migrate scheduled_sessions → tasks
        db.execSQL("""
            INSERT INTO tasks (...)
            SELECT ... FROM scheduled_sessions
        """.trimIndent())

        // Drop old tables (or keep for one version as backup)
    }
}
```

---

## 9. Test strategy

### 9.1 Unit tests (target: > 80% coverage)

- Every state machine — exhaustive transition tests
- Every Fsm decision — table-driven test cases
- Pomodoro timing math
- Streak calculation
- Time wall-clock accuracy
- LLM parser robustness (malformed JSON)

### 9.2 Integration tests

- Room migration v1 → v4
- WorkManager periodic execution
- AlarmManager exact firing
- AccessibilityService event chain → overlay

### 9.3 End-to-end (Maestro)

- Full onboarding flow
- Wake-up → planning → task active → transition → next → sleep
- Negotiation grant/deny paths
- Force-stop recovery
- Multi-language switching

### 9.4 Manual / red-team (quarterly)

- Bypass attempts on top 5 vendors (Samsung, Xiaomi, Huawei, Pixel, OnePlus)
- Battery drain test (24h)
- Memory leak (LeakCanary)
- ANR detection (StrictMode)
- Adversarial prompt injection
