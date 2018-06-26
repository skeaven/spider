package com.skeaven.ip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Iterator;

public class ProxyIPUtils {
    private static final Logger logger = LoggerFactory.getLogger(ProxyIPPool.class);

    public static void loadLocalCachedProxyIP(ProxyIPPool pool) {
        try {
            File file = new File("proxy_ip.bcp");
            if (!file.exists()) {
                file.createNewFile();
            } else {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] arr = line.split("\t");
                    ProxyIP proxyIP = new ProxyIP();
                    proxyIP.setIp(arr[0]);
                    proxyIP.setPort(Integer.valueOf(arr[1]));
                    proxyIP.setType(arr[2]);
                    pool.addProxyIP(proxyIP);
                }
            }
        } catch (Exception e) {
            logger.warn("本地缓存代理ip加载失败!");
        }
    }

    public static void saveLocalCachedProxyIP(ProxyIPPool pool) {
        logger.info("保存ip代理池到本地缓存...");
        File file = new File("proxy_ip.bcp");
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new IOException("文件创建失败!");
                }
            } catch (IOException e) {
                logger.warn("本地缓存代理ip文件创建失败!", e);
                return;
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (ProxyIP proxyIP : pool.getProxyIpList()) {
                writer.append(proxyIP.toString());
                writer.newLine();
            }

            Iterator<ProxyIP> iterator = pool.getProxyIPStack().iterator();
            while (iterator.hasNext()){
                writer.append(iterator.next().toString());
                writer.newLine();
            }

            writer.flush();
            logger.info("ip代理池保存完成!");
        } catch (Exception e) {
            logger.warn("本地缓存代理ip保存失败!");
        }
    }
}
