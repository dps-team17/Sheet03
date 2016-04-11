package team17.sheet03.ServiceLocatorService;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ServiceAnnouncer {

    private final int DEFAULT_PORT = 7000;
    private boolean debug = false;
    private int announcerPort = DEFAULT_PORT;

    private final String serviceName;
    private final String ipAdress;
    private final int port;

    private DatagramSocket socket;
    private Thread serviceThread;
    private Thread shutdownHook;

    public ServiceAnnouncer(String serviceName, String ipAddress, int port) {
        this.serviceName = serviceName;
        this.ipAdress = ipAddress;
        this.port = port;
    }

    public void StartService() {

        shutdownHook = new Thread() {
            public void run() {

                try {
                    if (debug) System.out.println("Service announcer: Shutdown request received");

                    // Stop the service
                    if (!socket.isClosed()) {
                        socket.close();
                    }

                    // Wait until service shutdown complete
                    serviceThread.join(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Runtime.getRuntime().addShutdownHook(shutdownHook);

        serviceThread = new Thread() {
            public void run() {
                try {
                    socket = new DatagramSocket(DEFAULT_PORT);
                    DatagramPacket packet;

                    if (debug) System.out.println("Service announcer: Started");

                    while (true) {

                        byte[] buf = new byte[256];
                        packet = new DatagramPacket(buf, buf.length);
                        socket.receive(packet);

                        String received = new String(packet.getData(), 0, packet.getLength());
                        if (debug) System.out.println("Service request received for service: " + received);

                        if (received.equals(serviceName)) {
                            String resp = String.format("%s:%d", ipAdress, port);
                            buf = resp.getBytes();
                            InetAddress address = packet.getAddress();
                            int port = packet.getPort();
                            packet = new DatagramPacket(buf, 0, buf.length, address, port);

                            if (debug)
                                System.out.printf("Sending response (%s) to %s on port %d\n", resp, address.toString(), port);
                            socket.send(packet);
                        }
                    }

                } catch (SocketException e) {

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (debug) System.out.println("Service Announcer: Finished.");
            }
        };
        serviceThread.start();
    }

    public void StopService() throws InterruptedException {

        System.out.println("Stop service request received");
        Runtime.getRuntime().removeShutdownHook(shutdownHook);
        Thread.sleep(500);

        if (!socket.isClosed()) {
            socket.close();
        }

        try {
            serviceThread.join(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void PrintDebugMessages(boolean newValue) {
        debug = newValue;
    }

    public void SetServiceAnnouncerPort(int port) {
        announcerPort = port;
    }
}