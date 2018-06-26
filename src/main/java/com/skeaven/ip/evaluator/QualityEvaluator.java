package com.skeaven.ip.evaluator;

import com.skeaven.ip.ProxyIP;
import com.skeaven.spider.CustomHttpClient;

public class QualityEvaluator implements IPEvaluator {
    private IPEvaluator evaluator;

    public QualityEvaluator(IPEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public void evaluate(CustomHttpClient httpClient, ProxyIP proxyIp) {
        evaluator.evaluate(httpClient, proxyIp);
        proxyIp.setQuality(2);
    }

}
