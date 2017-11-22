package roomwaitlist.thanasakis.ai.waitlistroom.di;

import android.app.Application;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import roomwaitlist.thanasakis.ai.waitlistroom.room.AppDatabase;
import roomwaitlist.thanasakis.ai.waitlistroom.room.WaitListDAO;

/**
 * Created by programbench on 11/20/2017.
 */
@Module
public class RoomModule {

    private AppDatabase appDb;

    public RoomModule(Application mApplication) {
        appDb = Room.databaseBuilder(mApplication, AppDatabase.class, "app-db").allowMainThreadQueries().build();
    }

    @Singleton
    @Provides
    AppDatabase providesRoomDatabase() {
        return appDb;
    }

    @Singleton
    @Provides
    WaitListDAO providesProductDao(AppDatabase appDb) {
        return appDb.getWaitListDao();
    }

}