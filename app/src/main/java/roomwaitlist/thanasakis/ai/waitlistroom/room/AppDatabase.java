package roomwaitlist.thanasakis.ai.waitlistroom.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import roomwaitlist.thanasakis.ai.waitlistroom.models.WaitListEntry;

/**
 * Created by programbench on 11/20/2017.
 */

//is important to make the class and method as abstract, Room will return the right DAO implementation

@Database(entities = {WaitListEntry.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DB_NAME = "app_db";

    public abstract WaitListDAO getWaitListDao();
}