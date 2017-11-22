package roomwaitlist.thanasakis.ai.waitlistroom.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by programbench on 11/20/2017.
 */

//By default, Room uses the class name as the database table name.
// If you want the table to have a different name, set the tableName property of the @Entity annotation:
@Entity(tableName = "entries")
public class WaitListEntry implements Parcelable{

    //To persist a field, Room must have access to it. You can make a field public, or you can provide a getter and setter for it.
    // If you use getter and setter methods, keep in mind that they're based on JavaBeans conventions in Room.
    @PrimaryKey
    @NonNull
    public String guestName;
    public int partySize;
    public long timestamp;

    public WaitListEntry(String guestName, int partySize, long timestamp) {
        this.guestName = guestName;
        this.partySize = partySize;
        this.timestamp  = timestamp;
    }

    //everyting below is needed to implement parcelable to save state of an arraylist with this custom object
    private WaitListEntry(Parcel in){
        guestName=in.readString();
        partySize=in.readInt();
        timestamp=in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(guestName);
        parcel.writeInt(partySize);
        parcel.writeLong(timestamp);
    }

    public static final Parcelable.Creator<WaitListEntry> CREATOR = new Parcelable.Creator<WaitListEntry>() {
        public WaitListEntry createFromParcel(Parcel in) {
            return new WaitListEntry(in);
        }

        public WaitListEntry[] newArray(int size) {
            return new WaitListEntry[size];
        }
    };
}
