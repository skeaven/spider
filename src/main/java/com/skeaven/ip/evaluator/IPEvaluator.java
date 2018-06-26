package com.skeaven.ip.evaluator;

import com.skeaven.ip.ProxyIP;
import com.skeaven.httpclient.CustomHttpClient;

public interface IPEvaluator {

    void evaluate(CustomHttpClient httpClient, ProxyIP proxyIp);
}
