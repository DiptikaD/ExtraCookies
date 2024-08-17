package zipcode.rocks.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static zipcode.rocks.domain.CustomersTestSamples.*;
import static zipcode.rocks.domain.PostsTestSamples.*;

import org.junit.jupiter.api.Test;
import zipcode.rocks.web.rest.TestUtil;

class PostsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Posts.class);
        Posts posts1 = getPostsSample1();
        Posts posts2 = new Posts();
        assertThat(posts1).isNotEqualTo(posts2);

        posts2.setId(posts1.getId());
        assertThat(posts1).isEqualTo(posts2);

        posts2 = getPostsSample2();
        assertThat(posts1).isNotEqualTo(posts2);
    }

    @Test
    void customersTest() {
        Posts posts = getPostsRandomSampleGenerator();
        Customers customersBack = getCustomersRandomSampleGenerator();

        posts.setCustomers(customersBack);
        assertThat(posts.getCustomers()).isEqualTo(customersBack);

        posts.customers(null);
        assertThat(posts.getCustomers()).isNull();
    }
}
