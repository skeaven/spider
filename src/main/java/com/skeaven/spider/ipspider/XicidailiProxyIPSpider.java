package com.skeaven.spider.ipspider;

import com.skeaven.ip.ProxyIP;
import com.skeaven.ip.evaluator.AvailableEvaluator;
import com.skeaven.ip.evaluator.QualityEvaluator;
import com.skeaven.httpclient.CustomHttpClient;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class XicidailiProxyIPSpider extends ProxyIPSpider {

    public XicidailiProxyIPSpider() {
        List<Header> list = new ArrayList<>();
        list.add(new BasicHeader("Host", "www.xicidaili.com"));
        list.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0"));
        list.add(new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
        list.add(new BasicHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2"));
        list.add(new BasicHeader("Accept-Encoding", "gzip, deflate"));
        list.add(new BasicHeader("Cookie", "_free_proxy_session=BAh7B0kiD3Nlc3Npb25faWQGOgZFVEkiJWU0M2RiNTgxZjZlZWNiMjQ3MzJhZDI1NzdkODFhMmEzBjsAVEkiEF9jc3JmX3Rva2VuBjsARkkiMXdMalFSbUxTb0tQNk9qT1lhbUhFYmlZV1Jud0c3NEprSkxBUWhiOFFWUjg9BjsARg%3D%3D--b39301ba60e27076c7c7ad9464a5e90b19affdbc; Hm_lvt_0cf76c77469e965d2957f0553e6ecf59=1529477732,1529488589,1529562955,1529562974; Hm_lpvt_0cf76c77469e965d2957f0553e6ecf59=1529564678"));
        list.add(new BasicHeader("DNT", "1"));
        list.add(new BasicHeader("Connection", "keep-alive"));
        list.add(new BasicHeader("Upgrade-Insecure-Requests", "1"));
        list.add(new BasicHeader("If-None-Match", "W/\"407b77a32764865c42113961aac67a3d\""));
        list.add(new BasicHeader("Cache-Control", "max-age=0, no-cache"));
        list.add(new BasicHeader("Pragma", "no-cache"));
        this.setHeaders(new Header[list.size()]);
        this.setUrl("http://www.xicidaili.com/wt/");
        this.setIPEvaluator(new QualityEvaluator(new AvailableEvaluator("http://www.baidu.com")));
        list.toArray(headers);
    }

    @Override
    public List<ProxyIP> parse(String html) {
        List<ProxyIP> proxyIps = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements trs = doc.select("#ip_list tr");
        int size = trs.size();
        for (int i = 1; i < size; i++) {
            String ip = trs.get(i).select("td").get(1).text();
            int port = Integer.valueOf(trs.get(i).select("td").get(2).text());
            String type = trs.get(i).select("td").get(5).text();
            ProxyIP proxyIp = new ProxyIP();
            proxyIp.setIp(ip);
            proxyIp.setPort(port);
            if (type == null || (!"http".equals(type) && !"https".equals(type))) {
                proxyIp.setType("http");
            } else {
                proxyIp.setType(type.toLowerCase());
            }
            proxyIp.setQuality(2);
            proxyIps.add(proxyIp);
        }
        return proxyIps;
    }

}
