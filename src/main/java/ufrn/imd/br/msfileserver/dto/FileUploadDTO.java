package ufrn.imd.br.msfileserver.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * Represents the Data Transfer Object for uploading a file.
 * This is the object received at the back
 * @param file
 */
public record FileUploadDTO(
        @NotNull
        MultipartFile file
) implements Serializable {
}
