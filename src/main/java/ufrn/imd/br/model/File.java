package ufrn.imd.br.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Where;

import java.util.Objects;

@Entity
@Table(name = "file")
@Where(clause = "active = true")
public class File extends BaseEntity{

    @NotBlank
    private String name;

    @NotBlank
    private String path;

    @NotNull
    private Long size;

    @NotBlank
    private String extensionType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getExtensionType() {
        return extensionType;
    }

    public void setExtensionType(String extensionType) {
        this.extensionType = extensionType;
    }

    @Override
    public boolean equals(Object o){
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;
        File fileEntity = (File) o;
        return Objects.equals(name, fileEntity.name)
                && Objects.equals(path, fileEntity.path)
                && Objects.equals(size, fileEntity.size)
                && Objects.equals(extensionType, fileEntity.extensionType);
    }

    @Override
    public int hashCode(){
        return Objects.hash(super.hashCode(), name, path, size, extensionType);
    }
}
