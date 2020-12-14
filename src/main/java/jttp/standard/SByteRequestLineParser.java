package jttp.standard;

import jttp.api.ByteRequestLineParser;
import jttp.api.ElementByteParseEventListener;
import jttp.api.RequestLineParser;
import jttp.api.exception.HttpParseException;

import java.nio.ByteBuffer;

public class SByteRequestLineParser extends ExceptionAdaptedElementParser implements ByteRequestLineParser {


    private final ByteMethodParser methodParser = new ByteMethodParser();
    private final ByteRouteParser routeParser = new ByteRouteParser();
    private final ByteVersionParser versionParser = new ByteVersionParser();




    @Override
    public ByteRequestLineParser setMethodListener(ElementByteParseEventListener listener) {

        methodParser.setMethodListener(listener);

        return this;
    }

    @Override
    public ByteRequestLineParser setRouteListener(ElementByteParseEventListener listener) {

        routeParser.setRoute(listener);

        return this;
    }

    @Override
    public ByteRequestLineParser setVersionListener(ElementByteParseEventListener listener) {

        versionParser.setVersion(listener);

        return this;
    }

    @Override
    int doRead(byte[] buffer, int offset, int length) throws HttpParseException {

        int read = 0;

        while (read<length && !versionParser.isElementParsed())
        {
            if(!methodParser.isElementParsed())
            {
                read += methodParser.read(buffer , offset+read , length-read);

            }else if(!routeParser.isElementParsed())
            {
                read += routeParser.read(buffer , offset+read , length-read);

            }else if(!versionParser.isElementParsed())
            {
                read += versionParser.read(buffer , offset+read , length-read);

            }
        }

        if(versionParser.isElementParsed())
        {
            setElementParsed();
        }

        return read;
    }

    @Override
    int doRead(ByteBuffer buffer) throws HttpParseException {
        return 0;
    }

    @Override
    void doRefresh() {
        methodParser.refresh();
        routeParser.refresh();
        versionParser.refresh();
    }
}
