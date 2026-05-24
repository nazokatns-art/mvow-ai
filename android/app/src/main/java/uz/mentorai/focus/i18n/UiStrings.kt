package uz.mentorai.focus.i18n

import uz.mentorai.focus.data.language.Language

/**
 * UI'dagi asosiy matnlar — kop tilli.
 * Kichik komponentlar (sodda label'lar) hozircha o'zbek tilida — kelajakda kengaytiriladi.
 */
object UiStrings {

    // Welcome & Intro
    fun welcomeTagline(l: Language) = when (l) {
        Language.ENGLISH -> "\"Discipline is the key to freedom.\""
        Language.UZBEK -> "\"Intizom — erkinlikning kalitidir.\""
        Language.RUSSIAN -> "«Дисциплина — ключ к свободе.»"
        Language.SPANISH -> "«La disciplina es la llave de la libertad.»"
        Language.ARABIC -> "«الانضباط مفتاح الحرية.»"
        Language.TURKISH -> "\"Disiplin özgürlüğün anahtarıdır.\""
    }

    fun welcomeSubtitle(l: Language) = when (l) {
        Language.ENGLISH -> "This is not a reminder app.\nThis is protection from your future weakness."
        Language.UZBEK -> "Bu — eslatma ilovasi emas.\nBu — sening kelajakdagi zaifligingdan himoya."
        Language.RUSSIAN -> "Это не приложение-напоминание.\nЭто защита от твоей будущей слабости."
        Language.SPANISH -> "Esta no es una app de recordatorios.\nEs protección de tu futura debilidad."
        Language.ARABIC -> "هذا ليس تطبيق تذكير.\nهذه حماية من ضعفك المستقبلي."
        Language.TURKISH -> "Bu bir hatırlatma uygulaması değil.\nGelecekteki zayıflığından korumadır."
    }

    fun btnStart(l: Language) = when (l) {
        Language.ENGLISH -> "Begin"
        Language.UZBEK -> "Boshlash"
        Language.RUSSIAN -> "Начать"
        Language.SPANISH -> "Comenzar"
        Language.ARABIC -> "ابدأ"
        Language.TURKISH -> "Başla"
    }

    fun btnContinue(l: Language) = when (l) {
        Language.ENGLISH -> "Continue"
        Language.UZBEK -> "Davom etish"
        Language.RUSSIAN -> "Далее"
        Language.SPANISH -> "Continuar"
        Language.ARABIC -> "متابعة"
        Language.TURKISH -> "Devam et"
    }

    fun btnUnderstood(l: Language) = when (l) {
        Language.ENGLISH -> "Understood"
        Language.UZBEK -> "Tushundim"
        Language.RUSSIAN -> "Понятно"
        Language.SPANISH -> "Entendido"
        Language.ARABIC -> "فهمت"
        Language.TURKISH -> "Anladım"
    }

    fun btnReturnToWork(l: Language) = when (l) {
        Language.ENGLISH -> "Return to work"
        Language.UZBEK -> "Ishga qaytish"
        Language.RUSSIAN -> "Вернуться к работе"
        Language.SPANISH -> "Volver al trabajo"
        Language.ARABIC -> "عُد إلى العمل"
        Language.TURKISH -> "İşine dön"
    }

    fun btnRequestOverride(l: Language) = when (l) {
        Language.ENGLISH -> "request override"
        Language.UZBEK -> "boshqa ish kerakligini aytish"
        Language.RUSSIAN -> "запросить исключение"
        Language.SPANISH -> "solicitar excepción"
        Language.ARABIC -> "طلب استثناء"
        Language.TURKISH -> "istisna talep et"
    }

    fun btnClose(l: Language) = when (l) {
        Language.ENGLISH -> "Close"
        Language.UZBEK -> "Yopish"
        Language.RUSSIAN -> "Закрыть"
        Language.SPANISH -> "Cerrar"
        Language.ARABIC -> "إغلاق"
        Language.TURKISH -> "Kapat"
    }

    // Hard block screen
    fun stop(l: Language) = when (l) {
        Language.ENGLISH -> "STOP."
        Language.UZBEK -> "STOP."
        Language.RUSSIAN -> "СТОП."
        Language.SPANISH -> "ALTO."
        Language.ARABIC -> "قف."
        Language.TURKISH -> "DUR."
    }

    // Negotiation
    fun statusListening(l: Language) = when (l) {
        Language.ENGLISH -> "Listening..."
        Language.UZBEK -> "Eshitayapman..."
        Language.RUSSIAN -> "Слушаю..."
        Language.SPANISH -> "Escuchando..."
        Language.ARABIC -> "أستمع..."
        Language.TURKISH -> "Dinliyorum..."
    }

    fun statusSpeakReason(l: Language) = when (l) {
        Language.ENGLISH -> "Say your reason out loud."
        Language.UZBEK -> "Sababingni baland ovozda ayt."
        Language.RUSSIAN -> "Назови причину вслух."
        Language.SPANISH -> "Di tu razón en voz alta."
        Language.ARABIC -> "قل سببك بصوت عال."
        Language.TURKISH -> "Sebebini sesli söyle."
    }

