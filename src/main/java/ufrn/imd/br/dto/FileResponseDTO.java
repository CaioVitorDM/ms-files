package ufrn.imd.br.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * Represents a DTO for file information that will be returned
 * to the front-end to save the file information at another entities.
 *
 * @param id    The file unique identifier at the database
 * @param name  The file name.
 */
public record FileResponseDTO(
        long id,
        @NotBlank
        String name)
        implements Serializable {
}
