package rocks.zipcode.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rocks.zipcode.domain.CustomersTestSamples.*;
import static rocks.zipcode.domain.PostsTestSamples.*;
import static rocks.zipcode.domain.UsersTestSamples.*;

import org.junit.jupiter.api.Test;
import rocks.zipcode.web.rest.TestUtil;

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
    void pidTest() {
        Posts posts = getPostsRandomSampleGenerator();
        Users usersBack = getUsersRandomSampleGenerator();

        posts.setPid(usersBack);
        assertThat(posts.getPid()).isEqualTo(usersBack);

        posts.pid(null);
        assertThat(posts.getPid()).isNull();
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
