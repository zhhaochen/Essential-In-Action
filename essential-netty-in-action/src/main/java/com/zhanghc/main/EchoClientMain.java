package com.zhanghc.main;

import com.zhanghc.client.EchoClient;

public class EchoClientMain {

    public static void main(String[] args) throws Exception {
        new EchoClient("0.0.0.0", 9002).start();
    }
}
