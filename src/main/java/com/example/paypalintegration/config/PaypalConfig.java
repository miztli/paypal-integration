package com.example.paypalintegration.config;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaypalConfig
{

    @Bean
    public PayPalHttpClient payPalHttpClient(final PaypalApplication paypalApplication) {
        final PaypalApplicationClient client = paypalApplication.getClient();
        final PayPalEnvironment environment = new PayPalEnvironment.Sandbox(client.getId(), client.getSecret());
        return new PayPalHttpClient(environment);
    }

    @Bean
    @ConfigurationProperties(prefix = "paypal.application")
    public PaypalApplication paypalApplication() {
        return new PaypalApplication();
    }

    public static class PaypalApplication {
        private PaypalApplicationClient client;
        private String bnCode;
        private String authAssertion;
        private String redirectionUrl;
        public PaypalApplicationClient getClient()
        {
            return client;
        }
        public void setClient(final PaypalApplicationClient client)
        {
            this.client = client;
        }
        public void setBnCode(String bnCode) {
            this.bnCode = bnCode;
        }
        public String getBnCode() {
            return bnCode;
        }
        public void setAuthAssertion(String authAssertion) {
            this.authAssertion = authAssertion;
        }
        public String getAuthAssertion() {
            return authAssertion;
        }
        public void setRedirectionUrl(String redirectionUrl) {
            this.redirectionUrl = redirectionUrl;
        }
        public String getRedirectionUrl() {
            return redirectionUrl;
        }
    }

    public static class PaypalApplicationClient {
        private String id;
        private String secret;
        public String getId()
        {
            return id;
        }
        public void setId(final String id)
        {
            this.id = id;
        }
        public String getSecret()
        {
            return secret;
        }
        public void setSecret(final String secret)
        {
            this.secret = secret;
        }
    }
}
