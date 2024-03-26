package poc.oidc.resource.server.rest;

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import poc.oidc.resource.server.dto.BookDto;
import poc.oidc.resource.server.exception.BookNotFoundException;
import poc.oidc.resource.server.service.BookService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
@Slf4j
public class BookRestController {


    private final BookService bookService;

    // @formatter:off
    @Operation(summary = "Get a book by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = BookDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content)})
    // @formatter:on
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_api://b7c5117f-6f75-4092-9dad-650685f04375/books.read')")
    public BookDto findById(@Parameter(description = "id of book to be searched") @PathVariable long id) throws BookNotFoundException {
        log.info("finding book with id: {}", id);
        return bookService.findById(id);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('APPROLE_books.read')")
    public Collection<BookDto> findBooks() {
        log.info("finding all books");
        return bookService.findBooks();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('APPROLE_books.write')")
    @ResponseStatus(HttpStatus.OK)
    public BookDto updateBook(@PathVariable("id") final Integer id, @NotNull @RequestBody @Valid final BookDto book) {
        log.info("updating book with id: {} book: {}", id, book);
        return bookService.updateBook(id, book);
    }

    // @PatchMapping("/{id}")
    // @ResponseStatus(HttpStatus.OK)
    // public BookDto patchBook(@PathVariable("id") final String id, @RequestBody final BookDto book) {
    //     return book;
    // }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('APPROLE_books.write')")
    public BookDto postBook(@NotNull @Valid @RequestBody final BookDto book) {
        log.info("creating  book: {}", book);
        return bookService.createBook(book);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('APPROLE_books.write')")
    @ResponseStatus(HttpStatus.OK)
    public long deleteBook(@PathVariable final long id) {
        log.info("deleting book with id: {}", id);
        return id;
    }

}
