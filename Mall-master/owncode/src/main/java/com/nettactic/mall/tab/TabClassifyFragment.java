package com.nettactic.mall.tab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nettactic.mall.R;
import com.nettactic.mall.adapter.TabClassifyLeftListAdapter;
import com.nettactic.mall.adapter.TabClassifyRightListAdapter;
import com.nettactic.mall.base.BaseFragment;
import com.nettactic.mall.utils.AssetsUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TabClassifyFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageView mBack;
    private TextView mTitle;
    private TextView mSave;
    private View mView;
    private ListView left_listview;
    private ListView right_listview;
    private TabClassifyLeftListAdapter mLeftAdapter;
    private TabClassifyRightListAdapter mRightAdapter;
    private JSONArray mData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_tab_classify, null);
        }
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }
        initView(mView);
        initData();
        return mView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        setRightAdapter(mData,position);
        mLeftAdapter.setSeclection(position);
        mLeftAdapter.notifyDataSetInvalidated();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.save:
                break;
            case R.id.back:

                break;
            default:
                break;
        }
    }

    private void initView(View view) {
        mBack = (ImageView) view.findViewById(R.id.back);
        mTitle = (TextView) view.findViewById(R.id.title);
        mSave = (TextView) view.findViewById(R.id.save);
        left_listview = (ListView) view.findViewById(R.id.left_listview);
        right_listview = (ListView) view.findViewById(R.id.right_listview);

        mSave.setText(R.string.delete);
        mSave.setVisibility(View.GONE);
        mTitle.setText(R.string.my_works);

        left_listview.setOnItemClickListener(this);
        mBack.setOnClickListener(this);
        mSave.setOnClickListener(this);
    }

    private void initData() {
        String json = AssetsUtils.getJson(getContext(), "categorys_info.txt");
        resolveInfo(json);

    }

    private void resolveInfo(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            String code = obj.optString("code");
            if ("0".equals(code)) {
                JSONObject result = obj.optJSONObject("result");
                mData = result.optJSONArray("categorys_info");
                setLeftAdapter(mData);
                setRightAdapter(mData,0);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setLeftAdapter(JSONArray data) {
        mLeftAdapter = new TabClassifyLeftListAdapter(getActivity(), getContext(), data);
        left_listview.setAdapter(mLeftAdapter);
    }

    private void setRightAdapter(JSONArray data, int position){
        try {
            JSONObject  positionJSONObject = data.getJSONObject(position);
            JSONArray mRightCategorys = positionJSONObject.optJSONArray("content");
            mRightAdapter = new TabClassifyRightListAdapter(getActivity(), getContext(), mRightCategorys);
            right_listview.setAdapter(mRightAdapter);
            mRightAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
