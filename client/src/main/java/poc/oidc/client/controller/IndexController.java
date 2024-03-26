package poc.oidc.client.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import poc.oidc.client.client.WebappClient;
import poc.oidc.client.dto.BookDto;

import java.util.List;

@Controller
@RequestMapping("/")
@Slf4j
@RequiredArgsConstructor
public class IndexController {


    private final WebappClient webappClient;

    @Value("${app.poc.aad.wa.baseUrl}")
    private String baseUrl;



    @GetMapping
    public String index(
            Model model, Authentication user,
            @RegisteredOAuth2AuthorizedClient("poc-aad-pa")
            OAuth2AuthorizedClient auth2AuthorizedClient)
    {
        log.info("GET /: user={}", user);
        model.addAttribute("user", user);

        List<BookDto> books = webappClient.retrieveBookDtos();
        model.addAttribute("books", books);
        return "index";
    }
}