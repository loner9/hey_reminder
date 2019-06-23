package info.project.hey.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import info.project.hey.AddReminder;
import info.project.hey.Class.RemindDaBase;
import info.project.hey.Class.Reminder;
import info.project.hey.Class.util.AlarmReceiver;
import info.project.hey.Class.util.DaTiSorter;
import info.project.hey.EditReminder;
import info.project.hey.LoginActivity;
import info.project.hey.R;


public class ReminderFragment extends Fragment {
    private RecyclerView mList;
    private SimpleAdapter mAdapter;
    private TextView mNoReminderView;
    private int mTempPost;
    private LinkedHashMap<Integer, Integer> IDmap = new LinkedHashMap<>();
    private RemindDaBase rb;
    private MultiSelector mMultiSelector = new MultiSelector();
    private AlarmReceiver mAlarmReceiver;

    ProgressDialog progressDialog;
    FirebaseAuth auth;
    Activity context;

    FloatingActionButton addRemind;
    TextView name;
    TextView email;

    public ReminderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View remindFrag = inflater.inflate(R.layout.activity_stu, container, false);

        progressDialog = new ProgressDialog(getActivity());
        setHasOptionsMenu(true);
        context = getActivity();

        Toolbar toolbar = remindFrag.findViewById(R.id.toolbarmain);
        addRemind = remindFrag.findViewById(R.id.addreminder);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        rb = new RemindDaBase(getActivity());

        mList = remindFrag.findViewById(R.id.reccle);
        mNoReminderView = remindFrag.findViewById(R.id.no_reminder_text);

        List<Reminder> mTest = rb.getAllReminders();

