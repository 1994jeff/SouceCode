package com.yhx.app.campus_life;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yhx.app.common.Constants;
import com.yhx.app.common.HttpHelper;
import com.yhx.app.entity.Users;
import com.yhx.app.service.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class UpdateInfoActivity extends UserRegisterActivity {

    private SharedPreferences sp;
    MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = this.getSharedPreferences("User.xml", MODE_PRIVATE);
        myApplication = (MyApplication) getApplicationContext();
        HashMap<String, Object> param = new HashMap<String,Object>();
        Users u = (Users) myApplication.userMap.get("user");
        if(u==null){
            Toast.makeText(this,"对不起，请先登录再修改个人信息!",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        ((EditText)findViewById(R.id.et_email)).setText(u.getEmail());
        ((EditText)findViewById(R.id.et_username)).setText(u.getUserName());
        ((EditText)findViewById(R.id.et_user_pwd)).setText(u.getPassword());
        ((EditText)findViewById(R.id.et_school)).setText(u.getSchool());
        ((EditText)findViewById(R.id.et_court)).setText(u.getCourt());
        ((EditText)findViewById(R.id.et_professional)).setText(u.getProfessional());
        ((EditText)findViewById(R.id.phone)).setText(u.getPhone());
//        param.put("username", u.getUserName());
//        HttpHelper.asyncPost(Constants.URL
//                        + "/Trade/query_user.do", param,
//        new HttpHelper.Callback() {
//            @Override
//            public void dataLoaded(Message msg) {
//                if (msg.what == 200) {
//                    Users user = new Users();
//                    try {
//                        JSONObject json = new JSONObject(
//                                msg.obj.toString());
//                        user.setUserId(json
//                                .getInt("userId"));
//                        user.setUserName(json
//                                .getString("userName"));
//                        user.setEmail(json
//                                .getString("email"));
//                        user.setPassword(json
//                                .getString("password"));
//                        user.setSchool(json
//                                .getString("school"));
//                        user.setCourt(json
//                                .getString("court"));
//                        user.setProfessional(json
//                                .getString("professional"));
//                        user.setPhone(json.getString("phone"));
//                        if (!sp.getString("user",
//                                "")
//                                .equals(json
//                                        .getString("userName"))) {
//                            SharedPreferences.Editor eidt = sp.edit();
//                            eidt.putString(
//                                    "user",
//                                    json.getString("userName"));
//                            eidt.putString(
//                                    "pw",
//                                    json.getString("password"));
//                            eidt.commit();
//                        }
//                        MyApplication.getInstance().userMap.put(
//                                "user", user);

//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    onFailedRequest();
//                }
//
//            }
//        });
    }

    @Override
    public void personal() {
        super.personal();
        findViewById(R.id.agreeContainer).setVisibility(View.GONE);
        findViewById(R.id.pwdContainer).setVisibility(View.GONE);
        ((CheckBox)findViewById(R.id.cb_protocol)).setChecked(true);
        title_right.setVisibility(View.GONE);
        button_register.setText("修改信息");
        title_tv.setText("修改信息");
    }

    @Override
    public String getUrl() {
        return Constants.URL
                + "/Trade/modify_user.do";
    }

    @Override
    public void getParams() {
        super.getParams();
        params.remove("pwd");
    }

    @Override
    public boolean pwdCheck() {
        return false;
    }

    @Override
    public void onSuccessRequest(Message msg) {
        if (msg.obj.toString()
                .equals("用户名不存在")) {
            tv_warn = (TextView) UpdateInfoActivity.this
                    .findViewById(R.id.tv_warn);
            tv_warn.setText("用户名不存在");
        } else {
            Toast.makeText(
                    UpdateInfoActivity.this,
                    msg.obj.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }
}
