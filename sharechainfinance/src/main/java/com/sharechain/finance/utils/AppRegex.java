package com.sharechain.finance.utils;

/***
 *
 * Created by ${zhoutao} on 2017/12/27 0013.
 */
public final class AppRegex {
	// 手机号码
	public static final String MOBILEPHONEREGEX = "^1[3-9]\\d{9}$";
	// 手机验证码
	public static final String PHONECODEREGEX = "^\\d{6,16}$";
	// 用户真实姓名
	public static final String USERCNNAMEREGEX = "^[\u4E00-\u9FA5]{2,45}(?:·[\u4E00-\u9FA5]{2,45})*$";
	// 用户昵称
	public static final String USERNICKNAME = "^\\{2,10}$";
	// 用户密码规则1
	// 用户密码规则2只允许输入字符、数字、下划线和连字符。
	public static final String USERPASSREGEX = "^[A-Za-z0-9 _-]{6,20}$";
	//不能输入中文！
	public static final String USERPASSREGEXNOCHINSE = "^[A-Za-z0-9]$";
	public static final String NUMBER_REGEX = "[0-9]+";
	public static final String USE_PASS_REGEXNO = "^[a-zA-Z\\d]+$";
	//身份证号码 正则表达式
	public static final String CARD_ID_NO_IS_VALID = "CARD_ID_NO_IS_VALID";
	// 保存当前用户登录信息的文件名
}
