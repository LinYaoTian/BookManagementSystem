package com.rdc.bms.mvp.model;

import com.rdc.bms.app.App;
import com.rdc.bms.config.Constants;
import com.rdc.bms.dto.LoginDTO;
import com.rdc.bms.entity.User;
import com.rdc.bms.mvp.contract.IInfoDetail;
import com.rdc.bms.util.GsonUtil;
import com.rdc.bms.util.OkHttpResultCallback;
import com.rdc.bms.util.OkHttpUtil;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import okhttp3.Call;

public class InfoModel implements IInfoDetail.Model {

    private IInfoDetail.Presenter mPresenter;

    public InfoModel(IInfoDetail.Presenter presenter){
        mPresenter = presenter;
    }

    @Override
    public void update(User user) {
        String url = Constants.BASE_URL + "users/update";
        HashMap<String,String> map = new HashMap<>();
        map.put("userId",user.getUserId());
        map.put("password",user.getPassword());
        map.put("major",user.getMajor());
        map.put("sex",user.getSex());
        map.put("name",user.getName());
        OkHttpUtil.getInstance().postAsync(url, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                mPresenter.updateError(e.getMessage());
            }

            @Override
            public void onResponse(byte[] bytes) {
                try {
                    String s = new String(bytes,"UTF-8");
                    LoginDTO dto = GsonUtil.gsonToBean(s,LoginDTO.class);
                    if (dto.isSuccess()){
                        if (dto.getData().getUserId().equals(App.getUser().getUserId())){
                            App.setUser(dto.getData());
                        }
                        mPresenter.updateSuccess("更新个人信息成功！");
                    }else {
                        mPresenter.updateError(dto.getMsg());
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        },map);
    }

    @Override
    public void add(User user) {
        String url = Constants.BASE_URL + "users/insert";
        HashMap<String,String> map = new HashMap<>();
        map.put("userId",user.getUserId());
        map.put("password",user.getPassword());
        map.put("major",user.getMajor());
        map.put("sex",user.getSex());
        map.put("name",user.getName());
        OkHttpUtil.getInstance().postAsync(url, new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
                mPresenter.addError(e.getMessage());
            }

            @Override
            public void onResponse(byte[] bytes) {
                try {
                    String s = new String(bytes,"UTF-8");
                    LoginDTO dto = GsonUtil.gsonToBean(s,LoginDTO.class);
                    if (dto.isSuccess()){
                        mPresenter.addSuccess("添加读者成功！");
                    }else {
                        mPresenter.addError(dto.getMsg());
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    mPresenter.addError(e.getMessage());
                }
            }
        },map);
    }
}
