package com.cmcc.mypicker.bean;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

public class JsonBean implements IPickerViewData {
    /**
     * name: 行政
     * typeOf2: [{name:询问, typeOf3:[证人， 被侵害人， 违法嫌疑人] ]
     */
    private String name;
    private List<TypeOf2Bean> typeOf2;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TypeOf2Bean> getTypeOf2() {
        return typeOf2;
    }

    public void setTypeOf2(List<TypeOf2Bean> typeOf2) {
        this.typeOf2 = typeOf2;
    }

    // 实现 IPickerViewData 接口，
    // 这个用来显示在PickerView上面的字符串，
    // PickerView会通过IPickerViewData获取getPickerViewText方法显示出来。
    @Override
    public String getPickerViewText() {
        return this.name;
    }

    public static class TypeOf2Bean {
        private String name;
        private List<String> typeOf3;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getTypeOf3() {
            return typeOf3;
        }

        public void setTypeOf3(List<String> typeOf3) {
            this.typeOf3 = typeOf3;
        }
    }
}
