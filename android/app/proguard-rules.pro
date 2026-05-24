# Hilt
-keep class dagger.hilt.** { *; }
-keep @dagger.hilt.android.HiltAndroidApp class * { *; }

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Moshi reflection
-keepattributes *Annotation*
-keep class com.squareup.moshi.** { *; }
-keep class kotlin.reflect.** { *; }

# Mentor — keep all manifest-declared services/receivers
-keep class uz.mentorai.focus.guard.** { *; }
-keep class uz.mentorai.focus.MentorApplication { *; }

# OkHttp / Retrofit
-dontwarn okhttp3.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
