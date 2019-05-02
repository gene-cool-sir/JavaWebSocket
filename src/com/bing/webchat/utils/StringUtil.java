package com.bing.webchat.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

/**
 * 
 * @Description: 字符串操作工具类
 */
public class StringUtil {
	
	 public static String getGuid(){
	        return UUID.randomUUID().toString().trim().replaceAll("-", "").toLowerCase();
	    }
	
	/**
	 * 
	 * @Description: 去除字符串中间的空格
	 * @author zhangxuejing
	 * @param str
	 * @return 
	 *
	 * @date:2016年11月18日 下午4:00:08
	 */
	public static String trimInMiddle(String str){
		StringTokenizer st = new StringTokenizer(str, " ");  
        StringBuffer sb = new StringBuffer();  
        while (st.hasMoreTokens()) {  
            sb.append(st.nextToken());  
        } 
        return sb.toString();
	}
	/**
	 * @date 2017年4月17日 下午6:22:31
	 *
	 * @author zhaol
	 *
	 * @Description 将数字转成中文
	 */
	public static String ToCH(int intInput) {  
        String si = String.valueOf(intInput);  
        String sd = "";  
        if (si.length() == 1){ // 個    
            sd += GetCH(intInput);  
            return sd;  
        } else if (si.length() == 2){//十    
            if (si.substring(0, 1).equals("1"))  
                sd += "十";  
            else  
                sd += (GetCH(intInput / 10) + "十");  
            sd += ToCH(intInput % 10);  
        } else if (si.length() == 3){//百  
            sd += (GetCH(intInput / 100) + "百");  
            if (String.valueOf(intInput % 100).length() < 2)  
                sd += "零";  
            sd += ToCH(intInput % 100);  
        }
        return sd;  
    }  
    private static String GetCH(int input) {  
        String sd = "";  
        switch (input) {
        case 0:
        	sd = "零";
        	break;
        case 1:  
            sd = "一";  
            break;  
        case 2:  
            sd = "二";  
            break;  
        case 3:  
            sd = "三";  
            break;  
        case 4:  
            sd = "四";  
            break;  
        case 5:  
            sd = "五";  
            break;  
        case 6:  
            sd = "六";  
            break;  
        case 7:  
            sd = "七";  
            break;  
        case 8:  
            sd = "八";  
            break;  
        case 9:  
            sd = "九";  
            break;  
        default:  
            break;  
        }  
        return sd;  
    }
    
    public static String formatStr(Object obj, String defaultStr){
    	try {
			String str = String.valueOf(obj);
			if(MatchUtil.isEmpty(str)){
				return defaultStr;
			} else {
				return str;
			}
		} catch (Exception e) {
			return defaultStr;
		}
    }
    /**
     * 从传入的字符串中，格式如下：
     * 4年2个月  4年整   4年2月 
     * 计算出对应的是几个月
     */
    public static Map<String, Integer> getYearAndMonth(String str){
    	Map<String, Integer> resoultMap = new HashMap<String, Integer>();
    	int year = 0;
    	int month = 0;
    	if(!MatchUtil.isEmpty(str)){
    		if(str.contains("年整")){
        		try{
        			year =  Integer.parseInt(str.substring(0,str.indexOf("年整")));
        		}catch(Exception e){
        		}
    			
    		}else {
    			if(str.contains("年")){
    	    		try{
    	    			year =  Integer.parseInt(str.substring(0,str.indexOf("年")));
    	    		}catch(Exception e){
    	    		}
    			}
    			if(str.contains("个月")){
    	    		try{
    	    			month = Integer.parseInt(str.substring(str.indexOf("年")+1,str.indexOf("个月")));
    	    		}catch(Exception e){
    	    		}
    			}else if(str.contains("月")){
    	    		try{
    	    			month = Integer.parseInt(str.substring(str.indexOf("年")+1,str.indexOf("月")));
    	    		}catch(Exception e){
    	    		}
    			}
    		}
    	}
    	resoultMap.put("year", year);
    	resoultMap.put("month", month);
    	return resoultMap;
    }
    /**
     * 判断字符串最后一位是否是标点
     */
    public static StringBuffer catPunctuation(StringBuffer docStringBuffer){
    	String docString=docStringBuffer.toString();
    	docString=catPunctuation(docString);
    	return new StringBuffer(docString);
    }
    
