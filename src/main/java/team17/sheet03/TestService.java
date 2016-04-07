package team17.sheet03;

/**
 * Created by adanek on 07.04.2016.
 */
public class TestService {

    public static void main(String[] args) throws InterruptedException {

        ServiceAnnouncer sa = new ServiceAnnouncer("Test", "127.0.0.1", 5000);
        sa.StartService();

        try {
            sa.join();
            System.out.println("Done");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
