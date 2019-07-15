/*
 * *****************************************************************************************************
 * 系统名称  ： Bizenit Smart Identity series products           
 * 文件名    ： StringUtils.java                                                                            
 * *****************************************************************************************************
 * 本软件由上海硅孚信息科技有限公司（以下简称为硅孚）独立开发完成，已经登记著作权许可，受著作权法保护  
 * 任何个人或者组织传播或使用该软件，都必须得到硅孚授权许可。                                           
 * 任何个人或者组织非法传播和或使用软该件，硅孚都将依法追究其侵权责任。                                
 * (C) Copyright Shanghai Bizenit Corporation 2013 All Rights Reserved.                              
 * *****************************************************************************************************
 */
package org.spring.springboot.utils;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 用于字符串的各种方法
 * 
 * @类名:StringUtils
 * @作者:林铤
 * @时间:2012-12-07
 */
public class StringUtil {

	private StringUtil() {
	}

	/** The counter. */
	private static int counter = 65;
	
	private static final String NOT_FOUND = "NOT_FOUND";
	private static final String IDMLANG = "idmlang";
	private static final String BR = "<br/>";
	private static final String LINE = "-----------------------<br/>";
	private static final String UM_SERVICE_SUPPORT_UNIT = "UM服务支持小组<br/>";
	private static final String THIS_IS_AN_AUTO_GENERATED_EMAIL_DO_NOT_REPLY = "(这是一封自动产生的email，请勿回复。) ";

	/**
	 *  例：1->A 88->CJ
	 * @param columnIndex
	 * @return
	 */
	public static String getExcelColIndexToStr(int columnIndex) {
		if (columnIndex <= 0) {
			return null;
		}
		String columnStr = "";
		columnIndex--;
		do {
			if (columnStr.length() > 0) {
				columnIndex--;
			}
			columnStr = ((char) (columnIndex % 26 + (int) 'A')) + columnStr;
			columnIndex = (int) ((columnIndex - columnIndex % 26) / 26);
		} while (columnIndex > 0);
		return columnStr;
	}
	
	/**
	 * 检查字符串是否为空值
	 * 
	 * @param str
	 *            需要检查的字符串
	 * @return 是否为空值（是返回true，不是返回false）
	 */
	public static boolean checkNull(Object str) {
		if (str == null) {
			return true;
		} else if (str instanceof String) {
			String strTemp = ((String) str).trim();
			return "".equals(strTemp) || strTemp.isEmpty() || "null".equals(strTemp)
					|| NOT_FOUND.equals(strTemp);
		} else if(str instanceof StringBuilder){
			String strTemp = str.toString().trim();
			return "".equals(strTemp) || strTemp.isEmpty() || "null".equals(strTemp)
					|| NOT_FOUND.equals(strTemp);
		}else {
			return str == null;
		}
	}



	/**
	 *  验证数据是否为空，支持String 集合 Map 数组为空的验证。。。
	 *  数组 集合 如果里面的没数据同样返回true
	 * 
	 */
	public static boolean isEmpty(Object obj){
		boolean empty=false;
		if(obj==null){
			empty=true;
		}else if(obj instanceof String || obj instanceof StringBuilder){
			//特殊情况
			if (NOT_FOUND.equals(obj.toString()) || "null".equalsIgnoreCase(obj.toString()) || "undefined".equalsIgnoreCase(obj.toString())) {
				empty=true;
			}else {
				empty="".equals(String.valueOf(obj));
			}
		}//Collection集合
		else if(obj instanceof Collection){
			Collection conn=(Collection)obj;
			empty=conn.isEmpty();
		}//Map集合
		else if(obj instanceof Map){
			Map map=(Map)obj;
			empty=map.isEmpty();
		}//数组
		else if(obj.getClass().isArray()){
			empty=Array.getLength(obj)<1;
		}

		return empty;
	}

	public static boolean isNotEmpty(Object obj){
		return !isEmpty(obj);
	}





	/**
	 * 生成密码（待指定生成规则）
	 *
	 * @return
	 */
	public static String passwordGeneration(int length) {
		return RandomStringUtils.random(length, false, true);
	}

