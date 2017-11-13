package com.newcore.orbpsutils.bussiness;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.util.IOUtils;
import com.halo.core.exception.BusinessException;

/**
 * @author huanglong
 * @date 2017年2月27日
 * @content  对象与XML互转
 */
public class XMLUtil {
	private final static Logger logger = LoggerFactory.getLogger(XMLUtil.class);
	/** 
	 * 将对象直接转换成String类型的 XML输出 
	 *  
	 * @param obj 
	 * @return 
	 */ 
	public static String convertToXml(Object obj) {
		
		Document doucment = DocumentHelper.createDocument();
		try {
			Annotation[] aArray;
			String classRoot="";
			aArray = Class.forName(obj.getClass().getName()).getAnnotations();
			for(Annotation annotation : aArray)
			{
				if(annotation.annotationType().equals(XmlRootElement.class)){
					classRoot = ((XmlRootElement)annotation).name();
					break;
				}
			}
			if(StringUtils.isEmpty(classRoot)){
				throw new BusinessException("0002",obj.getClass().getName() + ":类无@XmlRootElement注解或无name属性值!"); 
			}  		
			Element classRootElement = doucment.addElement(classRoot);
			fieldToXML(obj,classRootElement);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BusinessException("实体类转换XML失败");
		}
		return doucment.getRootElement().asXML(); 
	}

	/** 
	 * 将对象根据路径转换成xml文件 
	 *  
	 * @param obj 
	 * @param path 
	 * @return 
	 */  
	public static void convertToXml(Object obj, String path) {  
		FileWriter fileWriter = null;
		try {  
			// 利用jdk中自带的转换类实现  
			JAXBContext context = JAXBContext.newInstance(obj.getClass());  

			Marshaller marshaller = context.createMarshaller();  
			// 格式化xml输出的格式  
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT,Boolean.TRUE);
			// 将对象转换成输出流形式的xml  
			// 创建输出流  
			fileWriter  = new FileWriter(path);
			marshaller.marshal(obj, fileWriter);  
		} catch (JAXBException e) {  
			logger.error(e.getMessage(), e);
			throw new BusinessException("0002","对象转XML文件失败!");
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new BusinessException("0002","创建文件流失败!");
		}finally {
			IOUtils.close(fileWriter);
		}
	}
	/** 
	 * 将String类型的xml转换成对象 
	 */  
	public static Object convertXmlStrToObject(Class<?> clazz, String xmlStr) { 
		Object xmlObject = null;

		String root="";
		try {
			Annotation[] aArray;
			aArray = Class.forName(clazz.getName()).getAnnotations();
			for(Annotation an : aArray)
			{
				if(an.annotationType().equals(XmlRootElement.class)){
					root = ((XmlRootElement)an).name();
					break;
				}
			}
			if(StringUtils.isEmpty(root)){
				throw new BusinessException("0002",clazz.getName() + ":类无@XmlRootElement注解或无name属性值!"); 
			}
			Document doucment = DocumentHelper.parseText(xmlStr); 
			JAXBContext context = JAXBContext.newInstance(clazz);
			// 进行将Xml转成对象的核心接口  
			Unmarshaller unmarshaller = context.createUnmarshaller();
			StringReader stringReader = new StringReader(doucment.getRootElement().element("REQUEST").element(root).asXML());  
			xmlObject = unmarshaller.unmarshal(stringReader);  
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
			throw new BusinessException("0002","未发现类:"+clazz); 
		} catch (DocumentException e) {
			logger.error(e.getMessage(), e);
			throw new BusinessException("0002","XML字符串转Document失败!");
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
			throw new BusinessException("0002","XML字符串转对象失败!");
		}			
		return xmlObject;  
	}

	/** 
	 * 将file类型的xml转换成对象 
	 */  
	public static Object convertXmlFileToObject(Class<?> clazz, String xmlPath) {  

		Object xmlObject = null;
		try {
			String root="";       	
			Annotation [] aArray = Class.forName(clazz.getName()).getAnnotations();	
			for(Annotation an : aArray)
			{
				if(an.annotationType().equals(XmlRootElement.class)){
					root = ((XmlRootElement)an).name();
					break;
				}
			}
			if(StringUtils.isEmpty(root)){
				throw new BusinessException("0002",clazz.getName() + ":类无@XmlRootElement注解或无name属性值!"); 
			}
			SAXReader reader = new SAXReader();
			Document document = reader.read(new FileReader(xmlPath));
			JAXBContext context = JAXBContext.newInstance(clazz);  
			Unmarshaller unmarshaller = context.createUnmarshaller();  
			xmlObject = unmarshaller.unmarshal(new StringReader(document.getRootElement().element("RESPONSE").element(root).asXML()));
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
			throw new BusinessException("0002","未发现类:"+clazz); 
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
			throw new BusinessException("0002","未发现XML文件!");
		} catch (DocumentException e) {
			logger.error(e.getMessage(), e);
			throw new BusinessException("0002","XML字符串转Document失败!");
		} catch (JAXBException e) {
			logger.error(e.getMessage(), e);
			throw new BusinessException("0002","XML字符串转对象失败!");   
		}
		return xmlObject;  
	} 

	private static void fieldToXML(Object obj,Element root) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Field[] fields = obj.getClass().getDeclaredFields();
		for(Field field :fields){   //遍历所有属性			
			String name = field.getName();    //获取属性的名字
			if("serialVersionUID".equals(name))continue;
			String XmlName = "";
			for(Annotation annotation :field.getAnnotations()){
				if(annotation.annotationType().equals(XmlElement.class)){
					XmlName = ((XmlElement)annotation).name();
					break;
				}
			}
			if(StringUtils.isEmpty(XmlName)){
				throw new BusinessException("0002",obj.getClass().getName() + "中字段"+name+"无@XmlElement注解或无name属性值!"); 
			}
			name = name.substring(0,1).toUpperCase()+name.substring(1); //将属性的首字符大写，方便构造get，set方法
			String type = field.getGenericType().toString();//获取属性的类型
			Method methodGet = obj.getClass().getMethod("get"+name);
			if(type.contains("class com.newcore.orbps")){   //如果type是类类型，则前面包含"class "，后面跟类名             
				Object value = methodGet.invoke(obj);    //调用getter方法获取属性值
				if(null != value){
					fieldToXML(value, root.addElement(XmlName));
				}
			}else if (type.contains("java.util.List")) {
				@SuppressWarnings("unchecked")
				List<Object>  objects = (List<Object>)methodGet.invoke(obj);
				if(null == objects){
					root.addElement(XmlName);
					return;
				}
				for(Object object :objects){
					fieldToXML(object,root.addElement(XmlName));
				}
			}else {
				Object value = methodGet.invoke(obj);    //调用getter方法获取属性值
				if(null != value){
					root.addElement(XmlName).setText(String.valueOf(value));
				}else {
					root.addElement(XmlName);
				}
			} 
		}
	}




}
