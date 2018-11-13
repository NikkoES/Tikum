package com.tokosepeda.tikum.activity;

import android.support.annotation.ColorRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.tokosepeda.tikum.R;
import com.tokosepeda.tikum.adapter.bottomnav.BottomBarAdapter;
import com.tokosepeda.tikum.adapter.bottomnav.NoSwipePager;
import com.tokosepeda.tikum.fragment.AccountFragment;
import com.tokosepeda.tikum.fragment.GroupFragment;
import com.tokosepeda.tikum.fragment.HomeFragment;
import com.tokosepeda.tikum.fragment.TemanFragment;
import com.tokosepeda.tikum.fragment.TokoFragment;
import com.tokosepeda.tikum.model.User;

public class MainActivity extends AppCompatActivity {

    private NoSwipePager viewPager;
    private AHBottomNavigation bottomNavigation;
    private BottomBarAdapter pagerAdapter;
    private boolean notificationVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViewPager();

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        setupBottomNavBehaviors();
        setupBottomNavStyle();

        addBottomNavigationItems();
        bottomNavigation.setCurrentItem(0);

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
//                fragment.updateColor(ContextCompat.getColor(MainActivity.this, colors[position]));
                switch (position){
                    case 0 : {
                        getSupportActionBar().setTitle(getString(R.string.app_name));
                        break;
                    }
                    case 1 : {
                        getSupportActionBar().setTitle(R.string.menu_toko);
                        break;
                    }
                    case 2 : {
                        getSupportActionBar().setTitle(R.string.menu_teman);
                        break;
                    }
                    case 3 : {
                        getSupportActionBar().setTitle(R.string.menu_grup);
                        break;
                    }
                    case 4 : {
                        getSupportActionBar().setTitle(R.string.menu_akun);
                        break;
                    }
                }

                if (!wasSelected)
                    viewPager.setCurrentItem(position);

                // remove notification badge
                int lastItemPos = bottomNavigation.getItemsCount() - 1;
                if (notificationVisible && position == lastItemPos)
                    bottomNavigation.setNotification(new AHNotification(), lastItemPos);

                return true;
            }
        });
    }

    private void setupViewPager() {
        viewPager = (NoSwipePager) findViewById(R.id.viewpager);
        viewPager.setPagingEnabled(false);
        pagerAdapter = new BottomBarAdapter(getSupportFragmentManager());

        pagerAdapter.addFragments(new HomeFragment());
        pagerAdapter.addFragments(new TokoFragment());
        pagerAdapter.addFragments(new TemanFragment());
        pagerAdapter.addFragments(new GroupFragment());
        pagerAdapter.addFragments(new AccountFragment());

        viewPager.setAdapter(pagerAdapter);
    }


    public void setupBottomNavBehaviors() {
        bottomNavigation.setBehaviorTranslationEnabled(false);

        /*
        Before enabling this. Change MainActivity theme to MyTheme.TranslucentNavigation in
        AndroidManifest.

        Warning: Toolbar Clipping might occur. Solve this by wrapping it in a LinearLayout with a top
        View of 24dp (status bar size) height.
         */
        bottomNavigation.setTranslucentNavigationEnabled(false);
    }

    /**
     * Adds styling properties to {@link AHBottomNavigation}
     */
    private void setupBottomNavStyle() {
        /*
        Set Bottom Navigation colors. Accent color for active item,
        Inactive color when its view is disabled.

        Will not be visible if setColored(true) and default current item is set.
         */
        bottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.bottomtab_active));
        bottomNavigation.setAccentColor(fetchColor(R.color.bottomtab_active));
        bottomNavigation.setInactiveColor(fetchColor(R.color.bottomtab_item_resting));

        // Colors for selected (active) and non-selected items.
        bottomNavigation.setColoredModeColors(getResources().getColor(R.color.bottomtab_active),
                fetchColor(R.color.bottomtab_item_resting));

        //  Enables Reveal effect
        bottomNavigation.setColored(true);

        //  Displays item Title always (for selected and non-selected items)
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
    }


    /**
     * Adds (items) {@link AHBottomNavigationItem} to {@link AHBottomNavigation}
     * Also assigns a distinct color to each Bottom Navigation item, used for the color ripple.
     */
    private void addBottomNavigationItems() {
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_home, R.drawable.ic_home, R.color.bottomtab);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_toko, R.drawable.ic_toko, R.color.bottomtab);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.tab_teman, R.drawable.ic_account, R.color.bottomtab);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.tab_group, R.drawable.ic_group, R.color.bottomtab);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(R.string.tab_account, R.drawable.ic_setting, R.color.bottomtab);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.addItem(item5);
    }


    /**
     * Simple facade to fetch color resource, so I avoid writing a huge line every time.
     *
     * @param color to fetch
     * @return int color value.
     */
    private int fetchColor(@ColorRes int color) {
        return ContextCompat.getColor(this, color);
    }

    @Override
    public void onBackPressed() {
        finish();
        moveTaskToBack(true);
    }
}
