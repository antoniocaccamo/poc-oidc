package poc.oidc.client.client;

import poc.oidc.client.dto.BookDto;

import java.util.List;

public interface WebappClient {

    List<BookDto> retrieveBookDtos();
}
