package com.nettactic.mall.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nettactic.mall.R;
import com.nettactic.mall.utils.ToastUtils;


/**
 * 加减显示控件
 *
 * @author heshicaihao
 */
public class AddAndSubView extends LinearLayout {
    Context context;
    OnNumChangeListener onNumChangeListener;
    TextView addButton;
    TextView subButton;
    EditText editText;
    int num; // editText中的数值

    public AddAndSubView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View View = LayoutInflater.from(context).inflate(R.layout.widget_add_and_sub, this);
        num = 1;
        control();

    }

    private void control() {
        subButton = (TextView) findViewById(R.id.sub_btn);
        addButton = (TextView) findViewById(R.id.add_btn);
        editText = (EditText) findViewById(R.id.num_et);

        editText.setText("1");
        setViewListener();
    }

    /**
     * 获取editText中的值
     *
     * @return
     */
    public int getNum() {
        if (editText.getText().toString() != null) {
            return Integer.parseInt(editText.getText().toString());
        } else {
            return 1;
        }
    }

    /**
     * 设置editText中的值
     *
     * @param num
     */
    public void setNum(int num) {
        this.num = num;
        editText.setText(String.valueOf(num));
    }

    /**
     * 设置EditText文本变化监听
     *
     * @param onNumChangeListener
     */
    public void setOnNumChangeListener(OnNumChangeListener onNumChangeListener) {
        this.onNumChangeListener = onNumChangeListener;
    }

    /**
     * 设置文本变化相关监听事件
     */
    private void setViewListener() {
        subButton.setOnClickListener(new OnSubClickListener());
        addButton.setOnClickListener(new OnAddClickListener());
        editText.addTextChangedListener(new OnTextChangeListener());
    }

    public interface OnNumChangeListener {
        /**
         * 输入框中的数值改变事件
         *
         * @param view 整个AddAndSubView
         * @param num  输入框的数值
         */
        public void onNumChange(View view, int num);
    }

    /**
     * 减按钮事件监听器
     */
    class OnSubClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            subButton.setBackgroundResource(R.drawable.sub_bg_pressed);
            addButton.setBackgroundResource(R.drawable.add_bg_normal);
            String numString = editText.getText().toString();
            if (numString == null || numString.equals("")) {
                num = 1;
                editText.setText("1");
            } else {
                if (--num < 1) // 先减，再判断
                {
                    num++;
                    ToastUtils.show(R.string.mini_one);
                    editText.setText("1");
                    subButton.setBackgroundResource(R.drawable.sub_bg_normal);
                    addButton.setBackgroundResource(R.drawable.add_bg_pressed);
                } else {
                    editText.setText(String.valueOf(num));
                    if (onNumChangeListener != null) {
                        onNumChangeListener.onNumChange(AddAndSubView.this,
                                num);
                    }
                }
            }
        }
    }

    /**
     * 加按钮事件监听器
     */
    class OnAddClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            subButton.setBackgroundResource(R.drawable.sub_bg_normal);
            addButton.setBackgroundResource(R.drawable.add_bg_pressed);
            String numString = editText.getText().toString();
            if (numString == null || numString.equals("")) {
                num = 1;
                editText.setText("1");
            } else {
                if (++num < 1) // 先加，再判断
                {
                    num--;
                    ToastUtils.show(R.string.mini_one);
                    editText.setText("1");
                } else {
                    editText.setText(String.valueOf(num));

                    if (onNumChangeListener != null) {
                        onNumChangeListener.onNumChange(AddAndSubView.this,
                                num);
                    }
                }
            }
        }
    }

    /**
     * EditText输入变化事件监听器
     */
    class OnTextChangeListener implements TextWatcher {

        @Override
        public void afterTextChanged(Editable s) {
            String numString = s.toString();
            if (numString == null || numString.equals("")) {
                num = 1;
                editText.setText("1");
                if (onNumChangeListener != null) {
                    onNumChangeListener.onNumChange(AddAndSubView.this, num);
                }
            } else {
                int numInt = Integer.parseInt(numString);
                if (numInt < 1) {
                    ToastUtils.show(R.string.mini_one);
                    editText.setText("1");
                } else {
                    // 设置EditText光标位置 为文本末端
                    editText.setSelection(editText.getText().toString()
                            .length());
                    num = numInt;
                    if (onNumChangeListener != null) {
                        onNumChangeListener
                                .onNumChange(AddAndSubView.this, num);
                    }
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

    }

}

