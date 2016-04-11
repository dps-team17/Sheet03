package team17.sheet03;

public class TestService {

    public static void main(String[] args) throws InterruptedException {

        ServiceAnnouncer sa = new ServiceAnnouncer("Test", "127.0.0.1", 5000);
        sa.StartService();
        //sa.StopService();
    }
}
