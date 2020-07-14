package util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassUtils {
	public static void invoke(String classNm, String methodName, Class[] paramCls, Object[] paramValue) 
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		//param
	    Class[] methodParamClass   = paramCls;//new Class[] {String.class, String.class};  //인자 클래스
	    Object[] methodParamObject = paramValue; //new Object[] {"첫번째 arg", "두번째 arg"};       //인자 값
	    //class init
	    Class bizClass = Class.forName(classNm);
	    Object obj = bizClass.newInstance();
	    //invocation
	    Method method = bizClass.getMethod(methodName, methodParamClass);
	    method.invoke(obj, methodParamObject);
	}
	
	public static void invoke(String filePath,String classNm,String methodName, Object[] paramValues, Class[] parameterTypes ) throws Exception{
		File file = new File(filePath);
		URLClassLoader newLoader = new URLClassLoader(new URL[] {file.toURL()});
		
		Class targetCls = newLoader.loadClass(classNm);
		Object targetInx = targetCls.newInstance();
		
		Method method = targetCls.getMethod(methodName, parameterTypes);
		method.invoke(targetInx, paramValues);
		
		
	}
}