    fun statusMentorThinking(l: Language) = when (l) {
        Language.ENGLISH -> "Mentor responding..."
        Language.UZBEK -> "Mentor javob beryapti..."
        Language.RUSSIAN -> "Ментор отвечает..."
        Language.SPANISH -> "El Mentor responde..."
        Language.ARABIC -> "المرشد يجيب..."
        Language.TURKISH -> "Mentor yanıtlıyor..."
    }

    fun statusGranted(l: Language, minutes: Int) = when (l) {
        Language.ENGLISH -> "$minutes MIN. ALLOWED."
        Language.UZBEK -> "$minutes DAQ. RUXSAT."
        Language.RUSSIAN -> "$minutes МИН. РАЗРЕШЕНО."
        Language.SPANISH -> "$minutes MIN. PERMITIDO."
        Language.ARABIC -> "$minutes دقيقة. مسموح."
        Language.TURKISH -> "$minutes DK. İZİN VERİLDİ."
    }

    fun statusDenied(l: Language) = when (l) {
        Language.ENGLISH -> "NO."
        Language.UZBEK -> "YO'Q."
        Language.RUSSIAN -> "НЕТ."
        Language.SPANISH -> "NO."
        Language.ARABIC -> "لا."
        Language.TURKISH -> "HAYIR."
    }

    // Home screen
    fun yourPromise(l: Language) = when (l) {
        Language.ENGLISH -> "Your promise:"
        Language.UZBEK -> "Sening so'zing:"
        Language.RUSSIAN -> "Твоё обещание:"
        Language.SPANISH -> "Tu promesa:"
        Language.ARABIC -> "وعدك:"
        Language.TURKISH -> "Sözün:"
    }

    fun streak(l: Language) = when (l) {
        Language.ENGLISH -> "STREAK"
        Language.UZBEK -> "STREAK"
        Language.RUSSIAN -> "STREAK"
        Language.SPANISH -> "RACHA"
        Language.ARABIC -> "السلسلة"
        Language.TURKISH -> "SERİ"
    }

    fun durationSection(l: Language) = when (l) {
        Language.ENGLISH -> "Duration"
        Language.UZBEK -> "Davomiyligi"
        Language.RUSSIAN -> "Длительность"
        Language.SPANISH -> "Duración"
        Language.ARABIC -> "المدة"
        Language.TURKISH -> "Süre"
    }

    fun startSession(l: Language) = when (l) {
        Language.ENGLISH -> "Start session"
        Language.UZBEK -> "Sessiyani boshlash"
        Language.RUSSIAN -> "Начать сессию"
        Language.SPANISH -> "Iniciar sesión"
        Language.ARABIC -> "بدء الجلسة"
        Language.TURKISH -> "Oturumu başlat"
    }

    fun endSessionEarly(l: Language) = when (l) {
        Language.ENGLISH -> "End early"
        Language.UZBEK -> "Erta tugatish"
        Language.RUSSIAN -> "Завершить досрочно"
        Language.SPANISH -> "Terminar antes"
        Language.ARABIC -> "إنهاء مبكر"
        Language.TURKISH -> "Erken bitir"
    }

    fun endSessionEarlyWarning(l: Language) = when (l) {
        Language.ENGLISH -> "Ending early breaks the streak."
        Language.UZBEK -> "Sessiyani erta tugatish — streak'ga zarar."
        Language.RUSSIAN -> "Досрочное завершение прерывает streak."
        Language.SPANISH -> "Terminar antes rompe la racha."
        Language.ARABIC -> "الإنهاء المبكر يكسر السلسلة."
        Language.TURKISH -> "Erken bitirmek seriyi bozar."
    }

    fun completeSession(l: Language) = when (l) {
        Language.ENGLISH -> "Complete session"
        Language.UZBEK -> "Sessiyani yakunlash"
        Language.RUSSIAN -> "Завершить сессию"
        Language.SPANISH -> "Completar sesión"
        Language.ARABIC -> "إكمال الجلسة"
        Language.TURKISH -> "Oturumu tamamla"
    }

    fun activeSessionLabel(l: Language) = when (l) {
        Language.ENGLISH -> "Active session"
        Language.UZBEK -> "Faol sessiya"
        Language.RUSSIAN -> "Активная сессия"
        Language.SPANISH -> "Sesión activa"
        Language.ARABIC -> "الجلسة النشطة"
        Language.TURKISH -> "Aktif oturum"
    }

    fun interceptCount(l: Language, count: Int) = when (l) {
        Language.ENGLISH -> "$count blocks today"
        Language.UZBEK -> "$count marta to'sildi"
        Language.RUSSIAN -> "$count блокировок сегодня"
        Language.SPANISH -> "$count bloqueos hoy"
        Language.ARABIC -> "$count حجب اليوم"
        Language.TURKISH -> "Bugün $count engelleme"
    }

