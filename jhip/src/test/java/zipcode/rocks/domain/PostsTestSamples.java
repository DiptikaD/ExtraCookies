package zipcode.rocks.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PostsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Posts getPostsSample1() {
        return new Posts().id(1L).postId(1L).title("title1").location("location1").rating(1);
    }

    public static Posts getPostsSample2() {
        return new Posts().id(2L).postId(2L).title("title2").location("location2").rating(2);
    }

    public static Posts getPostsRandomSampleGenerator() {
        return new Posts()
            .id(longCount.incrementAndGet())
            .postId(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString())
            .rating(intCount.incrementAndGet());
    }
}
