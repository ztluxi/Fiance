package com.sharechain.finance.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;


/**
 * *Created by ${zhoutao} on 2017/12/14 0013.
 */
public class NetworkManager {
	/**
	* 当前网络是否为wifi
	* @param context 上下文
	* @return 是返回true，否返回false
	*/
	public static boolean isWiFiEnabled(Context context) {
        NetworkInfo info = NetworkManager.getNetworkInfo(context);

		return null != info && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI;
	}
    
	/**
	* 当前网络是否为gprs
	* @param context 上下文
	* @return 是返回true，否返回false
	*/
    public static boolean isGprsConnected(Context context) {
        NetworkInfo info = NetworkManager.getNetworkInfo(context);

		return null != info && info.isConnected() && info.getType() == ConnectivityManager.TYPE_ETHERNET;
	}
	
    /**
	* 当前网络是否为连接状态
	* @param context 上下文
	* @return 是返回true，否返回false
	*/
	public static boolean isConnnected(Context context) {
		 NetworkInfo info= NetworkManager.getNetworkInfo(context);

		return null != info && info.isConnected();
	}
	
	/**
	* 获取当前网络对象
	* @param context 上下文
	* @return 返回当前网络状态对象
	*/
	private static NetworkInfo getNetworkInfo(Context context){
		if(context == null){
			return null;
		}
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
        if (null != connectivityManager) {
		    NetworkInfo networkInfo= connectivityManager.getActiveNetworkInfo();
			return networkInfo;
		}
		return null;
	}

	public static boolean is4GNetWork = true;
	/***
	 * 判断当前网络是否是2G/3G/4G网络
	 * @param activity
	 * @return
	 */
	public static boolean is3G4GNetWork(Activity activity){
		if(is4GNetWork){
			String intentType = NetworkManager.GetNetworkType(activity); /**当前网络的类型*/
			if(!TextUtils.isEmpty(intentType)&&is4GNetWork){
				if(!intentType.startsWith("WIFI")){
					ToastManager.showToast(activity, "您当前使用的是2G/3G/4G流量,请注意合理使用流量！", true);
				}
			}
			//is4GNetWork = false;
		}
		return is4GNetWork;
	}
	/***
	 * 判断当前网络是否是3G 还是2G网络
	 * @param activity
	 * @return
	 */
	public static String GetNetworkType(Activity activity){
		String strNetworkType = "";
		ConnectivityManager connManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected())
		{
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
			{
				strNetworkType = "WIFI";
			}
			else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
			{
				String _strSubTypeName = networkInfo.getSubtypeName();
				// TD-SCDMA   networkType is 17
				int networkType = networkInfo.getSubtype();
				switch (networkType) {
					case TelephonyManager.NETWORK_TYPE_GPRS:
					case TelephonyManager.NETWORK_TYPE_EDGE:
					case TelephonyManager.NETWORK_TYPE_CDMA:
					case TelephonyManager.NETWORK_TYPE_1xRTT:
					case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
						strNetworkType = "2G";
						break;
					case TelephonyManager.NETWORK_TYPE_UMTS:
					case TelephonyManager.NETWORK_TYPE_EVDO_0:
					case TelephonyManager.NETWORK_TYPE_EVDO_A:
					case TelephonyManager.NETWORK_TYPE_HSDPA:
					case TelephonyManager.NETWORK_TYPE_HSUPA:
					case TelephonyManager.NETWORK_TYPE_HSPA:
					case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
					case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
					case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
						strNetworkType = "3G";
						break;
					case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
						strNetworkType = "4G";
						break;
					default:
						// http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
						if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000"))
						{
							strNetworkType = "3G";
						}
						else
						{
							strNetworkType = _strSubTypeName;
						}

						break;
				}
			}
		}
		return strNetworkType;
	}
}
