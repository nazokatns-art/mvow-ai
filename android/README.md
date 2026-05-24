# Mentor-AI Android

## Loyihani ochish

1. **Android Studio Ladybug | 2024.2.1** yoki yangiroq
2. `File → Open` → `c:\Users\User\Desktop\Focus AI\android` papkasini tanlang
3. Gradle sync avtomatik boshlanadi (10–15 daqiqa birinchi marta)
4. Run configuration: `app`

## Talablar

- JDK 17+
- Android SDK 35
- Min SDK: 29 (Android 10)
- Kotlin 2.1.0
- Gradle 8.10.2 (wrapper avtomatik yuklab oladi)

## Joriy holat (Sprint 0)

- ✅ Loyiha skeleton'i
- ✅ Gradle Kotlin DSL + Version Catalog
- ✅ Compose + Hilt sozlamalari
- ✅ Manifest — barcha permissions
- ✅ AccessibilityService skeleton
- ✅ FocusGuardService skeleton
- ✅ Boot va SessionStart receiver'lar (skeleton)
- ✅ Dark Discipline theme
- ⏳ Onboarding flow — Sprint 1
- ⏳ Hard-block overlay — Sprint 2
- ⏳ Calendar sync — Sprint 2.5
- ⏳ AI mentor — Sprint 3

## Loyiha tuzilishi

```
app/src/main/java/uz/mentorai/focus/
├── MentorApplication.kt          # Hilt entry, notification channels
├── MainActivity.kt               # Asosiy launcher activity
├── MentorApp.kt                  # Compose root
├── ui/
│   ├── theme/Theme.kt            # Dark Discipline rang sxemasi
│   ├── onboarding/               # Sprint 1
│   └── session/
│       └── SessionStartActivity.kt  # Lock screen ustida ochiladi
├── guard/
│   ├── FocusGuardService.kt      # Foreground service
│   ├── AppMonitorAccessibility.kt # Real-time app monitoring
│   ├── BootReceiver.kt           # Reboot recovery
│   └── SessionStartReceiver.kt   # Alarm-driven session start
├── overlay/                      # Sprint 2 — hard-block overlay
├── agent/                        # Sprint 3 — AI mentor (TTS/STT)
├── data/                         # Sprint 1+ — Room DB, repos
└── di/                           # Hilt modullar
```

## Keyingi qadam

Sprint 1 — Permission funnel onboarding flow.
