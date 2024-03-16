package com.zhanghc.main;

import com.zhanghc.server.EchoServer;

public class EchoServerMain {

    public static void main(String[] args) throws Exception {
        new EchoServer(9002).start();
    }
}
