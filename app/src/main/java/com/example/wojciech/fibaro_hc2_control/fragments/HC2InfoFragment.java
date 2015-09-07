package com.example.wojciech.fibaro_hc2_control.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.wojciech.fibaro_hc2_control.MainActivity;
import com.example.wojciech.fibaro_hc2_control.R;
import com.example.wojciech.fibaro_hc2_control.model.HC2;

import java.lang.reflect.Field;

public class HC2InfoFragment extends Fragment {

    private TableLayout tableLayout;

    public HC2InfoFragment() {
        // Required empty public constructor
    }

    public static HC2InfoFragment newInstance() {
        HC2InfoFragment fragment = new HC2InfoFragment();
        return fragment;
    }

    public static HC2InfoFragment newInstance(HC2 hC2) {
        HC2InfoFragment fragment = new HC2InfoFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hc2_info, container, false);
        tableLayout = (TableLayout) v.findViewById(R.id.table_layout);
        return v;
    }

    public void setUpData(HC2 hC2) {
        if (tableLayout != null && hC2 != null) {
            tableLayout.removeAllViews();
            for (Field field : hC2.getClass().getFields()) {
                if (field.getName().contains("CREATOR")
                        || field.getName().contains("CONTENTS_FILE_DESCRIPTOR")
                        || field.getName().contains("PARCELABLE_WRITE_RETURN_VALUE"))
                    continue;
                try {
                    TableRow row = new TableRow(getContext());

                    TextView t = new TextView(getContext());
                    t.setText(field.getName());

                    TextView t2 = new TextView(getContext());
                    t2.setText(field.get(hC2).toString());


                    // add the TextView  to the new TableRow
                    row.addView(t);
                    row.addView(t2);
                    // add the TableRow to the TableLayout
                    tableLayout.addView(row);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) context).onSectionAttached(1);
    }

}
