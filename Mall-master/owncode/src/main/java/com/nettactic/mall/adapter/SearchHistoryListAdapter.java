package com.nettactic.mall.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nettactic.mall.R;
import com.nettactic.mall.activity.SearchResultActivity;
import com.nettactic.mall.bean.SearchHistoryInfoBean;
import com.nettactic.mall.cache.ACache;
import com.nettactic.mall.common.MyApplication;
import com.nettactic.mall.database.SearchHistorySQLHelper;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.GUID;

import java.util.List;

public class SearchHistoryListAdapter extends BaseAdapter {

    private List<SearchHistoryInfoBean> mData;

    private Context mContext;
    private Activity mActivity;
    private LayoutInflater mInflater;

    private String TAG = "MainFragment";
    private ACache mCache;

    public SearchHistoryListAdapter(Activity mActivity, Context context,
                                    List<SearchHistoryInfoBean> mData) {
        this.mData = mData;
        this.mContext = context;
        this.mActivity = mActivity;
        mCache = ACache.get(MyApplication.getContext());
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int arg0) {
        return arg0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_search_history_list, null);
            holder = new ViewHolder();
            holder.search_keyTv = (TextView) convertView
                    .findViewById(R.id.search_key);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SearchHistoryInfoBean searchhistoryinfobean = mData.get(position);
        final String key = searchhistoryinfobean.getKey();
        holder.search_keyTv.setText(key);
        holder.search_keyTv.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                gotoSearchHistory(key);
            }
        } );
        return convertView;

    }

    private void gotoSearchHistory(String mKey) {
        String keyId = GUID.getGUID();
        String userid ="" ;
        String lasttime = System.currentTimeMillis() + "";
        SearchHistoryInfoBean data = new SearchHistoryInfoBean(keyId, mKey, userid, lasttime);
        SearchHistorySQLHelper.addSearchHistory(data);
        Intent intent=new Intent(mContext, SearchResultActivity.class);
        intent.putExtra("keyword",mKey);
        mActivity.startActivity(intent);
        AndroidUtils.enterActvityAnim(mActivity);
    }

    public static class ViewHolder {
        public TextView search_keyTv;

    }
}
