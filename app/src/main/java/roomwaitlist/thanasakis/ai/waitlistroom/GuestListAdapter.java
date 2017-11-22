package roomwaitlist.thanasakis.ai.waitlistroom;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import roomwaitlist.thanasakis.ai.waitlistroom.models.WaitListEntry;

/**
 * Created by programbench on 11/20/2017.
 */
public class GuestListAdapter extends RecyclerView.Adapter<GuestListAdapter.GuestViewHolder> {

    // Holds on to the cursor to display the waitlist
    private Context mContext;
    private List<WaitListEntry> entries;
    /**
     * Constructor using the context and the db cursor
     * @param context the calling context/activity
     * @param entries the list with the data to display
     */
    public GuestListAdapter(Context context, List<WaitListEntry> entries) {
        this.mContext = context;
        this.entries = entries;

    }

    @Override
    public GuestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.guest_list_item, parent, false);
        return new GuestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GuestViewHolder holder, int position) {
        WaitListEntry entry = entries.get(position);
        // Display the guest name
        holder.nameTextView.setText(entry.guestName);
        // Display the party count
        holder.partySizeTextView.setText(String.valueOf(entry.partySize));
        // COMPLETED (7) Set the tag of the itemview in the holder to the id
        holder.itemView.setTag(entry);
    }


    @Override
    public int getItemCount() {
        return entries.size();
    }

    public void setEntries(@NonNull List<WaitListEntry> entries) {
        this.entries = entries;
        notifyDataSetChanged();
    }

    /**
     * Inner class to hold the views needed to display a single item in the recycler-view
     */
    class GuestViewHolder extends RecyclerView.ViewHolder {

        // Will display the guest name
        TextView nameTextView;
        // Will display the party size number
        TextView partySizeTextView;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews
         *
         * @param itemView The View that you inflated in
         *                 {@link GuestListAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public GuestViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_text_view);
            partySizeTextView = (TextView) itemView.findViewById(R.id.party_size_text_view);
        }

    }
}
