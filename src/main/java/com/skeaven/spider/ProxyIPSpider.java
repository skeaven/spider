package com.skeaven.spider;

import com.skeaven.ip.ProxyIP;
import com.skeaven.ip.ProxyIPPool;
import com.skeaven.ip.evaluator.IPEvaluator;

import java.util.List;

public abstract class ProxyIPSpider extends AbstractSpider {

    //获取代理ip的网址
    protected String url;

    protected IPEvaluator evaluator;

    //代理ip池用来存储爬取的代理ip
    protected ProxyIPPool pool;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setIPEvaluator(IPEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public void run() {
        int j = 0;
        if (url == null || evaluator == null || pool == null || httpClient == null) {
            logger.warn("spider is not initialized!");
            return;
        }

        String html = httpClient.htmlGet(url, headers);
        if (html != null) {
            List<ProxyIP> ips = parse(html);
            for (int i = 0; i < ips.size(); i++) {
                ProxyIP proxyIp = ips.get(i);
                evaluator.evaluate(httpClient, proxyIp);
                if (proxyIp.isAvailable()) {
                    j++;
                    pool.addProxyIP(proxyIp);
                }
            }
        }
        logger.info("proxyIP spider 爬取完毕!本次爬取新增代理ip数量为{}", j);
    }

    /**
     * 解析代理ip网址返回的数据
     *
     * @param html 代理ip网址返回的数据
     * @return 返回解析后的ip列表
     */
    protected abstract List<ProxyIP> parse(String html);

    public ProxyIPSpider setPool(ProxyIPPool pool) {
        this.pool = pool;
        return this;
    }
}
