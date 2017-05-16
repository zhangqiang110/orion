package com.polaris.config.springmvc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.utils.Charsets;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.SimpleServletHandlerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.polaris.common.constant.PolarisConstants;
import com.polaris.common.utils.JsonUtil;

/**
 * SpringMVC 相关配置, 相当于xml时代的polaris-servlet.xml配置文件
 * 
 * @author John
 * @description :
 * 
 *              <pre class="brush:java;">
 * 不使用 {@link EnableWebMvc} 注解而是直接继承于 {@link WebMvcConfigurationSupport} 或者
 * {@link DelegatingWebMvcConfiguration}，之后通过覆盖方法可以实现更多可选功能。
 * 如果使用EnableWebMvc注解就可以解决需求的话，那直接继承WebMvcConfigurationAdapter就可以了。
 *              </pre>
 *
 */
@Configuration
@ComponentScan(basePackages = { "com.polaris.manage.web" }, useDefaultFilters = false, includeFilters = {
		@ComponentScan.Filter(type = FilterType.ANNOTATION, value = {Controller.class, RestController.class,
				Aspect.class, ControllerAdvice.class, RestControllerAdvice.class }) })
@EnableAspectJAutoProxy(proxyTargetClass = true) // 启用Springmvc层面的切面自动代理，用于AOP,并指定使用CGLIB代理
public class PolarisMvcConfig extends WebMvcConfigurationSupport {

	/**
	 * JSP视图解析器
	 * 
	 * @return
	 */
//	@Bean(name = "viewResolver")
//	public ViewResolver viewResolver() {
//		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//		viewResolver.setPrefix(PolarisConstants.VIEW_JSP_PREFIX);
//		viewResolver.setSuffix(PolarisConstants.VIEW_JSP_SUFFIX);
//		viewResolver.setViewClass(JstlView.class);
//		viewResolver.setContentType(PolarisConstants.CONTENT_TYPE);
//		viewResolver.setOrder(0); // 设置视图优先级
//		return viewResolver;
//	}

	/**
	 * FreeMarker视图解析器
	 */
//	@Bean
//	public FreeMarkerViewResolver freeMarkerViewResolver() {
//		FreeMarkerViewResolver freeMarkerViewResolver = new FreeMarkerViewResolver();
//		freeMarkerViewResolver.setViewClass(FreeMarkerView.class);
//		freeMarkerViewResolver.setContentType(PolarisConstants.CONTENT_TYPE);
//		freeMarkerViewResolver.setCache(true);
//		freeMarkerViewResolver.setRequestContextAttribute("basePath");
//		freeMarkerViewResolver.setSuffix(PolarisConstants.VIEW_FREEMARKER_SUFFIX);
//		freeMarkerViewResolver.setOrder(1); // 设置视图优先级
//		return freeMarkerViewResolver;
//	}

	/**
	 * FreeMarker模板配置
	 */
//	@Bean
//	public FreeMarkerConfigurer freeMarkerConfigurer() {
//		FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
//		freeMarkerConfigurer.setTemplateLoaderPath(PolarisConstants.VIEW_FREEMARKER_TEMPLATE_LOADER_PATH);
//		freeMarkerConfigurer.setDefaultEncoding(PolarisConstants.CHAESET_UTF_8);
//		return freeMarkerConfigurer;
//	}

	/**
	 * 消息转换器,Spring默认是注册了以下转换器的，但是为了让json转换器使用我们自定义的日期格式，所以需要全部重新配置
	 */
	private List<HttpMessageConverter<?>> createMessageConverters() {
		List<HttpMessageConverter<?>> converters = new ArrayList<>();
		converters.add(new ByteArrayHttpMessageConverter());
		StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
		stringConverter.setWriteAcceptCharset(false);
		stringConverter.setDefaultCharset(Charsets.UTF_8);
		// 文本消息转换器所支持的文本类型
		List<MediaType> textTypes = new ArrayList<>();
		// 原生格式
		textTypes.add(MediaType.TEXT_PLAIN);
		// HTML格式
		textTypes.add(MediaType.TEXT_HTML);
		// 以us-ascii编码XML内容
		textTypes.add(MediaType.TEXT_XML);
		// 以指定字符集编码XML内容
		textTypes.add(MediaType.APPLICATION_XML);
		stringConverter.setSupportedMediaTypes(textTypes);
		converters.add(stringConverter);
		// XML消息转换器，两种方式:Jaxb 和 Marshalling，任选其一
		converters.add(new Jaxb2RootElementHttpMessageConverter());
//		converters.add(new MarshallingHttpMessageConverter(new CastorMarshaller(), new CastorMarshaller()));
		// 待测 converters.add(new MarshallingHttpMessageConverter(new
		// Jaxb2Marshaller(), new Jaxb2Marshaller()));
		// Json消息转换器
		MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
		jsonConverter.setObjectMapper(JsonUtil.createMapper());
		jsonConverter.setDefaultCharset(Charsets.UTF_8);
		List<MediaType> jsonTypes = new ArrayList<>();
		jsonTypes.add(MediaType.APPLICATION_JSON);
		jsonTypes.add(MediaType.APPLICATION_JSON_UTF8);
		jsonTypes.add(MediaType.TEXT_HTML);// 避免IE出现下载JSON文件的情况
		jsonConverter.setSupportedMediaTypes(jsonTypes);
		converters.add(jsonConverter);
		return converters;
	}

