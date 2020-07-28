package com.dataway.cn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger2配置类
 * @author phil
 * @date 2020/06/10 14:19
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    /**
     * 可以注入多个docket，也就是多个版本的api，可以在看到有三个版本groupName不能是重复的，
     * v1和v2是ant风格匹配，配置文件
     */
    @Bean
    public Docket api() {
        //可以添加多个header或参数
        ParameterBuilder parameterBuilder = new ParameterBuilder();
        parameterBuilder
                .parameterType("header")
                .name("Authorization")
                .description("header中Authorization字段用于认证")
                .modelRef(new ModelRef("string"))
                //非必需，这里是全局配置，然而在登陆的时候是不用验证的
                .required(false).build();
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(parameterBuilder.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("v1")
                .select()
                //控制暴露出去的路径下的实例
                //如果某个接口不想暴露,可以使用以下注解
                //@ApiIgnore 这样,该接口就不会暴露在 swagger2 的页面下
                .apis(RequestHandlerSelectors.basePackage("com.dataway.cn.controller"))
                .paths(PathSelectors.ant("/**"))
                .build()
                .apiInfo(apiInfo1())
                .globalOperationParameters(parameters);
    }

    private ApiInfo apiInfo1() {
        return new ApiInfoBuilder()
                .title("Data Way的Server端 APIs")
                .contact(new Contact("Phil","","phil@163.com"))
                .version("v0.01")
                .build();
    }
}
