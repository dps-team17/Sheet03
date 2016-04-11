package team17.sheet03.ServiceLocatorService;

import com.sun.media.sound.InvalidDataException;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceLocator {

    public ServiceAddress locate(String serviceName) {

        ServiceAddress address = null;
        byte[] buf = serviceName.getBytes();

        try (DatagramSocket socket = new DatagramSocket()) {

            // Send location request
            InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
            DatagramPacket packet = new DatagramPacket(buf, buf.length, broadcastAddress, 7000);
            socket.send(packet);

            // Wait for response
            buf = new byte[256];
            packet = new DatagramPacket(buf, buf.length);
            socket.setSoTimeout(5000);
            socket.receive(packet);

            // Validate response
            String resp = new String(packet.getData(), 0, packet.getLength());
            final String[] parts = resp.split(":");
            if(parts.length != 2)
                throw new InvalidDataException("Response does not match pattern: " + resp);

            address = new ServiceAddress(parts[0], Integer.parseInt(parts[1]));

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }

    public List<ServiceAddress> locateAll(String serviceName) {

        List<ServiceAddress> servers = new ArrayList<>();

        byte[] buf = serviceName.getBytes();
        boolean timeOutReceived = false;

        try (DatagramSocket socket = new DatagramSocket()) {

            // Send location request
            InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
            DatagramPacket packet = new DatagramPacket(buf, buf.length, broadcastAddress, 7000);
            socket.send(packet);

            while(!timeOutReceived){

                try{
                    // Wait for response
                    buf = new byte[256];
                    packet = new DatagramPacket(buf, buf.length);
                    socket.setSoTimeout(5000);
                    socket.receive(packet);

                    // Validate response
                    String resp = new String(packet.getData(), 0, packet.getLength());
                    final String[] parts = resp.split(":");
                    if(parts.length != 2)
                        throw new InvalidDataException("Response does not match pattern: " + resp);

                    servers.add(new ServiceAddress(parts[0], Integer.parseInt(parts[1])));
                }
                catch (SocketTimeoutException e){
                    timeOutReceived = true;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return servers;
    }
}