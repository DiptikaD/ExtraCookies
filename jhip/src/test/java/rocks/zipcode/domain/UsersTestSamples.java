package rocks.zipcode.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UsersTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Users getUsersSample1() {
        return new Users().id(1L).uid(1L).email("email1").userName("userName1").passWord("passWord1").profilePicIUrl("profilePicIUrl1");
    }

    public static Users getUsersSample2() {
        return new Users().id(2L).uid(2L).email("email2").userName("userName2").passWord("passWord2").profilePicIUrl("profilePicIUrl2");
    }

    public static Users getUsersRandomSampleGenerator() {
        return new Users()
            .id(longCount.incrementAndGet())
            .uid(longCount.incrementAndGet())
            .email(UUID.randomUUID().toString())
            .userName(UUID.randomUUID().toString())
            .passWord(UUID.randomUUID().toString())
            .profilePicIUrl(UUID.randomUUID().toString());
    }
}
