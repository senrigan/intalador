package com.gdc.nms.setup.util;

public class InstallationParseException extends RuntimeException {

	private static final long serialVersionUID = 1780632773343729118L;

    public InstallationParseException() {
        super();
    }

    public InstallationParseException(String message) {
        super(message);
    }

    public InstallationParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstallationParseException(Throwable cause) {
        super(cause);
    }

    protected InstallationParseException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
	
}
