package com.lmz;

import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class RealIpUtils {
    public static void main(String[] args) {
//        getDeviceRealIp();
        InetAddress localHostExactAddress = getLocalHostExactAddress();
        System.out.println(localHostExactAddress.getHostAddress());

    }

    private static void getDeviceRealIp() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if (networkInterface.isUp() && !networkInterface.isLoopback()) {
                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = inetAddresses.nextElement();
                        if (inetAddress instanceof Inet4Address) {
                            //if  configIpList.contains(inetAddress.getHostAddress())
                            System.out.println("Real IP Address: " + inetAddress.getHostAddress());
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
    }

    public static InetAddress getLocalHostExactAddress() {
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.connect(InetAddress.getByName("10.255.255.255"), 1);
            InetAddress address = socket.getLocalAddress();
            socket.close();
            return address;
        } catch (UnknownHostException | SocketException e) {
            try {
                return InetAddress.getLocalHost();
            } catch (UnknownHostException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
