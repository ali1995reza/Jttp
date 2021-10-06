import jttp.standard.SHeaderParser;
import jttp.standard.SRequestLineParser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPOutputStream;

public class Test {

    private static byte[] GZIP;

    private final static byte[] gzip(byte[] data, int off, int len) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(stream);
        gzipOutputStream.write(data, off, len);
        gzipOutputStream.finish();
        gzipOutputStream.close();
        return stream.toByteArray();
    }

    private final static byte[] gzip(String fName) throws IOException {
        FileInputStream fileInputStream =
                new FileInputStream(
                        new File(fName)
                );

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(stream);


        byte[] chunk = new byte[200000];

        while (true) {
            int rf = fileInputStream
                    .read(chunk);
            if (rf < 0)
                break;

            gzipOutputStream.write(chunk, 0, rf);
        }


        gzipOutputStream.finish();
        return stream.toByteArray();
    }

    public static void main(String[] args) throws Exception {
        GZIP = gzip("D:\\Download\\Video\\Be_Vaghte_Sham_720p_HC.mp4");
        System.out.println("GZIPPED : " + GZIP.length);

        ExecutorService service = Executors.newFixedThreadPool(100);

        ServerSocket serverSocket = new ServerSocket();

        serverSocket.bind(new InetSocketAddress(5050));

        while (true) {
            Socket socket = serverSocket.accept();
            service.execute(
                    new HttpSocketReader(socket)
            );
        }
    }

    private final static class HttpSocketReader implements Runnable {

        private final Socket socket;

        private HttpSocketReader(Socket socket) {
            this.socket = socket;
        }

        private final static void handle(Socket socket) throws Throwable {

            final SRequestLineParser requestLineParser = new SRequestLineParser(
                    ByteBuffer.allocate(200)
            );

            final SHeaderParser headerParser = new SHeaderParser(
                    ByteBuffer.allocate(500)
            );

            boolean l = false;

            byte[] data = new byte[1000];

            long startTime = -1;

            while (true) {
                int read = socket.getInputStream()
                        .read(data);

                if (read < 0)
                    throw new IOException("EOS");

                if (startTime < 0)
                    startTime = System.currentTimeMillis();

                int offset = 0;
                if (!l) {
                    int r = requestLineParser
                            .read(data, 0, read);

                    offset += r;

                    if (requestLineParser.isElementParsed()) {
                        System.out.println(
                                "REQ-LINE : " + requestLineParser.line()
                        );

                        if (!requestLineParser.line().route().equals("/")) {
                            socket.close();
                            return;
                        }
                        l = true;
                    }

                }

                while (offset < read) {
                    int r = headerParser.read(data, offset, read - offset);
                    offset += r;
                    if (headerParser.isElementParsed()) {
                        if (headerParser.isElementCRLF()) {
                            System.out.println(
                                    "TIME : " + (System.currentTimeMillis() - startTime)
                            );
                            System.out.println("END OF HEADERS !");


                            ByteArrayOutputStream arr = new ByteArrayOutputStream();
                            GZIPOutputStream stream =
                                    new GZIPOutputStream(arr);

                            byte[] b = "HEllllllo WOlrd babe".getBytes();
                            stream.write(b);
                            stream.finish();

                            byte[] gzipData = arr.toByteArray();


                            socket.getOutputStream()
                                    .write(
                                            new StringBuffer()
                                                    .append(
                                                            "HTTP/1.1 200 OK\r\n"
                                                    ).append(
                                                            "Content-Type: video/mp4\r\n"
                                                    ).append(
                                                            "Content-Length: " + GZIP.length + "\r\n"
                                                    ).append(
                                                            "Content-Encoding: gzip\r\n"
                                                    ).append("\r\n").toString()
                                                    .getBytes()
                                    );




                            /*int write = 0;

                            System.out.println("LEN IS : "+GZIP.length);
                            while (write<GZIP.length)
                            {

                                int len = Math.min(20000 , GZIP.length-write);
                                socket.getOutputStream().write((Integer.toHexString(len)+"\r\n").getBytes());


                                socket.getOutputStream().write(GZIP , write , len);
                                socket.getOutputStream().write("\r\n".getBytes());
                                System.out.println("WROTE : "+len);

                                write+=len;
                            }
                            socket.getOutputStream().write("0\r\n\r\n".getBytes());*/
                            socket.getOutputStream().write(GZIP);
                            socket.getOutputStream().write("\r\n".getBytes());
                            socket.getOutputStream().close();
                            return;
                        } else {
                            System.out.println(
                                    "HEADER : " + headerParser.header()
                            );
                        }
                        headerParser.refresh();
                    }
                }
            }


        }

        @Override
        public void run() {
            try {
                handle(socket);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }


    }

    private final static class CharSequenceCLS implements CharSequence {

        @Override
        public int length() {
            return 0;
        }

        @Override
        public char charAt(int index) {
            return 0;
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return null;
        }
    }
}
