package team17.sheet03;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by adanek on 07.04.2016.
 */
public class ServiceAnnouncer extends Thread {

    private final String serviceName;
    private final String ipAdress;
    private final int port;

    private DatagramSocket socket;
    private Thread shutdownhook;

    public ServiceAnnouncer(String serviceName, String ipAddress, int port) {
        this.serviceName = serviceName;
        this.ipAdress = ipAddress;
        this.port = port;
    }

    public void StartService() {

       shutdownhook = new Thread() {
            public void run() {
                System.out.println("Shutdown request received");

                if (!socket.isClosed()) {
                    socket.close();
                }

                try {
                    this.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runtime.getRuntime().addShutdownHook(shutdownhook);

        this.start();
    }

    public void run() {

        try {
            socket = new DatagramSocket(7000);
            DatagramPacket packet;

            while (true) {

                byte[] buf = new byte[256];
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Service request received for service: " + received);

                if(received.equals(serviceName)){
                    String resp = String.format("%s:%d", ipAdress, port);
                    buf = resp.getBytes();
                    InetAddress address = packet.getAddress();
                    int port = packet.getPort();
                    packet = new DatagramPacket(buf, 0, buf.length, address, port);

                    System.out.printf("Sending response (%s) to %s on port %d\n",resp, address.toString(), port);
                    socket.send(packet);
                }
            }

        } catch (SocketException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void StopService() throws InterruptedException {

        System.out.println("Stop service request received");
        Runtime.getRuntime().removeShutdownHook(shutdownhook);
        Thread.sleep(500);

        if (!socket.isClosed()) {
            socket.close();
        }

        try {
            this.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}