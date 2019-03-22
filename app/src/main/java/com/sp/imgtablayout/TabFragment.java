package com.sp.imgtablayout;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
*  @describe  -- 测试用的
*  @author zgq
*  @version 1.0.0
*  @date 2019/3/22
*/
public class TabFragment extends Fragment {

    private String content;
    private View view;
    private TextView tvContent;

    public TabFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab, container, false);
        tvContent = view.findViewById(R.id.tv_content);
        if (!TextUtils.isEmpty(content)){
            tvContent.setText(content);
        }
        return view;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
