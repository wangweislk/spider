package com.bbd.download;

import com.bbd.domain.Page;
import com.bbd.utils.PageUtils;

/**
 * Created by bbd on 2016/9/19.
 */
public class HttpClientDownloadImpl implements Downloadable{
    public Page download(String url) {
        Page page = new Page();
        String content = PageUtils.getContent(url);
        page.setContent(content);
        page.setUrl(url);
        return page;
    }
}
