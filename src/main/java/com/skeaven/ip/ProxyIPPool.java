package com.skeaven.ip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ProxyIPPool {
    private static final Logger logger = LoggerFactory.getLogger(ProxyIPPool.class);

    //存放可用ip
    private Stack<ProxyIP> proxyIPStack;
    //存放使用中的ip
    private List<ProxyIP> proxyIpList;

    private long intervalTime = 10 * 1000;

    /**
     * 初始化代理ip池
     */
    public ProxyIPPool() {
        logger.info("初始化代理ip池...");
        proxyIPStack = new Stack<>();
        proxyIpList = new LinkedList<>();

        //加载本地缓存的代理ip
        ProxyIPUtils.loadLocalCachedProxyIP(this);
    }

    /**
     * 建议线程使用该方法获取ip为空时，等待1分钟后重新调用此方法
     * 从IP池中获取可用的ip
     *
     * @return 返回可用ip，如果无可用ip返回null;
     */
    public synchronized ProxyIP next() {
        int quality;
        ProxyIP proxyIP;
        do {
            if (proxyIPStack.empty()) {
                return null;
            } else {
                proxyIP = proxyIPStack.pop();
                quality = proxyIP.getQuality();
            }
        } while (quality > 4);

        long currentTime = new Date().getTime();
        proxyIP.setLastVisitTime(currentTime);
        proxyIpList.add(proxyIP);
        return proxyIP;
    }

    /**
     * 添加一个可用ip
     *
     * @param proxyIp 新增ip包装类
     */
    public void addProxyIP(ProxyIP proxyIp) {
        logger.info("代理ip池新增ip:{}", proxyIp.getIp());
        proxyIp.setIntervalTime(intervalTime);
        proxyIPStack.push(proxyIp);
    }

    public int getNum() {
        return proxyIpList.size() + proxyIPStack.size();
    }

    Stack<ProxyIP> getProxyIPStack() {
        return proxyIPStack;
    }

    List<ProxyIP> getProxyIpList() {
        return proxyIpList;
    }

    public synchronized void refresh() {

        logger.info("开始刷新代理池...");
        long currentTime = new Date().getTime();

        Iterator<ProxyIP> iterator = proxyIpList.iterator();
        while (iterator.hasNext()) {
            ProxyIP proxyIP = iterator.next();
            if ((currentTime - proxyIP.getLastVisitTime()) > proxyIP.getIntervalTime()) {
                addProxyIP(proxyIP);
                iterator.remove();
            }
        }
        logger.info("代理池刷新完毕!可用ip数量{},冷却中ip数量{},总计{}", proxyIPStack.size(), proxyIpList.size(), getNum());
    }
}

