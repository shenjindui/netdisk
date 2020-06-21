package com.micro.common;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanUtils {
	/**
	 * 集合转Map
	 * @author zwy
	 * 2018年10月14日
	 * @param obj
	 * @return
	 */
	public static List<Map<String, Object>> toList(List lists) {
		List<Map<String, Object>> beans=new ArrayList<Map<String, Object>>();
		if(lists!=null&&lists.size()!=0){
			for(int i=0;i<lists.size();i++){
				Map<String, Object> map = new HashMap<String,Object>();
				Object obj=lists.get(i);
				
				Class clazz = obj.getClass();
				Field[] fields = clazz.getDeclaredFields();
				try {
					for (Field field : fields) {
						field.setAccessible(true);
						map.put(field.getName(), field.get(obj));
					}
					
					beans.add(map);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return beans;
	}
	
	/*public static void main(String[] args) {
		TestBean t=new TestBean();
		t.setName("郑伟业");
		t.setStarttime(new Date());
		
		Map<String, Object> m=bean2Map(t);
		System.out.println(m);
		TestBean tb=(TestBean) map2Bean(m, TestBean.class);
		System.out.println(tb);
	}*/
	
	/**
	 * 实体转Map
	 * @author 郑伟业
	 * 2018年10月14日
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> bean2Map(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
        	SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Field field : fields) {
                field.setAccessible(true);
                if(field.getType().getSimpleName().equals("Date")){                	
                	map.put(field.getName(), dateFormat.format(field.get(obj)));
                }else{                	
                	map.put(field.getName(), field.get(obj));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
	
	/**
	 * map转对象
	 * @author 郑伟业
	 * 2018年10月14日
	 * @param map
	 * @param clazz
	 * @return
	 */
	public static Object map2Bean(Map<String, Object> map, Class<?> clazz) {
        if (map == null) {
            return null;
        }
        Object obj = null;
        try {
            obj = clazz.newInstance();

            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                if(field.getType().getSimpleName().equals("Date")){                
                	if(map.get(field.getName())!=null){                		
                		field.set(obj, dateFormat.parse(map.get(field.getName()).toString()));
                	}
                }else{                	
                	field.set(obj, map.get(field.getName()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return obj;
    }
}
