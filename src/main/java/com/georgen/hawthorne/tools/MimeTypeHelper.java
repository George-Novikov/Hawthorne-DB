package com.georgen.hawthorne.tools;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

public class MimeTypeHelper {
    public static String getType(byte[] data) throws IOException {
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(data);
        InputStream inStream = new BufferedInputStream(byteInputStream);
        return URLConnection.guessContentTypeFromStream(inStream);
    }
}
