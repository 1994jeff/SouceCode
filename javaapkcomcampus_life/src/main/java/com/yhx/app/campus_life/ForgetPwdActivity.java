package com.yhx.app.campus_life;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yhx.app.common.Constants;
import com.yhx.app.common.HttpHelper;
import com.yhx.app.entity.Users;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ForgetPwdActivity extends Activity implements View.OnClickListener {

    private Button mButtonBack;
    private TextView mTitleTv;
    private Button mButtonRight;
    private EditText mEtName;
    private EditText mEtPwd;
    private Button mBtRegistered;
    private Button mBtLogin;
    private TextView mTvWarn;

    String name,email;
    HashMap<String,Object> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        initView();
    }

    private void initView() {
        mButtonBack = (Button) findViewById(R.id.button_back);
        mTitleTv = (TextView) findViewById(R.id.title_tv);
        mButtonRight = (Button) findViewById(R.id.button_right);
        mEtName = (EditText) findViewById(R.id.et_name);
        mEtPwd = (EditText) findViewById(R.id.et_pwd);
        mBtRegistered = (Button) findViewById(R.id.bt_registered);
        mBtLogin = (Button) findViewById(R.id.bt_login);
        mTvWarn = (TextView) findViewById(R.id.tv_warn);

        mButtonRight.setVisibility(View.GONE);
        mTitleTv.setText("找回密码");
        mBtRegistered.setOnClickListener(this);
        mBtLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_registered:
                submit();
                break;
            case R.id.bt_login:
                onBackPressed();
                break;
        }
    }

    private void submit() {
        // validate
        name = mEtName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请输入用户名!", Toast.LENGTH_SHORT).show();
            return;
        }

        email = mEtPwd.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "请输入您的邮箱!", Toast.LENGTH_SHORT).show();
            return;
        }

        params = new HashMap<String, Object>();
        params.put("username",name);
        HttpHelper.asyncPost(Constants.URL + "/Trade/query_user.do", params, new HttpHelper.Callback() {
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
                        if(email.equals(user.getEmail())){
                            Toast.makeText(ForgetPwdActivity.this,"验证成功,进入修改密码页面",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ForgetPwdActivity.this,ModifyPwdActivity.class);
                            intent.putExtra("pwd",user.getPassword());
                            intent.putExtra("username",user.getUserName());
                            startActivity(intent);
                        }else {
                            Toast.makeText(ForgetPwdActivity.this,"用户名与邮箱不匹配！",Toast.LENGTH_SHORT).show();
                        }
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
        Toast.makeText(ForgetPwdActivity.this,"网络错误，找回失败！",Toast.LENGTH_SHORT).show();
    }
}
