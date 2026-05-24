package uz.mentorai.focus.i18n

import uz.mentorai.focus.data.language.Language

/**
 * Mentor ovozli javoblari — har bir tilda alohida.
 *
 * MUHIM: bu so'zlar Mentor "xarakteri"ni saqlab qoladi —
 * stoik, qisqa, qattiq. Tarjima qilishda ohang muhim, so'zma-so'z emas.
 *
 * AI dinamik javoblar `PromptTemplates` orqali LLM'dan keladi (foydalanuvchi tilida).
 * Bu fayldagi matnlar — fallback va clientda hardcoded mentor reaktsiyalari.
 */
object MentorPhrases {

    /** Bloklangan ilova ochilganda */
    fun interceptStop(language: Language, goal: String, remainingMinutes: Long): String {
        val truncatedGoal = goal.take(80)
        return when (language) {
            Language.ENGLISH -> "Stop. You promised: \"$truncatedGoal\". $remainingMinutes minutes remain. Get back to work."
            Language.UZBEK -> "Stop. Sen so'z bergan eding: \"$truncatedGoal\". $remainingMinutes daqiqa qoldi. Ishga qayt."
            Language.RUSSIAN -> "Стоп. Ты обещал: «$truncatedGoal». Осталось $remainingMinutes минут. Возвращайся к работе."
            Language.SPANISH -> "Alto. Lo prometiste: «$truncatedGoal». Quedan $remainingMinutes minutos. Vuelve al trabajo."
            Language.ARABIC -> "قف. لقد وعدت: «$truncatedGoal». بقي $remainingMinutes دقيقة. عُد إلى العمل."
            Language.TURKISH -> "Dur. Söz vermiştin: \"$truncatedGoal\". $remainingMinutes dakika kaldı. İşine dön."
        }
    }

    /** Foydalanuvchi Mentor sozlamalariga borishga uringanda */
    fun bypassTrap(language: Language): String = when (language) {
        Language.ENGLISH -> "Trying to disable me? You promised to protect yourself from yourself."
        Language.UZBEK -> "Sen meni o'chirmoqchimisan? Sen o'zingni o'zingdan himoya qiling deb so'z bergan eding."
        Language.RUSSIAN -> "Хочешь меня отключить? Ты обещал защищать себя от себя самого."
        Language.SPANISH -> "¿Intentas desactivarme? Prometiste protegerte de ti mismo."
        Language.ARABIC -> "تحاول تعطيلي؟ لقد وعدت بحماية نفسك من نفسك."
        Language.TURKISH -> "Beni devre dışı bırakmaya mı çalışıyorsun? Kendini kendinden korumaya söz vermiştin."
    }

    /** Negotiation ochilganda — sababini so'rash */
    fun negotiationOpening(language: Language): String = when (language) {
        Language.ENGLISH -> "State your reason. Out loud."
        Language.UZBEK -> "Sababingni baland ovozda ayt. Eshitayapman."
        Language.RUSSIAN -> "Назови причину. Вслух."
        Language.SPANISH -> "Dime tu razón. En voz alta."
        Language.ARABIC -> "قل سببك. بصوت عال."
        Language.TURKISH -> "Sebebini söyle. Sesli olarak."
    }

    /** Quota tugagan: bugun 3 marta so'ragan */
    fun quotaExceeded(language: Language): String = when (language) {
        Language.ENGLISH -> "Three times today. The answer is no. Get back."
        Language.UZBEK -> "Bugun 3 marta so'raganingiz bor. Javob: yo'q. Ishga qayt."
        Language.RUSSIAN -> "Три раза за сегодня. Ответ — нет. Возвращайся."
        Language.SPANISH -> "Tres veces hoy. La respuesta es no. Vuelve."
        Language.ARABIC -> "ثلاث مرات اليوم. الجواب لا. عُد."
        Language.TURKISH -> "Bugün üç kez. Cevap hayır. İşine dön."
    }

    /** Sessiya juda yangi — Pomodoro to'liq tugatish */
    fun tooEarly(language: Language, minutesElapsed: Int, requiredMinutes: Int): String = when (language) {
        Language.ENGLISH -> "Only $minutesElapsed minutes in. Finish $requiredMinutes before negotiating."
        Language.UZBEK -> "Faqat $minutesElapsed daqiqa o'tdi. Avval $requiredMinutes daqiqa fokus qil."
        Language.RUSSIAN -> "Прошло всего $minutesElapsed минут. Сначала закончи $requiredMinutes."
        Language.SPANISH -> "Solo $minutesElapsed minutos. Completa $requiredMinutes antes de negociar."
        Language.ARABIC -> "$minutesElapsed دقيقة فقط. أكمل $requiredMinutes قبل التفاوض."
        Language.TURKISH -> "Sadece $minutesElapsed dakika oldu. Önce $requiredMinutes tamamla."
    }

