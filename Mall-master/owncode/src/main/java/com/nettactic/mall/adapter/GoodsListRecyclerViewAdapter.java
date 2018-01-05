package com.nettactic.mall.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nettactic.mall.R;
import com.nettactic.mall.activity.GoodsDetailsActivity;
import com.nettactic.mall.utils.AndroidUtils;


/**
 * Created by Heshicaihao on 2017/7/4.
 */
public class GoodsListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private int type = 0;//0:LinearViewHolder  1:GridViewHolder
    private Activity mActivity;

    public GoodsListRecyclerViewAdapter(Context context, Activity mActivity) {
        this.context = context;
        this.mActivity = mActivity;
    }

    //点击切换布局的时候通过这个方法设置type
    public void setType(int type) {
        this.type = type;
    }

    @Override
    //用来获取当前项Item是哪种类型的布局
    public int getItemViewType(int position) {
        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View baseView;
        if (viewType == 0) {
            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview_goods_list, parent, false);
            LinearViewHolder linearViewHolder = new LinearViewHolder(baseView);
            return linearViewHolder;
        } else {
            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gridview_goods_list, parent, false);
            GridViewHolder gridViewHolder = new GridViewHolder(baseView);
            return gridViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (type == 0) {
            LinearViewHolder linearViewHolder = (LinearViewHolder) holder;
            linearViewHolder.tvName.setText("小米Mix256G 94%超高屏占比 概念机 小米商城黑色现货购买 赠送保护套");
            linearViewHolder.tvPriceYZ.setText("颐众价：¥ 3998");
            linearViewHolder.tvPrice.setText("市场价：¥ 4199");
            linearViewHolder.iv.setImageResource(R.drawable.timg);
            linearViewHolder.itemLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoSearchHistory("123");
                }
            });
        } else {
            GridViewHolder gridViewHolder = (GridViewHolder) holder;
            gridViewHolder.tvName.setText("小米note2顶配版 双曲面屏 小米商城黑色现货购买 赠送保护套");
            gridViewHolder.tvPriceYZ.setText("颐众价：¥ 2298");
            gridViewHolder.tvPrice.setText("市场价：¥ 2399");
            gridViewHolder.iv.setImageResource(R.drawable.note);
            gridViewHolder.itemLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoSearchHistory("123");
                }
            });
            if (position % 2 == 0)
                gridViewHolder.rightView.setVisibility(View.VISIBLE);
            else
                gridViewHolder.rightView.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return 35;
    }

    private void gotoSearchHistory(String goods_id) {
        Intent intent = new Intent(context, GoodsDetailsActivity.class);
        intent.putExtra("goods_id", goods_id);
        mActivity.startActivity(intent);
        AndroidUtils.enterActvityAnim(mActivity);
    }

    public static class LinearViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv;
        private TextView tvName, tvPriceYZ, tvPrice;
        private LinearLayout itemLinear;

        public LinearViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.imageview);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvPriceYZ = (TextView) itemView.findViewById(R.id.tv_price_yz);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            itemLinear = (LinearLayout) itemView.findViewById(R.id.listview_goods_list_item);

        }
    }

    public static class GridViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv;
        private TextView tvName, tvPriceYZ, tvPrice;
        private View rightView;
        private LinearLayout itemLinear;

        public GridViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.imageview);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvPriceYZ = (TextView) itemView.findViewById(R.id.tv_price_yz);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            rightView = itemView.findViewById(R.id.view_right);
            itemLinear = (LinearLayout) itemView.findViewById(R.id.gridview_goods_list_item);
        }
    }

}
