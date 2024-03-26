package poc.oidc.client.client.azure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import poc.oidc.client.client.WebappClient;
import poc.oidc.client.dto.BookDto;

import java.util.List;

/**
 * @auhtor antonio.caccamo on 2024-03-19 @ 10:44
 */

@Profile("azure")
@Component
@Slf4j
@RequiredArgsConstructor
public class AzureWebclient implements WebappClient {

    private final WebClient azureWebAppClient;


    @Value("${app.poc.aad.wa.baseUrl}")
    private String baseUrl;


    @Override
    public List<BookDto> retrieveBookDtos() {
        String uri = baseUrl + "/api/books";
        return azureWebAppClient.get()
                       .uri(uri)
                       .attributes(ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId("poc-aad-pa"))
                       .retrieve()
                       .bodyToFlux(BookDto.class)
                       .collectList()
                       .block()
                ;
    }
}