        if (mTest.isEmpty()) {
            mNoReminderView.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "Belum ada data", Toast.LENGTH_SHORT).show();
        }

        addRemind.setOnClickListener(v -> {
            getActivity().finish();
            Intent a = new Intent(context,AddReminder.class);
            startActivity(a);
        });

        mList.setLayoutManager(getLayoutManager());
        registerForContextMenu(mList);
        mAdapter = new SimpleAdapter();
        mAdapter.setItemCount(getDefaultItemCount());
        mList.setAdapter(mAdapter);

        // Initialize alarm
        mAlarmReceiver = new AlarmReceiver();

        return remindFrag;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.menu_add_reminder, menu);
    }

    private android.support.v7.view.ActionMode.Callback mDeleteMode = new ModalMultiSelectorCallback(mMultiSelector) {

        @Override
        public boolean onCreateActionMode(android.support.v7.view.ActionMode actionMode, Menu menu) {
            getActivity().getMenuInflater().inflate(R.menu.menu_add_reminder, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(android.support.v7.view.ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                // On clicking discard reminders
                case R.id.discard_reminder:
                    // Close the context menu
                    actionMode.finish();

                    // Get the reminder id associated with the recycler view item
                    for (int i = IDmap.size(); i >= 0; i--) {
                        if (mMultiSelector.isSelected(i, 0)) {
                            int id = IDmap.get(i);

                            // Get reminder from reminder database using id
                            Reminder temp = rb.getReminder(id);
                            // Delete reminder
                            rb.deleteReminder(temp);
                            // Remove reminder from recycler view
                            mAdapter.removeItemSelected(i);
                            // Delete reminder alarm
                            mAlarmReceiver.cancelAlarm(getActivity(), id);
                        }
                    }

                    // Clear selected items in recycler view
                    mMultiSelector.clearSelections();

                    mAdapter.onDeleteItem(getDefaultItemCount());

                    Toast.makeText(getActivity(),
                            "Terhapus",
                            Toast.LENGTH_SHORT).show();

                    List<Reminder> mTest = rb.getAllReminders();

                    if (mTest.isEmpty()) {
                        mNoReminderView.setVisibility(View.VISIBLE);
                    } else {
                        mNoReminderView.setVisibility(View.GONE);
                    }

                    return true;

                case R.id.save_reminder:
                    actionMode.finish();
                    mMultiSelector.clearSelections();
                    return true;

                default:
                    break;
            }
            return false;
        }
    };

    private void selectReminder(int mClickID) {
        String mStringClickID = Integer.toString(mClickID);

        Intent i = new Intent(getActivity(), EditReminder.class);
        i.putExtra(EditReminder.EXTRA_REMINDER_ID, mStringClickID);
        startActivityForResult(i, 1);
    }

    private void updateToken(String token){

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mAdapter.setItemCount(getDefaultItemCount());
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        Task<InstanceIdResult> token = FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(StuActivity.this,new OnSuccessListener<InstanceIdResult>() {
//            @Override
//            public void onSuccess(InstanceIdResult instanceIdResult) {
//                String newToken = instanceIdResult.getToken();
//
//
//            }
//        });
//
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child("deviceToken");
//        reference.setValue(token.getResult().getToken());
//    }

    @Override
    public void onResume(){
        super.onResume();

        List<Reminder> mTest = rb.getAllReminders();

        if (mTest.isEmpty()) {
            mNoReminderView.setVisibility(View.VISIBLE);
        } else {
            mNoReminderView.setVisibility(View.GONE);
        }

        mAdapter.setItemCount(getDefaultItemCount());
    }

    // Layout manager for recycler view
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    protected int getDefaultItemCount() {
        return 100;
    }

    // Create menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public class SimpleAdapter extends RecyclerView.Adapter<ReminderFragment.SimpleAdapter.VerticalItemHolder> {
        private ArrayList<SimpleAdapter.ReminderItem> mItems;

        public SimpleAdapter() {
            mItems = new ArrayList<>();
        }

        public void setItemCount(int count) {
            mItems.clear();
            mItems.addAll(generateData(count));
            notifyDataSetChanged();
        }

        public void onDeleteItem(int count) {
            mItems.clear();
            mItems.addAll(generateData(count));
        }

        public void removeItemSelected(int selected) {
            if (mItems.isEmpty()) return;
            mItems.remove(selected);
            notifyItemRemoved(selected);
        }

        // View holder for recycler view items
        @NotNull
        @Override
        public VerticalItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(container.getContext());
            View root = inflater.inflate(R.layout.item_event, container, false);

            return new VerticalItemHolder(root,this);
        }

        @Override
        public void onBindViewHolder(ReminderFragment.SimpleAdapter.VerticalItemHolder itemHolder, int position) {
            SimpleAdapter.ReminderItem item = mItems.get(position);
            itemHolder.setReminderTitle(item.mTitle);
            itemHolder.setReminderDateTime(item.mDateTime);
            itemHolder.setReminderRepeatInfo(item.mRepeat, item.mRepeatNo, item.mRepeatType);
            itemHolder.setActiveImage(item.mActive);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        // Class for recycler view items
        public  class ReminderItem {
            public String mTitle;
            public String mDateTime;
            public String mRepeat;
            public String mRepeatNo;
            public String mRepeatType;
            public String mActive;

            public ReminderItem(String Title, String DateTime, String Repeat, String RepeatNo, String RepeatType, String Active) {
                this.mTitle = Title;
                this.mDateTime = DateTime;
                this.mRepeat = Repeat;
                this.mRepeatNo = RepeatNo;
                this.mRepeatType = RepeatType;
                this.mActive = Active;
            }
        }

        // Class to compare date and time so that items are sorted in ascending order
        public class DateTimeComparator implements Comparator {
            DateFormat f = new SimpleDateFormat("dd/mm/yyyy hh:mm");

            public int compare(Object a, Object b) {
                String o1 = ((DaTiSorter)a).getDateTime();
                String o2 = ((DaTiSorter)b).getDateTime();

                try {
                    return f.parse(o1).compareTo(f.parse(o2));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }

        // UI and data class for recycler view items
        public  class VerticalItemHolder extends SwappingHolder
                implements View.OnClickListener, View.OnLongClickListener {
            private TextView mTitleText, mDateAndTimeText, mRepeatInfoText;
            private ImageView mActiveImage ;
            private SimpleAdapter mAdapter;

            public VerticalItemHolder(View itemView, SimpleAdapter adapter) {
                super(itemView, mMultiSelector);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
                itemView.setLongClickable(true);

                // Initialize adapter for the items
                mAdapter = adapter;

                // Initialize views
                mTitleText = itemView.findViewById(R.id.recycle_title);
                mDateAndTimeText = itemView.findViewById(R.id.recycle_date_time);
                mRepeatInfoText = itemView.findViewById(R.id.recycle_repeat_info);
                mActiveImage = itemView.findViewById(R.id.active_image);

            }

            // On clicking a reminder item
            @Override
            public void onClick(View v) {
                if (!mMultiSelector.tapSelection(this)) {
                    mTempPost = mList.getChildAdapterPosition(v);

                    int mReminderClickID = IDmap.get(mTempPost);
                    selectReminder(mReminderClickID);

                } else if(mMultiSelector.getSelectedPositions().isEmpty()){
                    mAdapter.setItemCount(getDefaultItemCount());
                }
            }

            // On long press enter action mode with context menu
            @Override
            public boolean onLongClick(View v) {
                AppCompatActivity activity = (AppCompatActivity)getActivity();
                activity.startSupportActionMode(mDeleteMode);
                mMultiSelector.setSelected(this, true);
                return true;
            }

            // Set reminder title view
            public void setReminderTitle(String title) {
                mTitleText.setText(title);
            }

            // Set date and time views
            public void setReminderDateTime(String datetime) {
                mDateAndTimeText.setText(datetime);
            }

            // Set repeat views
            public void setReminderRepeatInfo(String repeat, String repeatNo, String repeatType) {
                if(repeat.equals("true")){
                    mRepeatInfoText.setText("Setiap " + repeatNo + " " + repeatType + "(s)");
                }else if (repeat.equals("false")) {
                    mRepeatInfoText.setText("Pengulangan Mati");
                }
            }

            // Set active image as on or off
            public void setActiveImage(String active){
                if(active.equals("true")){
                    mActiveImage.setImageResource(R.drawable.ic_notifications_active_white_24dp);
                }else if (active.equals("false")) {
                    mActiveImage.setImageResource(R.drawable.ic_notifications_off_white_24dp);
                }
            }
        }

        // Generate random test data
        public SimpleAdapter.ReminderItem generateDummyData() {
            return new SimpleAdapter.ReminderItem("1", "2", "3", "4", "5", "6");
        }

        // Generate real data for each item
        public List<SimpleAdapter.ReminderItem> generateData(int count) {
            ArrayList<SimpleAdapter.ReminderItem> items = new ArrayList<>();

            // Get all reminders from the database
            List<Reminder> reminders = rb.getAllReminders();

            // Initialize lists
            List<String> Titles = new ArrayList<>();
            List<String> Repeats = new ArrayList<>();
            List<String> RepeatNos = new ArrayList<>();
            List<String> RepeatTypes = new ArrayList<>();
            List<String> Actives = new ArrayList<>();
            List<String> DateAndTime = new ArrayList<>();
            List<Integer> IDList= new ArrayList<>();
            List<DaTiSorter> DateTimeSortList = new ArrayList<>();

            // Add details of all reminders in their respective lists
            for (Reminder r : reminders) {
                Titles.add(r.getTitle());
                DateAndTime.add(r.getDate() + " " + r.getTime());
                Repeats.add(r.getRepeat());
                RepeatNos.add(r.getRepeatNo());
                RepeatTypes.add(r.getRepeatType());
                Actives.add(r.getActive());
                IDList.add(r.getID());
            }

            int key = 0;

            // Add date and time as DateTimeSorter objects
            for(int k = 0; k<Titles.size(); k++){
                DateTimeSortList.add(new DaTiSorter(key, DateAndTime.get(k)));
                key++;
            }

            // Sort items according to date and time in ascending order
            Collections.sort(DateTimeSortList, new SimpleAdapter.DateTimeComparator());

            int k = 0;

            // Add data to each recycler view item
            for (DaTiSorter item:DateTimeSortList) {
                int i = item.getIndex();

                items.add(new SimpleAdapter.ReminderItem(Titles.get(i), DateAndTime.get(i), Repeats.get(i),
                        RepeatNos.get(i), RepeatTypes.get(i), Actives.get(i)));
                IDmap.put(k, IDList.get(i));
                k++;
            }
            return items;
        }
    }


    // Setup menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.keluar:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setMessage("Keluar juga akan menghapus seluruh data pengingat anda,Lanjut?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", (dialog, which) -> {
                            progressDialog.setMessage("Memproses . . .");
                            auth.signOut();

                            getActivity().finish();
                            rb.deleteAll();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                        })
                        .setNegativeButton("Tidak",((dialog, which) -> {
                            Toast.makeText(getActivity()," ",Toast.LENGTH_SHORT).show();
                        }));
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
