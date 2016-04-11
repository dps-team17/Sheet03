package team17.sheet03.ServiceLocatorService;

/**
 * Created by adanek on 07.04.2016.
 */
public class ServiceAddress {

    private String ipAddress;
    private int port;

    public ServiceAddress(String address, int port){
        this.setIpAddress(address);
        this.setPort(port);
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
