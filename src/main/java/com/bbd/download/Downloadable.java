package com.bbd.download;

import com.bbd.domain.Page;

/**
 * 下载接口
 * Created by bbd on 2016/9/19.
 */
public interface Downloadable {

    public Page download(String url);

}
