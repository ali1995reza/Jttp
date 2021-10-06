package jttp.standard;

import jttp.api.ContentByteType;
import jttp.api.ContentParser;
import jttp.api.TransferEncoding;

public class SContentParser implements ContentParser {

    private TransferEncoding encoding;
    private long fixedSize;

    @Override
    public ContentParser withTransferEncoding(TransferEncoding transferEncoding) {
        this.encoding = transferEncoding;
        return this;
    }

    @Override
    public ContentParser fixedSize(long l) {
        this.fixedSize = l;
        return this;
    }

    @Override
    public ContentByteType read(byte b) {
        return null;
    }
}
