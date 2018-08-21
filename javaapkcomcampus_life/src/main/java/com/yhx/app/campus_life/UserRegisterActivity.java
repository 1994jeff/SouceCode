package com.yhx.app.campus_life;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yhx.app.common.Constants;
import com.yhx.app.common.HttpHelper;
import com.yhx.app.common.HttpHelper.Callback;
import com.yhx.app.service.MyApplication;

public class UserRegisterActivity extends Activity {
    public HashMap<String, Object> params;
    public Button title_back,title_right,button_register;
    public ImageView iv;
    public TextView tv_warn, title_tv;;
    public String email;
    public String username;
    public String pwd;
    public String phone;
    public String school;
    public String court;
    public String professional;
    private MyApplication myApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        myApplication = (MyApplication) this
                .getApplicationContext();
        myApplication.addActivity(this);
        title_tv = (TextView) this.findViewById(R.id.title_tv);
        title_tv.setText("用户注册");
        title_right = (Button) this.findViewById(R.id.button_right);
        title_back = (Button) this.findViewById(R.id.button_back);
        button_register = (Button) this.findViewById(R.id.btn_register);
        personal();
    }

    public void personal() {
        title_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(UserRegisterActivity.this,MainActivity.class);
                UserRegisterActivity.this.startActivity(intent);
                UserRegisterActivity.this.finish();

            }
        });
        title_right.setVisibility(View.GONE);
        button_register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checked()) {

                    getParams();

                    HttpHelper.asyncPost(getUrl(), params,
                            new Callback() {
                                @Override
                                public void dataLoaded(Message msg) {
                                    if (msg.what == 200) {
                                        onSuccessRequest(msg);
                                    } else {
                                        onFailedRequest();
                                    }

                                }
                            });

                }
            }
        });
    }

    public void onFailedRequest() {
        Toast.makeText(
                UserRegisterActivity.this,
                "失败，连接超时", Toast.LENGTH_LONG)
                .show();
    }

    public void onSuccessRequest(Message msg) {
        if (msg.obj.toString()
                .equals("用户名已被注册")) {
            tv_warn = (TextView) UserRegisterActivity.this
                    .findViewById(R.id.tv_warn);
            tv_warn.setText("用户名已被注册");
        } else {
            Toast.makeText(
                    UserRegisterActivity.this,
                    msg.obj.toString(),
                    Toast.LENGTH_LONG).show();
            UserRegisterActivity.this.finish();
        }
    }

    public void getParams() {
        params = new HashMap<String, Object>();
        params.put("email", email);
        params.put("username", username);
        params.put("pwd", pwd);
        params.put("phone", phone);
        params.put("school", school);
        params.put("court", court);
        params.put("professional", professional);
    }

    public String getUrl() {
        return Constants.URL
                + "/Trade/add_user.do";
    }

    private boolean checked() {
        email = ((EditText) this.findViewById(R.id.et_email)).getText()
                .toString();
        username = ((EditText) this.findViewById(R.id.et_username)).getText()
                .toString();
        pwd = ((EditText) this.findViewById(R.id.et_user_pwd)).getText()
                .toString();
        school = ((EditText) this.findViewById(R.id.et_school)).getText()
                .toString();
        professional = ((EditText) this.findViewById(R.id.et_professional))
                .getText().toString();

        court = ((EditText) this.findViewById(R.id.et_court)).getText()
                .toString();
        phone = ((EditText) this.findViewById(R.id.phone)).getText()
                .toString();
        boolean protocol = ((CheckBox) this.findViewById(R.id.cb_protocol))
                .isChecked();
        if (username == null || username.equals("")) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (school == null || school.equals("")) {
            Toast.makeText(this, "学校不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!protocol) {
            Toast.makeText(this, "必须同意协议", Toast.LENGTH_SHORT).show();
            return false;
        } else if (phone == null || phone.equals("")) {
            Toast.makeText(this, "联系号码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (phone.length()!=11) {
            Toast.makeText(this, "联系号码必须为11位", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (pwdCheck()) return false;

        return true;
    }

    public boolean pwdCheck() {
        if (pwd == null || pwd.equals("")) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

}
