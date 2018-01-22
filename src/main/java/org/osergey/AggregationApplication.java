package org.osergey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableScheduling
@EnableResourceServer
public class AggregationApplication {
    public static void main(String[] args) {
        SpringApplication.run(AggregationApplication.class, args);
    }

    @Configuration
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    protected static class SecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests().anyRequest().authenticated()
                    .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
            ;
        }
    }

    @Configuration
    @EnableOAuth2Client
    class DeptClientConfig {
        @Value("${oauth.token:http://localhost:8081/oauth/token}")
        private String tokenUrl;

        protected OAuth2ProtectedResourceDetails resource() {
            ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
            resource.setAccessTokenUri(tokenUrl);
            resource.setClientId("deptlogin");
            resource.setClientSecret("deptpass");
            resource.setGrantType("client_credentials");
            return resource;
        }

        @Bean
        public OAuth2RestOperations deptRest() {
            AccessTokenRequest atr = new DefaultAccessTokenRequest();
            return new OAuth2RestTemplate(resource(), new DefaultOAuth2ClientContext(atr));
        }
    }

    @Configuration
    @EnableOAuth2Client
    class ContactClientConfig {
        @Value("${oauth.token:http://localhost:8082/oauth/token}")
        private String tokenUrl;

        protected OAuth2ProtectedResourceDetails resource() {
            ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
            resource.setAccessTokenUri(tokenUrl);
            resource.setClientId("contactlogin");
            resource.setClientSecret("contactpass");
            resource.setGrantType("client_credentials");
            return resource;
        }

        @Bean
        public OAuth2RestOperations contactRest() {
            AccessTokenRequest atr = new DefaultAccessTokenRequest();
            return new OAuth2RestTemplate(resource(), new DefaultOAuth2ClientContext(atr));
        }
    }

    @Configuration
    @EnableOAuth2Client
    class PaymentClientConfig {
        @Value("${oauth.token:http://localhost:8083/oauth/token}")
        private String tokenUrl;

        protected OAuth2ProtectedResourceDetails resource() {
            ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
            resource.setAccessTokenUri(tokenUrl);
            resource.setClientId("paymentlogin");
            resource.setClientSecret("paymentpass");
            resource.setGrantType("client_credentials");
            return resource;
        }

        @Bean
        public OAuth2RestOperations paymentRest() {
            AccessTokenRequest atr = new DefaultAccessTokenRequest();
            return new OAuth2RestTemplate(resource(), new DefaultOAuth2ClientContext(atr));
        }
    }
}
