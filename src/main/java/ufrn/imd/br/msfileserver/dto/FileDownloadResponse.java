package ufrn.imd.br.msfileserver.dto;

import org.springframework.core.io.Resource;

/**
 * Represents the response for downloading a file.
 * It includes the file name and the resource containing the file data.
 * @param fileName
 * @param resource
 */
public record FileDownloadResponse(
        String fileName,
        Resource resource) {
}
