package kroryi.dagon.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

// 이미지 업로드용

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${app.file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = Paths.get("uploads").toAbsolutePath().toUri().toString();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir + "/");  // ← D:/uploads/


//        registry
//                .addResourceHandler("/**") // 요청 URL
//                .addResourceLocations("classpath:/static/"); // 실제 파일 경로
        registry
                .addResourceHandler("/js/**") // 요청 URL
                .addResourceLocations("classpath:/static/js/"); // 실제 파일 경로

        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");

        registry.addResourceHandler("/img/**")
                .addResourceLocations("classpath:/static/img/");

        registry.addResourceHandler("/images/**") // 요청 경로
                .addResourceLocations(uploadPath); // 물리 경로


    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                        "http://docs.yi.or.kr:8094",
                        "http://docs.yi.or.kr:8095",
                        "http://docs.yi.or.kr:8096",
                        "http://docs.yi.or.kr:8097",
                        "http://docs.yi.or.kr:25173",
                        "http://docs.yi.or.kr:5173",
                        "http://localhost:8094",
                        "http://localhost:8095",
                        "http://localhost:8096",
                        "http://localhost:5174",
                        "http://localhost:5173"
                )
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .allowCredentials(true);

        registry.addMapping("/v3/api-docs/**")
                .allowedOrigins(
                        "http://docs.yi.or.kr:8097",
                        "http://docs.yi.or.kr:8096",
                        "http://docs.yi.or.kr:8095",
                        "http://docs.yi.or.kr:8094",
                        "https://docs.yi.or.kr:8097",
                        "https://docs.yi.or.kr:8096",
                        "https://docs.yi.or.kr:8095",
                        "https://docs.yi.or.kr:8094",
                        "http://localhost:8094",
                        "http://localhost:8095",
                        "http://localhost:8096",
                        "http://localhost:8097"
                )
                .allowedMethods("*");

        registry.addMapping("/swagger-ui/**") // 만약 Swagger UI 경로도 CORS 걸리면 추가
                .allowedOrigins(
                        "https://docs.yi.or.kr:8094",
                        "https://docs.yi.or.kr:8095",
                        "https://docs.yi.or.kr:8096",
                        "https://docs.yi.or.kr:8097",
                        "http://docs.yi.or.kr:8094",
                        "http://docs.yi.or.kr:8095",
                        "http://docs.yi.or.kr:8096",
                        "http://docs.yi.or.kr:8097",
                        "http://localhost:8094",
                        "http://localhost:8095",
                        "http://localhost:8096",
                        "http://localhost:8097"
                )
                .allowedMethods("*");
    }

}