package server.rebid.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Map;

@Configuration
public class SwaggerConfig {
    @Value("${backend.base_url}")
    public String backendBaseURL;


    @Bean
    public OpenAPI SpringCodeBaseAPI() {
        String SOCIAL_TAG_NAME = "\uD83D\uDE80 소셜 로그인";

        // Servers 에 표시되는 정보 설정
        Server server = new Server();
        server.setUrl(backendBaseURL);
        server.setDescription("ReBid Server API");

//        Map<String, PathItem> paths = Map.of(
//                "/bids", new PathItem().post(new Operation().security(List.of(new SecurityRequirement()))),
//                "/bids/{bidId}", new PathItem().post(new Operation().security(List.of(new SecurityRequirement()))),
//                "/bids/real-time", new PathItem().post(new Operation().security(List.of(new SecurityRequirement()))),
//                "/bids/imminent", new PathItem().post(new Operation().security(List.of(new SecurityRequirement()))),
//                "/bids/category", new PathItem().post(new Operation().security(List.of(new SecurityRequirement()))),
//                "/health", new PathItem().post(new Operation().security(List.of(new SecurityRequirement()))),
//                "/hello", new PathItem().post(new Operation().security(List.of(new SecurityRequirement()))),
//                "/imageTest", new PathItem().post(new Operation().security(List.of(new SecurityRequirement())))
//        );

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Bearer Token",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                // 기본적으로 모든 엔드포인트에 대한 JWT 인증이 필요한 것으로 설정
                .addSecurityItem(new SecurityRequirement().addList("Bearer Token"))
                .info(getInfo())
                // 서버 정보 추가
                .servers(List.of(server))
                .tags(List.of(new Tag().name(SOCIAL_TAG_NAME).description("Oauth2 Endpoint")))
                .path("/oauth2/authorization/naver", new PathItem()
                        .get(new Operation()
                                .tags(List.of(SOCIAL_TAG_NAME))
                                .summary("네이버 로그인")
                                // 인증 비활성화
                                .security(List.of())
                                .description(String.format("[네이버 로그인](%s/oauth2/authorization/naver)", backendBaseURL))
                                .responses(new ApiResponses()
                                        .addApiResponse("302", new ApiResponse()
                                                .content(new Content().addMediaType("application/json",
                                                        new MediaType().schema(new Schema<Map<String, String>>()
                                                                .type("object")
                                                                .example(Map.of(
                                                                        "Set-Cookie", "accessToken=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c; Max-Age=3600; Path=/; Domain=dev.rebid.store; HttpOnly=false; Secure=false, refreshToken=dGhpcy1pcy1hLXRlc3QtcmVmcmVzaC10b2tlbg; Max-Age=3600; Path=/; Domain=dev.rebid.site; HttpOnly=false; Secure=false"
                                                                )))))))))
                // JWT 인증 좌물쇠 제거
                .path("/health", new PathItem()
                        .get(new Operation().security(List.of()))
                )
                .path("/hello", new PathItem()
                        .get(new Operation().security(List.of())))
                .path("/imageTest", new PathItem()
                        .post(new Operation().security(List.of())))
                .path("/bids", new PathItem()
                        .get(new Operation().security(List.of())))
                .path("/bids/{bidId}", new PathItem()
                        .get(new Operation().security(List.of())))
                .path("/bids/real-time", new PathItem()
                        .get(new Operation().security(List.of())))
                .path("/bids/imminent", new PathItem()
                        .get(new Operation().security(List.of())))
                .path("/bids/category", new PathItem()
                        .get(new Operation().security(List.of())))

                //"/bids", "/bids/{bidId}", "/bids/real-time", "/bids/imminent", "/bids/category"
                ;
    }

    private Info getInfo(){
        return new Info()
                .title("ReBid API")
                .description("ReBid API 명세서")
                .version("0.0.1");
    }
}
