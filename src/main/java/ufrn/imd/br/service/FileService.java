package ufrn.imd.br.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ufrn.imd.br.dto.FileDownloadResponse;
import ufrn.imd.br.dto.FileResponseDTO;
import ufrn.imd.br.dto.FileUploadDTO;
import ufrn.imd.br.mappers.FileMapper;
import ufrn.imd.br.model.File;
import ufrn.imd.br.repository.FileRepository;
import ufrn.imd.br.utils.exception.ResourceNotFoundException;

@Service
@Transactional
public class FileService {

    private final FileRepository repository;
    private final FileSysManagementService fileSysManagementService;
    private final FileMapper fileMapper;

    @Value("${file.upload-path}")
    private String uploadPath;

    public FileService(FileRepository repository, FileSysManagementService fileSysManagementService, FileMapper fileMapper) {
        this.repository = repository;
        this.fileSysManagementService = fileSysManagementService;
        this.fileMapper = fileMapper;
    }

    public FileResponseDTO upload(FileUploadDTO dto){
        var path = fileSysManagementService.upload(dto.file(), uploadPath);
        var fileToSave = new File();

        fileToSave.setName(dto.file().getOriginalFilename());
        fileToSave.setPath(path);
        fileToSave.setExtensionType(dto.file().getContentType());
        fileToSave.setSize(dto.file().getSize());

        var fileSaved = repository.save(fileToSave);

        return new FileResponseDTO(fileSaved.getId(), fileSaved.getName());
    }

    public FileDownloadResponse download(Long id){
        var fileEntity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("O arquivo não foi encontrado"));

        return fileSysManagementService.download(fileEntity.getPath());
    }

    public void deleteById(Long id){
        var fileEntity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("O arquivo não foi encontrado"));

        repository.deleteById(id);
        fileSysManagementService.remove(uploadPath, fileEntity.getName());
    }

    public Page<FileResponseDTO> findAll(Pageable pageable){
        var entityPage = repository.findAll(pageable);
        return new PageImpl<>(fileMapper.toDto(entityPage.getContent()), pageable, entityPage.getTotalElements());
    }
}
