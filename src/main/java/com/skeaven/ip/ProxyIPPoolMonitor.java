package com.skeaven.ip;

import com.skeaven.spider.ProxyIPSpider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

public class ProxyIPPoolMonitor extends TimerTask {
    private static final Logger logger = LoggerFactory.getLogger(ProxyIPPoolMonitor.class);

    private static final int threshold = 30;
    private ProxyIPPool pool;
    private Timer ipSpiderTimer;
    private ProxyIPSpider proxyIPSpider;
    private Thread getProxyIPThread;

    /**
     * 过滤当前代理ip池的失效ip，当代理ip池的总代理数量小于阈值则启动ip爬取程序
     */
    private void check() {
        pool.refresh();
        if ((pool.getNum() < threshold) && (getProxyIPThread == null || !getProxyIPThread.isAlive())) {
            logger.warn("当前代理ip池可用ip数量为{},过少,开始爬取可用代理ip...", pool.getNum());
            getProxyIPThread = new Thread(proxyIPSpider, "getProxyIPThread");
            getProxyIPThread.start();
        }
    }

    @Override
    public void run() {
        this.check();
    }

    public void startMonitor(ProxyIPPool pool, ProxyIPSpider proxyIPSpider) {
        this.pool = pool;
        this.proxyIPSpider = proxyIPSpider;
        ipSpiderTimer = new Timer("ProxyIPPoolMonitor");
        ipSpiderTimer.schedule(this, 0, 20 * 1000);
        logger.info("开启代理ip池监控...");
    }

    public void stopMonitor() {

        if (getProxyIPThread != null && getProxyIPThread.isAlive()) {
            logger.info("正在关闭{}线程...", getProxyIPThread.getName());
            getProxyIPThread.stop();
            logger.info("{}线程已关闭!", getProxyIPThread.getName());
        }

        if (this.ipSpiderTimer != null) {
            logger.info("正在关闭代理ip池监控线程...");
            this.ipSpiderTimer.cancel();
            logger.info("代理ip池监控线程已关闭!");
        }

        ProxyIPUtils.saveLocalCachedProxyIP(pool);
    }

}
