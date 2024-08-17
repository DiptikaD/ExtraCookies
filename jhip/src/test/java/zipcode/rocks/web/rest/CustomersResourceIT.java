package zipcode.rocks.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static zipcode.rocks.domain.CustomersAsserts.*;
import static zipcode.rocks.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import zipcode.rocks.IntegrationTest;
import zipcode.rocks.domain.Customers;
import zipcode.rocks.repository.CustomersRepository;

/**
 * Integration tests for the {@link CustomersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomersResourceIT {

    private static final Long DEFAULT_UID = 1L;
    private static final Long UPDATED_UID = 2L;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_PROFILE_PIC_I_URL = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE_PIC_I_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/customers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CustomersRepository customersRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomersMockMvc;

    private Customers customers;

    private Customers insertedCustomers;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customers createEntity(EntityManager em) {
        Customers customers = new Customers()
            .uid(DEFAULT_UID)
            .email(DEFAULT_EMAIL)
            .userName(DEFAULT_USER_NAME)
            .password(DEFAULT_PASSWORD)
            .profilePicIUrl(DEFAULT_PROFILE_PIC_I_URL);
        return customers;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customers createUpdatedEntity(EntityManager em) {
        Customers customers = new Customers()
            .uid(UPDATED_UID)
            .email(UPDATED_EMAIL)
            .userName(UPDATED_USER_NAME)
            .password(UPDATED_PASSWORD)
            .profilePicIUrl(UPDATED_PROFILE_PIC_I_URL);
        return customers;
    }

    @BeforeEach
    public void initTest() {
        customers = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedCustomers != null) {
            customersRepository.delete(insertedCustomers);
            insertedCustomers = null;
        }
    }

    @Test
    @Transactional
    void createCustomers() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Customers
        var returnedCustomers = om.readValue(
            restCustomersMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customers)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Customers.class
        );

        // Validate the Customers in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCustomersUpdatableFieldsEquals(returnedCustomers, getPersistedCustomers(returnedCustomers));

        insertedCustomers = returnedCustomers;
    }

    @Test
    @Transactional
    void createCustomersWithExistingId() throws Exception {
        // Create the Customers with an existing ID
        customers.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customers)))
            .andExpect(status().isBadRequest());

        // Validate the Customers in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customers.setUid(null);

        // Create the Customers, which fails.

        restCustomersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customers)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customers.setUserName(null);

        // Create the Customers, which fails.

        restCustomersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customers)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        customers.setPassword(null);

        // Create the Customers, which fails.

        restCustomersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customers)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCustomers() throws Exception {
        // Initialize the database
        insertedCustomers = customersRepository.saveAndFlush(customers);

        // Get all the customersList
        restCustomersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customers.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].profilePicIUrl").value(hasItem(DEFAULT_PROFILE_PIC_I_URL)));
    }

    @Test
    @Transactional
    void getCustomers() throws Exception {
        // Initialize the database
        insertedCustomers = customersRepository.saveAndFlush(customers);

        // Get the customers
        restCustomersMockMvc
            .perform(get(ENTITY_API_URL_ID, customers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customers.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.profilePicIUrl").value(DEFAULT_PROFILE_PIC_I_URL));
    }

    @Test
    @Transactional
    void getNonExistingCustomers() throws Exception {
        // Get the customers
        restCustomersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomers() throws Exception {
        // Initialize the database
        insertedCustomers = customersRepository.saveAndFlush(customers);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customers
        Customers updatedCustomers = customersRepository.findById(customers.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCustomers are not directly saved in db
        em.detach(updatedCustomers);
        updatedCustomers
            .uid(UPDATED_UID)
            .email(UPDATED_EMAIL)
            .userName(UPDATED_USER_NAME)
            .password(UPDATED_PASSWORD)
            .profilePicIUrl(UPDATED_PROFILE_PIC_I_URL);

        restCustomersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCustomers.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCustomers))
            )
            .andExpect(status().isOk());

        // Validate the Customers in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCustomersToMatchAllProperties(updatedCustomers);
    }

    @Test
    @Transactional
    void putNonExistingCustomers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customers.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customers.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customers))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customers in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customers.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(customers))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customers in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customers.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomersMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(customers)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Customers in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomersWithPatch() throws Exception {
        // Initialize the database
        insertedCustomers = customersRepository.saveAndFlush(customers);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customers using partial update
        Customers partialUpdatedCustomers = new Customers();
        partialUpdatedCustomers.setId(customers.getId());

        partialUpdatedCustomers.userName(UPDATED_USER_NAME).profilePicIUrl(UPDATED_PROFILE_PIC_I_URL);

        restCustomersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomers.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomers))
            )
            .andExpect(status().isOk());

        // Validate the Customers in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomersUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCustomers, customers),
            getPersistedCustomers(customers)
        );
    }

    @Test
    @Transactional
    void fullUpdateCustomersWithPatch() throws Exception {
        // Initialize the database
        insertedCustomers = customersRepository.saveAndFlush(customers);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the customers using partial update
        Customers partialUpdatedCustomers = new Customers();
        partialUpdatedCustomers.setId(customers.getId());

        partialUpdatedCustomers
            .uid(UPDATED_UID)
            .email(UPDATED_EMAIL)
            .userName(UPDATED_USER_NAME)
            .password(UPDATED_PASSWORD)
            .profilePicIUrl(UPDATED_PROFILE_PIC_I_URL);

        restCustomersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomers.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCustomers))
            )
            .andExpect(status().isOk());

        // Validate the Customers in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCustomersUpdatableFieldsEquals(partialUpdatedCustomers, getPersistedCustomers(partialUpdatedCustomers));
    }

    @Test
    @Transactional
    void patchNonExistingCustomers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customers.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customers.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customers))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customers in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customers.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(customers))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customers in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        customers.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomersMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(customers)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Customers in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomers() throws Exception {
        // Initialize the database
        insertedCustomers = customersRepository.saveAndFlush(customers);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the customers
        restCustomersMockMvc
            .perform(delete(ENTITY_API_URL_ID, customers.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return customersRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Customers getPersistedCustomers(Customers customers) {
        return customersRepository.findById(customers.getId()).orElseThrow();
    }

    protected void assertPersistedCustomersToMatchAllProperties(Customers expectedCustomers) {
        assertCustomersAllPropertiesEquals(expectedCustomers, getPersistedCustomers(expectedCustomers));
    }

    protected void assertPersistedCustomersToMatchUpdatableProperties(Customers expectedCustomers) {
        assertCustomersAllUpdatablePropertiesEquals(expectedCustomers, getPersistedCustomers(expectedCustomers));
    }
}
