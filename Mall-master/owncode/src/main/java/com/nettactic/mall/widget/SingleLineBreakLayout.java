package com.nettactic.mall.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nettactic.mall.R;
import com.nettactic.mall.activity.SearchResultActivity;
import com.nettactic.mall.bean.SearchHistoryInfoBean;
import com.nettactic.mall.database.SearchHistorySQLHelper;
import com.nettactic.mall.utils.AndroidUtils;
import com.nettactic.mall.utils.GUID;

import java.util.ArrayList;
import java.util.List;


public class SingleLineBreakLayout extends ViewGroup {
    private final static String TAG = "LineBreakLayout";
    /**
     * 所有标签
     */
    private List<String> lables;
    /**
     * 选中标签
     */
    private List<String> lableSelected = new ArrayList<>();

    //自定义属性
    private int LEFT_RIGHT_SPACE; //dip
    private int ROW_SPACE;
    private Activity activity;

    public SingleLineBreakLayout(Context context) {
        this(context, null);
    }

    public SingleLineBreakLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleLineBreakLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LineBreakLayout);
        LEFT_RIGHT_SPACE = ta.getDimensionPixelSize(R.styleable.LineBreakLayout_leftAndRightSpace, 10);
        ROW_SPACE = ta.getDimensionPixelSize(R.styleable.LineBreakLayout_rowSpace, 10);
        ta.recycle(); //回收
        // ROW_SPACE=20   LEFT_RIGHT_SPACE=40
        Log.v(TAG, "ROW_SPACE=" + ROW_SPACE + "   LEFT_RIGHT_SPACE=" + LEFT_RIGHT_SPACE);
    }

    /**
     * 添加标签
     *
     * @param lables 标签集合
     * @param add    是否追加
     */
    public void setLables(final Activity activity, List<String> lables, boolean add){
        this.activity = activity;
        if (this.lables == null) {
            this.lables = new ArrayList<>();
        }
        if (add) {
            this.lables.addAll(lables);
        } else {
            this.lables.clear();
            this.lables = lables;
        }
        if (lables != null && lables.size() > 0) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            for (final String lable : lables) {
                //获取标签布局
                final TextView tv = (TextView) inflater.inflate(R.layout.item_lable, null);
                tv.setText(lable);
                //设置选中效果
                if (lableSelected.contains(lable)) {
                    //选中
                    tv.setSelected(true);
                    tv.setTextColor(getResources().getColor(R.color.white));
                } else {
                    //未选中
                    tv.setSelected(false);
                    tv.setTextColor(getResources().getColor(R.color.text_color_gray_c));
                }
                //点击标签后，重置选中效果
                tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String  mKey =tv.getText().toString().trim();
                        String keyId = GUID.getGUID();
                        String userid ="" ;
                        String lasttime = System.currentTimeMillis() + "";
                        SearchHistoryInfoBean data = new SearchHistoryInfoBean(keyId, mKey, userid, lasttime);
                        SearchHistorySQLHelper.addSearchHistory(data);
                        Intent intent=new Intent(v.getContext(), SearchResultActivity.class);
                        intent.putExtra("keyword",mKey);
                        v.getContext().startActivity(intent);
                        AndroidUtils.enterActvityAnim(activity);
                    }
                });
                //将标签添加到容器中
                addView(tv);
            }
        }
    }

    /**
     * 获取选中标签
     */
    public List<String> getSelectedLables() {
        return lableSelected;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int height;
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int childCount = getChildCount();
            if (childCount <= 0) {
                height = 0;   //没有标签时，高度为0
            } else {
                for (int i = 0; i < childCount; i++) {
                    View view = getChildAt(i);
                    int childW = view.getMeasuredWidth();
                    width = width + childW + LEFT_RIGHT_SPACE;
                }
                int childH = getChildAt(0).getMeasuredHeight();
                height = childH;
            }
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int row = 0;
        int right = 0;   // 标签相对于布局的右侧位置
        int botom;       // 标签相对于布局的底部位置
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            int childW = childView.getMeasuredWidth();
            int childH = childView.getMeasuredHeight();
            right += childW;
            botom = row * (childH + ROW_SPACE) + childH;
            if (right > (r - LEFT_RIGHT_SPACE)) {
                row++;
                right = childW;
                botom = row * (childH + ROW_SPACE) + childH;
            }
            childView.layout(right - childW, botom - childH, right, botom);

            right += LEFT_RIGHT_SPACE;
        }
    }

}