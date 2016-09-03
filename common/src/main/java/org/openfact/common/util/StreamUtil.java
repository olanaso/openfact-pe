package org.openfact.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class StreamUtil {

    private StreamUtil() {
    }

    public static String readString(InputStream in) throws IOException
    {
        char[] buffer = new char[1024];
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        int wasRead;
        do
        {
            wasRead = reader.read(buffer, 0, 1024);
            if (wasRead > 0)
            {
                builder.append(buffer, 0, wasRead);
            }
        }
        while (wasRead > -1);

        return builder.toString();
    }
}