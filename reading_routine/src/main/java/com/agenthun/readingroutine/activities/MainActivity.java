package com.agenthun.readingroutine.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.datastore.UserData;
import com.agenthun.readingroutine.fragments.MenuFragment;
import com.agenthun.readingroutine.fragments.SettingsFragment;
import com.agenthun.readingroutine.transitionmanagers.TActivity;
import com.agenthun.readingroutine.utils.Avatar;
import com.agenthun.readingroutine.utils.CircleTransformation;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconToolbar;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.BmobUser;

/**
 * 这是主页面对应的Activity
 */
public class MainActivity extends TActivity implements MenuFragment.OnMenuInteractionListener {

    private static final String TAG = "MainActivity";
    private MaterialMenuIconToolbar materialMenuIconToolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private boolean direction;
    private boolean navigationItemSelected = false;
    private int mFragmentIndex = 0;

    @InjectView(R.id.avatar)
    ImageView avator;
    @InjectView(R.id.name)
    TextView name;
    @InjectView(R.id.email)
    TextView email;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.navigation_view)
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        Intent intent = getIntent();
        String fileName = intent.getStringExtra(SignUpActivity.AVATAR_URL);
/*        if (fileName != null && fileName.length() != 0) {
            BTPFileResponse response = BmobProFile.getInstance(this).upload(fileName, new UploadListener() {
                @Override
                public void onSuccess(String s, String s1, BmobFile bmobFile) {
                    UserData user = (UserData) UserData.getCurrentUser(MainActivity.this);
                    user.setPic(bmobFile);
                    user.save(MainActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            Picasso.with(MainActivity.this).load((File) UserData.getObjectByKey(MainActivity.this, "pic"))
                                    .transform(new CircleTransformation()).into(avator);
                            Log.d(TAG, "onSuccess() returned: save ok");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Log.d(TAG, "onFailure() returned: " + s);
                        }
                    });
                }

                @Override
                public void onProgress(int i) {

                }

                @Override
                public void onError(int i, String s) {

                }
            });
        } else {
            Picasso.with(this).load((File) UserData.getObjectByKey(this, "pic")).transform(new CircleTransformation()).into(avator);
        }*/
        if (!getIsTrial()) {
            final CircleTransformation transformation = new CircleTransformation(this, 2);
//        transformation.setBorderColor(getResources().getColor(R.color.gray_200));

            Picasso.with(this).load(Avatar.values()[((int) UserData.getObjectByKey(this, "avatarId"))].getDrawableId())
                    .transform(transformation).into(avator);

            name.setText((String) UserData.getObjectByKey(this, "username"));
            email.setText((String) UserData.getObjectByKey(this, "email"));
        }
        materialMenuIconToolbar = new MaterialMenuIconToolbar(this, getResources().getColor(R.color.color_white), MaterialMenuDrawable.Stroke.REGULAR) {
            @Override
            public int getToolbarViewId() {
                return R.id.toolbar;
            }
        };
        materialMenuIconToolbar.setNeverDrawTouch(false);

        toolbar.setTitleTextColor(getResources().getColor(R.color.color_white));
        toolbar.setTitle(R.string.text_main);
        toolbar.setBackgroundColor(getResources().getColor(R.color.background_daytime_material_blue));
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cnt = getCountBackStackEntryAt();
                if (cnt <= 1) {
                    if (!direction) {
                        drawerLayout.openDrawer(Gravity.LEFT);
                    } else {
                        drawerLayout.closeDrawer(Gravity.LEFT);
                    }
                } else {
                    if (mFragmentIndex == MenuFragment.SETTINGS_FRAGMENT) {
                        navigationItemSelected = false;
                    }
                    mFragmentIndex = 0;
                    toolbar.setTitleTextColor(getResources().getColor(R.color.color_white));
                    toolbar.setTitle(R.string.text_main);
                    toolbar.setBackgroundColor(getResources().getColor(R.color.background_daytime_material_blue));
                    materialMenuIconToolbar.setColor(getResources().getColor(R.color.color_white));
                    materialMenuIconToolbar.animateState(MaterialMenuDrawable.IconState.BURGER);
                }
            }
        });

/*
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        actionBarDrawerToggle.syncState();*/

        drawerLayout.setScrimColor(Color.parseColor("#66000000"));
