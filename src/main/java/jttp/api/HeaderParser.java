package jttp.api;

public interface HeaderParser extends Reader {

    HttpHeader header();

    HeaderParser refresh();
}
