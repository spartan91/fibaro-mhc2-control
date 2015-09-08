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

import com.example.wojciech.fibaro_hc2_control.MainActivity;
import com.example.wojciech.fibaro_hc2_control.R;
import com.example.wojciech.fibaro_hc2_control.model.Room;
import com.example.wojciech.fibaro_hc2_control.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;


public class RoomsFragment extends Fragment {
    private String TAG = RoomsFragment.class.getSimpleName();
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
        }
        setupRecyclerView(rv);

        return rv;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {

        roomListRecyclerViewAdapter = new RoomListRecyclerViewAdapter(recyclerView.getContext(), roomList);
        roomListRecyclerViewAdapter.SetOnItemClickListener(new RoomListRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Room room) {
                Toast.makeText(getContext(), "CLICKED " + room.name, Toast.LENGTH_SHORT).show();
                mListener.onFragmentInteraction(room);
            }
        });
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(roomListRecyclerViewAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        roomListRecyclerViewAdapter.notifyDataSetChanged();

    }

    public void update(List<Room> roomList) {
        if (roomListRecyclerViewAdapter != null) {
            this.roomList.clear();
            this.roomList.addAll(roomList);
            roomListRecyclerViewAdapter.notifyDataSetChanged();
        }
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
        ((MainActivity) context).onSectionAttached(3);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnRoomsFragmentInteractionListener {
        void onFragmentInteraction(Room room);
    }


    public static class RoomListRecyclerViewAdapter extends
            RecyclerView.Adapter<RoomListRecyclerViewAdapter.RoomViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        public OnItemClickListener mItemClickListener;

        public class RoomViewHolder extends RecyclerView.ViewHolder {
            public View view;
            public TextView title;
            public TextView description;


            public RoomViewHolder(View itemView) {
                super(itemView);
                this.view = itemView;
                this.title = (TextView) itemView.findViewById(R.id.item_title);
                this.description = (TextView) itemView.findViewById(R.id.item_description);

            }

        }



        private ArrayList<Room> roomsArrayList;

        private Context context;


        public RoomListRecyclerViewAdapter(Context context, ArrayList<Room> roomsArrayList) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            this.roomsArrayList = roomsArrayList;
            this.context = context;
        }


        @Override
        public RoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(context).
                    inflate(R.layout.item_room, parent, false);
            itemView.setBackgroundResource(mBackground);

            return new RoomViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final RoomViewHolder holder, int position) {
            final Room room = roomsArrayList.get(position);
            holder.title.setText(room.name);
            holder.description.setVisibility(View.GONE);
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