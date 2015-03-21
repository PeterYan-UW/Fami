package com.fami.setting;
import com.fami.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

public class MainSetting extends FragmentActivity {
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_setting);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(
                mTabHost.newTabSpec("User Info").setIndicator("User Info", null),
                UserInfo.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("Fami Info").setIndicator("Fami Info", null),
                FamiInfo.class, null);
    }
}