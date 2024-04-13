package ufrn.imd.br.utils.exception;

import org.springframework.http.HttpStatus;

public class FileOperationException extends RuntimeException{
    private final HttpStatus httpStatusCode;

    public FileOperationException(String message, HttpStatus httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public HttpStatus getHttpStatusCode() {
        return httpStatusCode;
    }
}
