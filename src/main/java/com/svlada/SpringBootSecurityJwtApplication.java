package com.svlada;

import com.svlada.common.utils.FileUploadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;


@SpringBootApplication
@EnableConfigurationProperties
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
@EnableScheduling
@RestController
//@EnableAutoConfiguration(exclude = {MultipartAutoConfiguration.class})
public class SpringBootSecurityJwtApplication {

	@Bean(name="conversionService")
	public ConversionServiceFactoryBean serviceFactoryBean() {
		ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();
		Set<Converter> converters = new HashSet<>();
		converters.add(new StringToDateConverter());
		bean.setConverters(converters);
		return bean;
	}
	@PostMapping(value = "upload/batch")
	public String setPic(/*@PathVariable("code") String code,*/
						 HttpServletRequest request,
						 @RequestParam(value = "majorImageFiles",required = false) MultipartFile[] majorImageFiles,
                                 @RequestParam(value = "detailImageFiles",required = false) MultipartFile[] detailImageFiles) {
		Map<String, MultipartFile> fileMap = ((MultipartHttpServletRequest) request).getFileMap();
		Enumeration<String> parameterNames = request.getParameterNames();
		MultiValueMap<String, MultipartFile> multiFileMap = ((MultipartHttpServletRequest) request).getMultiFileMap();
//		List<MultipartFile> majorImageFiles = ((MultipartHttpServletRequest)request).getFiles("majorImageFiles");
//		List<MultipartFile> detailImageFiles = ((MultipartHttpServletRequest)request).getFiles("detailImageFiles");
		List<String> filePaths = FileUploadUtils.saveCommonFile(majorImageFiles, "test");
		return "success";
	}

	@PostMapping("/upload/multi")
	public ResponseEntity<?> uploadFileMulti(
			@RequestParam(value = "majorImageFiles",required = false) MultipartFile[] majorImageFiles) {
		List<String> filePaths = FileUploadUtils.saveCommonFile(majorImageFiles, "majorImageFiles");
		return new ResponseEntity("Successfully uploaded - ", HttpStatus.OK);

	}
    private static final Logger log = LoggerFactory.getLogger(SpringApplication.class);
    public static void main(String[] args) throws UnknownHostException {
//        ConfigurableApplicationContext app = SpringApplication.run(SpringBootSecurityJwtApplication.class, args);
        SpringApplication app = new SpringApplication(SpringBootSecurityJwtApplication.class);
        Environment env = app.run().getEnvironment	();
		log.info("The Application start now!Come ON,Partner!!!!");
        log.info("Access URLs:\n----------------------------------------------------------\n\t" +
                "Local: \t\thttp://127.0.0.1:{}\n\t" +
                "External: \thttp://{}:{}\n----------------------------------------------------------",
            env.getProperty("server.port"),
            InetAddress.getLocalHost().getHostAddress(),
            env.getProperty("server.port"));
	}
}
