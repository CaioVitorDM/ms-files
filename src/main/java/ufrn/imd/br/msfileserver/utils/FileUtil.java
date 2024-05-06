package ufrn.imd.br.msfileserver.utils;

import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
public class FileUtil {

    /**
     * Deletes a file at the specific file path
     * @param filePath The path of the file to be deleted
     * @throws IOException If an error occurs while deleting the file
     */
    public void deleteFile(Path filePath) throws IOException{
        Files.deleteIfExists(filePath);
    }

    /**
     * Copies an uploaded file to the specific file path, replacing if it already
     * exists
     *
     * @param uploadFile Represents the uploaded file
     * @param filePath  The path to be copied to
     * @throws IOException  If an error occurs while deleting the file
     */
    public void copyFile(MultipartFile uploadFile, Path filePath) throws IOException{
        Files.copy(uploadFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Creates a URL resource based on the provided URI.
     *
     * @param uri The URI from which to create the URL resource.
     * @return A UrlResource representing the resource at the provided URI.
     * @throws IOException If an error occurs while creating the URL resource.
     */
    public UrlResource createUrlResource(URI uri) throws IOException{
        return new UrlResource(uri);
    }

}
