# VOW AI v2 — Texnik spetsifikatsiya

**INTIZOM Platform**

Bu papka — VOW AI'ning **to'liq texnik spetsifikatsiyasi**. v1 hujjatlari (`docs/PRD.md`, `docs/ARCHITECTURE.md`, `docs/ROADMAP.md`) endi **bekor qilingan** — barcha qarorlar shu yerdan kelib chiqadi.

---

## O'qish tartibi

1. **[SPEC.md](SPEC.md)** — Vizyon, foydalanuvchilar, 12 ta yadro funksiya, NFR'lar, monetizatsiya
2. **[DESIGN.md](DESIGN.md)** — Brand (VOW), design system, ekran spetsifikatsiyalari (mockup)
3. **[DOMAIN.md](DOMAIN.md)** — Room entity'lar, state machines, API kontraktlari, security
4. **[PLATFORMS.md](PLATFORMS.md)** — Android/iOS/TV/Web — har platforma'da nima ishlaydi
5. **[ROADMAP.md](ROADMAP.md)** — 22 haftalik sprint plan, milestone'lar

---

## Tezkor ko'rsatma

### Brand
- **Nom:** VOW AI (qisqartirilgan: VOW)
- **Tushunchasi:** *vow* = qasamyod, va'da, ahd
- **Tagline:** "You made a vow. We keep it."
- **AI ovozi:** Hali ham "Mentor" deb chaqiriladi (foydalanuvchi bilan suhbatda)

### Stack
- **Android:** Kotlin + Jetpack Compose + Hilt + Room
- **iOS (V2.5):** Swift + SwiftUI + FamilyControls
- **Backend:** Cloudflare Workers + D1
- **AI:** Anthropic Claude Haiku 4.5 + Sonnet 4.6
- **Voice:** Android TTS/STT (free), ElevenLabs (Premium)

### Falsafiy yadro
- **Kun-haydovchi (Day-Driver):** ilova foydalanuvchini boshqaradi, foydalanuvchi ishlatmaydi
- **VOW = qasamyod:** har ish — bu foydalanuvchining o'ziga bergan va'dasi
- **AI matn yozadi, FSM qaror qiladi:** prompt-injection-proof
- **Ichki ovoz tashqi ovozdan kuchli:** mentor — bu foydalanuvchining o'z ovozi

---

## Mavjud holat (2026-05-10)

### Tugatilgan
- ✅ v1 implementation (Sprint 1-4): onboarding, hard-block, AI negotiation, multi-language
- ✅ v2 spec (shu papka)

### Keyingi qadam
- 🔄 v1 → v2 migration (Sprint 1.1)
- 🔄 Brand rename: `uz.mentorai.focus` → `app.vowai`
- 🔄 DB v3 → v4 migration

---

## Qarorlar tarixi

| Sana | Qaror | Asos |
|---|---|---|
| 2026-05-09 | v1 boshlandi: session-based blocker | Initial spec |
| 2026-05-09 | Multi-language (6 til) | International app |
| 2026-05-10 | v2 pivot: Day-Driver model | Foydalanuvchi vizyon |
| 2026-05-10 | Rename: Mentor-AI → VOW AI | INTIZOM falsafasi |
| 2026-05-10 | Roadmap: 22 hafta to V2.0 beta | Realistic estimate |

---

## Hujjat o'zgarishlari

Hujjat versiyasi: 2.0 (initial v2 spec)
Keyingi rejalashtirilgan: V2.1 — beta launch'dan keyin
