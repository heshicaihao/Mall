package com.nettactic.mall.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nettactic.mall.bean.OrderBean;

public class JSONUtil {

	/**
	 * 将json转化为实体POJO
	 * 
	 * @param jsonStr
	 * @param obj
	 * @return
	 */
	public static <T> Object JSONToObj(String jsonStr, Class<T> obj) {
		T t = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			t = objectMapper.readValue(jsonStr, obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	/**
	 * 将实体POJO转化为JSON
	 * 
	 * @param obj
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 */
	public static <T> JSONObject objectToJson(T obj) {
		ObjectMapper mapper = new ObjectMapper();
		JSONObject jsonobject = null;
		String jsonStr = "";
		try {
			jsonStr = mapper.writeValueAsString(obj);
			jsonobject = new JSONObject(jsonStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonobject;
	}

	/**
	 * 截取除第一项外的JSONArray
	 * 
	 * @param jsonArray
	 * @return
	 */
	public static JSONArray resolveNofJsonSONArray(JSONArray jsonArray) {
		JSONArray NofJsonArray = new JSONArray();
		try {
			for (int i = 1; i < jsonArray.length(); i++) {
				NofJsonArray.put(jsonArray.get(i));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return NofJsonArray;
	}

	public static JSONArray resolveFrameJSONArray(JSONArray jsonArray) {
		JSONArray mFrameJsonArray = new JSONArray();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				JSONObject object = new JSONObject(
						jsonObject.getString("inpage_content"));
				mFrameJsonArray.put(object);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return mFrameJsonArray;
	}

	/**
	 * 解析请求成功后的结果JSONObject
	 * 
	 * @param responseBody
	 * @return JSONObject
	 * @throws UnsupportedEncodingException
	 * @throws JSONException
	 */
	public static JSONObject resolveResult(byte[] responseBody)
			throws UnsupportedEncodingException, JSONException {
		String json = new String(responseBody, "UTF-8");
		JSONObject JSONObject = new JSONObject(json);
		JSONObject result = JSONObject.optJSONObject("result");
		return result;
	}

	/**
	 * 查找数组中的对应 Index
	 * 
	 * @param JsonObject
	 * @param tier
	 * @param theOrder
	 * @param elementType
	 * @return
	 * @throws JSONException
	 */
	public static int getIndex(JSONArray JsonObject, int tier, int theOrder,
			String elementType) throws JSONException {
		int index = -1;
		for (int i = 0; i < JsonObject.length(); i++) {
			JSONObject jsonobject = (JSONObject) JsonObject.get(i);
			// int MyTier = jsonobject.getInt("tier");
			int MyTheOrder = jsonobject.getInt("theOrder");
			String MyElementType = jsonobject.getString("elementType");
			// if (tier == MyTier) {
			if (theOrder == MyTheOrder) {
				if (elementType.equals(MyElementType)) {
					index = i;
					break;
				} else {
					return -1;
				}
			}
			// }
		}
		return index;
	}

	/**
	 * 查找数组中的对应 Index
	 * 
	 * @param JsonObject
	 * @param theOrder
	 * @param elementType
	 * @return
	 * @throws JSONException
	 */
	public static int getIndex(JSONArray JsonObject, int theOrder,
			String elementType) throws JSONException {
		int index = -1;
		for (int i = 0; i < JsonObject.length(); i++) {
			JSONObject jsonobject = (JSONObject) JsonObject.get(i);
			int MyTheOrder = jsonobject.getInt("theOrder");
			String MyElementType = jsonobject.getString("elementType");
			if (theOrder == MyTheOrder) {
				if (elementType.equals(MyElementType)) {
					index = i;
					break;
				}
			}
		}
		return index;
	}

	/**
	 * 查找数组中的对应 Index
	 * 
	 * @param JsonObject
	 * @param elementType
	 * @return
	 * @throws JSONException
	 */
	public static int getIndex(JSONArray JsonObject, String elementType)
			throws JSONException {
		int index = -1;
		for (int i = 0; i < JsonObject.length(); i++) {
			JSONObject jsonobject = (JSONObject) JsonObject.get(i);
			String MyElementType = jsonobject.getString("elementType");
			if (elementType.equals(MyElementType)) {
				index = i;
				break;
			}
		}
		return index;
	}

	public static String getData(String code, String content) {
		JSONObject object = new JSONObject();
		try {
			object.put("code", code);
			object.put("content", content);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String data = object.toString();
		return data;
	}

	/**
	 * 拼屏幕 信息
	 * 
	 * @param screenWidth
	 * @param screenHeight
	 * @return
	 */
	public static String getScreenData(String screenWidth, String screenHeight) {
		JSONObject object = new JSONObject();
		try {
			object.put("screenwidth", screenWidth);
			object.put("screenheight", screenHeight);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String data = object.toString();
		return data;
	}

	/**
	 * 解析 获得订单数据
	 * 
	 * @param arr
	 * @return
	 */
	public static List<OrderBean> getOrderList(JSONArray arr) {
		List<OrderBean> orderList = new ArrayList<OrderBean>();

		for (int i = 0; i < arr.length(); i++) {
			OrderBean orderbean = new OrderBean();
			try {
				JSONObject object = (JSONObject) arr.get(i);
				String order_id = object.optString("order_id");
				orderbean.setOrder_id(order_id);

				String itemnum = object.optString("itemnum");
				orderbean.setItemnum(itemnum);

				String amount = object.optString("amount");
				orderbean.setAmount(amount);

				String createtime = object.optString("createtime");
				orderbean.setCreatetime(createtime);

				String pay_status = object.optString("pay_status");
				orderbean.setPay_status(pay_status);

				String ship_status = object.optString("ship_status");
				orderbean.setShip_status(ship_status);

				String status = object.optString("status");
				orderbean.setStatus(status);

				JSONArray itemarr = object.getJSONArray("item");
				orderbean.setItem(itemarr.toString());

				orderList.add(orderbean);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return orderList;

	}
	
	/**
	 * 拼屏幕 信息
	 * 
	 * @param user_id
	 * @param promote_code
	 * @return
	 */
	public static String getPromoteCodeData(String user_id, String promote_code) {
		JSONObject object = new JSONObject();
		try {
			object.put("user_id", user_id);
			object.put("promote_code", promote_code);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String data = object.toString();
		return data;
	}

}
