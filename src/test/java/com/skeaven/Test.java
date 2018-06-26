package com.skeaven;

import com.skeaven.ip.ProxyIP;
import com.skeaven.spider.CustomHttpClient;

public class Test {
    public static void main(String[] args) {
        CustomHttpClient client = new CustomHttpClient();
//        for (int i = 0; i < 10; i++) {
//            System.out.println(client.htmlGet("https://www.google.com"));
//        }


        for (int i = 0; i < 100; i++) {
            ProxyIP proxyIP = new ProxyIP();
            proxyIP.setIp("101.236.60.8");
            proxyIP.setPort(8866);
            System.out.println(client.htmlGet("https://www.baidu.com", proxyIP, null));
        }
    }
}
