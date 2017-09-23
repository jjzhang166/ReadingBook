package com.agenthun.readingroutine.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.datastore.UserData;
import com.agenthun.readingroutine.utils.CircleTransformation;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconToolbar;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.squareup.picasso.Picasso;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.EmailVerifyListener;
import cn.bmob.v3.listener.SaveListener;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    protected static final String AVATAR_URL = "AVATAR_URL";
    public static final int OPEN_IMAGE_FILE = 2;

    private MaterialMenuIconToolbar materialMenuIconToolbar;
    private Toolbar toolbar;
    private ImageView avatar;
    private TextView name;
    private TextView emailAddress;
    private TextView passwpord;
    private TextView passwpordAgain;
    private String avatarUrl;

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

        avatar = (ImageView) findViewById(R.id.avatar);
        avatar.setOnClickListener(onAddAvatarClick);
        name = (TextView) findViewById(R.id.login_name);
        emailAddress = (TextView) findViewById(R.id.email_address);
        passwpord = (TextView) findViewById(R.id.login_password);
        passwpordAgain = (TextView) findViewById(R.id.login_password_again);
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

        final UserData newUser = new UserData();
        newUser.setUsername(signUpName);
        newUser.setPassword(signUpPassword);
        newUser.setEmail(signUpEmailAddress);
        newUser.signUp(SignUpActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                //Toast.makeText(SignUpActivity.this, R.string.success_sign_up, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                if (avatarUrl != null && avatarUrl.length() != 0) {
                    intent.putExtra(AVATAR_URL, avatarUrl);
                } else {
                    intent.putExtra(AVATAR_URL, "");
                }
                startActivity(intent);

                BmobUser.requestEmailVerify(SignUpActivity.this, signUpEmailAddress, new EmailVerifyListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(SignUpActivity.this, "请求验证邮件成功，请到" + signUpEmailAddress + "邮箱中进行激活。", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(SignUpActivity.this, "请求验证邮件失败: " + s, Toast.LENGTH_LONG).show();
                    }
                });

                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(SignUpActivity.this, "注册失败: " + s, Toast.LENGTH_LONG).show();
            }
        });
    }

    private View.OnClickListener onAddAvatarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SignUpActivity.this, FilePickerActivity.class);
            startActivityForResult(intent, OPEN_IMAGE_FILE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_IMAGE_FILE && resultCode == RESULT_OK) {
            String fileName = String.valueOf(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
            // open file
            if (fileName.endsWith(".jpg") || fileName.endsWith(".png")) {
                avatarUrl = new String(fileName);
                Picasso.with(SignUpActivity.this).load(new File(fileName)).transform(new CircleTransformation()).into(avatar);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
