package com.gdc.nms.setup.util.os.windows;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class WindowsUtil {

    private static String execProcess(String[] command) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(command);
        Process process = null;
        String str = null;
        try {
            process = pb.start();
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            InputStream inputStream;
            byte[] buf;
            if (process.exitValue() == 0) {
                inputStream = process.getInputStream();
                buf = new byte[inputStream.available()];
                inputStream.read(buf);
                str = new String(buf);
            } else {
                inputStream = process.getErrorStream();
                buf = new byte[inputStream.available()];
                inputStream.read(buf);
                process.destroy();
                throw new Exception("Exit Value: " + process.exitValue() + "\n"
                        + new String(buf, Charset.forName("UTF-8")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return str;
    }

    public static String readRegistry(String key, String keyName, String regType) throws Exception {
        String[] arrayOfString = { "reg", "query", key, "/v", keyName };
        String str = execProcess(arrayOfString);
        return str.split(regType)[1].trim();
    }

    private WindowsUtil() {
    }

    public static void main(String[] args) {
        String KEY = "HKLM\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders";
        try {
            String regKey = readRegistry(KEY, "Common Programs", "REG_SZ");
            System.out.println(regKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