	/**
	 * 生成部门编号
	 */
	public static synchronized String departmentNumberGeneration() {
		Date date = new Date();
		char ch = (char) counter;
		String id = "BIZENIT" + date.getTime() + ch;
		if (++counter == 91) {
			counter = 65;
		}
		return id;
	}


	/**
	 * Checks if is chinese.
	 * 
	 * @param c
	 *            the c
	 * @return true, if is chinese
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS ? true : false;
	}


	/**
	 * 生成管理员重置用户密码的邮件内容
	 * 
	 * @param operator
	 *            操作人
	 * @param usercn
	 *            被操作人姓名
	 * @param time
	 *            操作时间
	 * @param account
	 *            操作账号
	 * @param password
	 *            密码
	 * @return 邮件内容
	 */
	public static String buildResetUserPasswordMailText(String operator,
			String usercn, String time, String account, String password) {
		StringBuilder builder = new StringBuilder();
		builder.append(usercn + ",您好。<br/>");
		builder.append("您的账号" + account + "的密码于" + time + "已经被管理员：" + operator
				+ "重置，重置的密码为" + password + "，请知晓，<br/>");
		builder.append("为了保护您的账号安全，如果你未发起过任何的重置申请，请立即联系管理员。<br/><br/>");
		builder.append(LINE);
		builder.append(UM_SERVICE_SUPPORT_UNIT);
		builder.append(THIS_IS_AN_AUTO_GENERATED_EMAIL_DO_NOT_REPLY);
		return builder.toString();
	}

	/**
	 * 生成自助修改密码的邮件内容
	 * 
	 * @param usercn
	 *            用户姓名
	 * @param time
	 *            操作时间
	 * @param account
	 *            账号
	 * @return 邮件内容
	 */
	public static String buildSelfResetPasswordMailText(String usercn,
			String time, String account) {
		StringBuilder builder = new StringBuilder();
		builder.append("尊敬的用户：<br/><br/>");
		builder.append(usercn + ",您好！<br/><br/>");
		builder.append("您于" + time + "在智能身份自助服务平台上成功修改账号" + account
				+ "的密码！请确认，谢谢！<br/>");
		builder.append("---------------------------------------------------------------------------------------------------------------------<br/>");
		builder.append("有任何问题请联系智能身份自助服务平台管理员！<br/><br/>");
		builder.append("感谢您使用智能身份自助服务平台！<br/><br/>");
		builder.append("硅孚智能身份自助服务<br/><br/>");
		builder.append(time + BR);
		builder.append(THIS_IS_AN_AUTO_GENERATED_EMAIL_DO_NOT_REPLY);
		return builder.toString();
	}

	/**
	 * 生成邮箱找回密码的邮件内容
	 * 
	 * @param url
	 *            链接
	 * @param usercn
	 *            用户姓名
	 * @param time
	 *            操作时间
	 * @param hour
	 *            有效时间
	 * @param account
	 *            账号
	 * @param retrieveUser
	 *            加密用户账号
	 * @param retrieveSeckey
	 *            加密密匙
	 * @return 邮件内容
	 */
	public static String buildRetrievePasswordMailText(String url,
			String usercn, String time, String hour, String account,
			String retrieveUser, String retrieveSeckey) {
		StringBuilder builder = new StringBuilder();
		builder.append("尊敬的用户：<br/><br/>");
		builder.append(usercn + ",您好！<br/><br/>");
		builder.append("您在" + time + "提交找回密码请求，请点击下面的链接修改账号" + account
				+ "的密码:<br/><br/>");
		builder.append("<a href='" + url + "/retrieve/modify.do?RU="
				+ retrieveUser + "&RK=" + retrieveSeckey + "'>" + url
				+ "/retrieve/modify.do?RU=" + retrieveUser + "&RK="
				+ retrieveSeckey + "</a><br/><br/>");
		builder.append("(如果您无法点击这个链接，请将此链接复制到浏览器地址栏后访问) <br/>");
		builder.append("为了保证您帐号的安全性，该链接有效期为" + hour
				+ "小时，并且点击该链接修改密码成功之后将会失效!<br>");
		builder.append("设置并牢记密码保护问题将更好地保障您的帐号安全。<br/>");
		builder.append("如果您误收到此电子邮件，则可能是其他用户在尝试帐号设置时的误操作，如果您并未发起该请求，则无需再进行任何操作，并可以放心地忽略此电子邮件。<br/>");
		builder.append("若您担心帐号安全，建议您立即登录，进入“安全中心”-“修改密码”，修改密码。<br/>");
		builder.append("感谢您使用智能身份自助服务平台！<br/><br/><br/><br/>");
		builder.append(time + BR);
		builder.append(THIS_IS_AN_AUTO_GENERATED_EMAIL_DO_NOT_REPLY);
		return builder.toString();
	}

