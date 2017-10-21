package xuwei.com.passwordgenerator.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import xuwei.com.passwordgenerator.R;
import xuwei.com.passwordgenerator.fragment.BankcardFragment;
import xuwei.com.passwordgenerator.fragment.OtherFragment;
import xuwei.com.passwordgenerator.fragment.WebSideFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";


    private ViewPager viewPager;
    private TabLayout tabLayout;

    private BankcardFragment bankcardFragment = new BankcardFragment();
    private WebSideFragment webSideFragment = new WebSideFragment();
    private OtherFragment otherFragment = new OtherFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //注册监听
        viewPager.addOnPageChangeListener(myPageChangeListener);
        tabLayout.addOnTabSelectedListener(myTabSelectedListener);

        //添加适配器，在viewPager里引入Fragment
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return bankcardFragment;
                    case 1:
                        return webSideFragment;
                    case 2:
                        return otherFragment;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
    }

    private ViewPager.OnPageChangeListener myPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //viewPager滑动之后显示触发
            Log.d(TAG, "onPageSelected position = " + position);
            tabLayout.getTabAt(position).select();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private TabLayout.OnTabSelectedListener myTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            //TabLayout里的TabItem被选中的时候触发
            Log.d(TAG, "onTabSelected position = " + tab.getPosition());
            viewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };
}