//        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (!navigationItemSelected) {
                    if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
                        materialMenuIconToolbar.setTransformationOffset(MaterialMenuDrawable.AnimationState.BURGER_ARROW, direction ? 2 - slideOffset : slideOffset);
                    }
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                direction = true;
                if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
                    toolbar.setTitleTextColor(getResources().getColor(R.color.color_white));
                } else {
                    toolbar.setTitleTextColor(getResources().getColor(R.color.background_daytime_material_blue));
                }
                toolbar.setTitle(getString(R.string.text_reading_routine));
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                direction = false;
                navigationItemSelected = false;
                if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
                    toolbar.setTitleTextColor(getResources().getColor(R.color.color_white));
                    toolbar.setTitle(R.string.text_main);
                } else {
                    toolbar.setTitleTextColor(getResources().getColor(R.color.background_daytime_material_blue));
                    toolbar.setTitle(R.string.text_null);
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        if (savedInstanceState == null) {
            pushFragmentToBackStack(MenuFragment.class, null);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(false);
                String menuItemTitle = menuItem.getTitle() + "";
                //Log.i(TAG, menuItemTitle);
                switch (menuItemTitle) {
                    case "首页":
                        drawerLayout.closeDrawers();
                        if (mFragmentIndex > 0) {
                            mFragmentIndex = 0;
                            getCountBackStackEntryAt();
                            materialMenuIconToolbar.setColor(getResources().getColor(R.color.color_white));
                            toolbar.setTitleTextColor(getResources().getColor(R.color.color_white));
                            toolbar.setTitle(R.string.text_main);
                            toolbar.setBackgroundColor(getResources().getColor(R.color.background_daytime_material_blue));
                        }
                        break;
                    case "设置":
                        navigationItemSelected = true;
                        if (mFragmentIndex != MenuFragment.SETTINGS_FRAGMENT) {
                            if (mFragmentIndex > 0) getCountBackStackEntryAt();
                            mFragmentIndex = MenuFragment.SETTINGS_FRAGMENT;
                            materialMenuIconToolbar.setColor(getResources().getColor(R.color.background_daytime_material_blue));
                            materialMenuIconToolbar.setState(MaterialMenuDrawable.IconState.ARROW);
                            toolbar.setTitle(R.string.text_null);
                            toolbar.setBackgroundColor(getResources().getColor(R.color.color_white));
                            pushFragmentToBackStack(SettingsFragment.class, null);
                        }
                        drawerLayout.closeDrawers();
                        break;
                    case "退出":
                        BmobUser.logOut(getApplicationContext());
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                        break;
                }
                return false;
            }
        });

        //AlarmNoiser.startAlarmNoiserService(this, time, AlarmNoiserReciever.class, AlarmNoiserIntentService.ACTION_NOTIFICATION);
        //AlarmNoiserIntentService.startActionNotification(this, "test", "");

        callRoutineService();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        refreshDrawerState();
        materialMenuIconToolbar.syncState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        materialMenuIconToolbar.onSaveInstanceState(outState);
    }

    private void refreshDrawerState() {
        direction = drawerLayout.isDrawerOpen(Gravity.START);
    }

    //开启各个fragment，根据点击的fragmentIndex分别跳转至不同的fragment
    @Override
    public void onFragmentInteraction(int fragmentIndex) {
        mFragmentIndex = fragmentIndex;
        switch (fragmentIndex) {
            case MenuFragment.READING_FRAGMENT:
                openReadingFragment();
                break;
            case MenuFragment.ROUTINES_FRAGMENT:
                openRoutinesFragment();
                break;
            case MenuFragment.SHOPPING_FRAGMENT:
                openShoppingFragment();
                break;
            case MenuFragment.ABOUT_FRAGMENT:
                openAboutFragment();
                break;
            case MenuFragment.NOTES_FRAGMENT:
                openNotesFragment();
                break;
            case MenuFragment.SETTINGS_FRAGMENT:
                openSettingsFragment();
                break;
        }
    }

    private void openReadingFragment() {
        materialMenuIconToolbar.setColor(getResources().getColor(R.color.background_daytime_material_blue));
        materialMenuIconToolbar.animateState(MaterialMenuDrawable.IconState.ARROW);
        toolbar.setTitle(R.string.text_null);
        toolbar.setBackgroundColor(getResources().getColor(R.color.color_white));
    }

    private void openRoutinesFragment() {
        materialMenuIconToolbar.setColor(getResources().getColor(R.color.background_daytime_material_blue));
        materialMenuIconToolbar.animateState(MaterialMenuDrawable.IconState.ARROW);
        toolbar.setTitle(R.string.text_null);
        toolbar.setBackgroundColor(getResources().getColor(R.color.color_white));
    }

    private void openShoppingFragment() {
//        toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
/*        materialMenuIconToolbar.setColor(getResources().getColor(R.color.background_daytime_material_blue));
        materialMenuIconToolbar.animateState(MaterialMenuDrawable.IconState.ARROW);*/
    }

    private void openAboutFragment() {
//        materialMenuIconToolbar.setColor(getResources().getColor(R.color.background_daytime_material_blue));
        materialMenuIconToolbar.animateState(MaterialMenuDrawable.IconState.ARROW);
        toolbar.setTitle(R.string.text_about);
//        toolbar.setBackgroundColor(getResources().getColor(R.color.color_white));
    }

    private void openNotesFragment() {
        materialMenuIconToolbar.setColor(getResources().getColor(R.color.background_daytime_material_blue));
        materialMenuIconToolbar.animateState(MaterialMenuDrawable.IconState.ARROW);
        toolbar.setTitle(R.string.text_null);
        toolbar.setBackgroundColor(getResources().getColor(R.color.color_white));
    }

    private void openSettingsFragment() {
        materialMenuIconToolbar.setColor(getResources().getColor(R.color.background_daytime_material_blue));
        materialMenuIconToolbar.animateState(MaterialMenuDrawable.IconState.ARROW);
        toolbar.setTitle(R.string.text_null);
        toolbar.setBackgroundColor(getResources().getColor(R.color.color_white));
//        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.container;
    }

    @Override
    public void onBackPressed() {
        if (mFragmentIndex == MenuFragment.SETTINGS_FRAGMENT) {
            navigationItemSelected = false;
        }
        mFragmentIndex = 0;
        materialMenuIconToolbar.setColor(getResources().getColor(R.color.color_white));
        materialMenuIconToolbar.animateState(MaterialMenuDrawable.IconState.BURGER);
        toolbar.setTitleTextColor(getResources().getColor(R.color.color_white));
        toolbar.setTitle(R.string.text_main);
        toolbar.setBackgroundColor(getResources().getColor(R.color.background_daytime_material_blue));
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
