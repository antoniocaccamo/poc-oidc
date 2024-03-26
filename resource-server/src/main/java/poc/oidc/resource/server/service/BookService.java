package poc.oidc.resource.server.service;


import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import poc.oidc.resource.server.dto.BookDto;
import poc.oidc.resource.server.exception.BookNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BookService {

    private AtomicLong bookIdSequence = new AtomicLong(0);
    private List<BookDto> books = new ArrayList<>();

    @PostConstruct
    private void postConstruct() {

        for ( int i = 0; i < 5 ; i++ ) {
            Long id = bookIdSequence.addAndGet(1);
            books.add(
                    BookDto.builder()
                            .id(id)
                            .title(String.format("title %d", id))
                            .author(String.format("author %d", id))
                            .build()
            );
        }

    }

    public BookDto findById(long id) throws BookNotFoundException {
        return books.stream().filter(b -> b.getId().equals(id)).findFirst()
                .orElseThrow(() -> new BookNotFoundException());
    }

    public Collection<BookDto> findBooks() {
        return books;
    }

    public BookDto updateBook(int i, BookDto book) {
        books.add(i, book);
        return book;
    }
    public BookDto createBook(BookDto book) {
        book.setId(bookIdSequence.addAndGet(1));
        books.add(book);
        return book;
    } 
}
