package io.github.Toku2001.trackandfieldapp.exception;

public class DatabaseOperationException extends RuntimeException {
    public DatabaseOperationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    // メッセージのみ
    public DatabaseOperationException(String message) {
        super(message);
    }
}