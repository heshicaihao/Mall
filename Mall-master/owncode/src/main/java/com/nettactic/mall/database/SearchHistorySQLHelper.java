package com.nettactic.mall.database;

import com.nettactic.mall.bean.SearchHistoryInfoBean;
import com.nettactic.mall.common.MyApplication;

import java.util.List;

public class SearchHistorySQLHelper {

    /**
     * 最近作品插入 保留20条记录
     *
     * @param data
     */
    public static void addSearchHistory(SearchHistoryInfoBean data) {
        DataHelper dh = new DataHelper(MyApplication.getContext());
        dh.addSearchHistory(data);
    }

    /**
     *全部记录 查询
     *
     */
    public static List<SearchHistoryInfoBean> querytSearchHistory() {
        DataHelper dh = new DataHelper(MyApplication.getContext());
        List<SearchHistoryInfoBean> lists = dh.querytSearchHistory();
        return lists;
    }

    /**
     * 作品 删单条
     *
     */
    public static int deleteSearchHistory() {
        DataHelper dh = new DataHelper(MyApplication.getContext());
        int id = dh.deleteSearchHistory();
        return id;
    }


}
