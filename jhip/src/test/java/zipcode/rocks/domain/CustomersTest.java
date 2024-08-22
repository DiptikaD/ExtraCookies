package zipcode.rocks.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static zipcode.rocks.domain.CustomersTestSamples.*;

import org.junit.jupiter.api.Test;
import zipcode.rocks.web.rest.TestUtil;

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
}
