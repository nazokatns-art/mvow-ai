package uz.mentorai.focus.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.mentorai.focus.data.db.MentorDatabase
import uz.mentorai.focus.data.scheduled.ScheduledSessionDao
import uz.mentorai.focus.data.session.SessionDao
import uz.mentorai.focus.data.stats.DailyStatsDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MentorDatabase =
        Room.databaseBuilder(
            context,
            MentorDatabase::class.java,
            MentorDatabase.DB_NAME
        )
            .fallbackToDestructiveMigration(true)  // Sprint 2.5'gacha — migration yo'q
            .build()

    @Provides
    fun provideSessionDao(db: MentorDatabase): SessionDao = db.sessionDao()

    @Provides
    fun provideScheduledSessionDao(db: MentorDatabase): ScheduledSessionDao =
        db.scheduledSessionDao()

    @Provides
    fun provideDailyStatsDao(db: MentorDatabase): DailyStatsDao = db.dailyStatsDao()
}
