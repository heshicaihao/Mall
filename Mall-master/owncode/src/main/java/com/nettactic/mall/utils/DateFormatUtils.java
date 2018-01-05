package com.nettactic.mall.utils;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtils {

	/**
	 * 
	 * 把毫秒转化成日期
	 * 
	 * @param dateFormat
	 *            (日期格式，例如：MM/ dd/yyyy HH:mm:ss)
	 * 
	 * @param millSec
	 *            (毫秒数)
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String transferLongToDate(String dateFormat, Long millSec) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date date = new Date(millSec);
		return sdf.format(date);

	}

}
