package jttp.api.exception;


public class HttpParseException extends Exception {

    public HttpParseException(Throwable e) {
        super(e);
    }

    public HttpParseException(String e) {
        super(e);
    }

    public HttpParseException() {
        super();
    }
}
