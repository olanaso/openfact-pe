package org.openfact.pe.models.jpa.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "SUNAT_STORAGE_FILE")
@NamedQueries({
        @NamedQuery(name = "getAllSunatStorageFiles", query = "select f from SunatStorageFileEntity f")
})
public class SunatStorageFileEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Access(AccessType.PROPERTY)
    private String id;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "MIME_TYPE")
    private String mimeType;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "FILE")
    private byte[] file;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

}
