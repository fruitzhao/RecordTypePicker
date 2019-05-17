package com.cmcc.mypicker;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.cmcc.mypicker.bean.JsonBean;
import com.cmcc.mypicker.util.DateFormatUtil;
import com.cmcc.mypicker.util.GetJsonDataUtil;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tx_recordType;
    private TextView tx_StartTime;
    private TextView tx_arriveType;

    //笔录类型Picker
    private OptionsPickerView recordTypePicker;

    //时间picker
    private TimePickerView timePickerView;

    //到案方式Picker
    private OptionsPickerView arriveTypePicker;

    //笔录类型三级选项数据
    private List<JsonBean> optionsOneItems = new ArrayList<>();
    private ArrayList<ArrayList<String>> optionsTwoItems = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> optionsThreeItems = new ArrayList<>();

    //到案方式选项数据
    private ArrayList<String> optionsArriveType = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();  ///等数据加载完毕再初始化并显示Picker,以免还未加载完数据就显示,造成APP崩溃
        initView();
    }

    private void initData(){
        initJsonData();   //通过解析json数据初始化笔录类型选项数据

        //初始化到案方式选项数据
        optionsArriveType.add("空");
        optionsArriveType.add("口头传唤");
        optionsArriveType.add("被扭送");
        optionsArriveType.add("自动投案");
    }

    private void initView() {
        tx_recordType = findViewById(R.id.text_type);
        tx_StartTime = findViewById(R.id.text_time_start);
        tx_arriveType = findViewById(R.id.arrive_type);
        //添加一个下拉提示图标
        Drawable arrowDown = getResources().getDrawable(R.drawable.arrow_down);
        arrowDown.setBounds(0,0,80,80);
        tx_recordType.setCompoundDrawables(null,null,arrowDown,null);
        tx_StartTime.setCompoundDrawables(null,null,arrowDown,null);
        tx_arriveType.setCompoundDrawables(null,null,arrowDown,null);
        initRecordTypePicker();
        initTimePicker();
        initArriveTypePicker();
        tx_recordType.setOnClickListener(new View.OnClickListener() {
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
        tx_arriveType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arriveTypePicker.show();
            }
        });
    }

    private void initRecordTypePicker() {
        recordTypePicker = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
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
                tx_recordType.setText(tx);
            }
        })      .setTitleText("类型选择")
                .setContentTextSize(22)  //选项文字大小
                .isRestoreItem(true)   //前一选项变化则后面选项复位到第一项
                .build();
        //填充选项
        recordTypePicker.setPicker(optionsOneItems, optionsTwoItems, optionsThreeItems);
    }

    private void initTimePicker() {
        timePickerView = new TimePickerBuilder(this, new OnTimeSelectListener() {
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
        arriveTypePicker = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tx_arriveType.setText(optionsArriveType.get(options1));
            }
        }).setTitleText("到案方式")
                .setContentTextSize(22)
                .build();
        arriveTypePicker.setPicker(optionsArriveType);
    }

    private void initJsonData() {
        String jsonData = new GetJsonDataUtil().getJson(this, "record_type.json");
        ArrayList<JsonBean> jsonBean = parseData(jsonData);//用Gson 转成实体

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

    private ArrayList<JsonBean> parseData(String jsonData) {
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(jsonData);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

}
