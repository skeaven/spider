package com.skeaven.htmlparse;

import org.apache.http.HttpEntity;

public interface HtmlParser {
    Object htmlParse(String html);
}
