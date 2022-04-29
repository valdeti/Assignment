package com.assignment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

  @NestedConfigurationProperty
  private final Security security = new Security();

  @Data
  public static class Security {

    private final Authentication authentication = new Authentication();

    @Data
    public static class Authentication {

      private final Jwt jwt = new Jwt();

      @Data
      public static class Jwt {

        private String secret;
      }
    }
  }
}
