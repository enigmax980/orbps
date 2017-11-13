/*
 * Copyright (c) 2016 China Life Insurance(Group) Company.
 */

package com.newcore.orbpsutils.validation;

import com.halo.core.exception.BusinessException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Guojunjie,Huanglong
 *         Created on 16-8-19
 */
public class ValidationUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidationUtils.class);

	private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();

	/**
	 * 私有构造方法
	 */
	private ValidationUtils() {
		//use static method to get instance
	}

	/**
	 * 获取验证器
	 * @return 验证器
	 */
	private static Validator getValidator() {
		return FACTORY.getValidator();
	}

	/**
	 * 验证对象
	 * @param object 带验证的对象
	 * @param <T> 对象类型
	 * @return 验证结果
	 */
	private static <T> ValidationResult validateObject(T object) {
		Set<ConstraintViolation<T>> constraintViolations = getValidator().validate(object);
		return getValidationResult(constraintViolations);
	}

	/**
	 * 验证对象
	 * @param object 带验证的对象
	 * @param groups 验证群组
	 * @param <T> 对象类型
	 * @return 验证结果
	 */
	private static <T> ValidationResult validateObject(T object, @SuppressWarnings("rawtypes") Class... groups) {
		Set<ConstraintViolation<T>> constraintViolations = getValidator().validate(object, groups);
		return getValidationResult(constraintViolations);
	}

	/**
	 * 封装验证的结果
	 * @param constraintViolations 验证的结果
	 * @param <T> 对象类型
	 * @return 封装后的结果
	 */
	private static <T> ValidationResult getValidationResult(Set<ConstraintViolation<T>> constraintViolations) {
		List<String> causes = new ArrayList<>();
		constraintViolations.forEach(violation -> causes.add(violation.getMessage()));
		ValidationResult result = new ValidationResult();
		if (causes.isEmpty()) {
			result.setNotPass(false);
		} else {
			result.setNotPass(true);
			result.setCauses(causes);
		}
		return result;
	}

	/**
	 * 根据对象中的注解验证对象
	 * @param object 待验证对象
	 * @param <T> 对象类型
	 * @throws BusinessException 如果有问题抛出业务异常
	 */
	public static <T> void validate(T object){
		notNull(object, "被验证对象");
//		LOGGER.info("验证对象：{}", JSONUtils.toJSON(object));

		List<ValidationResult> validationResults = new ArrayList<>();		
		validateiIterine(object,validationResults);
		validationResults.removeAll(Collections.singleton(null));
		StringBuilder strValidation  = new StringBuilder();
		int flag = 0;
		for(ValidationResult validation : validationResults){
			if (validation.isNotPass()) {
				validation.getCauses().forEach(LOGGER::warn);
				strValidation.append(JSONUtils.toJSON(validation.getCauses()));
				strValidation.append("|");
				flag = -1;
			}		
		}
		if(-1 == flag){
			throw new BusinessException("1234",strValidation.toString());
		}

	}
	/**
	 * 根据对象中的注解验证对象
	 * @param object 待验证对象,validationResults 验证结果集
	 * @param <T> 对象类型	,List<ValidationResult> 对象类型
	 */
	public static <T> void validateiIterine(T object,List<ValidationResult> validationResults) {
		notNull(object, "被验证对象");
		LOGGER.info("验证对象：{}", JSONUtils.toJSON(object));	
		ValidationResult validation = ValidationUtils.validateObject(object);
		if(validation.isNotPass()){
			validation.getCauses().add(object.getClass().getName());
		}
		validationResults.add(validation);
		Field [] fields = object.getClass().getDeclaredFields();
		for(Field field:fields){
			String type = field.getGenericType().toString();
			if(type.contains("class com.newcore.orbps")){
				validateEmbedObject(object,field,validationResults);
			}
			if(type.contains("java.util.List")){
				validateEmbedList(object,field,validationResults);
			}
		}
	}

	/**
	 * @author huanglong
	 * @date 2016年12月19日
	 * @param validationResults
	 * @return void
	 * @content 对内嵌对象的校验函数进行拆分
	 */
	public static <T> void validateEmbedObject(T object,Field field,List<ValidationResult> validationResults){
		Assert.notNull(object);
		Assert.notNull(field);
		Assert.notNull(validationResults);
		String name = field.getName();
		field.setAccessible(true);
		name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
		try {
			Method method = object.getClass().getMethod("get" + name);
			if(null != method){		
				validateiIterine(method.invoke(object),validationResults); 					
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}
	

	/**
	 * @author huanglong
	 * @date 2016年12月19日
	 * @param validationResults
	 * @return void
	 * @content 对内嵌List的校验函数进行拆分
	 */
	public static <T> void validateEmbedList(T object,Field field,List<ValidationResult> validationResults){
		Assert.notNull(object);
		Assert.notNull(field);
		Assert.notNull(validationResults);
		
		String name = field.getName();
		String type = field.getGenericType().toString();
		field.setAccessible(true);
		String subStr = type.substring(type.indexOf('<')+1,type.indexOf('>'));
		if(subStr.indexOf("com.newcore.orbps") == -1) {
			return;
		}
		name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
		try {
			Method method = object.getClass().getMethod("get" + name);
			if(null != method){
				@SuppressWarnings("unchecked")
				List<Object> objects = (List<Object>) method.invoke(object);
				for(Object objectSub :objects){
					validateiIterine(objectSub,validationResults); 					
				}
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}	
	
	/**
	 * 根据对象中的注解验证对象
	 * @param object 待验证对象
	 * @param <T> 对象类型
	 * @throws BusinessException 如果有问题抛出业务异常
	 */
	public static <T> void validate(T object, @SuppressWarnings("rawtypes") Class... groups) {
		notNull(object, "被验证对象");
		LOGGER.info("验证对象：{}", JSONUtils.toJSON(object));
		ValidationResult validation = ValidationUtils.validateObject(object,groups);
		if (validation.isNotPass()) {
			validation.getCauses().forEach(LOGGER::warn);
			throw new BusinessException("1234",JSONUtils.toJSON(validation.getCauses()));
		}
	}

	/**
	 * 字符串是否有长度
	 * @param string 待检查字符串
	 * @param description 字符串描述
	 * @throws BusinessException 业务异常
	 */
	public static void hasLength(String string, String description) {
		LOGGER.info("验证{}[{}]是否有长度", description, string);
		if (!StringUtils.hasLength(string)) {
			throw new BusinessException("1234", description + "必须有内容");
		}
	}

	/**
	 * 验证不能为空
	 * @param object 待验证对象
	 * @param description 对象描述
	 * @throws BusinessException 业务异常
	 */
	public static void notNull(Object object, String description){
		if(null == object)
			throw new BusinessException("1234", description + "不能为空");
	}
}
