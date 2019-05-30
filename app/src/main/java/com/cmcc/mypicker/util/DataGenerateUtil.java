package com.cmcc.mypicker.util;

import java.util.ArrayList;
import java.util.Arrays;

public class DataGenerateUtil {

    //到案方式选项
    private static String[] arriveTypes = {"空", "口头传唤", "被扭送", "自动投案"};


    //案件原因选项
    private static String[] caseReasons = {"a", "b", "c", "盗窃", "杀人",
            "故意伤人", "抢劫", "ab", "ac", "bc", "abc"};

    public static ArrayList<String> getArriveType() {
        return new ArrayList<>(Arrays.asList(arriveTypes));
    }

    public static ArrayList<String> getCaseReason() {
        return new ArrayList<>(Arrays.asList(caseReasons));
    }
}
