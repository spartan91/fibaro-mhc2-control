package com.example.wojciech.fibaro_hc2_control.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.wojciech.fibaro_hc2_control.MainActivity;
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
        }
        setupRecyclerView(rv);

        return rv;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        deviceListRecyclerViewAdapter = new DeviceListRecyclerViewAdapter(recyclerView.getContext(), deviceList);
        deviceListRecyclerViewAdapter.SetOnItemClickListener(new DeviceListRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onBinaryLightSwitch(Device device, boolean value) {
                Intent i = new Intent(getContext(), ServiceHC2.class);
                i.putExtra(ServiceHC2.EXTRA_REQUESTED_ACTION, ServiceHC2.ServiceHC2Action.CallActionSwitch.getValue());
                i.putExtra(ServiceHC2.EXTRA_REQUESTED_OBJECT, device);
                i.putExtra(ServiceHC2.EXTRA_REQUESTED_VALUE, value);
                getContext().startService(i);
            }

            @Override
            public void onDimmableLightSlide(Device device, int progress) {
                Intent i = new Intent(getContext(), ServiceHC2.class);
                i.putExtra(ServiceHC2.EXTRA_REQUESTED_ACTION, ServiceHC2.ServiceHC2Action.CallActionDimm.getValue());
                i.putExtra(ServiceHC2.EXTRA_REQUESTED_OBJECT, device);
                i.putExtra(ServiceHC2.EXTRA_REQUESTED_VALUE, progress);
                getContext().startService(i);
            }

        });
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(deviceListRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        deviceListRecyclerViewAdapter.notifyDataSetChanged();

    }

    public void setUp(List<Device> deviceList) {
        if (deviceListRecyclerViewAdapter != null) {
            this.deviceList.clear();
            this.deviceList.addAll(deviceList);
            deviceListRecyclerViewAdapter.notifyDataSetChanged();
        }
    }
    public void update(List<Device> deviceList) {
        if (deviceListRecyclerViewAdapter != null) {
            for(Device dNew : deviceList){
                int i=0;
                for(Device dd : this.deviceList) {
                    if (dNew.id == dd.id){
                        this.deviceList.set(i, dNew);
                    }
                    i++;
                }
            }
            deviceListRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) context).onSectionAttached(4);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



    public static class DeviceListRecyclerViewAdapter extends
            RecyclerView.Adapter<DeviceListRecyclerViewAdapter.DeviceViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        public OnItemClickListener mItemClickListener;

        public class DeviceViewHolder extends RecyclerView.ViewHolder {
            public View view;
            public TextView title;
            public TextView description;
            public Switch binarySwitch;
            public SeekBar dimmableSeek;


            public DeviceViewHolder(View itemView) {
                super(itemView);
                this.view = itemView;
                this.title = (TextView) itemView.findViewById(R.id.item_title);
                this.description = (TextView) itemView.findViewById(R.id.item_description);
                this.binarySwitch = (Switch) itemView.findViewById(R.id.item_binary_switch);
                this.dimmableSeek = (SeekBar) itemView.findViewById(R.id.item_dimmable_switch);

            }

        }


        private ArrayList<Device> devicesArrayList;
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
            View itemView = LayoutInflater.from(context).
                    inflate(R.layout.item_device, parent, false);
            itemView.setBackgroundResource(mBackground);
            return new DeviceViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final DeviceViewHolder holder, int position) {
            final Device device = devicesArrayList.get(position);
            holder.title.setText(device.name);
            holder.description.setVisibility(View.GONE);
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
                    if(holder.dimmableSeek.getVisibility() == View.VISIBLE){
                        holder.dimmableSeek.setProgress(holder.binarySwitch.isChecked()?100:0);
                    }
                    holder.dimmableSeek.setEnabled(false);
                    holder.binarySwitch.setEnabled(false);
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
                    holder.dimmableSeek.setEnabled(false);
                    holder.binarySwitch.setEnabled(false);
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