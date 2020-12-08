package jttp.api;

public class RequestLine {

    private final String method;
    private final String route;
    private final HttpVersion version;

    public RequestLine(String method, String route, HttpVersion version) {
        this.method = method;
        this.route = route;
        this.version = version;
    }

    public String method() {
        return method;
    }

    public HttpVersion httpVersion() {
        return version;
    }

    public String route() {
        return route;
    }
}
