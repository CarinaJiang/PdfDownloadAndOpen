package com.elvis.pdfreader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpDownloader {
    private URL url = null;
    FileUtils fileUtils = new FileUtils();

    public int downfile(String urlStr, String path, String fileName) {
        if (fileUtils.isFileExist(path + fileName)) {
            return 1;
        } else {
            try {
                InputStream input = null;
                // ����һ��URL����urlStrָ��������IP��ַ
                url = new URL(urlStr);
               // ����һ��HttpURLConnection����
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();

                input = urlConn.getInputStream();
                File resultFile = fileUtils.write2SDFromINput(path, fileName, input);
                if (resultFile == null) {
                    return -1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    //���ڵõ�һ��InputStream�����������ļ�����ǰ����Ĳ��������Խ����������װ����һ������
    public InputStream getInputStream(String urlStr) throws IOException {
        InputStream is = null;
        try {
            url = new URL(urlStr);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("GET");
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setConnectTimeout(50000);
            urlConn.setReadTimeout(50000);


            //ʵ������
            urlConn.connect();
            if (urlConn.getResponseCode() == 200) {

                is = urlConn.getInputStream();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return is;
    }


}















