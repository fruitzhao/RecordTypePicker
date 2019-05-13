package com.cmcc.mypicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.cmcc.mypicker.bean.JsonBean;
import com.cmcc.mypicker.util.GetJsonDataUtil;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btn_ShowPicker;

    //笔录类型Picker
    private OptionsPickerView recordTypePicker;

    //三级选项数据
    private List<JsonBean> optionsOneItems = new ArrayList<>();
    private ArrayList<ArrayList<String>> optionsTwoItems = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> optionsThreeItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initData();  ///等数据加载完毕再初始化并显示Picker,以免还未加载完数据就显示,造成APP崩溃
        initJsonData();
        initView();
    }

    private void initView() {
        btn_ShowPicker = findViewById(R.id.show_picker);
        initTypePicker();
        btn_ShowPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordTypePicker.show();
            }
        });
    }

    private void initTypePicker() {
        recordTypePicker = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                String opt1tx = optionsOneItems.size() > 0 ?
//                        optionsOneItems.get(options1) : ""; //手动添加选项时
                String opt1tx = optionsOneItems.size() > 0 ?
                        optionsOneItems.get(options1).getPickerViewText() : "";  //解析json数据时
                String opt2tx = optionsTwoItems.size() > 0
                        && optionsTwoItems.get(options1).size() > 0 ?
                        optionsTwoItems.get(options1).get(options2) : "";
                String opt3tx = optionsTwoItems.size() > 0
                        && optionsThreeItems.get(options1).size() > 0
                        && optionsThreeItems.get(options1).get(options2).size() > 0 ?
                        optionsThreeItems.get(options1).get(options2).get(options3) : "";
                String tx = opt1tx + " " + opt2tx + " " + opt3tx;
                Toast.makeText(MainActivity.this, tx, Toast.LENGTH_SHORT).show();
            }
        })      .setTitleText("类型选择")
                .setContentTextSize(22)  //选项文字大小
                .isRestoreItem(true)   //前一选项变化则后面选项复位到第一项
                .build();
        //填充选项
        recordTypePicker.setPicker(optionsOneItems, optionsTwoItems, optionsThreeItems);
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

//    private void initData() {
//        //一级选项--
//        //填充Picker一级选项数据
//        optionsOneItems.add("行政");
//        optionsOneItems.add("刑事");
//
//        //二级选项--
//        //属于一级选项1
//        ArrayList<String> optionList2Of1 = new ArrayList<>();
//        optionList2Of1.add("询问");
//        optionList2Of1.add("检查");
//        optionList2Of1.add("勘察");
//        optionList2Of1.add("现场");
//        //属于一级选项2
//        ArrayList<String> optionList2Of2 = new ArrayList<>();
//        optionList2Of2.add("询问");
//        optionList2Of2.add("讯问");
//        optionList2Of2.add("现场辨认");
//        optionList2Of2.add("检查");
//        optionList2Of2.add("查封");
//        optionList2Of2.add("扣押");
//        optionList2Of2.add("搜查");
//        optionList2Of2.add("提取");
//        optionList2Of2.add("复验复查");
//        optionList2Of2.add("侦查实验");
//        //填充Picker二级选项数据
//        optionsTwoItems.add(optionList2Of1);
//        optionsTwoItems.add(optionList2Of2);
//
//        //三级选项数据--
//        //属于二级选项1-1
//        ArrayList<String> optionList3Of1_1 = new ArrayList<>();
//        optionList3Of1_1.add("证人");
//        optionList3Of1_1.add("被侵害人");
//        optionList3Of1_1.add("违法嫌疑人");
//        //属于二级选项1-2
//        ArrayList<String> optionList3Of1_2 = new ArrayList<>();
//        //属于二级选项1-3
//        ArrayList<String> optionList3Of1_3 = new ArrayList<>();
//        //属于二级选项1-4
//        ArrayList<String> optionList3Of1_4 = new ArrayList<>();
//        //属于一级选项1
//        ArrayList<ArrayList<String>> optionList3Of1 = new ArrayList<>();
//        optionList3Of1.add(optionList3Of1_1);
//        optionList3Of1.add(optionList3Of1_2);
//        optionList3Of1.add(optionList3Of1_3);
//        optionList3Of1.add(optionList3Of1_4);
//
//        //属于二级选项2-1
//        ArrayList<String> optionList3Of2_1 = new ArrayList<>();
//        optionList3Of2_1.add("被害人");
//        optionList3Of2_1.add("证人");
//        //属于二级选项2-2
//        ArrayList<String> optionList3Of2_2 = new ArrayList<>();
//        optionList3Of2_2.add("犯罪嫌疑人");
//        //属于二级选项2-3
//        ArrayList<String> optionList3Of2_3 = new ArrayList<>();
//        //属于二级选项2-4
//        ArrayList<String> optionList3Of2_4 = new ArrayList<>();
//        //属于二级选项2-5
//        ArrayList<String> optionList3Of2_5 = new ArrayList<>();
//        //属于二级选项2-6
//        ArrayList<String> optionList3Of2_6 = new ArrayList<>();
//        //属于二级选项2-7
//        ArrayList<String> optionList3Of2_7 = new ArrayList<>();
//        //属于二级选项2-8
//        ArrayList<String> optionList3Of2_8 = new ArrayList<>();
//        //属于二级选项2-9
//        ArrayList<String> optionList3Of2_9 = new ArrayList<>();
//        //属于二级选项2-10
//        ArrayList<String> optionList3Of2_10 = new ArrayList<>();
//
//        //属于一级选项2
//        ArrayList<ArrayList<String>> optionList3Of2 = new ArrayList<>();
//        optionList3Of2.add(optionList3Of2_1);
//        optionList3Of2.add(optionList3Of2_2);
//        optionList3Of2.add(optionList3Of2_3);
//        optionList3Of2.add(optionList3Of2_4);
//        optionList3Of2.add(optionList3Of2_5);
//        optionList3Of2.add(optionList3Of2_6);
//        optionList3Of2.add(optionList3Of2_7);
//        optionList3Of2.add(optionList3Of2_8);
//        optionList3Of2.add(optionList3Of2_9);
//        optionList3Of2.add(optionList3Of2_10);
//
//        //填充Picker三级选项数据
//        optionsThreeItems.add(optionList3Of1);
//        optionsThreeItems.add(optionList3Of2);
//    }
}
