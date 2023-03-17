package com.lmzz.system.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketBIO {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(9999, 20);

        while (true) {
            Socket client = server.accept();
            System.out.println("client:" + client.getRemoteSocketAddress());

            new Thread(() -> {

                InputStream in = null;
                try {
                    in = client.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                    while (true) {
                        String s = bufferedReader.readLine();
                        if (s != null) {
                            System.out.println(s);
                        } else {
                            System.out.println("close");
                            client.close();
                            break;
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        assert in != null;
                        in.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }
    }

}
