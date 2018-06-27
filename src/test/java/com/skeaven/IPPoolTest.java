package com.skeaven;

import com.skeaven.ip.ProxyIPPool;
import com.skeaven.timertask.ProxyIPPoolMonitor;
import com.skeaven.spider.BaiduTestSpider;
import com.skeaven.spider.ipspider.XicidailiProxyIPSpider;

public class IPPoolTest {
    public static void main(String[] args) {
        ProxyIPPool pool = new ProxyIPPool();
        ProxyIPPoolMonitor monitor = new ProxyIPPoolMonitor();
        monitor.startMonitor(pool, new XicidailiProxyIPSpider().setPool(pool));

        Thread thread1 = new Thread(new BaiduTestSpider().openProxy(pool), "baidu spider1");
        thread1.start();
        Thread thread2 = new Thread(new BaiduTestSpider().openProxy(pool), "baidu spider2");
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            System.out.println("爬虫线程异常中断~");
        } finally {
            monitor.stopMonitor();
        }
    }
}
