package com.cmcc.mypicker;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private Fragment[] mFragments;
    private Toolbar mToolbar;
    private int fragIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();  ///等数据加载完毕再初始化并显示Picker,以免还未加载完数据就显示,造成APP崩溃
        initView();
    }

    private void initData(){
        mFragments = new Fragment[4];
        fragIndex = 0;
        mFragments[0] = new RecordTypeFragment();
        mFragments[1] = new PersonInfoFragment();
        mFragments[2] = new RecordTypeFragment();
        mFragments[3] = new PersonInfoFragment();
    }

    private void initView() {
        mToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTabLayout.getTabAt(fragIndex - 1).select();
            }
        });
        mTabLayout = findViewById(R.id.bottom_tab);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabItemSelected(tab.getPosition());

                //改变Tab 状态
                for(int i=0;i< mTabLayout.getTabCount();i++){
                    if(i == tab.getPosition()){
                        //mTabLayout.getTabAt(i).setIcon(getResources().getDrawable(DataGenerator.mTabResPressed[i]));
                    }else{
                        //mTabLayout.getTabAt(i).setIcon(getResources().getDrawable(DataGenerator.mTabRes[i]));
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mTabLayout.addTab(mTabLayout.newTab().setText("案件"));
        mTabLayout.addTab(mTabLayout.newTab().setText("人员"));
        mTabLayout.addTab(mTabLayout.newTab().setText("问答"));
        mTabLayout.addTab(mTabLayout.newTab().setText("其它"));
    }

    private void onTabItemSelected(int position){
        fragIndex = position;
        if (fragIndex > 0) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = mFragments[0];
                break;
            case 1:
                fragment = mFragments[1];
                break;

            case 2:
                fragment = mFragments[2];
                break;
            case 3:
                fragment = mFragments[3];
                break;
        }
        if(fragment!=null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,fragment).commit();
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if (fragIndex < 3) {
                    mTabLayout.getTabAt(fragIndex + 1).select();
                } else {
                    Toast.makeText(this, "已保存", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
        return true;
    }

}
