package com.willchun.library.demo.view;/**
 * Created by Administrator on 2014/9/24.
 */

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.willchun.library.adapter.MixedChoiceAdapter;
import com.willchun.library.base.AndActivity;
import com.willchun.library.demo.R;
import com.willchun.library.view.ToastView;

/**
 * @author willchun (277143980@qq.com)
 * @github https://github.com/willchun
 * @date 2014/9/24
 */
public class MixedChoiceAdapterViewDemo extends AndActivity implements View.OnClickListener{

    private MixedChoiceAdapter<String> mAdapter;

    private String[] cities = {"全程", "闵行","徐汇","黄埔","虹口","卢湾","杨浦","宋江","上海周边"};


    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.demo_mixed_choice_view_adpater);

        mAdapter = new MixedChoiceAdapter<String>(this, R.layout.item_mixed_choice_adapter){
            @Override
            protected void update(String item, View root, int position) {
                ((TextView)root.findViewById(R.id.item_mixed_choice_tv)).setText(item);
            }
        };
        mAdapter.setItemBg(R.drawable.shape_bg_mixed_choose_default, R.drawable.shape_bg_mixed_choose_selected);
        mAdapter.addAll(cities);
        mAdapter.setMaxNumber(3);

        mAdapter.setOnMixedChoiceListener(new MixedChoiceAdapter.OnMixedChoiceListener() {
            @Override
            public void onCurrentClickItem(int position, boolean status) {
                ToastView.show(MixedChoiceAdapterViewDemo.this, "position:" + position + "  status:" + status);
            }
        });
        mAdapter.setMixedKeys(0);

        aq.find(R.id.demo_view_mixed_choice_adapter_gv).adapter(mAdapter);
        aq.find(R.id.demo_view_mixed_choice_adapter_gv).getGridView().setOnItemClickListener(mAdapter);

        aq.find(R.id.demo_view_mixed_choice_adapter_button).clicked(this);

        ((RadioGroup)aq.id(R.id.demo_view_mixed_choice_adapter_rg).getView()).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.demo_view_mixed_choice_adapter_mode_single_rb:
                        mAdapter.setMode(MixedChoiceAdapter.MODE.SINGLE);
                        break;
                    case R.id.demo_view_mixed_choice_adapter_mode_mulit_rb:
                        mAdapter.setMode(MixedChoiceAdapter.MODE.MULTI);
                        break;
                    case R.id.demo_view_mixed_choice_adapter_mode_mixed_rb:
                        mAdapter.setMode(MixedChoiceAdapter.MODE.MIXED);
                        break;
                }
            }
        });
        ((RadioButton)aq.id(R.id.demo_view_mixed_choice_adapter_mode_mulit_rb).getView()).setChecked(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.demo_view_mixed_choice_adapter_button:
                ToastView.show(MixedChoiceAdapterViewDemo.this, "size :" + mAdapter.getCurrentSelectedPosition().size() + "  ===>" + mAdapter.getCurrentSelectedPosition().toString());
            break;

        }
    }
}
