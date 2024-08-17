package rocks.zipcode.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CustomersTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Customers getCustomersSample1() {
        return new Customers().id(1L).uid(1L).email("email1").userName("userName1").password("password1").profilePicIUrl("profilePicIUrl1");
    }

    public static Customers getCustomersSample2() {
        return new Customers().id(2L).uid(2L).email("email2").userName("userName2").password("password2").profilePicIUrl("profilePicIUrl2");
    }

    public static Customers getCustomersRandomSampleGenerator() {
        return new Customers()
            .id(longCount.incrementAndGet())
            .uid(longCount.incrementAndGet())
            .email(UUID.randomUUID().toString())
            .userName(UUID.randomUUID().toString())
            .password(UUID.randomUUID().toString())
            .profilePicIUrl(UUID.randomUUID().toString());
    }
}