    fun appsToBlock(l: Language, count: Int) = when (l) {
        Language.ENGLISH -> "$count apps will be blocked"
        Language.UZBEK -> "$count ta ilova bloklanadi"
        Language.RUSSIAN -> "$count приложений будут заблокированы"
        Language.SPANISH -> "$count apps serán bloqueadas"
        Language.ARABIC -> "$count تطبيقات ستُحجب"
        Language.TURKISH -> "$count uygulama engellenecek"
    }

    // Calendar
    fun plansTitle(l: Language) = when (l) {
        Language.ENGLISH -> "Today and ahead"
        Language.UZBEK -> "Bugun va kelgusi"
        Language.RUSSIAN -> "Сегодня и далее"
        Language.SPANISH -> "Hoy y próximamente"
        Language.ARABIC -> "اليوم وما بعده"
        Language.TURKISH -> "Bugün ve sonrası"
    }

    fun plansLabel(l: Language) = when (l) {
        Language.ENGLISH -> "PLANS"
        Language.UZBEK -> "REJALAR"
        Language.RUSSIAN -> "ПЛАНЫ"
        Language.SPANISH -> "PLANES"
        Language.ARABIC -> "الخطط"
        Language.TURKISH -> "PLANLAR"
    }

    fun plansEmptyTitle(l: Language) = when (l) {
        Language.ENGLISH -> "NO PLANS."
        Language.UZBEK -> "REJANG YO'Q."
        Language.RUSSIAN -> "ПЛАНОВ НЕТ."
        Language.SPANISH -> "SIN PLANES."
        Language.ARABIC -> "لا توجد خطط."
        Language.TURKISH -> "PLAN YOK."
    }

    fun plansEmptyHint(l: Language) = when (l) {
        Language.ENGLISH -> "Add one with the button below."
        Language.UZBEK -> "Pastdagi tugma orqali qo'sh."
        Language.RUSSIAN -> "Добавь через кнопку ниже."
        Language.SPANISH -> "Añade con el botón de abajo."
        Language.ARABIC -> "أضف عبر الزر أدناه."
        Language.TURKISH -> "Aşağıdaki düğmeyle ekle."
    }

    // Tabs
    fun tabSession(l: Language) = when (l) {
        Language.ENGLISH -> "SESSION"
        Language.UZBEK -> "SESSIYA"
        Language.RUSSIAN -> "СЕССИЯ"
        Language.SPANISH -> "SESIÓN"
        Language.ARABIC -> "الجلسة"
        Language.TURKISH -> "OTURUM"
    }

    fun tabPlan(l: Language) = when (l) {
        Language.ENGLISH -> "PLAN"
        Language.UZBEK -> "REJA"
        Language.RUSSIAN -> "ПЛАН"
        Language.SPANISH -> "PLAN"
        Language.ARABIC -> "الخطة"
        Language.TURKISH -> "PLAN"
    }

    fun tabSettings(l: Language) = when (l) {
        Language.ENGLISH -> "SETTINGS"
        Language.UZBEK -> "SOZLAMA"
        Language.RUSSIAN -> "НАСТРОЙКИ"
        Language.SPANISH -> "AJUSTES"
        Language.ARABIC -> "الإعدادات"
        Language.TURKISH -> "AYARLAR"
    }

    // Language selection
    fun chooseLanguage(l: Language) = when (l) {
        Language.ENGLISH -> "Choose your language."
        Language.UZBEK -> "Tilni tanla."
        Language.RUSSIAN -> "Выбери свой язык."
        Language.SPANISH -> "Elige tu idioma."
        Language.ARABIC -> "اختر لغتك."
        Language.TURKISH -> "Dilini seç."
    }

    fun chooseLanguageSubtitle(l: Language) = when (l) {
        Language.ENGLISH -> "Mentor will speak to you in this language."
        Language.UZBEK -> "Mentor sen bilan shu tilda gaplashadi."
        Language.RUSSIAN -> "Ментор будет говорить с тобой на этом языке."
        Language.SPANISH -> "El Mentor te hablará en este idioma."
        Language.ARABIC -> "سيتحدث المرشد إليك بهذه اللغة."
        Language.TURKISH -> "Mentor seninle bu dilde konuşacak."
    }

    fun settingsLabel(l: Language) = when (l) {
        Language.ENGLISH -> "Settings"
        Language.UZBEK -> "Sozlamalar"
        Language.RUSSIAN -> "Настройки"
        Language.SPANISH -> "Ajustes"
        Language.ARABIC -> "الإعدادات"
        Language.TURKISH -> "Ayarlar"
    }

    fun languageLabel(l: Language) = when (l) {
        Language.ENGLISH -> "Language"
        Language.UZBEK -> "Til"
        Language.RUSSIAN -> "Язык"
        Language.SPANISH -> "Idioma"
        Language.ARABIC -> "اللغة"
        Language.TURKISH -> "Dil"
    }
}
