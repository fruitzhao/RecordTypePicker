package com.cmcc.mypicker;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.cmcc.mypicker.util.DataGenerateUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PickReason extends AppCompatActivity {

    private List<String> reasonList;  //全部案件原因
    private List<String> validList;   //符合搜索关键字的原因
    private ArrayAdapter<String> adapter;
    private SearchView mSearchView;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_reason);
        initList();
        initView();
    }

    public void initView() {
        mListView = findViewById(R.id.reason_list);
        mSearchView = findViewById(R.id.reason_search);
        adapter = new ArrayAdapter<>(PickReason.this, android.R.layout.simple_list_item_1, validList);
        mListView.setAdapter(adapter);

        //修改搜索框文字颜色
        int id = mSearchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = mSearchView.findViewById(id);
        textView.setTextColor(Color.parseColor("#ffffff"));
        textView.setHintTextColor(Color.parseColor("#ffffff"));
        //取消下划线
        if (mSearchView != null) {
            try {        //--拿到字节码
                Class<?> argClass = mSearchView.getClass();
                //--指定某个私有属性,mSearchPlate是搜索框父布局的名字
                Field ownField = argClass.getDeclaredField("mSearchPlate");
                //--暴力反射,只有暴力反射才能拿到私有属性
                ownField.setAccessible(true);
                View mView = (View) ownField.get(mSearchView);
                //--设置背景
                mView.setBackgroundColor(Color.TRANSPARENT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //修改搜索图标颜色
        int magId = getResources().getIdentifier("android:id/search_mag_icon",null, null);
        ImageView magImage = mSearchView.findViewById(magId);
        //magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        magImage.setColorFilter(Color.parseColor("#ffffff"));

        mSearchView.setFocusable(true);
        mSearchView.setFocusableInTouchMode(true);
        mSearchView.requestFocus();

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    //Toast.makeText(AddCity.this, "输入内容已改变", Toast.LENGTH_SHORT).show();
                    validList.clear();
                    for(int i = 0; i < reasonList.size(); i++) {
                        if(reasonList.get(i).contains(newText)) {
                            validList.add(reasonList.get(i));
                        }
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    validList.clear();
                    validList.addAll(reasonList);
                    adapter.notifyDataSetChanged();
                    //mListView.clearTextFilter();
                }
                return false;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String reason = validList.get(position);
                Intent intent = new Intent();
                intent.putExtra("reason_picked", reason);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


    public void initList() {
        reasonList = DataGenerateUtil.getCaseReason();
        validList = new ArrayList<>();
        validList.addAll(reasonList);
    }
}
