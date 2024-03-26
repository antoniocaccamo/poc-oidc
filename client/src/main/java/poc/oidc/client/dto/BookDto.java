package poc.oidc.client.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;


@Builder
@Getter @Setter @ToString
@RequiredArgsConstructor @AllArgsConstructor
public class BookDto implements Serializable{

        private Long id;

        @NotBlank
        @Size(min = 0, max = 20)
        private String title;

        @NotBlank
        @Size(min = 0, max = 30)
        private String author;        

}