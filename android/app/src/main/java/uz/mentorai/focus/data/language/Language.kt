package uz.mentorai.focus.data.language

import java.util.Locale

/**
 * App'da qo'llab-quvvatlanadigan tillar.
 * Yangi til qo'shish: enum'ga add → MentorPhrases + UiStrings'da `when` filiallarini to'ldirish.
 */
enum class Language(
    val code: String,
    val displayName: String,        // Native nom (foydalanuvchiga ko'rsatamiz)
    val englishName: String,
    val ttsLocale: Locale,
    val sttLocaleTag: String
) {
    ENGLISH(
        code = "en",
        displayName = "English",
        englishName = "English",
        ttsLocale = Locale.ENGLISH,
        sttLocaleTag = "en-US"
    ),
    UZBEK(
        code = "uz",
        displayName = "O'zbekcha",
        englishName = "Uzbek",
        ttsLocale = Locale("uz", "UZ"),
        sttLocaleTag = "uz-UZ"
    ),
    RUSSIAN(
        code = "ru",
        displayName = "Русский",
        englishName = "Russian",
        ttsLocale = Locale("ru", "RU"),
        sttLocaleTag = "ru-RU"
    ),
    SPANISH(
        code = "es",
        displayName = "Español",
        englishName = "Spanish",
        ttsLocale = Locale("es", "ES"),
        sttLocaleTag = "es-ES"
    ),
    ARABIC(
        code = "ar",
        displayName = "العربية",
        englishName = "Arabic",
        ttsLocale = Locale("ar"),
        sttLocaleTag = "ar-SA"
    ),
    TURKISH(
        code = "tr",
        displayName = "Türkçe",
        englishName = "Turkish",
        ttsLocale = Locale("tr", "TR"),
        sttLocaleTag = "tr-TR"
    );

    companion object {
        fun fromCode(code: String?): Language? =
            entries.firstOrNull { it.code == code }

        /** Tizim locale'iga qarab default tilni topadi */
        fun systemDefault(): Language {
            val sysLang = Locale.getDefault().language
            return entries.firstOrNull { it.code == sysLang } ?: ENGLISH
        }
    }
}
