package rocks.zipcode.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rocks.zipcode.domain.CustomersTestSamples.*;
import static rocks.zipcode.domain.PostsTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import rocks.zipcode.web.rest.TestUtil;

class CustomersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customers.class);
        Customers customers1 = getCustomersSample1();
        Customers customers2 = new Customers();
        assertThat(customers1).isNotEqualTo(customers2);

        customers2.setId(customers1.getId());
        assertThat(customers1).isEqualTo(customers2);

        customers2 = getCustomersSample2();
        assertThat(customers1).isNotEqualTo(customers2);
    }

    @Test
    void uidTest() {
        Customers customers = getCustomersRandomSampleGenerator();
        Posts postsBack = getPostsRandomSampleGenerator();

        customers.addUid(postsBack);
        assertThat(customers.getUids()).containsOnly(postsBack);
        assertThat(postsBack.getCustomers()).isEqualTo(customers);

        customers.removeUid(postsBack);
        assertThat(customers.getUids()).doesNotContain(postsBack);
        assertThat(postsBack.getCustomers()).isNull();

        customers.uids(new HashSet<>(Set.of(postsBack)));
        assertThat(customers.getUids()).containsOnly(postsBack);
        assertThat(postsBack.getCustomers()).isEqualTo(customers);

        customers.setUids(new HashSet<>());
        assertThat(customers.getUids()).doesNotContain(postsBack);
        assertThat(postsBack.getCustomers()).isNull();
    }
}
