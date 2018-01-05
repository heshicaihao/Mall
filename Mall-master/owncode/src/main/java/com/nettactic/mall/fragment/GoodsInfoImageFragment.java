package com.nettactic.mall.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nettactic.mall.R;
import com.nettactic.mall.activity.BrowseImageActivity;

import org.json.JSONArray;

import com.nettactic.mall.photoview.PhotoViewAttacher;
import com.nettactic.mall.utils.AndroidUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class GoodsInfoImageFragment extends Fragment {

    private String mImageUrl;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private PhotoViewAttacher mAttacher;
    private String mImageList;
    private String position;

    public static GoodsInfoImageFragment newInstance(String imageUrl, JSONArray list, int position) {
        final GoodsInfoImageFragment f = new GoodsInfoImageFragment();
        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        args.putString("mImageList", list.toString());
        args.putString("position", position + "");
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
        mImageList = getArguments() != null ? getArguments().getString("mImageList") : null;
        position = getArguments() != null ? getArguments().getString("position") : null;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_goods_info_image, container, false);
        mImageView = (ImageView) v.findViewById(R.id.image);

        ViewGroup.LayoutParams lp = mImageView.getLayoutParams();
        lp.width = AndroidUtils.getScreenWidth(getActivity());
        lp.height = lp.width * 480 / 800;
        mImageView.setLayoutParams(lp);

        mAttacher = new PhotoViewAttacher(mImageView);
        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                gotoImageDetailActivity();
            }
        });
        mProgressBar = (ProgressBar) v.findViewById(R.id.loading);
        return v;
    }

    private void gotoImageDetailActivity() {
        Intent intent = new Intent(getContext(), BrowseImageActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(BrowseImageActivity.EXTRA_IMAGE_URLS, mImageList);
        int positionInt = Integer.parseInt(position);
        intent.putExtra(BrowseImageActivity.EXTRA_IMAGE_INDEX, positionInt);
        getContext().startActivity(intent);
        AndroidUtils.enterActvityAnim(getActivity());

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageLoader.getInstance().displayImage(mImageUrl, mImageView, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                String message = null;
                switch (failReason.getType()) {
                    case IO_ERROR:
                        message = "下载错误";
                        break;
                    case DECODING_ERROR:
                        message = "图片无法显示";
                        break;
                    case NETWORK_DENIED:
                        message = "网络有问题，无法下载";
                        break;
                    case OUT_OF_MEMORY:
                        message = "图片太大无法显示";
                        break;
                    case UNKNOWN:
                        message = "未知的错误";
                        break;
                }
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                mProgressBar.setVisibility(View.GONE);
                mAttacher.update();
            }
        });

    }

}
