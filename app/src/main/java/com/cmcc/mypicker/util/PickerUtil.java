package com.cmcc.mypicker.util;

import android.content.Context;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;

import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PickerUtil {

    public static final int CASE_WRITER_TYPE = 0;  //笔录类型
    public static final int PERSON_TYPE = 1;       //证件类型
    public static final int PERSON_SEX = 2;        //性别
    public static final int PERSON_NATION = 3;     //民族
    public static final int PERSON_EDUCATION = 4;  //文化程度
    public static final int ARRIVE_TYPE = 5;

    public static final int SHOW_YEAR = 1;
    public static final int SHOW_YEAR_MONTH = 2;
    public static final int SHOW_YEAR_MONTH_DAY = 3;
    public static final int SHOW_YEAR_MONTH_DAY_HOUR = 4;
    public static final int SHOW_YEAR_MONTH_DAY_HOUR_MINUTE = 5;

    public static String[] caseTypeItem_1 = {"行政", "刑事"};
    public static String[][] caseTypeItem_2 = {{"询问", "检查", "勘察", "现场"},
            {"询问", "讯问", "现场辨认", "检查", "查封", "扣押", "搜查", "提取", "复验复查", "侦查实验"}};
    public static String[][][] caseTypeItem_3 = {{{"证人", "被侵害人", "违法嫌疑人"},{},{},{}},
            {{"被害人", "证人"}, {"犯罪嫌疑人"}, {}, {}, {}, {} ,{} ,{}, {}, {}}};

    public static String[] arriveItems = {"空", "口头传唤", "被扭送", "自动投案"};

    public static String[] pagerItems = {"居民身份证", "临时居民身份证", "户口簿", "暂住证", "警官证", "检察官证"};
    public static String[] sexItems = {"男", "女", "未知性别", "未说明的性别"};
    public static String[] nationItems = {"汉族", "蒙古族", "回族", "藏族", "维吾尔族", "苗族", "彝族", "壮族", "布依族", "朝鲜族", "满族", "侗族", "瑶族", "白族", "土家族",
            "哈尼族", "哈萨克族", "傣族", "黎族", "傈僳族", "佤族", "畲族", "高山族", "拉祜族", "水族", "东乡族", "纳西族", "景颇族", "柯尔克孜族",
            "土族", "达斡尔族", "仫佬族", "羌族", "布朗族", "撒拉族", "毛南族", "仡佬族", "锡伯族", "阿昌族", "普米族", "塔吉克族", "怒族", "乌孜别克族",
            "俄罗斯族", "鄂温克族", "德昂族", "保安族", "裕固族", "京族", "塔塔尔族", "独龙族", "鄂伦春族", "赫哲族", "门巴族", "珞巴族", "基诺族"};
    public static String[] educationItems = {"小学毕业", "初中毕业", "高中毕业", "中专毕业", "大学专科毕业", "大学本科毕业", "硕士研究生毕业", "博士研究生毕业"};

    public interface selectTextListener {
        void selectText(String str);
    }

    public static void showOptionsPicker(Context context, int type, String title, final selectTextListener listener) {
        //Picker
        OptionsPickerView optionsPicker;

        //三级选项数据
        final List<String> options1Items;
        final List<ArrayList<String>> options2Items = new ArrayList<>();
        final List<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

        switch (type) {
            case CASE_WRITER_TYPE:
                options1Items = new ArrayList<>(Arrays.asList(caseTypeItem_1));
                for(int i = 0; i < caseTypeItem_1.length; i++) { //遍历一级选项
                    ArrayList<String> option2Of1 = new ArrayList<>(); //该一级选项下的二级选项列表
                    ArrayList<ArrayList<String>> option3Of1 = new ArrayList<>(); //该一级选项下的所有三级选项列表
                    for (int j = 0; j < caseTypeItem_2[i].length; j++) {  //遍历该一级选项下的所有二级选项
                        option2Of1.add(caseTypeItem_2[i][j]);
                        ArrayList<String> option3Of2 = new ArrayList<>(Arrays.asList(caseTypeItem_3[i][j])); //该二级选项下的三级选项列表
                        option3Of1.add(option3Of2);
                    }
                    options2Items.add(option2Of1);
                    options3Items.add(option3Of1);
                }
                break;
            case ARRIVE_TYPE:
                options1Items = new ArrayList<>(Arrays.asList(arriveItems));
                break;
            case PERSON_TYPE:
                options1Items = new ArrayList<>(Arrays.asList(pagerItems));
                break;
            case PERSON_SEX:
                options1Items = new ArrayList<>(Arrays.asList(sexItems));
                break;
            case PERSON_NATION:
                options1Items = new ArrayList<>(Arrays.asList(nationItems));
                break;
            case PERSON_EDUCATION:
                options1Items = new ArrayList<>(Arrays.asList(educationItems));
                break;
            default:
                options1Items = new ArrayList<>();
                break;
        }

        optionsPicker = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //创建选中监听
                //获得选中的三级选项内容
                String opt1tx = options1Items.size() > 0 ?
                        options1Items.get(options1) : ""; //手动添加选项时
                String opt2tx = options2Items.size() > 0
                        && options2Items.get(options1).size() > 0 ?
                        " " + options2Items.get(options1).get(options2) : "";
                String opt3tx = options2Items.size() > 0
                        && options3Items.get(options1).size() > 0
                        && options3Items.get(options1).get(options2).size() > 0 ?
                        " " + options3Items.get(options1).get(options2).get(options3) : "";
                String tx = opt1tx + opt2tx + opt3tx;
                listener.selectText(tx);
            }
        })      .setTitleText(title)     //Picker标题
                .setContentTextSize(20)  //选项文字大小
                .isRestoreItem(true)   //前一选项变化则后面选项复位到第一项
                .build();
        //填充选项
        if (options3Items.size() > 0) {
            optionsPicker.setPicker(options1Items, options2Items, options3Items);
        } else if (options2Items.size() > 0) {
            optionsPicker.setPicker(options1Items, options2Items);
        } else {
            optionsPicker.setPicker(options1Items);
        }
        optionsPicker.show();
    }

    public static void showTimePicker(Context context, int type, String title, final selectTextListener listener) {
        SimpleDateFormat dateFormatTemp;
        //uiType = {false, false, false, false, false, false} 每个元素分别表示picker中年月日时分秒是否显示
        boolean[] uiType;
        switch (type) {
            case SHOW_YEAR:
                dateFormatTemp = new SimpleDateFormat("yyyy年");
                uiType = new boolean[]{true, false, false, false, false, false};
                break;
            case SHOW_YEAR_MONTH:
                dateFormatTemp = new SimpleDateFormat("yyyy-MM");
                uiType = new boolean[]{true, true, false, false, false, false};
                break;
            case SHOW_YEAR_MONTH_DAY:
                dateFormatTemp = new SimpleDateFormat("yyyy-MM-dd");
                uiType = new boolean[]{true, true, true, false, false, false};
                break;
            case SHOW_YEAR_MONTH_DAY_HOUR:
                dateFormatTemp = new SimpleDateFormat("yyyy-MM-dd HH时");
                uiType = new boolean[]{true, true, true, true, false, false};
                break;
            case SHOW_YEAR_MONTH_DAY_HOUR_MINUTE:
                dateFormatTemp = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                uiType = new boolean[]{true, true, true, true, true, false};
                break;
            default:
                dateFormatTemp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                uiType = new boolean[]{true, true, true, true, true, true};
                break;
        }
        final SimpleDateFormat dateFormat = dateFormatTemp;
        TimePickerView timePickerView = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                listener.selectText(dateFormat.format(date));
            }
        }).setType(uiType)
                .setTitleText(title)
                .isCenterLabel(true)    //单位标签
                .build();
        timePickerView.show();
    }
}
