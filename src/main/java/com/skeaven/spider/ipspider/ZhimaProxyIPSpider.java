package com.skeaven.spider.ipspider;

import com.skeaven.ip.ProxyIP;
import com.skeaven.ip.evaluator.AvailableEvaluator;
import com.skeaven.ip.evaluator.QualityEvaluator;
import com.skeaven.httpclient.CustomHttpClient;
import com.skeaven.spider.ProxyIPSpider;

import java.util.ArrayList;
import java.util.List;

public class ZhimaProxyIPSpider extends ProxyIPSpider {
    public ZhimaProxyIPSpider() {
        this.setUrl("http://webapi.http.zhimacangku.com/getip?num=20&type=1&pro=&city=0&yys=0&port=1&pack=23329&ts=0&ys=0&cs=0&lb=1&sb=0&pb=4&mr=1&regions=");
        this.setIPEvaluator(new QualityEvaluator(new AvailableEvaluator("http://www.baidu.com")));
        this.setHttpClient(new CustomHttpClient());
    }

    @Override
    protected List<ProxyIP> parse(String html) {
        List<ProxyIP> proxyIPList = new ArrayList<>();

        String[] res = html.split("\r\n");
        for (String str : res) {
            ProxyIP proxyIP = new ProxyIP();
            String[] arr = str.split(":");
            proxyIP.setIp(arr[0]);
            proxyIP.setPort(Integer.valueOf(arr[1]));
            proxyIP.setType("http");
            proxyIP.setQuality(2);
            proxyIPList.add(proxyIP);
        }

        return proxyIPList;
    }
}