    public static String catPunctuation(String docString){
    	docString=docString.trim();
    	String reg = ".*[\\.。,，;；、]$";
    	if(!MatchUtil.isEmpty(docString)){
    		if (docString.matches(reg)) {
        		docString = docString.substring(0, docString.length() - 1);
        		return catPunctuation(docString);
        	}
    	}
    	return docString;
    }
    
    
    public static StringBuffer cutStringEnd(String endString,StringBuffer docStringBuffer){
    	String docString=docStringBuffer.toString();
    	docString=docString.trim();
    	if(!MatchUtil.isEmpty(docString)){
    		String regex=".*"+endString;
        	if(docString.matches(regex)){
        		docString = docString.substring(0, docString.length() - endString.length());
        		return cutStringEnd(endString,new StringBuffer(docString));
        	}
    	}
    	return new StringBuffer(docString);
    }
    
    public static String cutEnd(String endString,String docStringBuffer){
    	String docString=docStringBuffer.toString();
    	docString=docString.trim();
    	if(!MatchUtil.isEmpty(endString)){
        	String regex=".*"+endString+"$";
        	if(docString.matches(regex)){
        		docString = docString.substring(0, docString.length() - endString.length());
        		return cutEnd(endString,docString);
        	}
    	}
    	return docString;
    }
    
    public static String cutNumberEnd(String docStringBuffer){
    	String docString=docStringBuffer.toString();
    	if(!MatchUtil.isEmpty(docString)){
    		docString=docString.trim();
        	String regex=".*[0-9]$";
        	if(docString.matches(regex)){
        		docString = docString.substring(0, docString.length()-1);
        		return cutNumberEnd(docString);
        	}
    	}
    	return docString;
    }
    
    public static StringBuffer cutStringBegain(String beginString,StringBuffer docStringBuffer){
    	String docString=docStringBuffer.toString();
    	docString=cutStringBegain(beginString,docString);
    	return new StringBuffer(docString);
    }
    
    public static String cutStringBegain(String beginString,String docString){
    	docString=docString.trim();
    	if(!MatchUtil.isEmpty(docString)){
    		String regex="^"+beginString+".*";
        	if(docString.matches(regex)||docString.indexOf(beginString)==0){
        		docString = docString.substring(beginString.length());
        		return cutStringBegain(beginString,docString);
        	}
    	}
    	return docString;
    }
    
    /**
     * 删除开始结束的中文空格
     * @param beginString
     */
    public static String cutStringTrimBlank(String line){
    	line=cutStringBegain("　",line);
    	line=cutStringEnd("　",new StringBuffer(line)).toString();
    	return line;
    }
    
    public static int specificCharacterPosition(String str,String character,int num){
    	int beginIndex=0;
    	try{
			for(int i=0;i<num;i++){
	    		beginIndex=str.indexOf(character,beginIndex+1);
	    	}
    	}catch(Exception e){
    		return 0;
    	}
    	return beginIndex;
    }
    
    /**
     * 替换中/英空格
     */
    public static String replaceAllBlankSpace(String content){
    	if (!MatchUtil.isEmpty(content)) {
    		content = content.replaceAll(" ", "");
    		content = content.replaceAll("　", "");
    		return content;
    	}
    	return content;
    }
    /**
	 * 去除字符串多余空格
	 * @Description: TODO
	 * @param shishiStr 要操作的字符串
	 * @return 
	 * @date:2017年10月18日 上午10:45:16
	 */
	public static String removeTrim(String shishiStr) {
		return shishiStr.replaceAll("　", "").replaceAll(" ", "").replaceAll("\n", "").trim();
	}
	
	/**
	 * 把源字符串中的某一字符或字符串全部 按照正则 换成指定的字符或字符串
	 * @param content 需要替换的原内容
	 * @param regex 正则表达式字符串
	 * @param replacement 符合正则表达式的内容替换为该值
	 * @return 替换后的字符串
	 */
	public static String replaceAllByRegx(String content,String regex,String replacement) {
		content = content.replaceAll(regex, replacement);
		return content;
	}
}
