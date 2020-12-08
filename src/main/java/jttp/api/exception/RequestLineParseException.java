package jttp.api.exception;

public class RequestLineParseException extends HttpParseException {

    public RequestLineParseException(Throwable e)
    {
        super(e);
    }

    public RequestLineParseException(String e)
    {
        super(e);
    }

    public RequestLineParseException()
    {
        super();
    }

}
