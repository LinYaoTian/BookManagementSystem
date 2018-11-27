package com.rdc.bms.mvp.model;

import com.rdc.bms.app.App;
import com.rdc.bms.config.Constants;
import com.rdc.bms.dto.LoginDTO;
import com.rdc.bms.mvp.contract.ILoginContract;
import com.rdc.bms.util.GsonUtil;
import com.rdc.bms.util.OkHttpResultCallback;
import com.rdc.bms.util.OkHttpUtil;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class LoginModel implements ILoginContract.Model {

    private ILoginContract.Presenter mPresenter;

    public LoginModel(ILoginContract.Presenter presenter){
        mPresenter = presenter;
    }

    @Override
    public void login(String account, String password, int type) {
        String url = Constants.BASE_URL + "users/login?";
        Map<String,String> map = new HashMap<>();
        map.put("userId",account);
        map.put("password",password);
        map.put("type",type+"");
        OkHttpUtil.getInstance().postAsync(url, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                mPresenter.loginError(e.getMessage());
            }

            @Override
            public void onResponse(byte[] bytes) {
                try {
                    String s = new String(bytes,"UTF-8");
                    LoginDTO dto = GsonUtil.gsonToBean(s,LoginDTO.class);
                    if (dto.isSuccess()){
                        App.setUser(dto.transform());
                        mPresenter.loginSuccess();
                    }else {
                        mPresenter.loginError(dto.getMsg());
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    mPresenter.loginError(e.getMessage());
                }
            }
        },map);
    }
}
