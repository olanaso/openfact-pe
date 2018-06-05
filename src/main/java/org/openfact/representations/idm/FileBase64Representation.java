package org.openfact.representations.idm;

import javax.validation.constraints.NotNull;

public class FileBase64Representation {

    @NotNull
    private String fileName;

    @NotNull
    private String file;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
