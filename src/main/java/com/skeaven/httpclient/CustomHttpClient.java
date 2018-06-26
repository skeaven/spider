package com.skeaven.httpclient;

import com.skeaven.ip.ProxyIP;
import com.skeaven.ip.ProxyIPPool;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;


/**
 * HttpClient
 *
 * @author rchen
 */
public class CustomHttpClient {
    private static Logger logger = LoggerFactory.getLogger(CustomHttpClient.class); // 日志记录
    private int timeout = 2000;
    private CloseableHttpClient httpclient = HttpClients.createDefault();

    //设置代理
    private RequestConfig.Builder configBuilder = RequestConfig.custom()
            .setSocketTimeout(timeout).setConnectTimeout(timeout); //设置连接超时时间


    private ProxyIPPool proxyIPPool;

    /**
     * 获取当前网址的返回结果
     * 普通访问
     * 不包含header
     *
     * @param url 指定的url
     * @return 返回此url的返回结果
     */
    public String htmlGet(String url) {
        return htmlGet(url, false, null);
    }

    /**
     * 获取当前网址的返回结果
     * 普通访问
     * 包含header
     *
     * @param url     指定url
     * @param headers http头信息
     * @return 返回次url的返回结果
     */
    public String htmlGet(String url, Header[] headers) {
        return htmlGet(url, false, headers);
    }

    /**
     * 获取当前网址的返回结果
     * 使用代理访问
     * 不包含header
     *
     * @param url       指定url
     * @param openProxy 启用代理
     * @return 返回次url的返回结果
     */
    public String htmlGet(String url, boolean openProxy) {
        return htmlGet(url, openProxy, null);
    }

    /**
     * 获取当前网址的返回结果
     * 使用代理访问
     * 不包含header
     *
     * @param url     指定url
     * @param proxyIP 指定代理ip
     * @param headers http头信息
     * @return 返回次url的返回结果
     */
    public String htmlGet(String url, ProxyIP proxyIP, Header[] headers) {
        if (proxyIP == null) {
            logger.warn("指定代理ip为空!");
            return null;
        }
        HttpHost proxy = new HttpHost(proxyIP.getIp(), proxyIP.getPort(), "http");
        configBuilder.setProxy(proxy);

        HttpGet request = new HttpGet();
        request.setURI(URI.create(url));
        request.setConfig(configBuilder.build());
        if (headers != null) {
            request.setHeaders(headers);
        }

        request.setHeader("Content-Type", "text/html; charset=UTF-8");

        try {
            CloseableHttpResponse response = httpclient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, "UTF-8");
            } else {
                logger.warn("服务器未正确返回!");
            }
            proxyIP.setQuality((proxyIP.getQuality() - 1 > 0) ? proxyIP.getQuality() - 1 : 1);
            return null;
        } catch (ConnectTimeoutException e) {
            logger.error("请求超时!");
        } catch (IOException e) {
            logger.error("请求异常!");
        } finally {
            request.releaseConnection();
        }
        proxyIP.setQuality(proxyIP.getQuality() + 1);
        return null;
    }

    /**
     * 获取当前网址的返回结果
     * 使用代理访问
     * 包含header
     *
     * @param url       指定url
     * @param openProxy 开启代理
     * @param headers   http头信息
     * @return 返回次url的返回结果
     */
    public String htmlGet(String url, boolean openProxy, Header[] headers) {
        ProxyIP proxyIP = null;
        if (openProxy && proxyIPPool != null) {

            while (proxyIP == null) {
                proxyIP = proxyIPPool.next();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    logger.error("线程{}异常中断!", Thread.currentThread().getName());
                }
            }
            logger.info("使用代理ip {}:{} 访问 {}", proxyIP.getIp(), proxyIP.getPort(), url);
            HttpHost proxy = new HttpHost(proxyIP.getIp(), proxyIP.getPort(), "http");
            configBuilder.setProxy(proxy);
        } else {
            logger.info("未使用代理访问 {}", url);
            configBuilder.setProxy(null);
        }


        HttpGet request = new HttpGet();
        request.setURI(URI.create(url));
        request.setConfig(configBuilder.build());
        if (headers != null) {
            request.setHeaders(headers);
        }
        request.setHeader("Content-Type", "text/html; charset=UTF-8");

        try {
            CloseableHttpResponse response = httpclient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, "UTF-8");
            } else {
                logger.warn("服务器未正确返回!");
            }
            if (proxyIP != null) {
                proxyIP.setQuality((proxyIP.getQuality() - 1 > 0) ? proxyIP.getQuality() - 1 : 1);
            }
            return null;
        } catch (ConnectTimeoutException e) {
            logger.error("请求超时!");
        } catch (IOException e) {
            logger.error("请求异常!");
        } finally {
            request.releaseConnection();
        }
        if (proxyIP != null) {
            proxyIP.setQuality(proxyIP.getQuality() + 1);
        }
        return null;
    }

    public void setProxyIPPool(ProxyIPPool proxyIPPool) {
        this.proxyIPPool = proxyIPPool;
    }
}