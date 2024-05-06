package ufrn.imd.br.msfileserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ufrn.imd.br.msfileserver.dto.FileDownloadResponse;
import ufrn.imd.br.msfileserver.utils.FileUtil;
import ufrn.imd.br.msfileserver.utils.exception.FileOperationException;
import ufrn.imd.br.msfileserver.utils.exception.ResourceNotFoundException;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class FileSysManagementService {
    private static final Logger logger = LoggerFactory.getLogger(FileSysManagementService.class);
    private final FileUtil fileUtil;

    public FileSysManagementService(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    public String upload(MultipartFile uploadFile, String path){
        try{
            Path directory = Path.of(path);
            if(!Files.exists(directory)){
                Files.createDirectories(directory);
            }

            String originalFileName = Objects.requireNonNull(uploadFile.getOriginalFilename());
            String fileName = originalFileName;
            Path filePath = directory.resolve(fileName);

            while (Files.exists(filePath)) {
                String fileExtension;
                int lastDotIndex = originalFileName.lastIndexOf(".");
                if (lastDotIndex > 0) {
                    fileExtension = originalFileName.substring(lastDotIndex);
                    fileName = originalFileName.substring(0, lastDotIndex) + "-" + System.currentTimeMillis()
                            + fileExtension;
                } else {
                    fileName = originalFileName + "-" + System.currentTimeMillis();
                }
                filePath = directory.resolve(fileName);
            }

            fileUtil.copyFile(uploadFile, filePath);
            logger.info("O arquivo foi registrado: {}", filePath);
            return filePath.toString();

        } catch (IOException e) {
            logger.error("Falha ao salvar arquivo no path: {}", path);
            throw new FileOperationException("Falha ao salvar arquivo!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public FileDownloadResponse download(String filePath){
        try {
            Path path = Path.of(filePath);
            if(!Files.exists(path)){
                logger.warn("Arquivo não encontrado: {}", filePath);
                throw new ResourceNotFoundException("Arquivo não encontrado");
            }

            var resource = fileUtil.createUrlResource(path.toUri());
            return new FileDownloadResponse(resource.getFilename(), resource);
        }
        catch (IOException e){
            logger.error("Erro ao tentar fazer download do arquivo: {}", filePath);
            throw new FileOperationException("Erro ao tentar fazer download do arquivo!",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void remove(String path, String fileName){
        Path filePath = Paths.get(path, fileName);

        if(Files.exists(filePath)){
            try{
                fileUtil.deleteFile(filePath);
            }
            catch (IOException e){
                logger.error("Erro ao tentar remover o arquivo. Caminho do arquivo: {}", filePath);
                throw new FileOperationException("Erro ao tentar remover arquivo", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            logger.info("Arquivo removido: {}", filePath);
        }
        else{
            logger.warn("Arquivo não encontrado: {}", filePath);
            throw new ResourceNotFoundException("Arquivo não encontrado");
        }
    }
}
