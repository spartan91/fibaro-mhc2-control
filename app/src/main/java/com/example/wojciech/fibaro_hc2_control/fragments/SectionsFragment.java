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
import com.example.wojciech.fibaro_hc2_control.model.Section;
import com.example.wojciech.fibaro_hc2_control.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnSectionsFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SectionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SectionsFragment extends Fragment {
    private String TAG = SectionsFragment.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private CVListRecyclerViewAdapter adapter;
    private static final String ARG_PARAM1 = "sections";
    private ArrayList<Section> sectionList = new ArrayList<>();
    SectionListRecyclerViewAdapter sectionListRecyclerViewAdapter;

    private OnSectionsFragmentInteractionListener mListener;

    public static SectionsFragment newInstance(ArrayList<Section> sectionList) {
        SectionsFragment fragment = new SectionsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM1, sectionList);
        fragment.setArguments(args);
        return fragment;
    }

    public static SectionsFragment newInstance() {
        SectionsFragment fragment = new SectionsFragment();
        return fragment;
    }

    public SectionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ARG_PARAM1,sectionList);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sectionList = getArguments().getParcelableArrayList(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_sections, container, false);
        if (sectionList == null) sectionList = new ArrayList<>();
        if (savedInstanceState != null) {
            sectionList = savedInstanceState.getParcelableArrayList(ARG_PARAM1);
        } else {
            //sectionList = getArguments().getParcelableArrayList(ARG_PARAM1);
        }
        setupRecyclerView(rv);

        return rv;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        // Create adapter passing in the sample user data
        sectionListRecyclerViewAdapter = new SectionListRecyclerViewAdapter(recyclerView.getContext(), sectionList);
        sectionListRecyclerViewAdapter.SetOnItemClickListener(new SectionListRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Section section) {
                Toast.makeText(getContext(),"CLICKED "+section.name,Toast.LENGTH_SHORT).show();
                mListener.onFragmentInteraction(section);
            }
        });
        // Attach the adapter to the recyclerview to populate items
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(sectionListRecyclerViewAdapter);
        // Set layout manager to position the items
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        sectionListRecyclerViewAdapter.notifyDataSetChanged();
    }

    public void update(List<Section> sectionList) {
        if (sectionListRecyclerViewAdapter != null) {
            this.sectionList.clear();
            this.sectionList.addAll(sectionList);
            sectionListRecyclerViewAdapter.notifyDataSetChanged();
        }
        //mListener.onFragmentInteraction(cvList);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnSectionsFragmentInteractionListener) context;
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
    public interface OnSectionsFragmentInteractionListener {
        void onFragmentInteraction(Section section);
    }


    public static class SectionListRecyclerViewAdapter extends
            RecyclerView.Adapter<SectionListRecyclerViewAdapter.SectionViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        public OnItemClickListener mItemClickListener;
        // Provide a direct reference to each of the views within a data item
        // Used to cache the views within the item layout for fast access

        public class SectionViewHolder extends RecyclerView.ViewHolder {
            // Your holder should contain a member variable
            // for any view that will be set as you render a row
            public View view;
            public TextView title;
            public TextView description;

            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview

            public SectionViewHolder(View itemView) {
                super(itemView);
                this.view = itemView;
                this.title = (TextView) itemView.findViewById(R.id.item_title);
                this.description = (TextView) itemView.findViewById(R.id.item_description);

            }

        }


        // Store a member variable for the users
        private ArrayList<Section> sectionsArrayList;
        // Store the context for later use
        private Context context;

        // Pass in the context and users array into the constructor
        public SectionListRecyclerViewAdapter(Context context, ArrayList<Section> sectionsArrayList) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            this.sectionsArrayList = sectionsArrayList;
            this.context = context;
        }


        @Override
        public SectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Inflate the custom layout
            View itemView = LayoutInflater.from(context).
                    inflate(R.layout.item_section, parent, false);
            itemView.setBackgroundResource(mBackground);
            // Return a new holder instance
            return new SectionViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final SectionViewHolder holder, int position) {
// Get the data model based on position
            final Section section = sectionsArrayList.get(position);
            // Set item views based on the data model
            holder.title.setText(section.name);
            holder.description.setVisibility(View.GONE);
            //holder.description.setText(section.name);
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(section);
                }
            });
        }

        @Override
        public int getItemCount() {
            return sectionsArrayList.size();
        }

        public interface OnItemClickListener {
            void onItemClick(Section section);
        }

        public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
            this.mItemClickListener = mItemClickListener;
        }

    }
}
