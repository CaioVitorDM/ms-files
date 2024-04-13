package ufrn.imd.br.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ufrn.imd.br.dto.ApiResponseDTO;
import ufrn.imd.br.dto.FileDownloadResponse;
import ufrn.imd.br.dto.FileResponseDTO;
import ufrn.imd.br.dto.FileUploadDTO;
import ufrn.imd.br.service.FileService;
import ufrn.imd.br.utils.exception.BusinessException;
import ufrn.imd.br.utils.exception.ResourceNotFoundException;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("v1/files")
@Validated
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }


    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<FileResponseDTO>>> getAll(@ParameterObject Pageable pageable){
        return ResponseEntity.ok(new ApiResponseDTO<>(
                true,
                "Sucess: Entities located successfully",
                fileService.findAll(pageable),
                null));
    }

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<ApiResponseDTO<FileResponseDTO>> upload(
            @Valid @ModelAttribute FileUploadDTO dto){

        return ResponseEntity.ok(new ApiResponseDTO<>(
                true,
                "Sucess: file uploaded successfully",
                fileService.upload(dto),
                null
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> inlineFile(@PathVariable Long id, HttpServletRequest request){
        return createFileResponse(request, fileService.download(id), "inline");
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id, HttpServletRequest request){
        return createFileResponse(request, fileService.download(id), "attachment");
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponseDTO<FileResponseDTO>> delete(@PathVariable Long id){
        fileService.deleteById(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(
                true,
                "Sucess: file deleted successfully",
                null,
                null
        ));
    }

    private ResponseEntity<Resource> createFileResponse(HttpServletRequest request, FileDownloadResponse response, String contentDisposition){
        if(response.resource() == null){
            throw new ResourceNotFoundException("Arquivo não encontrado");
        }

        File file;

        try {
            file = response.resource().getFile();
        } catch (IOException e) {
            throw new BusinessException("Falha na leitura do arquivo", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String contentType = request.getServletContext().getMimeType(file.getAbsolutePath());
        if(contentType == null){
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        contentDisposition + "; filename=\"" + response.resource().getFilename())
                .body(response.resource());
    }

}
