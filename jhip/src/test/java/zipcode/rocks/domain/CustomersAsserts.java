package zipcode.rocks.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomersAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCustomersAllPropertiesEquals(Customers expected, Customers actual) {
        assertCustomersAutoGeneratedPropertiesEquals(expected, actual);
        assertCustomersAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCustomersAllUpdatablePropertiesEquals(Customers expected, Customers actual) {
        assertCustomersUpdatableFieldsEquals(expected, actual);
        assertCustomersUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCustomersAutoGeneratedPropertiesEquals(Customers expected, Customers actual) {
        assertThat(expected)
            .as("Verify Customers auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCustomersUpdatableFieldsEquals(Customers expected, Customers actual) {
        assertThat(expected)
            .as("Verify Customers relevant properties")
            .satisfies(e -> assertThat(e.getEmail()).as("check email").isEqualTo(actual.getEmail()))
            .satisfies(e -> assertThat(e.getUserName()).as("check userName").isEqualTo(actual.getUserName()))
            .satisfies(e -> assertThat(e.getPassword()).as("check password").isEqualTo(actual.getPassword()))
            .satisfies(e -> assertThat(e.getProfilePicIUrl()).as("check profilePicIUrl").isEqualTo(actual.getProfilePicIUrl()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCustomersUpdatableRelationshipsEquals(Customers expected, Customers actual) {}
}
