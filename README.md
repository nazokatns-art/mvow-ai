# M-VoW AI

> **INTIZOM Platform** — *"You made a vow. We keep it."*

A Day-Driver mobile app that takes over the user's full day to enforce discipline:
forced wake-up, AI-driven planning, always-on focus timer, hard app blocking,
sleep training, and a stoic AI mentor who confronts the user in their own voice.

---

## Vision

Most productivity apps are *tools* — you use them. **M-VoW** is a *system* — it uses you.
The app is your morning-self's voice, hired to override your weak-moment self.

Read the full spec: [docs/v2/](docs/v2/)

---

## Status

- ✅ v1 implementation: onboarding, hard-block overlay, AI negotiation (Anthropic Claude), 6-language i18n, streak system, anti-tamper foundation
- 🔄 v2 in progress: Day-Driver state machine, force wake-up, always-on overlay, morning AI plan dialog, sleep training
- 📅 Open Beta target: **2026-10-04**

---

## Tech stack

| Layer | Tech |
|---|---|
| Android | Kotlin 2.1, Jetpack Compose, Hilt, Room, WorkManager |
| iOS (V2.5) | Swift, SwiftUI, FamilyControls, ActivityKit |
| Backend | Cloudflare Workers + D1 |
| AI | Anthropic Claude (Haiku 4.5 + Sonnet 4.6) |
| Voice | Android TTS/STT (free), ElevenLabs (Pro) |

---

## Getting started

### Android

```bash
cd android
# Open in Android Studio (Ladybug 2024.2.1+)
# Set your Anthropic API key in local.properties:
echo "ANTHROPIC_API_KEY=sk-ant-..." > local.properties
./gradlew assembleDebug
```

Min SDK: 29 (Android 10). Target SDK: 35.

---

## Languages supported

🇬🇧 English · 🇺🇿 O'zbekcha · 🇷🇺 Русский · 🇪🇸 Español · 🇸🇦 العربية · 🇹🇷 Türkçe

---

## Documentation

- **[docs/v2/SPEC.md](docs/v2/SPEC.md)** — Full product specification
- **[docs/v2/DESIGN.md](docs/v2/DESIGN.md)** — Brand identity, design system, screen specs
- **[docs/v2/DOMAIN.md](docs/v2/DOMAIN.md)** — Data models, state machines, API
- **[docs/v2/PLATFORMS.md](docs/v2/PLATFORMS.md)** — Per-platform capabilities
- **[docs/v2/ROADMAP.md](docs/v2/ROADMAP.md)** — 22-week build plan

---

## License

Proprietary — all rights reserved. Source available for review.

🤖 Built with [Claude Code](https://claude.com/claude-code)
