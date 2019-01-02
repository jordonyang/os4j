package org.jordon.os.constants;

public enum FileType {

    DIRECTORY(0),
    FILE(1),
    ;

    private int code;

    FileType(int code) {
        this.code = code;
    }
}
