package roomwaitlist.thanasakis.ai.waitlistroom.di;

import android.app.Application;
import android.arch.persistence.room.Dao;

import javax.inject.Singleton;

import dagger.Component;
import roomwaitlist.thanasakis.ai.waitlistroom.MainActivity;
import roomwaitlist.thanasakis.ai.waitlistroom.room.AppDatabase;
import roomwaitlist.thanasakis.ai.waitlistroom.room.WaitListDAO;

/**
 * Created by programbench on 11/20/2017.
 */
@Singleton
@Component(dependencies = {}, modules = {AppModule.class, RoomModule.class})
public interface AppComponent {

    void inject(MainActivity mainActivity);

    WaitListDAO waitlistDao();

    AppDatabase appDatabase();

    Application application();

}
