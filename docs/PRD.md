# PRD — Mentor-AI: Focus & Discipline

**Hujjat versiyasi:** 0.1
**Sana:** 2026-05-09
**Holat:** Loyiha boshi — ko'rib chiqilmoqda

---

## 1. Mahsulot vizyoni

Foydalanuvchining o'zi tomonidan, o'zining yaxshi lahzasida, o'ziga qarshi qo'yilgan to'siq. Mentor-AI — bu telefonni ish quroli sifatida tiklab, dofamine quduqlariga aylangan ilovalardan jismoniy chegara o'rnatadigan tizim.

**Bir gapda:** *"Sen o'zingga so'z bergan eding. Men o'sha so'zni eslatib turuvchiman."*

---

## 2. Maqsadli foydalanuvchi (Persona)

### Asosiy persona: "Aqlli ammo iroda zaif" (90% bozor)
- **Yoshi:** 18–35
- **Kasbi:** Talaba, dasturchi, freelancer, yozuvchi, kreator
- **Muammo:** Bilim bor, motivatsiya bor, lekin Instagram/TikTok/YouTube quduqqa tortib turadi
- **Allaqachon sinab ko'rgan:** Pomodoro, Forest, Cold Turkey, oddiy app blocker'lar — ularning hammasi yumshoq, bekor qilish oson
- **Tayyor:** Pulli xizmat uchun pul to'lash, hatto qo'shimcha qattiqchilik uchun

### Ikkinchi persona: "Tiklanish bosqichidagi" (10% bozor)
- Ijtimoiy tarmoqlardan, qimor, pornografiyaga qaramlikdan chiqayotganlar
- Ehtiyoj: shafqatsiz to'siq, hatto telefonni qayta ishga tushirsa ham buzilmasligi kerak
- Tibbiy/psixologik kontekstda foydali

