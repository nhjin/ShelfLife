package com.example.demo.util;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils; //기존에 사용하던 apache에서 스프링 프레임워크 StringUtils로 교체

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * <pre>
 * Class Name  : MapperUtils.java
 * Description : 
 * Modification Information  
 * 
 *     수정일　　　 　　         수정자　　　       수정내용
 *    ---------------   -----------  ---------------------------
 *    2016. 5. 30.        beany         최초생성
 * </pre>
 *
 * @author beany
 * @since 2016. 5. 30.
 * @version 1.0
 * 
 * Copyright (C) 2016 by Beany All right reserved.
 */
public class MapperUtils {

    public static String getLangCd() {
        String langCd = LocaleContextHolder.getLocale().getLanguage();

        if (StringUtils.isEmpty(langCd)) {
            langCd = Locale.getDefault().getLanguage();
        }

        return LocaleContextHolder.getLocale().getLanguage();
    }

    public static boolean isEmpty(Object param) {
        if (param == null) {
            return true;
        } else {

			// Character 은 'Y' 와 같이 파라미터가 한자리일 경우 인식을 위해처리
			if (param instanceof String || param instanceof Character) {
				return StringUtils.isEmpty(param.toString());
			} else if (param instanceof Map) {
				Map<?, ?> paramMap = (Map<?, ?>) param;

				// 'Y' 단일 문자일 경우 java.util.LinkedHashMap 으로 넘어오는 경우가 발생되며 paramMap 의 키가 null 이 아닌 "" 빈 문자열로 인식됨.
				if (paramMap.size() == 1 && (paramMap.containsKey(null) || paramMap.containsKey(""))) {
					return true;
				} else {
					return false;
				}
			} else if (param instanceof List) {
				return ((List<?>) param).isEmpty();
			} else if (param instanceof Object[]) {
				// 멀티 선택박스 입력시 사용
				if (Array.getLength(param) == 1 && Arrays.toString((String[]) param).equals("[]")) return true;
				else return Array.getLength(param) == 0;
			} else {
				return true;
			}
		}
    }

    public static boolean isNotEmpty(Object param) {
        return !isEmpty(param);
    }

    public static boolean isEmpty2(Object obj){
        if( obj instanceof String ) {
        	return obj==null || "".equals(obj.toString().trim());
        }
        else if( obj instanceof List ) {      	
        	return obj==null || ((List)obj).isEmpty();
        }
        else if( obj instanceof Map ) {       	
        	return obj==null || ((Map)obj).isEmpty();
        }
        else if( obj instanceof Object[] ) { 
			if (Array.getLength(obj)==1 && Arrays.toString((String[]) obj).equals("[]")) {
				return true;
			}else {			
				return obj==null || Array.getLength(obj)==0;
			}
        }
        else {       	
        	return obj==null;
        }
    }
     
    public static boolean isNotEmpty2(String s){
        return !isEmpty2(s);
    }
/*
    public static void main(String[] args) throws Exception {
        System.out.println("----- " + MapperUtils.isNotEmpty("Y"));

        Map<Object, Object> paramMap = new LinkedHashMap<>();
        paramMap.put(null, null);

        System.out.println("size : " + paramMap.size());

        if (paramMap.size() == 1 && paramMap.containsKey(null)) {
            System.out.println("####### NULL");

        }

        System.out.println(MapperUtils.isEmpty(paramMap));
    }
*/
    public static int length(String s){
        return s.length();
    }
    
    /**
     * 문자열이 "" or "          " or NULL이 아니면 TRUE
     * @param value
     * @return
     * @throws Exception
     */
//    public static boolean isNotBlank(String value) throws Exception {
//      return StringUtils.isNotBlank(value);
//    }
    /**
     * 문자열이 "" or "          " or NULL이면 TRUE
     * @param value
     * @return
     * @throws Exception
     */
//    public static boolean isBlank(String value) throws Exception {
//      return StringUtils.isBlank(value);
//    }
    /**
     * 비교 문자열의 값이 서로 같다면 true
     * @param src
     * @param dst
     * @return
     * @throws Exception
     */
//    public static boolean isEqual(String src, String dst) throws Exception {
//      return StringUtils.equals(src, dst);
//    }
    /**
     * 비교 문자열의 값이 서로 다르다면 true
     * @param src
     * @param dst
     * @return
     * @throws Exception
     */
//    public static boolean isNotEqual(String src, String dst) throws Exception {
//      return !isEqual(src, dst);
//    }
    
    /**
     * 문자열안에 정의된 문자목록이 포함되어있는지 여부
     * @param source ["111","222","333","444","555"]
     * @param target "111","222","333","444","555"
     * @param splitString ","
     * @return
     * @throws Exception
     */
    public static boolean isContains(String[] source, String target,String splitString) throws Exception {
		/*
    	System.out.println("source");
    	System.out.println(source);
    	System.out.println("target");
    	System.out.println(target);
    	System.out.println("splitString");
    	System.out.println(splitString);
    	*/
    	if(source!=null) {
			target = target.replaceAll(" ", "");
			String[] targetArr = target.split(splitString);
			for (String outter : source) {
				for (String inner : targetArr) {
					if (outter.equals(inner)) {
						//System.out.println("TRUE!!");
						return true;
					}

				}
			}
			//System.out.println("FALSE!!");
		}
    	return false;
    }
}
