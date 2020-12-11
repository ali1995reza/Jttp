package jttp.api;

public interface ContentParser {

    ContentParser withTransferEncoding(TransferEncoding transferEncoding);

    ContentParser fixedSize(long l);

    ContentByteType read(byte b);
}
