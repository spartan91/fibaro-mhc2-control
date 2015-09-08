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
        }
        setupRecyclerView(rv);

        return rv;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        sectionListRecyclerViewAdapter = new SectionListRecyclerViewAdapter(recyclerView.getContext(), sectionList);
        sectionListRecyclerViewAdapter.SetOnItemClickListener(new SectionListRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Section section) {
                mListener.onFragmentInteraction(section);
            }
        });
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(sectionListRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        sectionListRecyclerViewAdapter.notifyDataSetChanged();
    }

    public void update(List<Section> sectionList) {
        if (sectionListRecyclerViewAdapter != null) {
            this.sectionList.clear();
            this.sectionList.addAll(sectionList);
            sectionListRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnSectionsFragmentInteractionListener) context;
        } catch (ClassCastException e) {
             throw new ClassCastException(context.toString()
                    + " must implement OnSectionsFragmentInteractionListener");
        }
        ((MainActivity) context).onSectionAttached(2);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSectionsFragmentInteractionListener {
        void onFragmentInteraction(Section section);
    }


    public static class SectionListRecyclerViewAdapter extends
            RecyclerView.Adapter<SectionListRecyclerViewAdapter.SectionViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        public OnItemClickListener mItemClickListener;

        public class SectionViewHolder extends RecyclerView.ViewHolder {
            public View view;
            public TextView title;
            public TextView description;


            public SectionViewHolder(View itemView) {
                super(itemView);
                this.view = itemView;
                this.title = (TextView) itemView.findViewById(R.id.item_title);
                this.description = (TextView) itemView.findViewById(R.id.item_description);

            }

        }


        private ArrayList<Section> sectionsArrayList;
        private Context context;

        public SectionListRecyclerViewAdapter(Context context, ArrayList<Section> sectionsArrayList) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            this.sectionsArrayList = sectionsArrayList;
            this.context = context;
        }


        @Override
        public SectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).
                    inflate(R.layout.item_section, parent, false);
            itemView.setBackgroundResource(mBackground);
            return new SectionViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final SectionViewHolder holder, int position) {
            final Section section = sectionsArrayList.get(position);
            holder.title.setText(section.name);
            holder.description.setVisibility(View.GONE);
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
