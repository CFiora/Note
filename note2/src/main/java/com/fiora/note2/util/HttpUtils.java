package com.fiora.note2.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.*;

public class HttpUtils {

    public static <T> T httpGetRequest(String url, Map<String, String> paramMap, Map<String, String> headers, String charSet, Class<T> clazz) {
        JSON json = httpGetRequest(url, paramMap, headers, charSet);
        if(json == null) {
            return null;
        }
        return json.toJavaObject(clazz);
    }

    public static JSON httpGetRequest(String url, Map<String, String> paramMap, Map<String, String> headers, String charSet) {
        String content = httpGetRequestStr(url, paramMap, headers, charSet);
        JSONObject json = JSON.parseObject(content);
        return json;
    }

    public static String httpGetRequestStr(String url, Map<String, String> paramMap, Map<String, String> headers, String charSet) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (paramMap != null && paramMap.size() > 0) {
                for (String paramName : paramMap.keySet()) {
                    uriBuilder.setParameter(paramName, paramMap.get(paramName));
                }
            }
            HttpGet get = new HttpGet(uriBuilder.build());

            if (headers != null && headers.size() > 0) {
                for (String headerName : headers.keySet()) {
                    get.addHeader(headerName, headers.get(headerName));
                }
            }
            CloseableHttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                String content = null;
                if (charSet == null || charSet.trim().isEmpty()) {
                    content = EntityUtils.toString(entity, "UTF-8");
                } else {
                    content = EntityUtils.toString(entity,charSet);
                }
                return content;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String httpPostRequest(String url, Map<String, String> headers, String json) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        if (headers != null && headers.size() > 0) {
            for (String headerName : headers.keySet()) {
                post.addHeader(headerName, headers.get(headerName));
            }
        }
        try {
            HttpEntity requestEntity = new StringEntity(json, "application/json", "utf-8");
            post.setEntity(requestEntity);
            CloseableHttpResponse response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                return EntityUtils.toString(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String httpPostRequest(String url, Map<String, String> headers, Map<String, String> params, String charSet) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        if (headers != null && headers.size() > 0) {
            for (String headerName : headers.keySet()) {
                post.addHeader(headerName, headers.get(headerName));
            }
        }
        try {
            if (params != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for(String key: params.keySet()){
                    paramList.add(new BasicNameValuePair(key, params.get(key)));
                }
                UrlEncodedFormEntity requestEntity = null;
                if (charSet == null || charSet.trim().isEmpty()) {
                    requestEntity = new UrlEncodedFormEntity(paramList, "UTF-8");
                } else {
                    requestEntity = new UrlEncodedFormEntity(paramList, charSet);
                }
                post.setEntity(requestEntity);
            }
            CloseableHttpResponse response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                String content = null;
                if (charSet == null || charSet.trim().isEmpty()) {
                    content = EntityUtils.toString(entity, "UTF-8");
                } else {
                    content = EntityUtils.toString(entity,charSet);
                }
                return content;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String imageUrl2Base64(String imgUrl, Map<String, String> headers) {
        InputStream is = null;
        URL url = null;
        HttpURLConnection conn = null;
        try {
            url = new URL(imgUrl);
            conn = (HttpURLConnection) url.openConnection();
            if (headers != null && headers.size() > 0) {
                for (String headerName : headers.keySet()) {
                    conn.setRequestProperty(headerName, headers.get(headerName));
                }
            }
            conn.connect();
            is = conn.getInputStream();
            String base64 = stream2Base64Str(is);
            return base64;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    public static String stream2Base64Str(InputStream is) {
        try (ByteArrayOutputStream outStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            return Base64.getEncoder().encodeToString(outStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean downloadImageWithHeaders(String url,File localFile, Map<String, String> headers) {
        boolean isSuccess = false;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            HttpGet get = new HttpGet(url);
            if (headers != null && headers.size() > 0) {
                for (String headerName : headers.keySet()) {
                    get.addHeader(headerName, headers.get(headerName));
                }
            }
            response = httpClient.execute(get);
            if (response!= null && response.getEntity() != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                OutputStream os = new FileOutputStream(localFile);
                response.getEntity().writeTo(os);
                isSuccess = true;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isSuccess;
    }


    public static boolean downloadImageWithHeaders(String imageUrl, String formatName, File localFile, Map<String, String> headers) {
        boolean isSuccess = false;
        InputStream stream = null;
        try {
            URL url = new URL(imageUrl);
            URLConnection conn = url.openConnection();
            if (headers != null && !headers.isEmpty()) {
                //设置头信息
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            conn.setDoInput(true);
            stream = conn.getInputStream();
            BufferedImage bufferedImg = ImageIO.read(stream);
            if (bufferedImg != null) {
                isSuccess = ImageIO.write(bufferedImg, formatName, localFile);
            } else {
                throw new RuntimeException("图片[" + imageUrl + "]下载失败");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isSuccess;
    }

    public static void useHttpConfig() {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(1000) // max time to create a connection, ms
            .setConnectionRequestTimeout(500) // max time to get a connection, ms
            .setSocketTimeout(10 * 1000) // max time to transfer data
            .build();
        HttpGet httpGet = new HttpGet();
        httpGet.setConfig(config);
        System.out.println(httpGet);
    }

    public static void usePoolingConnectionManager() {
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(manager).build();
        System.out.println(client);
    }

    public static void main(String[] args) {
        test4();
    }

    private static void test4() {
        String url = "https://search.51job.com/list/010000,000000,0000,01%252c32,9,99,Java,2,1.html?lang=c&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&ord_field=0&dibiaoid=0&line=&welfare=";
        String html = httpGetRequestStr(url, null, null, "gbk");
        int indexOfResult = html.indexOf("window.__SEARCH_RESULT__");
        String subHtml = html.substring(indexOfResult);
        String jsonStr = subHtml.substring(0, subHtml.indexOf("</script>")).replace("window.__SEARCH_RESULT__ = ", "");
        System.out.println(jsonStr);
        JSONObject json = JSONObject.parseObject(jsonStr);
        System.out.println(json.getString("total_page"));
    }

    public static void test3() {
        String url = "http://api.longfor.uat/smartpassiservice-uat/SmartPassIService/events/face-imags";
        Map<String, String> headers = new HashMap<String, String>() {{
            put("X-Gaia-API-Key","effaedbc-8c52-4568-b1cb-8ea7abb01a0e");
            put("app-name","canteen");
            put("app-key","86b1cad7-6b42-42e9-b515-c08f8e002028");
        }};
        Map<String, String> params = new HashMap<String, String>() {{
            put("mode","0");
            put("page","1");
            put("startTime","2021-02-26 00:00:00");
            put("endTime","2021-02-27 00:00:00");
        }};
        String response = httpGetRequestStr(url, params, headers, null);
        System.out.println(response);
    }

    public static void test2() {
        String url = "http://www.itcast.cn";
        String charSet = "UTF-8";
        String response = httpGetRequestStr(url, null, null, charSet);
        System.out.println(response.length());
    }

    public static void test1() {
        String url = "http://api.longfor.uat/smartpassiservice-uat/SmartPassIService/events/download-face-imags?workNo=mafan&expired=1617019158190&signature=thoPoMR91OGueEM9J3Zm48rWf1Y%3D";
        Map<String, String> headers = new HashMap<String, String>() {{
            put("X-Gaia-API-Key","effaedbc-8c52-4568-b1cb-8ea7abb01a0e");
            put("app-name","canteen");
            put("app-key","86b1cad7-6b42-42e9-b515-c08f8e002028");
        }};
        String filePath = "D:/test.png";
        System.out.println(downloadImageWithHeaders(url, "png", new File(filePath), headers));
    }
}
