package com.agenthun.readingroutine.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.adapters.AvatarAdapter;
import com.agenthun.readingroutine.datastore.UserData;
import com.agenthun.readingroutine.transitionmanagers.TActivity;
import com.agenthun.readingroutine.utils.Avatar;
import com.agenthun.readingroutine.utils.UiUtils;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconToolbar;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.EmailVerifyListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 注册页面Activity（包含Grid布局）
 */
public class SignUpGridActivity extends TActivity {

    private static final String TAG = "SignUpGridActivity";

    private MaterialMenuIconToolbar materialMenuIconToolbar;
    private Toolbar toolbar;
    private Avatar selectedAvatar = Avatar.ONE;
    private View selectedAvatarView;
    private GridView avatarGrid;
    private TextView name;
    private TextView emailAddress;
    private TextView passwpord;
    private TextView passwpordAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        materialMenuIconToolbar = new MaterialMenuIconToolbar(this, getResources().getColor(R.color.color_white), MaterialMenuDrawable.Stroke.REGULAR) {
            @Override
            public int getToolbarViewId() {
                return R.id.toolbar;
            }
        };
        materialMenuIconToolbar.setState(MaterialMenuDrawable.IconState.ARROW);
        toolbar.setTitle(R.string.text_sign_up);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        avatarGrid = (GridView) findViewById(R.id.avatars);
        setupGridView();

        name = (TextView) findViewById(R.id.login_name);
        emailAddress = (TextView) findViewById(R.id.email_address);
        passwpord = (TextView) findViewById(R.id.login_password);
        passwpordAgain = (TextView) findViewById(R.id.login_password_again);
    }

    private void setupGridView() {
        avatarGrid.setAdapter(new AvatarAdapter(getApplicationContext()));
        avatarGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedAvatarView = view;
                selectedAvatar = Avatar.values()[position];
            }
        });
        avatarGrid.setNumColumns(calculateSpanCount());
        avatarGrid.setItemChecked(selectedAvatar.ordinal(), true);
    }

    private int calculateSpanCount() {
        int avatarSize = getResources().getDimensionPixelSize(R.dimen.btn_fab_size);
        int avatarPadding = getResources().getDimensionPixelSize(R.dimen.margin_horizontal);
        return (UiUtils.getScreenWidthPixels(getApplicationContext()) - avatarPadding * 2) / (avatarSize + avatarPadding);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_done:
                    attemptSignUpAndLogin();
                    break;
            }
            return true;
        }
    };

    private void attemptSignUpAndLogin() {
        String signUpName = name.getText().toString().trim();
        final String signUpEmailAddress = emailAddress.getText().toString().trim();
        String signUpPassword = passwpord.getText().toString();
        String signUpPasswordAgain = passwpordAgain.getText().toString();

        if (!signUpPassword.equals(signUpPasswordAgain)) {
            Snackbar.make(avatarGrid, R.string.error_difference_password, Snackbar.LENGTH_SHORT).show();
            YoYo.with(Techniques.Shake).duration(500).delay(100).playOn(passwpordAgain);
            return;
        }
        final UserData newUser = new UserData();
        newUser.setUsername(signUpName);
        newUser.setPassword(signUpPassword);
        newUser.setEmail(signUpEmailAddress);
        newUser.setAvatarId(selectedAvatar.ordinal());
        newUser.signUp(SignUpGridActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                //Toast.makeText(SignUpActivity.this, R.string.success_sign_up, Toast.LENGTH_SHORT).show();
                setIsTrial(false);
                BmobUser.requestEmailVerify(SignUpGridActivity.this, signUpEmailAddress, new EmailVerifyListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(SignUpGridActivity.this, "请求验证邮件成功，请到" + signUpEmailAddress + "邮箱中进行激活。", Toast.LENGTH_LONG).show();

                        toolbar.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                newUser.login(SignUpGridActivity.this, new SaveListener() {
                                    @Override
                                    public void onSuccess() {
                                        Intent intent = new Intent(SignUpGridActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        Toast.makeText(SignUpGridActivity.this, getResources().getString(R.string.msg_fail) + ": " + s, Toast.LENGTH_LONG).show();
                                    }
                                });
                                finish();
                            }
                        }, 200);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(SignUpGridActivity.this, "请求验证邮件失败: " + s, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(SignUpGridActivity.this, "注册失败: " + s, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected int getFragmentContainerId() {
        return 0;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
