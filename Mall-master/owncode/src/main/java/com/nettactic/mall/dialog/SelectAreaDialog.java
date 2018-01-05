package com.nettactic.mall.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.nettactic.mall.R;
import com.nettactic.mall.widget.WheelView;
import com.nettactic.mall.widget.wheelview.OnWheelChangedListener;
import com.nettactic.mall.widget.wheelview.adapters.ArrayWheelAdapter;
import com.nettactic.mall.widget.wheelview.bean.CityModel;
import com.nettactic.mall.widget.wheelview.bean.DistrictModel;
import com.nettactic.mall.widget.wheelview.bean.ProvinceModel;

public class SelectAreaDialog {

	private int dCurrent = 0;
	private String data;
	private String callBackData;
	private String mCurrentProviceName;
	private String mCurrentCityName;
	private String mCurrentDistrictName;
	private String mCurrentZipCode;
	private String[] mProvinceDatas;
	private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	private Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
	private Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();
	private WheelView provinceWV;
	private WheelView cityWV;
	private WheelView districtWV;
	private Button btn_cancel;
	private Button btn_confirm;

	private Context context;
	private SelectCallBack callBack;

	public SelectCallBack getCallBack() {
		return callBack;
	}

	public void setCallBack(SelectCallBack callBack) {
		this.callBack = callBack;
	}

	public interface SelectCallBack {
		public void isConfirm(String callBackData);
	}

	public SelectAreaDialog(Context mcontext, String data) {
		this.context = mcontext;
		this.data = data;
	}

