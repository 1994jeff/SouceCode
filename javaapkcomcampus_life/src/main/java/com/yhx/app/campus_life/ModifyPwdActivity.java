package com.yhx.app.campus_life;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yhx.app.common.Constants;
import com.yhx.app.common.HttpHelper;
import com.yhx.app.entity.Users;
import com.yhx.app.service.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by jfdeng@grandstream.cn on 18-8-21.
 */

public class ModifyPwdActivity extends Activity implements View.OnClickListener {

    private TextView mTitleTv;
    private Button mButtonRight;
    private EditText mEtOldPwd;
    private EditText mEtPwd;
    private EditText mEtPwd2;
    private Button mBtRegistered;
    MyApplication myApplication;
    LinearLayout oldPwdContainer;

    String oldPwd,userName;
    HashMap<String, Object> param;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifyt_pwd);

        initView();
    }

    private void initUser() {
        myApplication = (MyApplication) getApplicationContext();
        HashMap<String, Object> param = new HashMap<String,Object>();
        Users u = (Users) myApplication.userMap.get("user");
        if(u!=null && !TextUtils.isEmpty(u.getUserName())){
            modifyPwd(param, u.getUserName());
        }else if(!TextUtils.isEmpty(userName)){
            modifyPwd(param,userName);
        }else {
            Toast.makeText(this,"对不起，请先登录再修改个人信息!",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    private void modifyPwd(HashMap<String, Object> param,String userName) {
        param.put("username", userName);
        HttpHelper.asyncPost(Constants.URL
                        + "/Trade/query_user.do", param,
                new HttpHelper.Callback() {
                    @Override
                    public void dataLoaded(Message msg) {
                        if (msg.what == 200) {
                            Users user = new Users();
                            try {
                                JSONObject json = new JSONObject(
                                        msg.obj.toString());
                                user.setUserId(json
                                        .getInt("userId"));
                                user.setUserName(json
                                        .getString("userName"));
                                user.setEmail(json
                                        .getString("email"));
                                user.setPassword(json
                                        .getString("password"));
                                user.setSchool(json
                                        .getString("school"));
                                user.setCourt(json
                                        .getString("court"));
                                user.setProfessional(json
                                        .getString("professional"));
                                user.setPhone(json.getString("phone"));
                                myApplication.userMap.put(
                                        "user", user);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            onFailedRequest();
                        }

                    }
                });
    }


    private void onFailedRequest() {
        Toast.makeText(ModifyPwdActivity.this,"网络错误！",Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        mTitleTv = (TextView) findViewById(R.id.title_tv);
        mButtonRight = (Button) findViewById(R.id.button_right);
        mEtOldPwd = (EditText) findViewById(R.id.et_oldPwd);
        mEtPwd = (EditText) findViewById(R.id.et_pwd);
        mEtPwd2 = (EditText) findViewById(R.id.et_pwd2);
        mBtRegistered = (Button) findViewById(R.id.bt_registered);
        oldPwdContainer = (LinearLayout) findViewById(R.id.oldPwdContainer);

        mTitleTv.setText("修改密码");
        mButtonRight.setVisibility(View.GONE);

        mButtonRight.setOnClickListener(this);
        mBtRegistered.setOnClickListener(this);

        oldPwd = getIntent().getStringExtra("pwd");
        userName = getIntent().getStringExtra("username");
        if(!TextUtils.isEmpty(oldPwd)){
            oldPwdContainer.setVisibility(View.GONE);
        }

        initUser();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_registered:
                submit();
                break;
        }
    }

    private void submit() {
        if(TextUtils.isEmpty(oldPwd)){
            oldPwd = mEtOldPwd.getText().toString().trim();
            if (TextUtils.isEmpty(oldPwd)) {
                Toast.makeText(this, "请输入旧密码", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String pwd = mEtPwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "请输入您的新密码验证", Toast.LENGTH_SHORT).show();
            return;
        }

        String pwd2 = mEtPwd2.getText().toString().trim();
        if (TextUtils.isEmpty(pwd2)) {
            Toast.makeText(this, "请再次输入您的新密码验证", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pwd2.equals(pwd)) {
            Toast.makeText(this, "两次输入的新密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        Users u = (Users) myApplication.userMap.get("user");
        if(u==null){
            Toast.makeText(this, "网络错误请重试", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Log.i("jeff",u.getPassword()+","+oldPwd);
        if(!u.getPassword().equals(oldPwd)){
            Toast.makeText(this, "旧密码错误,请重试!", Toast.LENGTH_SHORT).show();
            oldPwd = "";
            return;
        }

        param = new HashMap<String, Object>();
        param.put("username",u.getUserName());
        param.put("pwd",pwd);

        HttpHelper.asyncPost(Constants.URL + "/Trade/modify_user.do", param, new HttpHelper.Callback() {
            @Override
            public void dataLoaded(Message msg) {
                if (msg.what == 200) {
                    if(msg.obj.toString().equals("用户名不存在")){
                        Toast.makeText(ModifyPwdActivity.this,"用户名不存在",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(ModifyPwdActivity.this,"密码修改成功,请重新登录!",Toast.LENGTH_SHORT).show();
                        myApplication.userMap.clear();
                        Intent intent = new Intent(ModifyPwdActivity.this,
                                PersonnalActivity.class);
                        startActivity(intent);
                        ModifyPwdActivity.this.finish();
                    }
                } else {
                }
            }
        });

    }
}
