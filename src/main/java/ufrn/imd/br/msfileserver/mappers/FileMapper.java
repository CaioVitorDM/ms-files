package ufrn.imd.br.msfileserver.mappers;

import org.springframework.stereotype.Component;
import ufrn.imd.br.msfileserver.dto.FileResponseDTO;
import ufrn.imd.br.msfileserver.model.File;

@Component
public class FileMapper implements DtoMapper<File, FileResponseDTO> {

    @Override
    public FileResponseDTO toDto(File entity) {
        return new FileResponseDTO(
                entity.getId(),
                entity.getName()
        );
    }

    @Override
    public File toEntity(FileResponseDTO fileResponseDTO) {
        return Parser.parse(fileResponseDTO, File.class);
    }
}
