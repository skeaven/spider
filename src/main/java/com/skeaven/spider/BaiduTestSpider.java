package com.skeaven.spider;

import com.skeaven.httpclient.CustomHttpClient;

public class BaiduTestSpider extends AbstractSpider {

    public BaiduTestSpider(){
        this.httpClient = new CustomHttpClient();
    }

    @Override
    public void run() {
        String url = "http://www.baidu.com";
        for (int i = 0; i < 100; i++) {
            String res = null;
            while (res == null) {
                res = httpClient.htmlGet(url, proxy);
            }
            System.out.println(Thread.currentThread().getName() + "\t" + res);
        }
    }
}
