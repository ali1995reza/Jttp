package jttp.api;

import jttp.api.exception.HttpParseException;

public enum  HttpMethod {

    GET("GET") ,
    POST("POST") ,
    PUT("PUT"),
    HEAD("HEAD") ,
    DELETE("DELETE") ,
    CONNECT("CONNECT") ,
    OPTIONS("OPTIONS") ,
    TRACE("TRACE") ,
    PATCH("PATCH");

    private final String name;

    HttpMethod(String name) {
        this.name = name;
    }

    public final static HttpMethod getByName(String name , boolean ignoreCase) throws HttpParseException
    {
        if(ignoreCase)
        {
            if(GET.name.equalsIgnoreCase(name))
                return GET;

            if(POST.name.equalsIgnoreCase(name))
                return POST;

            if(PUT.name.equalsIgnoreCase(name))
                return PUT;

            if(HEAD.name.equalsIgnoreCase(name))
                return HEAD;

            if(DELETE.name.equalsIgnoreCase(name))
                return DELETE;

            if(CONNECT.name.equalsIgnoreCase(name))
                return CONNECT;

            if(OPTIONS.name.equalsIgnoreCase(name))
                return OPTIONS;

            if(TRACE.name.equalsIgnoreCase(name))
                return TRACE;

            if(PATCH.name.equalsIgnoreCase(name))
                return PATCH;
        }else {

            if(GET.name.equals(name))
                return GET;

            if(POST.name.equals(name))
                return POST;

            if(PUT.name.equals(name))
                return PUT;

            if(HEAD.name.equals(name))
                return HEAD;

            if(DELETE.name.equals(name))
                return DELETE;

            if(CONNECT.name.equals(name))
                return CONNECT;

            if(OPTIONS.name.equals(name))
                return OPTIONS;

            if(TRACE.name.equals(name))
                return TRACE;

            if(PATCH.name.equals(name))
                return PATCH;
        }

        throw new HttpParseException("can not find method for name ["+name+"]");
    }

    public final static HttpMethod getByName(String name) throws HttpParseException
    {
        return getByName(name , true);
    }
}