	/**
	 * 生成通知将近过期的外部用户的邮件内容
	 * 
	 * @param usercn
	 *            用户姓名
	 * @param time
	 *            过期时间
	 * @param account
	 *            账号
	 * @return 邮件内容
	 */
	public static String buildNearExpiredMailText(String usercn, String time,
			String account) {
		StringBuilder builder = new StringBuilder();
		builder.append(usercn + "，您好: <br/>");
		builder.append("您的账号" + account + "即将在" + time
				+ "过期。如果您仍需使用该账号，请联系UM管理员延长账号过期时间。<br/><br/><br/>");
		builder.append(LINE);
		builder.append(UM_SERVICE_SUPPORT_UNIT);
		builder.append(THIS_IS_AN_AUTO_GENERATED_EMAIL_DO_NOT_REPLY);
		return builder.toString();
	}

	/**
	 * 主账号生成
	 * 
	 * @param empSuffix
	 *            后缀
	 * @param deptNum
	 *            主账号所在部门
	 * @param sn
	 *            对应编号
	 * @return
	 */
	public static String uidGennerator(String empSuffix, String deptNum,
			String sn, Integer limit) {
		String prefix = "1";
		StringBuilder zeros = new StringBuilder();
		for (int i = 0; i < limit - sn.length(); i++) {
			zeros.append("0");
		}
		return prefix + deptNum + zeros.toString() + sn + empSuffix;
	}


	public static String buildResetUserPasswordSmsText(String operator,
			String usercn, String time, String account, String password) {
		StringBuilder builder = new StringBuilder();
		builder.append(usercn + "，您好: 您的账号" + account + "的密码于" + time + "已被管理员"
				+ operator + "重置，重置的密码为" + password + "请知晓。");
		return builder.toString();
	}

	public static String buildSelfResetPasswordSmsText(String usercn,
			String time, String account, String password) {
		StringBuilder builder = new StringBuilder();
		builder.append(usercn + "，您好:您于" + time + "在智能身份自助服务平台上成功修改账号"
				+ account + "的密码，密码为" + password + "。请确认，谢谢！");
		return builder.toString();
	}

	public static String buildNewExternalUserSmsText(String operator,
			String usercn, String username, String password, String time) {
		StringBuilder builder = new StringBuilder();
		builder.append(usercn + ",您好。<br/>");
		builder.append("您的账号" + username + "已于" + time + "由管理员：" + operator
				+ "创建，密码为" + password + BR);
		builder.append("您现在可以使用该密码登录智能身份自助服务平台进行修改基本信息，查看权限，绑定手机等操作。<br/>");
		builder.append(LINE);
		builder.append(UM_SERVICE_SUPPORT_UNIT);
		builder.append(THIS_IS_AN_AUTO_GENERATED_EMAIL_DO_NOT_REPLY);
		return builder.toString();
	}

	/**
	 * 根据指定字段排序
	 * 
	 * @param value1
	 *            字段1
	 * @param value2
	 *            字段2
	 * @return 1表示大于，0表示等于，-1表示小于
	 */
	public static int sort(String value1, String value2) {
		if (StringUtils.isNumeric(value1) && StringUtils.isNumeric(value2)){
			Long order1 = Long.MIN_VALUE;
			Long order2 = Long.MIN_VALUE;
			if (!checkNull(value1)) {
				order1 = Long.valueOf(value1);
			}
			if (!checkNull(value2)) {
				order2 = Long.valueOf(value2);
			}
			return order1.compareTo(order2);
		}
		return value1.compareTo(value2);
	}

