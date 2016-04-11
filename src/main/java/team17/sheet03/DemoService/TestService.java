package team17.sheet03.DemoService;

import team17.sheet03.ServiceLocatorService.ServiceAnnouncer;

public class TestService {

    private static final Object lock = new Object();
    private static final Thread mainThread = Thread.currentThread();
    private static boolean serviceRunning;

    public static void main(String[] args) {

        String serviceName = "Test";
        String serviceIP = "127.0.0.1";
        int servicePort = 5000;
        serviceRunning = true;

        addShutdownHook();

        // publish the demo service information
        ServiceAnnouncer sa = new ServiceAnnouncer(serviceName, serviceIP, servicePort);
        sa.PrintDebugMessages(true);
        sa.StartService();

        // Start demo service
        System.out.println("Demo service: Started");

        synchronized (lock) {

            while (serviceRunning) {
                try {

                    // keep alive
                    lock.wait(5000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Demo service: Finished");
    }

    // Handle Ctrl-C command
    private static void addShutdownHook() {
        Thread shutdownHook = new Thread() {
            public void run() {
                System.out.println("Demo service: Shutdown request received");

                try {
                    synchronized (lock) {
                        serviceRunning = false;
                        lock.notifyAll();
                    }

                    mainThread.join(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }
}