	private void setDialogLayoutParams(Dialog builder) {
		Window dialogWindow = builder.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);
		lp.height = LayoutParams.WRAP_CONTENT; // 高度
		// lp.alpha = 0.7f; // 透明度
		lp.width = LayoutParams.MATCH_PARENT;
		dialogWindow.setAttributes(lp);
	}

	public Dialog onCreateDialog() {
		final View view = View.inflate(context,
				R.layout.dialog_select_address, null);

		provinceWV = (WheelView) view.findViewById(R.id.id_province);
		cityWV = (WheelView) view.findViewById(R.id.id_city);
		districtWV = (WheelView) view.findViewById(R.id.id_district);
		btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		btn_confirm = (Button) view.findViewById(R.id.btn_confirm);

		initAreaViewData(data);
		final Dialog builder = new Dialog(context, R.style.Theme_dialog);
		builder.setContentView(view);
		setDialogLayoutParams(builder);
		builder.show();

		provinceWV.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				showProvince();
			}
		});
		cityWV.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				showCity();
			}
		});
		districtWV.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				showDistrict(newValue);
				dCurrent = newValue;
			}
		});

		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				builder.dismiss();
			}
		});
		btn_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (callBack != null) {
					callBack.isConfirm(callBackData);
				}
				builder.dismiss();
			}
		});
		return builder;
	}

	/**
	 * 显示 省 WheelView的信息，并给当前省对应的 市添加数据 和设置初始值
	 */
	private void showProvince() {
		int pCurrent = provinceWV.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		cityWV.setViewAdapter(new ArrayWheelAdapter<String>(context, cities));
		cityWV.setCurrentItem(0);
		showCity();
	}

	/**
	 * 显示 市 WheelView的信息，并给当前市对应的 区添加数据 和设置初始值
	 */
	private void showCity() {
		int cCurrent = cityWV.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[cCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentProviceName
				+ mCurrentCityName);
		if (areas == null) {
			areas = new String[] { "" };
		}
		districtWV
				.setViewAdapter(new ArrayWheelAdapter<String>(context, areas));
		districtWV.setCurrentItem(0);

		showDistrict(dCurrent);
	}

	/**
	 * 显示 区 WheelView的信息，并给当前区对应的 邮编
	 */
	private void showDistrict(int newValue) {
		mCurrentDistrictName = mDistrictDatasMap.get(mCurrentProviceName
				+ mCurrentCityName)[newValue];
		mCurrentZipCode = mZipcodeDatasMap.get(mCurrentProviceName
				+ mCurrentCityName + mCurrentDistrictName);
		callBackData = getShipAreaData(mCurrentProviceName, mCurrentCityName,
				mCurrentDistrictName, mCurrentZipCode);
	}

	/**
	 * 初始化默认选中的省、市、区
	 */
	private void setDefaultArea() {
		provinceWV.setViewAdapter(new ArrayWheelAdapter<String>(context,
				mProvinceDatas));
		// 设置可见条目数量
		provinceWV.setVisibleItems(7);
		cityWV.setVisibleItems(7);
		districtWV.setVisibleItems(7);
		showProvince();
	}

	private void initAreaViewData(String data) {
		try {
			JSONArray result = new JSONArray(data);
			changeArray(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setDefaultArea();
	}

	/**
	 * 将数据转化为 省、市、区 数组
	 */
	private void changeArray(JSONArray result) throws Exception {
		List<ProvinceModel> provinceList = getJson2Data(result);

		mProvinceDatas = new String[provinceList.size()];
		for (int i = 0; i < provinceList.size(); i++) {
			// 将全国省份数据转成数组保存到 mProvinceDatas
			mProvinceDatas[i] = provinceList.get(i).getName();
			List<CityModel> cityList = provinceList.get(i).getCityList();
			String[] cityNames = new String[cityList.size()];
			for (int j = 0; j < cityList.size(); j++) {
				// 遍历省下面的所有市的数据
				cityNames[j] = cityList.get(j).getName();
				List<DistrictModel> districtList = cityList.get(j)
						.getDistrictList();
				String[] distrinctNameArray = new String[districtList.size()];
				DistrictModel[] distrinctArray = new DistrictModel[districtList
						.size()];
				for (int k = 0; k < districtList.size(); k++) {
					// 遍历市下面所有区/县的数据
					DistrictModel districtModel = new DistrictModel(
							districtList.get(k).getName(), districtList.get(k)
									.getZipcode());
					// 将全国各省市区邮编数据转成数组保存到 mZipcodeDatasMap
					mZipcodeDatasMap.put(provinceList.get(i).getName()
							+ cityNames[j] + districtList.get(k).getName(),
							districtList.get(k).getZipcode());
					distrinctArray[k] = districtModel;
					distrinctNameArray[k] = districtModel.getName();
				}
				// 将全国各省市区 的区数据转成数组保存到 mDistrictDatasMap
				mDistrictDatasMap.put(provinceList.get(i).getName()
						+ cityNames[j], distrinctNameArray);
			}
			// 将全国各省市 的市数据转成数组保存到 mCitisDatasMap
			mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
		}

	}

	private String getShipAreaData(String ProviceName, String CityName,
			String DistrictName, String Code) {
		String data = "mainland:";
		data += ProviceName + "/";
		data += CityName + "/";
		data += DistrictName + ":";
		data += Code;
		return data;
	}

	/**
	 * 将 json数据转化为 对象
	 * 
	 * @param provinceList
	 * @return
	 * @throws Exception
	 */
	public static List<ProvinceModel> getJson2Data(JSONArray provinceList)
			throws Exception {
		List<ProvinceModel> mProvinceList = new ArrayList<ProvinceModel>();

		for (int i = 0; i < provinceList.length(); i++) {
			JSONObject province = provinceList.getJSONObject(i);
			String provinceName = province.optString("name");
			JSONArray cityList = province.optJSONArray("children");
			if (cityList != null) {
				ProvinceModel provinceModel = new ProvinceModel();
				provinceModel.setName(provinceName);
				List<CityModel> mCityList = new ArrayList<CityModel>();
				for (int j = 0; j < cityList.length(); j++) {
					JSONObject city = cityList.getJSONObject(j);
					String cityName = city.optString("name");
					JSONArray districtList = city.optJSONArray("children");
					if (cityList != null) {
						CityModel cityModel = new CityModel();
						cityModel.setName(cityName);

						List<DistrictModel> mDistrictList = new ArrayList<DistrictModel>();
						for (int k = 0; k < districtList.length(); k++) {
							JSONObject district = districtList.getJSONObject(k);
							String districtName = district.optString("name");
							String districtCode = district.optString("code");

							DistrictModel districtModel = new DistrictModel();

							districtModel.setName(districtName);
							districtModel.setZipcode(districtCode);

							mDistrictList.add(districtModel);
						}
						cityModel.setDistrictList(mDistrictList);
						mCityList.add(cityModel);
					}
				}
				provinceModel.setCityList(mCityList);
				mProvinceList.add(provinceModel);
			}
		}
		return mProvinceList;
	}

}