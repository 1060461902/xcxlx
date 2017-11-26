package com.e.support.util;

import org.junit.Test;

/**
 * Created by asus on 2017/11/26.
 */
public class PayUtilTest {
    @Test
    public void getPaySign() throws Exception {
        System.out.println(PayUtil.getPaySign("wxd678efh567hg6787",
                "5K8264ILTKCH16CQ2502SI8ZNMTM67VS",
                "wx2017033010242291fcfe0db70013231072",
                "1490840662"));
    }

}