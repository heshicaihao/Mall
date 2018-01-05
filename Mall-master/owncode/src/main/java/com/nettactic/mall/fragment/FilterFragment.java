package com.nettactic.mall.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nettactic.mall.R;
import com.nettactic.mall.adapter.FilterGridViewHolder;
import com.nettactic.mall.adapter.FilterListViewHolder;
import com.nettactic.mall.bean.FirstEvent;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.utils.LogUtils;
import com.nettactic.mall.widget.MyGridView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class FilterFragment extends Fragment implements View.OnClickListener {

    public String TAG = "FilterFragment";

    private View mRootView;

    private TextView mOkBtn;
    private TextView mResetBtn;
    private TextView mTextView;
    private ListView mListView;
    private JSONArray mData;
    public List<String> mSelectPaths = new ArrayList<String>();

    public void setData(JSONArray mData) {
        this.mData = mData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_filter, container,
                false);
        initView(mRootView);
        initData();
        return mRootView;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_btn:
                EventBus.getDefault().post(
                        new FirstEvent("FirstEvent btn clicked"));

                break;

            case R.id.reset_btn:
                mSelectPaths.clear();
                setAdapter();

                break;

            default:

                break;

        }

    }

    private void initView(View view) {
        mOkBtn = (TextView) view.findViewById(R.id.ok_btn);
        mResetBtn = (TextView) view.findViewById(R.id.reset_btn);
        mListView = (ListView) view.findViewById(R.id.list_view);

        mOkBtn.setOnClickListener(this);
        mResetBtn.setOnClickListener(this);
    }

    private void initData() {
        setAdapter();
    }

    private void setAdapter() {
        MyListAdapter mAdapter = new MyListAdapter(getActivity(), MyApplication.getContext(), mData);
        mListView.setAdapter(mAdapter);
    }

    /**
     *
     * @author heshicaihao
     */
    class MyListAdapter extends BaseAdapter {

        private Activity mActivity;
        private Context mContext;
        private LayoutInflater mInflater;
        private ChildGridAdapter mChildAdapter;

        private JSONArray mData;

        public MyListAdapter(Activity activity, Context context,
                             JSONArray mData) {
            this.mData = mData;
            this.mContext = context;
            this.mActivity = activity;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            int count = mData == null ? 0 : mData.length();
            return count;
        }

        @Override
        public Object getItem(int position) {
            try {
                return mData == null ? null : mData.get(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FilterListViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(
                        R.layout.item_filter_list, null);
                holder = new FilterListViewHolder();
                holder.mTitle = (TextView) convertView.findViewById(R.id.classify_name_tv);
                holder.mGrid = (MyGridView) convertView
                        .findViewById(R.id.mGrid);
                convertView.setTag(holder);
            } else {
                holder = (FilterListViewHolder) convertView.getTag();
            }

            try {
                JSONObject jsonobject = (JSONObject) mData.get(position);
                String title = jsonobject.optString("title");
                holder.mTitle.setText(title);
                final JSONArray content = jsonobject.optJSONArray("content");
                mChildAdapter = new ChildGridAdapter(mActivity, mContext,
                        content);
                holder.mGrid.setAdapter(mChildAdapter);
                holder.mGrid.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {
                                mTextView = (TextView) view.findViewById(R.id.classify_value_tv);
                                try {
                                    JSONObject jsonobject = (JSONObject) content.get(position);
                                    String content_id = jsonobject.optString("id");
                                    boolean contains = mSelectPaths.contains(content_id);
                                    if (!contains) {
                                        mTextView.setBackgroundResource(R.drawable.btn_bg);
                                        mTextView.setTextColor(0xffffffff);
                                        mSelectPaths.add(content_id);
                                        LogUtils.logd(TAG, "holder.mGrid_btn_bg");
                                    } else {
                                        mTextView.setBackgroundResource(R.drawable.back_btn_bg);
                                        mTextView.setTextColor(0xff666666);
                                        mSelectPaths.remove(content_id);
                                        LogUtils.logd(TAG, "holder.mGrid_back_btn_bg");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return convertView;

        }

    }

    /**
     * 选择图片每一个日期的Grid 的适配器
     *
     * @author heshicaihao
     */
    class ChildGridAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private JSONArray mData;

        private Activity mActivity;
        private Context mContext;


        public ChildGridAdapter(Activity activity, Context context,
                                JSONArray mData) {
            this.mData = mData;
            this.mContext = context;
            this.mActivity = activity;
            this.mInflater = LayoutInflater.from(context);

        }

        @Override
        public int getCount() {
            int count = mData == null ? 0 : mData.length();
            return count;
        }

        @Override
        public Object getItem(int position) {
            try {
                return mData == null ? null : mData.get(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FilterGridViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(
                        R.layout.item_filter_grid, null);
                holder = new FilterGridViewHolder();
                holder.mTextView = (TextView) convertView
                        .findViewById(R.id.classify_value_tv);
                holder.mGridItem = (RelativeLayout) convertView
                        .findViewById(R.id.grid_item);
                convertView.setTag(holder);
            } else {
                holder = (FilterGridViewHolder) convertView.getTag();
            }
            try {
                JSONObject jsonobject = (JSONObject) mData.get(position);
                String id = jsonobject.optString("id");
                String title = jsonobject.optString("title");
                holder.mTextView.setText(title);
                if (mSelectPaths.contains(id)) {
                    holder.mTextView.setBackgroundResource(R.drawable.btn_bg);
                    holder.mTextView.setTextColor(0xffffffff);
                    LogUtils.logd(TAG, "getView_btn_bg");
                } else {
                    holder.mTextView.setBackgroundResource(R.drawable.back_btn_bg);
                    holder.mTextView.setTextColor(0xff666666);
                    LogUtils.logd(TAG, "getView_back_btn_bg");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return convertView;

        }

    }

}

