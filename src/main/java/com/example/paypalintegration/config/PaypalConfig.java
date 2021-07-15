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

    @ConfigurationProperties(prefix = "paypal.application")
    public class PaypalApplication {
        private PaypalApplicationClient client;

        public PaypalApplicationClient getClient()
        {
            return client;
        }

        public void setClient(final PaypalApplicationClient client)
        {
            this.client = client;
        }
    }

    public class PaypalApplicationClient {
        private String id;
        private String secret;
        private String bncode;

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

        public void setBncode(String bncode) {
            this.bncode = bncode;
        }

        public String getBncode() {
            return bncode;
        }
    }
}
