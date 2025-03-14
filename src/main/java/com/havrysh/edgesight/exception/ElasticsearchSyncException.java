package com.havrysh.edgesight.exception;

public class ElasticsearchSyncException extends RuntimeException {

    public ElasticsearchSyncException(String message, Throwable cause) {
        super(message, cause);
    }
}