	public static String addZeroForNum(String str, int strLength) {
		int strLen = str.length();
		if (strLen < strLength) {
			while (strLen < strLength) {
				StringBuilder sb = new StringBuilder();
				sb.append("0").append(str);//左补0
				// sb.append(str).append("0");//右补0
				str = sb.toString();
				strLen = str.length();
			}
		}
		return str;
	}


	/**
	 * 生成指定长度短信验证码
	 * 
	 * @param length
	 * @return
	 */
	public static String generateSmsCode(int length) {
		return RandomStringUtils.random(length, false, true);
	}


	/**
	 * 字符串编码格式转换
	 * @param srcBytes
	 * @param srcCharset
	 * @param targetCharset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String changeCharset(byte[] srcBytes, String srcCharset, String targetCharset) throws UnsupportedEncodingException {
		byte[] targetBytes = new String(srcBytes, srcCharset).getBytes(targetCharset);
		return new String(targetBytes);
	}


	/**
	 * 判断值是否在数组里
	 * @param arr
	 * @param targetValue
	 * @return
	 */
	public static boolean isInArry(String[] arr, String targetValue) {
		return Arrays.asList(arr).contains(targetValue);
	}


	/**
	 * 获取某个时间的上一周期对应时间
	 * @param cycle
	 * @return
	 */
	public static Date getLastCycleDate(Date date, int cycle) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		//天
		if (cycle==0) {
			cal.add(Calendar.DATE, -1);
		} 
		//周
		else if (cycle==1) {
			cal.add(Calendar.WEEK_OF_MONTH, -1);
		} 
		//月
		else if (cycle==2) {
			cal.add(Calendar.MONTH, -1);
		}
		return cal.getTime();
	}


	/**
	 * 获取某个时间的下一周期对应时间
	 * @param cycle
	 * @return
	 */
	public static Date getNextCycleDate(Date date, int cycle) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		//天
		if (cycle==0) {
			cal.add(Calendar.DATE, +1);
		} 
		//周
		else if (cycle==1) {
			cal.add(Calendar.WEEK_OF_MONTH, +1);
		} 
		//月
		else if (cycle==2) {
			cal.add(Calendar.MONTH, +1);
		}
		return cal.getTime();
	}


	/**
	 * 把指定日期转为当月第一天
	 * @return
	 */
	public static Date dateToFirstDay(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);

		return cal.getTime();
	}

	/**
	 * 把指定日期转为当月最后一天
	 * @return
	 */
	public static Date dateToLastDay(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		//当前月加1，就到了次月，然后把次月重置到1号，然后减一天，就到了当前月最后一天
		cal.add(Calendar.MONTH, +1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);

		return cal.getTime();
	}


	/**
	 * 把开始日期转为当前周的星期一
	 * @param startDate
	 * @return
	 */
	public static Date dateTomonday(Date startDate){
		//把开始日期转为当前周的星期一
		Calendar cal=Calendar.getInstance();
		cal.setTime(startDate);
		cal.setFirstDayOfWeek(Calendar.MONDAY); //以周1为首日
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

		return cal.getTime();
	}


	/**
	 * 判断数组是否存在目标值
	 * @param arr
	 * @param targetValue
	 * @return
	 */
	public static boolean arryHasVlu(String[] arr, String targetValue) {
		return Arrays.asList(arr).contains(targetValue);
	}

	/**
	 * 将带有逗号的字符串  转换为 long数组
	 * @param
	 * @return
	 */

	public static List<Long>  split(String numbers) {
		String[] nums=numbers.split(","); 
		List<Long> ids=new ArrayList<>();
		for(String s:nums) {
			Long l=  Long.parseLong(s);
			ids.add(l);
		}

		return ids ;
	} 


	/**
	 * 遍历request中所有参数
	 * @param request
	 * @return Map<String,String>
	 */
	public static Map<String, String> getRequestParameMap(HttpServletRequest request){
		Map<String, String> resultMap = new HashMap<>();
		Enumeration<String> paraNames=request.getParameterNames();
		for(Enumeration<String> e=paraNames;e.hasMoreElements();){
			String thisName=e.nextElement();
			String thisValue=request.getParameter(thisName);
			resultMap.put(thisName, thisValue);
		}

		return resultMap;
	}


	/**
	 * 将字符串数组转化为list
	 * @param numbers
	 * @return
	 */
	public static List<String> aslist(String[] numbers) {
		List<String> list = new ArrayList<>();
		for (String number : numbers) {
			list.add(number);
		}
		return list;
	}


	/**
	 * 获取cookie
	 * @param request
	 * @param key
	 * @return Cookie
	 */
	public static String getCookie(HttpServletRequest request, String key){

		if (isNotEmpty(key) && isNotEmpty(request)) {

			Cookie[] cookies = request.getCookies();//这样便可以获取一个cookie数组
			if(StringUtil.isNotEmpty(cookies)){
				for(Cookie cookie : cookies){
					if (cookie.getName().equals(key)) {
						return cookie.getValue();
					}
				}
			}
		}

		return null;
	}




	/**
	 * 获取session
	 * @param request
	 * @param key
	 * @return
	 */
	public static Object getSession(HttpServletRequest request, String key){
		HttpSession session = request.getSession();

		return session.getAttribute(key);
	}

	/**
	 * 设置session
	 * @param request
	 * @param key
	 * @param value
	 */
	public static void setSession(HttpServletRequest request, String key, Object value){
		HttpSession session = request.getSession();
		session.setAttribute(key, value);
	}

	/**
	 * 判断字符串是否是数字
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str){
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}

	/**
	 * 随机获取字母与数字组合
	 * @param length 字符串长度
	 * @param letter 字母
	 * @param number 数字
	 * @param start 数字开始
	 * @param end 数字结束
	 * @return
	 */
	public static String getRandomCharAndNum(int length,boolean letter,boolean number,int start,int end){
		StringBuilder sbf = new StringBuilder();
		Random random = new Random();  
		for(int i=0;i<length;i++){
			boolean flag = random.nextBoolean();
			boolean mark = true;
			if(letter && flag){
				boolean b = random.nextBoolean();
				if(b){
					//大写字母A-Z
					sbf.append((char)(65 + random.nextInt(26)));
				} else {
					//小写字母a-z
					sbf.append((char)(97 + random.nextInt(26)));
				}	
				mark = false;
			}
			if(number && !flag && mark){
				sbf.append((random.nextInt((end-start+1))+start));
			}
		}
		return sbf.toString();
	}
	
	/**
	 * 获取32位字母与数字组合
	 * @return
	 */
	public static String getRandomCharAndNum(){
		return getRandomCharAndNum(32, true, true, 2, 7);
	}
	
	/**
	 * 屏蔽Json字符串中的敏感字符
	 * @param jsonStr	原Json字符串
	 * @return	屏蔽敏感字符后的Json字符串
	 */
	public static String maskSensitiveInfoForJsonStr(String jsonStr, String... sensWords) {
		String outStr = jsonStr;
		if (isNotEmpty(jsonStr) && isNotEmpty(sensWords)) {
			for (String sensWord : sensWords) {
				int fromIdx = 0;
				int keyIdx = -1;
				int valStart;
				int valEnd;
				while ((keyIdx = outStr.indexOf(sensWord, fromIdx)) >= 0) {
					valStart = outStr.indexOf(":\"", keyIdx);
					if (valStart > 0) {
						valEnd = outStr.indexOf('\"', valStart + 2);
						if (valEnd > valStart) {
							outStr = outStr.substring(0, valStart + 2) + "******" + outStr.substring(valEnd, outStr.length());
						}
						fromIdx = valStart + 2;
					}
				}
			}
		}
		return outStr;
	}
}
