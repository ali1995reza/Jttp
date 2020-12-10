package jttp.api;

public class RequestLine {

    private final HttpMethod method;
    private final String route;
    private final HttpVersion version;

    public RequestLine(HttpMethod method, String route, HttpVersion version) {
        this.method = method;
        this.route = route;
        this.version = version;
    }

    public HttpMethod method() {
        return method;
    }

    public HttpVersion httpVersion() {
        return version;
    }

    public String route() {
        return route;
    }

    @Override
    public String toString() {
        return "RequestLine{" +
                "method='" + method + '\'' +
                ", route='" + route + '\'' +
                ", version=" + version +
                '}';
    }
}
