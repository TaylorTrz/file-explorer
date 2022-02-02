package com.taylor.tooz.bytecode;

import com.taylor.tooz.utils.LogUtil;
import com.taylor.tooz.utils.format.DocFormatUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;


/**
 * Helper for me to read and comprehend byte-code
 *
 * @author taylor
 * @since 2022/2/2 10:00
 */
public class ReadHelper {
    private static final String CLASS_FILE = "out/production/file-explorer/com/taylor/tooz/Main.class";

    public void start() {
        InputStream iStream = getInputStream(CLASS_FILE);
        List<byte[]> sBuffer = readStream(iStream);
        String content = parseToHuman(sBuffer);
        LogUtil.info("\n" + content + "\n");
    }


    /**
     * @param fName
     * @return
     */
    private InputStream getInputStream(String fName) {
        InputStream iStream = null;
        try {
            File classFile = new File(CLASS_FILE);
            iStream = new BufferedInputStream(new FileInputStream(classFile));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return iStream;
    }


    /**
     * read byte from byte-code file(.class)
     *
     * @param iStream
     * @return
     */
    private List<byte[]> readStream(InputStream iStream) {
        List<byte[]> sBuffer = new ArrayList<>();
        byte[] bytes = new byte[8];
        int n = 0;
        try {
            while ((n = iStream.read(bytes)) != -1) {
                byte[] buffers = new byte[8];
                System.arraycopy(bytes, 0, buffers, 0, n);
                sBuffer.add(buffers);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return sBuffer;
    }


    /**
     * @param sBuffer
     * @return
     */
    private String parseToHuman(List<byte[]> sBuffer) {
        assert sBuffer != null;
        DocFormatUtil.DocBuilder builder = new DocFormatUtil().getBuilder();
        for (byte[] bytes : sBuffer) {
            builder.then(bin2Hex(bytes));
            builder.enter();
        }
        return builder.build();
    }


    private String bin2Hex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(Integer.toHexString(b & 0xFF | 0x100).toUpperCase().substring(1, 3));
        }
        return hex.toString();
    }


    public static void main(String[] args) {
        new ReadHelper().start();
    }
}
