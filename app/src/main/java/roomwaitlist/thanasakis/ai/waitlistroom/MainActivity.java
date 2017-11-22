package roomwaitlist.thanasakis.ai.waitlistroom;

import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import roomwaitlist.thanasakis.ai.waitlistroom.di.AppModule;
import roomwaitlist.thanasakis.ai.waitlistroom.di.DaggerAppComponent;
import roomwaitlist.thanasakis.ai.waitlistroom.di.RoomModule;
import roomwaitlist.thanasakis.ai.waitlistroom.models.WaitListEntry;
import roomwaitlist.thanasakis.ai.waitlistroom.room.WaitListDAO;

public class MainActivity extends AppCompatActivity {

    @Inject
    public WaitListDAO waitListDAO;
    public ArrayList<WaitListEntry> mEntries;
    private GuestListAdapter mAdapter;
    @BindView(R.id.all_guests_list_view)
    RecyclerView waitlistRecyclerView;
    @BindView(R.id.person_name_edit_text)
    EditText mNewGuestNameEditText;
    @BindView(R.id.party_count_edit_text)
    EditText mNewPartySizeEditText;
    @BindView(R.id.add_to_waitlist_button)
    Button mGuestAddButton;


    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private final String KEY_RECYCLER_ITEMS = "recycler_items";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Set layout for the RecyclerView, because it's a list we are using the linear layout
        waitlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mEntries = new ArrayList<WaitListEntry>();
        DaggerAppComponent.builder()
                .appModule(new AppModule(getApplication()))
                .roomModule(new RoomModule(getApplication()))
                .build()
                .inject(this);
        //check to add dummy first only query needs background thread not add
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            // COMPLETED (4) Override onMove and simply return false inside
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //do nothing, we only care about swiping
                return false;
            }

            // COMPLETED (5) Override onSwiped
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // COMPLETED (8) Inside, get the viewHolder's itemView's tag and store in a long variable id
                //get the id of the item being swiped
                //long id = (long) viewHolder.itemView.getTag();
                  //remove from DB
                WaitListEntry itemSwiped = (WaitListEntry) viewHolder.itemView.getTag();
                removeGuest(itemSwiped);

                //update the list
                mAdapter.notifyDataSetChanged();
            }

            //COMPLETED (11) attach the ItemTouchHelper to the waitlistRecyclerView
        }).attachToRecyclerView(waitlistRecyclerView);

        mAdapter = new GuestListAdapter(this, mEntries);
        waitlistRecyclerView.setAdapter(mAdapter);
        if (savedInstanceState==null){
            loadEntries();
        }else{
            restorePreviousState(savedInstanceState); // Restore data found in the Bundle
        }
        // Link the adapter to the RecyclerView


    }

    private void loadEntries() {
        new AsyncTask<Void, Void, List<WaitListEntry>>() {
            @Override
            protected List<WaitListEntry> doInBackground(Void... params) {
                return waitListDAO.getAllEntries();
            }

            @Override
            protected void onPostExecute(List<WaitListEntry> entries) {
                if (entries.size() <= 1){
                    addDummyData();
                }else {
                    mEntries.clear();
                    mEntries.addAll(entries);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }.execute();
    }

    private void addDummyData(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                waitListDAO.insertEntry(new WaitListEntry("nikos",6,12323));
                waitListDAO.insertEntry(new WaitListEntry("takis",7,12323));
                waitListDAO.insertEntry(new WaitListEntry("kostas",85,12323));
                return null;
            }

            @Override
            protected void onPostExecute(Void a) {
                loadEntries();
            }
        }.execute();

    }
    private void removeGuest(WaitListEntry entry) {
        new AsyncTask<WaitListEntry, Void, Void>() {
            @Override
            protected Void doInBackground(WaitListEntry... params) {
                waitListDAO.delete(params[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void a) {
                loadEntries();
            }
        }.execute(entry);
    }
    private void addGuest(WaitListEntry entry) {
        new AsyncTask<WaitListEntry, Void, Void>() {
            @Override
            protected Void doInBackground(WaitListEntry... params) {
                waitListDAO.insertEntry(params[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void a) {
                loadEntries();
            }
        }.execute(entry);
    }

    @OnClick(R.id.add_to_waitlist_button)
    public void addToWaitlist(View view) {
        if (mNewGuestNameEditText.getText().length() == 0 ||
                mNewPartySizeEditText.getText().length() == 0) {
            return;
        }
        //default party size to 1
        int partySize = 1;
        try {
            //mNewPartyCountEditText inputType="number", so this should always work
            partySize = Integer.parseInt(mNewPartySizeEditText.getText().toString());
        } catch (NumberFormatException ex) {
            Log.e(LOG_TAG, "Failed to parse party size text to number: " + ex.getMessage());
        }
        WaitListEntry tempEntry = new WaitListEntry(mNewGuestNameEditText.getText().toString(), partySize, System.currentTimeMillis());
        // Add guest info to mDb
        addGuest(tempEntry);

        //clear UI text fields
        mNewPartySizeEditText.clearFocus();
        mNewGuestNameEditText.getText().clear();
        mNewPartySizeEditText.getText().clear();
    }


    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //save recyclerview items
        outState.putParcelableArrayList(KEY_RECYCLER_ITEMS, mEntries);
        Parcelable listState = waitlistRecyclerView.getLayoutManager().onSaveInstanceState();
        // putting recyclerview position
        outState.putParcelable(KEY_RECYCLER_STATE, listState);
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    public void restorePreviousState(Bundle mSavedInstanceState){
        // getting recyclerview position
        Parcelable mListState = mSavedInstanceState.getParcelable(KEY_RECYCLER_STATE);
        // getting recyclerview items
        mEntries = mSavedInstanceState.getParcelableArrayList(KEY_RECYCLER_ITEMS);
        // Restoring adapter items
        mAdapter.setEntries(mEntries);
        // Restoring recycler view position
        waitlistRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
    }
}
