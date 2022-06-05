package cn.archliu.horus.server.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.EndpointLinksResolver;
import org.springframework.boot.actuate.endpoint.web.EndpointMapping;
import org.springframework.boot.actuate.endpoint.web.EndpointMediaTypes;
import org.springframework.boot.actuate.endpoint.web.ExposableWebEndpoint;
import org.springframework.boot.actuate.endpoint.web.WebEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

        @Bean
        public Docket createRestApi() {
                return new Docket(DocumentationType.OAS_30) // v2 不同
                                .apiInfo(apiInfo())
                                .select()
                                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))// 加了ApiOperation注解的类，才生成接口文档
                                .apis(RequestHandlerSelectors.basePackage("cn.archliu.horus.server.domain")) // 设置扫描路径
                                // 忽略以"/error"开头的路径,可以防止显示如404错误接口
                                .paths(PathSelectors.regex("/error.*").negate())
                                .build();
        }

        private ApiInfo apiInfo() {
                return new ApiInfoBuilder()
                                .title("Horus") // 页面标题
                                .version("1.0") // 版本号
                                .description("ArchLiu's Horus") // 描述
                                .build();
        }

        /**
         * 解决SpringBoot升级到2.6.x之后，swagger 报错
         * 主要是因为 endPoint 扫出来的 HandlerMethod 的 patternsCondition 为 null
         *
         * @param webEndpointsSupplier        the web endpoints supplier
         * @param servletEndpointsSupplier    the servlet endpoints supplier
         * @param controllerEndpointsSupplier the controller endpoints supplier
         * @param endpointMediaTypes          the endpoint media types
         * @param corsProperties              the cors properties
         * @param webEndpointProperties       the web endpoint properties
         * @param environment                 the environment
         * @return the web mvc endpoint handler mapping
         */
        @Bean
        public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(
                        WebEndpointsSupplier webEndpointsSupplier, ServletEndpointsSupplier servletEndpointsSupplier,
                        ControllerEndpointsSupplier controllerEndpointsSupplier, EndpointMediaTypes endpointMediaTypes,
                        CorsEndpointProperties corsProperties, WebEndpointProperties webEndpointProperties,
                        Environment environment) {

                List<ExposableEndpoint<?>> allEndpoints = new ArrayList<>();
                Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
                allEndpoints.addAll(webEndpoints);
                allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
                allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
                String basePath = webEndpointProperties.getBasePath();
                EndpointMapping endpointMapping = new EndpointMapping(basePath);
                boolean shouldRegisterLinksMapping = shouldRegisterLinksMapping(webEndpointProperties,
                                environment, basePath);
                return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes,
                                corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath),
                                shouldRegisterLinksMapping, null);
        }

        /**
         * shouldRegisterLinksMapping
         * 
         * @param webEndpointProperties webEndpointProperties
         * @param environment           environment
         * @param basePath              /
         * @return boolean
         */
        private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties,
                        Environment environment, String basePath) {
                return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath)
                                || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
        }

}
