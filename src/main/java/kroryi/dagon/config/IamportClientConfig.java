package kroryi.dagon.config;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IamportClientConfig {

    @Bean
    public IamportClient iamportClient() {
        return new IamportClient("3323773528544406", "d0Yp1UsqIGZVknON6XdkovVAzBxihOdP6wPABoyqDEP2DJnif90iC90KkSmpLTuwGEYo99nFa6Ii4Vi1");
    }
}
