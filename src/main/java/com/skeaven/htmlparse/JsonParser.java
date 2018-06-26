package com.skeaven.htmlparse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class JsonParser implements HtmlParser {

    /**
     * 将返回的文本数据转换为JSONObject
     *
     * @param html
     * @return
     */
    public Object htmlParse(String html) {
        JSONObject object = JSON.parseObject(html);
        return object;
    }
}