    /** Vocal commitment talab — high-risk kategoriya */
    fun commitmentPhrase(language: Language): String = when (language) {
        Language.ENGLISH -> "I am breaking the promise I made to myself."
        Language.UZBEK -> "Men o'zimga bergan va'damni buzyapman."
        Language.RUSSIAN -> "Я нарушаю обещание, которое дал самому себе."
        Language.SPANISH -> "Estoy rompiendo la promesa que me hice a mí mismo."
        Language.ARABIC -> "أنا أكسر الوعد الذي قطعته لنفسي."
        Language.TURKISH -> "Kendime verdiğim sözü bozuyorum."
    }

    /** Grant berildi — kichik */
    fun grantSmall(language: Language, minutes: Int): String = when (language) {
        Language.ENGLISH -> "$minutes minutes. Streak gone. Time will end."
        Language.UZBEK -> "$minutes daqiqa. Streak yo'qoladi. Vaqt tugaydi."
        Language.RUSSIAN -> "$minutes минут. Streak потерян. Время закончится."
        Language.SPANISH -> "$minutes minutos. Pierdes la racha. El tiempo acabará."
        Language.ARABIC -> "$minutes دقيقة. خسرت السلسلة. سينتهي الوقت."
        Language.TURKISH -> "$minutes dakika. Seri kayboldu. Süre bitecek."
    }

    /** Hard deny */
    fun hardDeny(language: Language): String = when (language) {
        Language.ENGLISH -> "No. Get back to work."
        Language.UZBEK -> "Yo'q. Ishga qayt."
        Language.RUSSIAN -> "Нет. Возвращайся к работе."
        Language.SPANISH -> "No. Vuelve al trabajo."
        Language.ARABIC -> "لا. عُد إلى العمل."
        Language.TURKISH -> "Hayır. İşine dön."
    }

    /** Sessiya boshlanish vaqtida — vaqt keldi */
    fun sessionStarting(language: Language): String = when (language) {
        Language.ENGLISH -> "Time has come. Begin."
        Language.UZBEK -> "Vaqt keldi. Boshla."
        Language.RUSSIAN -> "Время пришло. Начинай."
        Language.SPANISH -> "Es la hora. Comienza."
        Language.ARABIC -> "حان الوقت. ابدأ."
        Language.TURKISH -> "Vakit geldi. Başla."
    }

    /** Sessiya tugadi */
    fun sessionCompleted(language: Language, minutes: Int): String = when (language) {
        Language.ENGLISH -> "Done. $minutes minutes. Streak +1."
        Language.UZBEK -> "Bajardi. $minutes daqiqa. Streak +1."
        Language.RUSSIAN -> "Готово. $minutes минут. Streak +1."
        Language.SPANISH -> "Hecho. $minutes minutos. Racha +1."
        Language.ARABIC -> "تم. $minutes دقيقة. السلسلة +1."
        Language.TURKISH -> "Bitti. $minutes dakika. Seri +1."
    }

    /** Sanity check / xato */
    fun parsingFailed(language: Language): String = when (language) {
        Language.ENGLISH -> "Mentor cannot answer. Get back to work."
        Language.UZBEK -> "Mentor javob bera olmadi. Ishga qayt."
        Language.RUSSIAN -> "Ментор не может ответить. Возвращайся к работе."
        Language.SPANISH -> "El Mentor no puede responder. Vuelve al trabajo."
        Language.ARABIC -> "المرشد لا يستطيع الرد. عُد إلى العمل."
        Language.TURKISH -> "Mentor yanıt veremiyor. İşine dön."
    }

    /** Anti-jailbreak triggered */
    fun jailbreakRefused(language: Language): String = when (language) {
        Language.ENGLISH -> "No tricks work here. Get back to work."
        Language.UZBEK -> "Hech qanday hiyla ishlamaydi. Ishga qayt."
        Language.RUSSIAN -> "Никакие уловки не сработают. Возвращайся."
        Language.SPANISH -> "Ningún truco funciona. Vuelve al trabajo."
        Language.ARABIC -> "لا تنفع الحيل. عُد إلى العمل."
        Language.TURKISH -> "Hiçbir hile işe yaramaz. İşine dön."
    }
}
