package com.example.wojciech.fibaro_hc2_control.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wojciech.fibaro_hc2_control.R;
import com.example.wojciech.fibaro_hc2_control.model.Device;
import com.example.wojciech.fibaro_hc2_control.service.ServiceHC2;
import com.example.wojciech.fibaro_hc2_control.utils.DividerItemDecoration;
import com.example.wojciech.fibaro_hc2_control.utils.HC2DeviceUtil;

import java.util.ArrayList;
import java.util.List;

public class DevicesFragment extends Fragment {
    private String TAG = DevicesFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "devices";
    private ArrayList<Device> deviceList = new ArrayList<>();
    DeviceListRecyclerViewAdapter deviceListRecyclerViewAdapter;

    private OnDevicesFragmentInteractionListener mListener;

    public static DevicesFragment newInstance(ArrayList<Device> deviceList) {
        DevicesFragment fragment = new DevicesFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM1, deviceList);
        fragment.setArguments(args);
        return fragment;
    }

    public static DevicesFragment newInstance() {
        DevicesFragment fragment = new DevicesFragment();
        return fragment;
    }

    public DevicesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ARG_PARAM1,deviceList);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            deviceList = getArguments().getParcelableArrayList(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_devices, container, false);
        if (deviceList == null) deviceList = new ArrayList<>();
        if (savedInstanceState != null) {
            deviceList = savedInstanceState.getParcelableArrayList(ARG_PARAM1);
        } else {
            //deviceList = getArguments().getParcelableArrayList(ARG_PARAM1);
        }
        setupRecyclerView(rv);

        return rv;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        // Create adapter passing in the sample user data
        deviceListRecyclerViewAdapter = new DeviceListRecyclerViewAdapter(recyclerView.getContext(), deviceList);
        deviceListRecyclerViewAdapter.SetOnItemClickListener(new DeviceListRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onBinaryLightSwitch(Device device, boolean value) {
                Intent i = new Intent(getContext(), ServiceHC2.class);
                //i.putExtra(ServiceHC2.EXTRA_REQUESTED_ACTION, ServiceHC2.ServiceHC2Action.GetSections.getValue());
                i.putExtra(ServiceHC2.EXTRA_REQUESTED_ACTION, ServiceHC2.ServiceHC2Action.CallActionSwitch.getValue());
                i.putExtra(ServiceHC2.EXTRA_REQUESTED_OBJECT, device);
                i.putExtra(ServiceHC2.EXTRA_REQUESTED_VALUE, value);


                getContext().startService(i);
            }

            @Override
            public void onDimmableLightSlide(Device device, int progress) {
               // Toast.makeText(getContext(), "CLICKED " + device.name, Toast.LENGTH_SHORT).show();
                //mListener.onFragmentInteraction(device);
                Intent i = new Intent(getContext(), ServiceHC2.class);
                i.putExtra(ServiceHC2.EXTRA_REQUESTED_ACTION, ServiceHC2.ServiceHC2Action.CallActionDimm.getValue());
                i.putExtra(ServiceHC2.EXTRA_REQUESTED_OBJECT, device);
                i.putExtra(ServiceHC2.EXTRA_REQUESTED_VALUE, progress);
                getContext().startService(i);
            }

        });
        // Attach the adapter to the recyclerview to populate items
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(deviceListRecyclerViewAdapter);
        // Set layout manager to position the items
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        deviceListRecyclerViewAdapter.notifyDataSetChanged();

    }

    public void update(List<Device> deviceList) {
        if (deviceListRecyclerViewAdapter != null) {
            this.deviceList.clear();
            this.deviceList.addAll(deviceList);
            deviceListRecyclerViewAdapter.notifyDataSetChanged();
        }
        //mListener.onFragmentInteraction(cvList);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnDevicesFragmentInteractionListener) context;
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
    public interface OnDevicesFragmentInteractionListener {
        void onFragmentInteraction(Device device);
    }


    public static class DeviceListRecyclerViewAdapter extends
            RecyclerView.Adapter<DeviceListRecyclerViewAdapter.DeviceViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        public OnItemClickListener mItemClickListener;
        // Provide a direct reference to each of the views within a data item
        // Used to cache the views within the item layout for fast access

        public class DeviceViewHolder extends RecyclerView.ViewHolder {
            // Your holder should contain a member variable
            // for any view that will be set as you render a row
            public View view;
            public TextView title;
            public TextView description;
            public Switch binarySwitch;
            public SeekBar dimmableSeek;

            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview

            public DeviceViewHolder(View itemView) {
                super(itemView);
                this.view = itemView;
                this.title = (TextView) itemView.findViewById(R.id.item_title);
                this.description = (TextView) itemView.findViewById(R.id.item_description);
                this.binarySwitch = (Switch) itemView.findViewById(R.id.item_binary_switch);
                this.dimmableSeek = (SeekBar) itemView.findViewById(R.id.item_dimmable_switch);

            }

        }


        // Store a member variable for the users
        private ArrayList<Device> devicesArrayList;
        // Store the context for later use
        private Context context;

        // Pass in the context and users array into the constructor
        public DeviceListRecyclerViewAdapter(Context context, ArrayList<Device> devicesArrayList) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            this.devicesArrayList = devicesArrayList;
            this.context = context;
        }


        @Override
        public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Inflate the custom layout
            View itemView = LayoutInflater.from(context).
                    inflate(R.layout.item_device, parent, false);
            itemView.setBackgroundResource(mBackground);
            // Return a new holder instance
            return new DeviceViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final DeviceViewHolder holder, int position) {
// Get the data model based on position
            final Device device = devicesArrayList.get(position);
            // Set item views based on the data model
            holder.title.setText(device.name);
            holder.description.setVisibility(View.GONE);
            //holder.description.setText(device.name);
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            holder.binarySwitch.setEnabled(false);
            holder.dimmableSeek.setEnabled(false);
            if(device.type.equals(HC2DeviceUtil.TYPE_BINARY_LIGHT)){
                holder.binarySwitch.setVisibility(View.VISIBLE);
                holder.dimmableSeek.setVisibility(View.GONE);
                holder.binarySwitch.setEnabled(true);
                holder.binarySwitch.setChecked(device.properties.value.equals("1")?true:false);
            }else
            if(device.type.equals(HC2DeviceUtil.TYPE_DIMMABLE_LIGHT)){
                holder.binarySwitch.setVisibility(View.VISIBLE);
                holder.dimmableSeek.setVisibility(View.VISIBLE);
                int val = Integer.parseInt(device.properties.value);
                if(val>0){
                    holder.binarySwitch.setChecked(true);
                }else{
                    holder.binarySwitch.setChecked(false);
                }
                holder.dimmableSeek.setProgress(val);
                holder.binarySwitch.setEnabled(true);
                holder.dimmableSeek.setEnabled(true);

            }else{
                holder.binarySwitch.setVisibility(View.GONE);
                holder.dimmableSeek.setVisibility(View.GONE);
            }
            holder.binarySwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onBinaryLightSwitch(device,holder.binarySwitch.isChecked());
                }
            });
            holder.dimmableSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    mItemClickListener.onDimmableLightSlide(device,seekBar.getProgress());

                }
            });
        }

        @Override
        public int getItemCount() {
            return devicesArrayList.size();
        }

        public interface OnItemClickListener {
            void onBinaryLightSwitch(Device device,boolean value);
            void onDimmableLightSlide(Device device,int progress);
        }

        public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
            this.mItemClickListener = mItemClickListener;
        }

    }
}