package com.bbd.store;

import com.bbd.domain.Page;
import com.sun.glass.ui.SystemClipboard;

/**
 * Created by bbd on 2016/9/20.
 */
public class ConsoleStoreImpl implements Storeable{
    public void stroe(Page page) {

        System.out.println(page.getValues().get("title")+"----"+page.getValues().get("price"));
    }
}
