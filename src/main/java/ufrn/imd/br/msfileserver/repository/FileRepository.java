package ufrn.imd.br.msfileserver.repository;

import org.springframework.stereotype.Repository;
import ufrn.imd.br.msfileserver.model.File;

@Repository
public interface FileRepository extends GenericRepository<File> {
}
