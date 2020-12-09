package jttp.api;

public interface ContentParser extends Reader {

    ContentParser withTransferEncoding(TransferEncoding transferEncoding);

    ContentParser fixedSize(long l);

    Content lastPart();
}
