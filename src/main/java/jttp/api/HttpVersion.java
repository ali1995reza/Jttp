package jttp.api;

import jttp.api.exception.HttpParseException;

public enum HttpVersion {

    HTTP_1_1("HTTP/1.1"), HTTP_2("HTTP/2.0");

    private final String name;

    HttpVersion(String name) {
        this.name = name;
    }

    public final static HttpVersion getByName(String name, boolean ignoreCase) throws HttpParseException {
        if (ignoreCase) {
            if (name.equalsIgnoreCase(HTTP_1_1.name))
                return HTTP_1_1;
            else if (name.equalsIgnoreCase(HTTP_2.name))
                return HTTP_2;
        } else {
            if (name.equals(HTTP_1_1.name))
                return HTTP_1_1;
            else if (name.equals(HTTP_2.name))
                return HTTP_2;
        }


        throw new HttpParseException("can not find http version by [" + name + "]");
    }

    public final static HttpVersion getByName(String name) throws HttpParseException {
        return getByName(name, true);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
