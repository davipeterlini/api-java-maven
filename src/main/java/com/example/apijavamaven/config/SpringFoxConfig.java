package com.example.apijavamaven.config;

import static java.text.MessageFormat.format;

import com.example.apijavamaven.util.DateUtils;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.servers.Server;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.web.OpenApiTransformationContext;
import springfox.documentation.oas.web.WebMvcOpenApiTransformationFilter;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ListVendorExtension;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {

  public static final String GROUPING_TAG = "Resources";
  private static final String X_CLIENT_KEY = "x-client-key";
  private static final String HISTORY_TABLE_TEMPLATE =
      "<br>"
          + "<table>\n"
          + "  <thead>\n"
          + "    <tr>\n"
          + "      <th>Versão</th>\n"
          + "      <th>Elaborado por</th>\n"
          + "      <th>Data</th>\n"
          + "      <th>Alterações</th>\n"
          + "    </tr>\n"
          + "  </thead>\n"
          + "  <tbody>\n"
          + "    <tr>\n"
          + "      <td>{0}</td>\n"
          + "      <td>{1}</td>\n"
          + "      <td>{2}</td>\n"
          + "      <td>{3}</td>\n"
          + "    </tr>\n"
          + "  </tbody>\n"
          + "</table>";

  private final ListVendorExtension<String> extension =
      new ListVendorExtension<>("x-domains", List.of("Customer"));

  private static List<Server> createServers() {
    return List.of(
        new Server()
            .description("localhost")
            .url("http://localhost:8091/api/"),
        new Server()
            .description("ENVIRONMENT")
            .url("http://DNS:PORT/BASE-PATH/"));
  }

  private Contact createContact() {
    return new Contact(
        "Example - API with Java 11 and Maven",
        "https://github.com/davipeterlini/api-java-maven",
        "davipeterlini@gmail.com");
  }

  private ApiInformation createApiInformation() {
    return ApiInformation.builder()
        .apiName("API Java Maven")
        .description(
            "API com códigos em Java (11) utilizando Maven")
        .build();
  }

  private List<ChangeHistory> createChangeHistories() {
    return List.of(
        ChangeHistory.builder()
            .version("1.0.0")
            .createdBy("Davi Peterlini")
            .date(LocalDate.of(2022, 2, 28))
            .changeDescription(
                "Created API Java Maven (Project)")
            .build(),
        ChangeHistory.builder()
            .version("2.0.0")
            .createdBy("Davi Peterlini")
            .date(LocalDate.of(2022, 2, 28))
            .changeDescription(
                "Code First with Swagger")
            .build());
  }

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
        .paths(PathSelectors.any())
        .build()
        .tags(
            new Tag(
                GROUPING_TAG,
                "\"Este Controller tem como objetivo exibir uma saída de texto (hello wolrd) para teste da API "))
        .securitySchemes(securitySchemes())
        .securityContexts(securityContexts())
        .apiInfo(apiInfo());
  }

  private List<SecurityScheme> securitySchemes() {
    var securityScheme = new ApiKey(X_CLIENT_KEY, "ApiKeyAuth", In.HEADER.name());
    return List.of(securityScheme);
  }

  private List<SecurityReference> defaultAuth() {
    AuthorizationScope[] scopes = {new AuthorizationScope("global", "Read/Write Access")};
    return List.of(new SecurityReference(X_CLIENT_KEY, scopes));
  }

  private List<SecurityContext> securityContexts() {
    var securityContext =
        SecurityContext.builder()
            .securityReferences(defaultAuth())
            .build();
    return List.of(securityContext);
  }

  private ApiInfo apiInfo() {
    var changeHistories = createChangeHistories();
    return new ApiInfoBuilder()
        .title(createApiInformation().getApiName())
        .description(createDescriptionTable(changeHistories))
        .version(createVersionText(changeHistories))
        .contact(createContact())
        .extensions(List.of(extension))
        .build();
  }

  @Bean
  public CorsFilter corsFilter() {
    var config = new CorsConfiguration();
    config.addAllowedOriginPattern("*");
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }

  private String createVersionText(List<ChangeHistory> changeHistories) {
    var mostRecentChange = getMostRecentChangeFrom(changeHistories);
    return mostRecentChange.getMajorValue() + ";" + mostRecentChange.getDateText();
  }

  private ChangeHistory getMostRecentChangeFrom(List<ChangeHistory> changeHistories) {
    var changeHistoriesTemp = new ArrayList<>(changeHistories);
    changeHistoriesTemp.sort(Comparator.comparing(a -> a.date));
    return changeHistoriesTemp.get(0);
  }

  private String createDescriptionTable(List<ChangeHistory> changeHistories) {
    var htmlDescriptionTable = new StringBuilder(createApiInformation().getDescription());
    changeHistories.forEach(
        changeHistory -> htmlDescriptionTable.append(createDescriptionRow(changeHistory)));
    return htmlDescriptionTable.toString();
  }

  private String createDescriptionRow(ChangeHistory changeHistory) {
    return format(
        HISTORY_TABLE_TEMPLATE,
        changeHistory.getVersion(),
        changeHistory.getCreatedBy(),
        changeHistory.getDateText(),
        changeHistory.getChangeDescription());
  }

  @Builder
  @Getter
  static class ApiInformation {

    private String apiName;
    private String description;
  }

  @Builder
  @Getter
  static class ChangeHistory {

    private String version;
    private String createdBy;
    private LocalDate date;
    private String changeDescription;

    private String getDateText() {
      return date.format(DateUtils.DATE_FORMATTER);
    }

    private String getMajorValue() {
      return version.substring(0, version.contains(".") ? version.indexOf(".") : version.length());
    }
  }

  @Component
  class SpringfoxSwaggerHostResolver implements WebMvcOpenApiTransformationFilter {

    @Override
    public OpenAPI transform(OpenApiTransformationContext<HttpServletRequest> context) {
      return context.getSpecification().servers(createServers());
    }

    @Override
    public boolean supports(DocumentationType docType) {
      return docType.equals(DocumentationType.OAS_30);
    }
  }
}