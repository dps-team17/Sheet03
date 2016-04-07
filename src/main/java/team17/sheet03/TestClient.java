package team17.sheet03;

import java.util.List;

/**
 * Created by adanek on 07.04.2016.
 */
public class TestClient {

    public static void main(String[] args){

        ServiceLocator locator = new ServiceLocator();

//        final ServiceAddress test = locator.locate("Test");
//        if(test!= null){
//            System.out.printf("Service found at %s on port %d\n", test.getIpAddress(), test.getPort());
//        } else
//        {
//            System.out.println("No Service found");
//        }

        final List<ServiceAddress> addresses = locator.locateAll("Test");

        for (ServiceAddress a: addresses) {

            System.out.printf("Service found at %s on port %d\n", a.getIpAddress(), a.getPort());
        }
    }
}
