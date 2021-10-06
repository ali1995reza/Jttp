package jttp.standard;

import jttp.api.ElementByteParseEventListener;
import jttp.api.exception.HttpParseException;
import jttp.api.exception.RequestLineParseException;

import java.nio.ByteBuffer;

import static jttp.standard.HttpProtocolConstant.isCTL;
import static jttp.standard.HttpProtocolConstant.spaceCharacter;

public class ByteRouteParser extends ExceptionAdaptedElementParser {


    private ElementByteParseEventListener route = ElementByteParseEventListener.EMPTY;


    public ByteRouteParser setRoute(ElementByteParseEventListener route) {
        this.route = route == null ? ElementByteParseEventListener.EMPTY : route;
        return this;
    }

    private boolean validate(byte b) throws RequestLineParseException {
        boolean space = spaceCharacter(b);
        if (isCTL(b) && !space)
            throw new RequestLineParseException("CTL byte in route");

        if (space) {
            setElementParsed();
            return true;
        }

        return false;

    }

    @Override
    int doRead(byte[] buffer, int offset, int length) throws HttpParseException {
        int i = offset;
        for (; i < offset + length; i++) {
            if (validate(buffer[i])) {
                route.onElementData(buffer, offset, i - offset, true);
                i++;
                break;
            }
        }

        if (!isElementParsed()) {
            route.onElementData(buffer, offset, i - offset, false);
        }

        return i - offset;
    }

    @Override
    int doRead(ByteBuffer buffer) throws HttpParseException {
        return 0;
    }

    @Override
    void doRefresh() {

    }
}
