package com.skeaven.spider;

import com.skeaven.ip.ProxyIPPool;
import com.skeaven.httpclient.CustomHttpClient;
import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSpider implements Runnable {
    protected Logger logger = LoggerFactory.getLogger(ProxyIPPool.class);

    //是否启用代理
    protected boolean proxy = false;

    //自定义的http httpclient
    protected CustomHttpClient httpClient;

    //爬取网址需要的头文件
    protected Header[] headers;

    /**
     * 开启代理
     *
     * @return 返回自己
     */
    public AbstractSpider openProxy(ProxyIPPool proxyIPPool) {
        proxy = true;
        httpClient.setProxyIPPool(proxyIPPool);
        return this;
    }

    /**
     * 关闭代理
     */
    public void closeProxy() {
        proxy = false;
        httpClient.setProxyIPPool(null);
    }

    protected void setHttpClient(CustomHttpClient client) {
        this.httpClient = client;
    }

    //限制仅子类在构造时可以设置
    protected void setHeaders(Header[] headers) {
        this.headers = headers;
    }
}
