package com.cmcc.mypicker;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmcc.mypicker.util.PickerUtil;

import static android.app.Activity.RESULT_OK;

public class RecordTypeFragment extends Fragment implements View.OnClickListener {

    private TextView tx_CaseReason;   //案件原因
    private TextView tx_RecordType;   //笔录类型
    private TextView tx_StartTime;    //开始时间
    private TextView tx_ArriveType;   //到案方式
    //private TextView tx_RecordTimes;  //次数

    //private Spinner mSpinner;   //次数选择spinner


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_record_type, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }


    private void initView() {
        tx_CaseReason = getView().findViewById(R.id.text_case_reason);
        tx_RecordType = getView().findViewById(R.id.text_type);
        tx_StartTime = getView().findViewById(R.id.text_time_start);
        tx_ArriveType = getView().findViewById(R.id.text_arrive_type);
        tx_RecordType.setOnClickListener(this);
        tx_ArriveType.setOnClickListener(this);
        tx_StartTime.setOnClickListener(this);
        //tx_RecordTimes = getView().findViewById(R.id.text_record_times);

//        mSpinner = getView().findViewById(R.id.times_spinner);
//        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    String times= mSpinner.getItemAtPosition(position).toString();
//                    tx_RecordTimes.setText(times);
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//                }
//        });
//        mSpinner.setGravity(Gravity.CENTER_HORIZONTAL);

        //添加一个提示打开案件原因选项的右箭头
        Drawable arrowRight = getResources().getDrawable(R.drawable.arrow_right);
        arrowRight.setBounds(0,0,80,80);
        tx_CaseReason.setCompoundDrawables(null, null, arrowRight, null);
        //添加一个提示弹出选择器的下箭头
        Drawable arrowDown = getResources().getDrawable(R.drawable.arrow_down);
        arrowDown.setBounds(0,0,80,80);
        tx_RecordType.setCompoundDrawables(null,null,arrowDown,null);
        tx_StartTime.setCompoundDrawables(null,null,arrowDown,null);
        tx_ArriveType.setCompoundDrawables(null,null,arrowDown,null);

        tx_CaseReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PickReason.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_arrive_type:
                PickerUtil.showOptionsPicker(getActivity(), PickerUtil.ARRIVE_TYPE, "到案方式",
                        new PickerUtil.selectTextListener() {
                            @Override
                            public void selectText(String str) {
                                tx_ArriveType.setText(str);
                            }
                        });
                break;
            case R.id.text_type:
                PickerUtil.showOptionsPicker(getActivity(), PickerUtil.CASE_WRITER_TYPE, "笔录类型",
                        new PickerUtil.selectTextListener() {
                            @Override
                            public void selectText(String str) {
                                tx_RecordType.setText(str);
                            }
                        });
                break;
            case R.id.text_time_start:
                PickerUtil.showTimePicker(getActivity(), PickerUtil.SHOW_YEAR_MONTH_DAY_HOUR_MINUTE, "开始时间",
                        new PickerUtil.selectTextListener() {
                            @Override
                            public void selectText(String str) {
                                tx_StartTime.setText(str);
                            }
                        });
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String pickedReason = data.getStringExtra("reason_picked");
                    tx_CaseReason.setText(pickedReason);
                }
                break;
            default:
        }
    }
}
