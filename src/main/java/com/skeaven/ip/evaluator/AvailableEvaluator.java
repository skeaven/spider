package com.skeaven.ip.evaluator;

import com.skeaven.spider.CustomHttpClient;
import com.skeaven.ip.ProxyIP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AvailableEvaluator implements IPEvaluator {
    private static final Logger logger = LoggerFactory.getLogger(AvailableEvaluator.class);
    private String targetUrl;

    public AvailableEvaluator(String targetUrl) {
        this.targetUrl = targetUrl;
    }


    public void evaluate(CustomHttpClient httpClient, ProxyIP proxyIp) {
        for (int i = 1; i > 0; i--) {
            if (httpClient.htmlGet(targetUrl, proxyIp, null) != null) {
                logger.info(proxyIp.getIp() + ":" + proxyIp.getPort()+"测试通过");
                proxyIp.setAvailable(true);
                return;
            }
        }
        proxyIp.setAvailable(false);
    }
}
