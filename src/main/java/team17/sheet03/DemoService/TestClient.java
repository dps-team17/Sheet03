package team17.sheet03.DemoService;

import team17.sheet03.ServiceLocatorService.ServiceAddress;
import team17.sheet03.ServiceLocatorService.ServiceLocator;

import java.util.List;

public class TestClient {

    public static void main(String[] args){

        ServiceLocator locator = new ServiceLocator();

        // Find one test service
        System.out.println("Looking for one test service...");
        final ServiceAddress test = locator.locate("Test");
        if(test!= null){
            System.out.printf("Service found at %s on port %d\n", test.getIpAddress(), test.getPort());
        } else
        {
            System.out.println("No Service found");
        }

        // Find all test services
        System.out.println("\nSearching all available test services...");
        final List<ServiceAddress> addresses = locator.locateAll("Test");
        for (ServiceAddress a: addresses) {

            System.out.printf("Service found at %s on port %d\n", a.getIpAddress(), a.getPort());
        }
    }
}
