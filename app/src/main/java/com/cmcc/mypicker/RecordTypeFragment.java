package com.cmcc.mypicker;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.cmcc.mypicker.bean.RecordTypeBean;
import com.cmcc.mypicker.util.DateFormatUtil;
import com.cmcc.mypicker.util.GetJsonDataUtil;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class RecordTypeFragment extends Fragment {

    private TextView tx_CaseReason;   //案件原因
    private TextView tx_RecordType;   //笔录类型
    private TextView tx_StartTime;    //开始时间
    private TextView tx_ArriveType;   //到案方式
    //private TextView tx_RecordTimes;  //次数

    //private Spinner mSpinner;   //次数选择spinner


    //笔录类型Picker
    private OptionsPickerView recordTypePicker;

    //时间picker
    private TimePickerView timePickerView;

    //到案方式Picker
    private OptionsPickerView arriveTypePicker;

    //笔录类型三级选项数据
    private List<RecordTypeBean> optionsOneItems = new ArrayList<>();
    private ArrayList<ArrayList<String>> optionsTwoItems = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> optionsThreeItems = new ArrayList<>();

    //到案方式选项数据
    private ArrayList<String> optionsArriveType = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_record_type, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();  ///等数据加载完毕再初始化并显示Picker,以免还未加载完数据就显示,造成APP崩溃
        initView();
    }

    private void initData(){
        initJsonData();   //通过解析json数据初始化笔录类型选项数据

        //初始化到案方式选项数据
        optionsArriveType.clear();
        optionsArriveType.add("空");
        optionsArriveType.add("口头传唤");
        optionsArriveType.add("被扭送");
        optionsArriveType.add("自动投案");
    }

    private void initView() {
        tx_CaseReason = getView().findViewById(R.id.text_case_reason);
        tx_RecordType = getView().findViewById(R.id.text_type);
        tx_StartTime = getView().findViewById(R.id.text_time_start);
        tx_ArriveType = getView().findViewById(R.id.arrive_type);
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
        initRecordTypePicker();
        initTimePicker();
        initArriveTypePicker();
        tx_CaseReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PickReason.class);
                startActivityForResult(intent, 1);
            }
        });
        tx_RecordType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordTypePicker.show();
            }
        });
        tx_StartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerView.show();
            }
        });
        tx_ArriveType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arriveTypePicker.show();
            }
        });
    }

    private void initRecordTypePicker() {
        recordTypePicker = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //创建选中监听
                //获得选中的三级选项内容
//                String opt1tx = optionsOneItems.size() > 0 ?
//                        optionsOneItems.get(options1) : ""; //手动添加选项时
                String opt1tx = optionsOneItems.size() > 0 ?
                        optionsOneItems.get(options1).getPickerViewText() : "";  //解析json数据时
                String opt2tx = optionsTwoItems.size() > 0
                        && optionsTwoItems.get(options1).size() > 0 ?
                        " - " + optionsTwoItems.get(options1).get(options2) : "";
                String opt3tx = optionsTwoItems.size() > 0
                        && optionsThreeItems.get(options1).size() > 0
                        && optionsThreeItems.get(options1).get(options2).size() > 0 ?
                        " - " + optionsThreeItems.get(options1).get(options2).get(options3) : "";
                String tx = opt1tx + opt2tx + opt3tx;
                tx_RecordType.setText(tx);
            }
        })      .setTitleText("类型选择")
                .setContentTextSize(22)  //选项文字大小
                .isRestoreItem(true)   //前一选项变化则后面选项复位到第一项
                .build();
        //填充选项
        recordTypePicker.setPicker(optionsOneItems, optionsTwoItems, optionsThreeItems);
    }

    private void initTimePicker() {
        timePickerView = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tx_StartTime.setText(DateFormatUtil.DateToString(date, "yyyy-MM-dd HH:mm"));
                //Toast.makeText(MainActivity.this, date.toString(), Toast.LENGTH_SHORT).show();
            }
        }).setType(new boolean[]{true, true, true, true, true, false})
                .isCenterLabel(true)
                .build();
    }

    private void initArriveTypePicker() {
        arriveTypePicker = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tx_ArriveType.setText(optionsArriveType.get(options1));
            }
        }).setTitleText("到案方式")
                .setContentTextSize(22)
                .build();
        arriveTypePicker.setPicker(optionsArriveType);
    }

    private void initJsonData() {
        String jsonData = new GetJsonDataUtil().getJson(getActivity(), "record_type.json");
        ArrayList<RecordTypeBean> jsonBean = parseData(jsonData);//用Gson 转成实体

        optionsOneItems = jsonBean;

        for(int i = 0; i < jsonBean.size(); i++) { //遍历一级选项
            ArrayList<String> option2Of1 = new ArrayList<>(); //该一级选项下的二级选项列表
            ArrayList<ArrayList<String>> option3Of1 = new ArrayList<>(); //该一级选项下的所有三级选项列表

            for(int j = 0; j < jsonBean.get(i).getTypeOf2().size(); j++) { //遍历该一级选项下的所有二级选项
                String option2Name = jsonBean.get(i).getTypeOf2().get(j).getName();
                option2Of1.add(option2Name);
                ArrayList<String> option3Of2 = new ArrayList<>(); //该二级选项下的三级选项列表
                option3Of2.addAll(jsonBean.get(i).getTypeOf2().get(j).getTypeOf3());
                option3Of1.add(option3Of2);
            }

            optionsTwoItems.add(option2Of1);
            optionsThreeItems.add(option3Of1);
        }

    }

    private ArrayList<RecordTypeBean> parseData(String jsonData) {
        ArrayList<RecordTypeBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(jsonData);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                RecordTypeBean entity = gson.fromJson(data.optJSONObject(i).toString(), RecordTypeBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
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