	/**
	 * 为映射请求处理器配置消息映射器
	 * 
	 * @return
	 */
	@Bean
	@Override
	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
		return super.requestMappingHandlerMapping();
	}

	/**
	 * 为映射请求处理器配置消息转换器
	 * 
	 * @return
	 */
	@Bean
	@Override
	public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
		super.requestMappingHandlerAdapter();
		RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
		requestMappingHandlerAdapter.setMessageConverters(createMessageConverters());
		return requestMappingHandlerAdapter;
	}

	/**
	 * 消息国际化
	 * 
	 * @return
	 */
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename(PolarisConstants.MESSAGE_SOURCE);
		messageSource.setCacheSeconds(5);
		return messageSource;
	}

	/**
	 * 添加静态资源映射
	 */
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(PolarisConstants.RESOURCE_HANDLER)
				.addResourceLocations(PolarisConstants.RESOURCE_LOCATION);
	}

	/**
	 * 因为将spring的拦截模式设置为"/"时会对静态资源进行拦截, 将对于静态资源的请求转发到Servlet容器的默认处理静态资源的servlet
	 */
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	/**
	 * 描述 : <注册servlet适配器>. <br>
	 * <p>
	 * <只需要在自定义的servlet上用@Controller("映射路径")标注即可>
	 * </p>
	 * 
	 * @return
	 */
	@Bean
	public HandlerAdapter servletHandlerAdapter() {
		return new SimpleServletHandlerAdapter();
	}

	/**
	 * 描述 : <基于cookie的本地化资源处理器>. <br>
	 * <p>
	 * <使用方法说明>
	 * </p>
	 * 
	 * @return
	 */
	@Bean
	public CookieLocaleResolver cookieLocaleResolver() {
		return new CookieLocaleResolver();
	}

	/**
	 * 描述 : <本地化拦截器>. <br>
	 * <p>
	 * <使用方法说明>
	 * </p>
	 * 
	 * @return
	 */
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		return new LocaleChangeInterceptor();
	}

	/**
	 * 描述 : <注册自定义拦截器>. <br>
	 * <p>
	 * <使用方法说明>
	 * </p>
	 * 
	 * @return
	 */
	/*
	 * @Bean public PolarisInitializingInterceptor
	 * polarisInitializingInterceptor() { return new
	 * PolarisInitializingInterceptor(); }
	 */

	/**
	 * 描述 : <添加拦截器>. <br>
	 * <p>
	 * <使用方法说明>
	 * </p>
	 * 
	 * @param registry
	 */
	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor()); // 本地化拦截器
		// registry.addInterceptor(polarisInitializingInterceptor()); // 自定义拦截器
	}

	/**
	 * 描述 : <文件上传处理器>. <br>
	 * <p>
	 * <使用方法说明>
	 * </p>
	 * 
	 * @return
	 */
	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver commonsMultipartResolver() {
		return new CommonsMultipartResolver();
	}

	/**
	 * 描述 : <异常处理器>. <br>
	 * <p>
	 * <系统运行时遇到指定的异常将会跳转到指定的页面>
	 * </p>
	 * 
	 * @return
	 */
	/*
	 * @Bean public PolarisExceptionResolver polarisExceptionResolver() {
	 * PolarisExceptionResolver polarisExceptionResolver = new
	 * PolarisExceptionResolver();
	 * polarisExceptionResolver.setDefaultErrorView("common_error");
	 * polarisExceptionResolver.setExceptionAttribute("exception"); Properties
	 * properties = new Properties();
	 * properties.setProperty("java.lang.RuntimeException", "common_error");
	 * polarisExceptionResolver.setExceptionMappings(properties); return
	 * polarisExceptionResolver; }
	 */

	/**
	 * 描述 : <注册通用属性编辑器>. <br>
	 * <p>
	 * <这里只增加了字符串转日期和字符串两边去空格的处理>
	 * </p>
	 * 
	 * @return
	 */
	/*
	 * @Override protected ConfigurableWebBindingInitializer
	 * getConfigurableWebBindingInitializer() {
	 * ConfigurableWebBindingInitializer initializer =
	 * super.getConfigurableWebBindingInitializer(); PolarisEditorRegistrar
	 * register = new PolarisEditorRegistrar();
	 * register.setFormat("yyyy-MM-dd");
	 * initializer.setPropertyEditorRegistrar(register); return initializer; }
	 */

}