### Persona EMAS:
- Bolalar uchun ota-ona nazorati (bozor to'la, bizning ishimiz boshqa)
- Korporativ MDM (boshqa biznes-model)

---

## 3. Asosiy foydalanish stsenariylari (User Stories)

### US-1: Sessiya boshlash
> *Foydalanuvchi sifatida, men 90 daqiqalik chuqur ish sessiyasi boshlamoqchiman, va o'sha vaqt mobaynida Instagram, TikTok, YouTube'ga kira olmaydigan bo'lishim kerak.*

**Qabul mezonlari:**
- Foydalanuvchi maqsadini o'z so'zlari bilan yozadi (min 10 so'z)
- Maqsadini ovozi bilan ham o'qiydi (yozib olinadi)
- Bloklanadigan ilovalar ro'yxati ko'rsatiladi
- Sessiya boshlangach, "Stop" tugmasi yashirilgan/qiyinlashtirilgan

### US-2: Bloklangan ilovaga urinish
> *Sessiya davomida men Instagram'ni ochsam, ilova butun ekranni egallab, meni qaytarishi kerak.*

**Qabul mezonlari:**
- 50ms ichida overlay paydo bo'ladi
- Foydalanuvchining o'z maqsadi katta harf bilan ko'rsatiladi
- Mentor ovozi ("Stop. Sen 90 daqiqaga so'z bergan eding") chiqadi
- Faqat "Ishga qaytish" tugmasi balandligi ko'zga tashlanadi
- "5 daqiqa so'rash" tugmasi bor, lekin kichik va xira

### US-3: "5 daqiqa qo'shimcha" so'rash
> *Sessiya davomida charchadim va 5 daqiqa Instagram ko'rmoqchiman.*

**Qabul mezonlari:**
- AI mentor bilan ovozli muloqot ochiladi
- Foydalanuvchi sababini ovozi bilan aytishi kerak
- Mentor sababni tahlil qiladi (haqiqiymi, bahonami)
- Qaror serverda qabul qilinadi (clientda emas — buzib bo'lmasin)
- Agar berilsa: 2 daqiqa beriladi, "streak" 1 ga kamayadi
- Agar berilmasa: stoik javob, ish davom etadi

### US-4: Ilovani o'chirishga urinish
> *Sessiya davomida men Mentor-AI'ni o'chirib, telefonni "ozod qilmoqchiman."*

**Qabul mezonlari:**
- Sozlamalar > Ilovalar > Mentor-AI sahifasi ochilganda overlay paydo bo'ladi
- DeviceAdmin orqali o'chirish bloklanadi
- AccessibilityService o'chirilsa: ilova darhol bilib oladi va qattiq bildirishnoma chiqaradi
- Hisob (account) ulangan bo'lsa, qayta o'rnatishda ham streak yo'qolishi haqida ogohlantiriladi

### US-5: Ertalabki rejalashtirish
> *Har kuni ertalab Mentor men bilan bugungi maqsadlarni belgilaydi.*

**Qabul mezonlari:**
- Belgilangan vaqtda Mentor bildirishnoma yuboradi
- Foydalanuvchi 3 ta asosiy maqsad belgilaydi
- Har bir maqsad uchun sessiya rejalashtiriladi
- Kechqurun "shu maqsadlar bajarildimi?" deb so'raydi

---

## 4. Funktsional talablar (MVP)

### MVP scope (3 oy ichida ishlab chiqish):
- ✅ Sessiya boshlash/tugatish
- ✅ Ilovalarni qora ro'yxatga olish (manual selection)
- ✅ Hard-block overlay
- ✅ Foydalanuvchi maqsadini yozish + ovozli yozib olish
- ✅ Asosiy AI mentor (Haiku) — bloklash javobi
- ✅ "5 daqiqa" so'rovi flow
- ✅ Streak tracker
- ✅ AccessibilityService + DeviceAdmin
- ✅ Reboot'dan keyin tiklanish

### MVP'dan tashqari (keyingi versiyalar):
- ❌ iOS (V2)
- ❌ Accountability partner (V2)
- ❌ Web blocking (V3)
- ❌ Hisobotlar/analytics dashboard (V2)
- ❌ Premium ovozli mentor (V2)
- ❌ Statistika (V2)

---

## 5. Funktsional bo'lmagan talablar

| Talab | Maqsad |
|---|---|
| Overlay javob vaqti | < 50ms |
| AI javob vaqti (text) | < 400ms (streaming) |
| AI ovozli javob | < 1.5s tushish |
| Battery isteʼmoli | Kunlik foydalanishda < 5% |
| Sozlama yo'qolishi | Reboot, force-stop, low memory'da ham saqlanadi |
| Tilning qo'llab-quvvatlashi | O'zbek (asosiy), Rus, Ingliz |
| Eng past Android | Android 10 (API 29) |

---

## 6. Muvaffaqiyat metrikalari

### Mahsulot metrikalari (3 oydan keyin):
- **D1 retention:** > 60%
- **D7 retention:** > 35%
- **D30 retention:** > 20%
- **O'rtacha sessiya soni / kun:** > 2
- **Sessiya tugatish darajasi:** > 70% (boshlangan sessiyalardan)
- **Bypass urinishlari / sessiya:** monitoring uchun, qiymat MVP'dan keyin belgilanadi

### Biznes metrikalari (6 oydan keyin):
- 10,000 oylik faol foydalanuvchi
- 5% Premium konversiya
- NPS > 40

---

## 7. Risklar va tahdidlar

| Risk | Ehtimol | Taʼsir | Yechim |
|---|---|---|---|
| Play Store siyosati buzilishi | O'rta | Yuqori | Permission justification puxta tayyorlash, manual review uchun video tayyor turish |
| Foydalanuvchilar bypass yo'lini topishadi | Yuqori | O'rta | Quartal red-team review, har versiyada anti-bypass testing |
| AI'ni "jailbreak" qilib unblock olish | O'rta | Yuqori | FSM LLM'dan ustun — qaror serverda, prompt-injection'ga immunitet |
| Foydalanuvchi reklama qila olmaydi (jiddiy ilova) | O'rta | O'rta | Influencer marketing — productivity yutuber'lar orqali |
| iOS entitlement rad etilishi | O'rta | Yuqori | V2'da iOS, ehtimoliy reject uchun "shaxsiy parental control" pozitsiyasi |

---

## 8. Bozordagi raqobatchilar

| Raqobatchi | Kuchli tomoni | Zaif tomoni | Bizning afzalligimiz |
|---|---|---|---|
| **Forest** | Chiroyli, qulay | Yumshoq, kechiriladi | Qattiq blok + AI |
| **Cold Turkey** (desktop) | Mashhur, kuchli blok | Faqat desktop | Mobil + AI mentor |
| **Opal** (iOS) | Yaxshi UX | iOS only, AI yo'q | Android + mentor + Rus/O'zbek tili |
| **Jomo** | Yaxshi gamification | Zaif blok | Stoik tone + ovozli mentor |
| **Freedom** | Cross-platform | Statik, AI yo'q, qimmat | Dinamik AI agent |

**Differentsiya:** AI mentor + qattiq qatlamli blok + lokal til (O'zbek/Rus bozori bo'sh).

---

## 9. Monetizatsiya strategiyasi

**Model:** Freemium

### Free tier:
- 1 sessiya / kun
- 5 ta bloklanadigan ilova
- Asosiy mentor (text faqat)

### Premium ($4.99/oy yoki $39/yil):
- Cheksiz sessiyalar
- Cheksiz ilovalar
- Ovozli mentor (TTS + STT)
- Statistika va dashboard
- Accountability partner
- Premium ovoz tovushlari (ElevenLabs)

### Hard tier ($9.99/oy):
- Hammasi + "Vault mode" (bypass'i imkonsiz qilingan rejim)
- Pul/xayriya garovi integratsiyasi
- Shaxsiy AI mentor stilini sozlash

---

## 10. Ochiq savollar (jamoa muhokamasi uchun)

1. Onboarding'da ovozli yozib olish majburiymi yoki ixtiyoriy?
2. "Hard tier"ni Play Store ichidami yoki sideload qilib chiqaramizmi?
3. Birinchi versiyada AI faqat ingliz tildami yoki o'zbek/rus ham bormi?
4. Premium konversiya: trial 7 kun yoki 14 kun?
5. Foydalanuvchi sessiyani "shoshilinch" buzgan holatda qanday penalty bo'lishi kerak?
