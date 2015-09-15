package org.silentsoft.everywhere.server.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class BeanUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;
	
	public ApplicationContext getApplicationContext() {
		return BeanUtil.applicationContext;
	}
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		BeanUtil.applicationContext = applicationContext;
	}
	
	public static void setApplicationContextForJUnitTest() {
		BeanUtil.applicationContext = new ClassPathXmlApplicationContext("META-INF/config/spring/application-context.xml");
	}

	public static <T> T getBean(Class<T> clazz) {
		if (clazz == null) {
			return null;
		}
		
		String name = clazz.getPackage() + "." + clazz.getName();
		if (!applicationContext.containsBean(name)) {
			createBean(clazz);
		}
		
		return applicationContext.getBean(clazz);
		//return applicationContext.getAutowireCapableBeanFactory().createBean(clazz);
	}
	
	private static void createBean(Class<?> clazz) {
		String name = clazz.getPackage() + "." + clazz.getName();
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ((AbstractApplicationContext) applicationContext).getBeanFactory();
		beanFactory.registerBeanDefinition(name, newBeanDefinition(clazz));
	}
	
	private static BeanDefinition newBeanDefinition(Class<?> clazz) {
		BeanDefinition bd = null;
		
		if (applicationContext instanceof GenericApplicationContext) {
			bd = BeanDefinitionBuilder.genericBeanDefinition(clazz).getBeanDefinition();
		} else if (applicationContext instanceof AbstractRefreshableApplicationContext) {
			bd = BeanDefinitionBuilder.rootBeanDefinition(clazz).getBeanDefinition();
		}
		
		return bd;
	}
	
	public static <T> T get(String name, Class<T> requiredType) {
		Object obj = get(name);
		if (requiredType != null && !requiredType.isAssignableFrom(obj.getClass())) {
			throw new BeanNotOfRequiredTypeException(name, requiredType, obj.getClass());
		}
		return (T) obj;
	}
	
	public static Object get(final String name) {
		return applicationContext.getBean(name);
	}
	
}
