package com.contexthub.notifyme;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;

import com.astuetz.PagerSlidingTabStrip;
import com.contexthub.notifyme.fragments.AboutFragment;
import com.contexthub.notifyme.fragments.DeviceFragment;
import com.contexthub.notifyme.fragments.PushReceiveFragment;
import com.contexthub.notifyme.fragments.PushSendFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.tab_strip) PagerSlidingTabStrip tabStrip;
    @InjectView(R.id.view_pager) ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(3);
        tabStrip.setViewPager(viewPager);
    }

    class PagerAdapter extends FragmentPagerAdapter {

        private final int[] TITLES = new int[]{R.string.send, R.string.receive, R.string.device, R.string.about};

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(TITLES[position]);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new PushSendFragment();
                case 1:
                    return new PushReceiveFragment();
                case 2:
                    return new DeviceFragment();
                case 3:
                    return new AboutFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }
    }
}
