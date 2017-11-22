package roomwaitlist.thanasakis.ai.waitlistroom.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import roomwaitlist.thanasakis.ai.waitlistroom.models.WaitListEntry;

/**
 * Created by programbench on 11/20/2017.
 */

@Dao
public interface WaitListDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEntry(WaitListEntry entry);

    // Removes an entry from the database
    @Delete
    void delete(WaitListEntry entry);

    @Query("delete from entries where timestamp = :id")
    void deleteById(long id);

    // Gets all entries in the database
    @Query("SELECT * FROM entries")
    List<WaitListEntry> getAllEntries();

}
