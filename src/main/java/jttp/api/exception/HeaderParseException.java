package jttp.api.exception;

public class HeaderParseException extends HttpParseException {

    public HeaderParseException(Throwable e) {
        super(e);
    }

    public HeaderParseException(String e) {
        super(e);
    }

    public HeaderParseException() {
        super();
    }
}

