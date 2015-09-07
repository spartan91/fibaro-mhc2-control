package com.example.wojciech.fibaro_hc2_control.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wojciech.fibaro_hc2_control.R;
import com.example.wojciech.fibaro_hc2_control.model.Room;
import com.example.wojciech.fibaro_hc2_control.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;


public class RoomsFragment extends Fragment {
    private String TAG = RoomsFragment.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private CVListRecyclerViewAdapter adapter;
    private static final String ARG_PARAM1 = "rooms";
    private ArrayList<Room> roomList = new ArrayList<>();
    RoomListRecyclerViewAdapter roomListRecyclerViewAdapter;

    private OnRoomsFragmentInteractionListener mListener;

    public static RoomsFragment newInstance(ArrayList<Room> roomList) {
        RoomsFragment fragment = new RoomsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM1, roomList);
        fragment.setArguments(args);
        return fragment;
    }

    public static RoomsFragment newInstance() {
        RoomsFragment fragment = new RoomsFragment();
        return fragment;
    }

    public RoomsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ARG_PARAM1,roomList);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            roomList = getArguments().getParcelableArrayList(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_rooms, container, false);
        if (roomList == null) roomList = new ArrayList<>();
        if (savedInstanceState != null) {
            roomList = savedInstanceState.getParcelableArrayList(ARG_PARAM1);
        } else {
            //roomList = getArguments().getParcelableArrayList(ARG_PARAM1);
        }
        setupRecyclerView(rv);

        return rv;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        // Create adapter passing in the sample user data
        roomListRecyclerViewAdapter = new RoomListRecyclerViewAdapter(recyclerView.getContext(), roomList);
        roomListRecyclerViewAdapter.SetOnItemClickListener(new RoomListRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Room room) {
                Toast.makeText(getContext(), "CLICKED " + room.name, Toast.LENGTH_SHORT).show();
                mListener.onFragmentInteraction(room);
            }
        });
        // Attach the adapter to the recyclerview to populate items
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(roomListRecyclerViewAdapter);
        // Set layout manager to position the items
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        roomListRecyclerViewAdapter.notifyDataSetChanged();

    }

    public void update(List<Room> roomList) {
        if (roomListRecyclerViewAdapter != null) {
            this.roomList.clear();
            this.roomList.addAll(roomList);
            roomListRecyclerViewAdapter.notifyDataSetChanged();
        }
        //mListener.onFragmentInteraction(cvList);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnRoomsFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener, by Context");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnRoomsFragmentInteractionListener {
        void onFragmentInteraction(Room room);
    }


    public static class RoomListRecyclerViewAdapter extends
            RecyclerView.Adapter<RoomListRecyclerViewAdapter.RoomViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        public OnItemClickListener mItemClickListener;
        // Provide a direct reference to each of the views within a data item
        // Used to cache the views within the item layout for fast access

        public class RoomViewHolder extends RecyclerView.ViewHolder {
            // Your holder should contain a member variable
            // for any view that will be set as you render a row
            public View view;
            public TextView title;
            public TextView description;

            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview

            public RoomViewHolder(View itemView) {
                super(itemView);
                this.view = itemView;
                this.title = (TextView) itemView.findViewById(R.id.item_title);
                this.description = (TextView) itemView.findViewById(R.id.item_description);

            }

        }


        // Store a member variable for the users
        private ArrayList<Room> roomsArrayList;
        // Store the context for later use
        private Context context;

        // Pass in the context and users array into the constructor
        public RoomListRecyclerViewAdapter(Context context, ArrayList<Room> roomsArrayList) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            this.roomsArrayList = roomsArrayList;
            this.context = context;
        }


        @Override
        public RoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Inflate the custom layout
            View itemView = LayoutInflater.from(context).
                    inflate(R.layout.item_room, parent, false);
            itemView.setBackgroundResource(mBackground);
            // Return a new holder instance
            return new RoomViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final RoomViewHolder holder, int position) {
// Get the data model based on position
            final Room room = roomsArrayList.get(position);
            // Set item views based on the data model
            holder.title.setText(room.name);
            holder.description.setVisibility(View.GONE);
            //holder.description.setText(room.name);
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(room);
                }
            });
        }

        @Override
        public int getItemCount() {
            return roomsArrayList.size();
        }

        public interface OnItemClickListener {
            void onItemClick(Room room);
        }

        public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
            this.mItemClickListener = mItemClickListener;
        }

    }
}