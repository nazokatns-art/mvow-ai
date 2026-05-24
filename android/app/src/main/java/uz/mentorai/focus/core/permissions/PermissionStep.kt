package uz.mentorai.focus.core.permissions

/**
 * Onboarding'da ketma-ket so'raladigan permission'lar.
 * Tartib muhim — eng oson'dan eng qiyin'ga.
 */
enum class PermissionStep(
    val displayName: String,
    val rationale: String,
    val whyItMatters: String,
    val mechanism: PermissionMechanism
) {
    NOTIFICATIONS(
        displayName = "Bildirishnomalar",
        rationale = "Mentor sen bilan bildirishnoma orqali muloqot qiladi.",
        whyItMatters = "Sessiya boshlanish vaqti, taymer va og'ohlantirishlar — bularsiz Mentor jim qoladi.",
        mechanism = PermissionMechanism.RUNTIME_PERMISSION
    ),

    USAGE_STATS(
        displayName = "Ilovalar foydalanish statistikasi",
        rationale = "Mentor qaysi ilovani ochayotganingni bilishi uchun bu kerak.",
        whyItMatters = "Bu ruxsatsiz Instagram'ni Telegram'dan ajrata olmaymiz. Bu — minimal kuzatuv, faqat ilova nomi.",
        mechanism = PermissionMechanism.SETTINGS_USAGE_ACCESS
    ),

    OVERLAY(
        displayName = "Boshqa ilovalar ustida ko'rinish",
        rationale = "To'siq ekranini Instagram ustida ko'rsatish uchun.",
        whyItMatters = "Bu ruxsatsiz Mentor faqat o'z ekranida qoladi — bloklash ishlamaydi.",
        mechanism = PermissionMechanism.SETTINGS_OVERLAY
    ),

    ACCESSIBILITY(
        displayName = "Maxsus imkoniyatlar",
        rationale = "Real-time bloklash va Sozlamalar trapasi uchun bu ENG MUHIM ruxsat.",
        whyItMatters = "Bu Mentor'ning yuragi. Faqat bloklangan ilovalarni aniqlash uchun foydalanamiz — boshqa hech narsa.",
        mechanism = PermissionMechanism.SETTINGS_ACCESSIBILITY
    ),

    DEVICE_ADMIN(
        displayName = "Qurilma boshqaruvchisi",
        rationale = "Mentor'ni tasodifan o'chirib qo'yishdan saqlaydi.",
        whyItMatters = "Zaif lahzada o'zingni o'zingdan himoya qilish — shu ruxsat orqali.",
        mechanism = PermissionMechanism.DEVICE_ADMIN_ACTIVATION
    ),

    AUDIO_RECORDING(
        displayName = "Mikrofon",
        rationale = "Mentor bilan ovozli muloqot va majburiyat yozib olish uchun.",
        whyItMatters = "Ovozing — sening eng kuchli isboting. U yozib olinadi va kerak vaqtda senga eshittiriladi.",
        mechanism = PermissionMechanism.RUNTIME_PERMISSION
    );

    companion object {
        val ORDERED = values().toList()
    }
}

enum class PermissionMechanism {
    RUNTIME_PERMISSION,           // POST_NOTIFICATIONS, RECORD_AUDIO
    SETTINGS_USAGE_ACCESS,         // ACTION_USAGE_ACCESS_SETTINGS
    SETTINGS_OVERLAY,              // ACTION_MANAGE_OVERLAY_PERMISSION
    SETTINGS_ACCESSIBILITY,        // ACTION_ACCESSIBILITY_SETTINGS
    DEVICE_ADMIN_ACTIVATION        // ACTION_ADD_DEVICE_ADMIN
}
