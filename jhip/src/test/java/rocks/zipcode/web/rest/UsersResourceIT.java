package rocks.zipcode.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static rocks.zipcode.domain.UsersAsserts.*;
import static rocks.zipcode.web.rest.TestUtil.createUpdateProxyForBean;

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
import rocks.zipcode.IntegrationTest;
import rocks.zipcode.domain.Users;
import rocks.zipcode.repository.UsersRepository;

/**
 * Integration tests for the {@link UsersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UsersResourceIT {

    private static final Long DEFAULT_UID = 1L;
    private static final Long UPDATED_UID = 2L;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASS_WORD = "AAAAAAAAAA";
    private static final String UPDATED_PASS_WORD = "BBBBBBBBBB";

    private static final String DEFAULT_PROFILE_PIC_I_URL = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE_PIC_I_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUsersMockMvc;

    private Users users;

    private Users insertedUsers;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Users createEntity(EntityManager em) {
        Users users = new Users()
            .uid(DEFAULT_UID)
            .email(DEFAULT_EMAIL)
            .userName(DEFAULT_USER_NAME)
            .passWord(DEFAULT_PASS_WORD)
            .profilePicIUrl(DEFAULT_PROFILE_PIC_I_URL);
        return users;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Users createUpdatedEntity(EntityManager em) {
        Users users = new Users()
            .uid(UPDATED_UID)
            .email(UPDATED_EMAIL)
            .userName(UPDATED_USER_NAME)
            .passWord(UPDATED_PASS_WORD)
            .profilePicIUrl(UPDATED_PROFILE_PIC_I_URL);
        return users;
    }

    @BeforeEach
    public void initTest() {
        users = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedUsers != null) {
            usersRepository.delete(insertedUsers);
            insertedUsers = null;
        }
    }

    @Test
    @Transactional
    void createUsers() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Users
        var returnedUsers = om.readValue(
            restUsersMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(users)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Users.class
        );

        // Validate the Users in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUsersUpdatableFieldsEquals(returnedUsers, getPersistedUsers(returnedUsers));

        insertedUsers = returnedUsers;
    }

    @Test
    @Transactional
    void createUsersWithExistingId() throws Exception {
        // Create the Users with an existing ID
        users.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(users)))
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        users.setUid(null);

        // Create the Users, which fails.

        restUsersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(users)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        users.setUserName(null);

        // Create the Users, which fails.

        restUsersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(users)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPassWordIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        users.setPassWord(null);

        // Create the Users, which fails.

        restUsersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(users)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUsers() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get all the usersList
        restUsersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(users.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].passWord").value(hasItem(DEFAULT_PASS_WORD)))
            .andExpect(jsonPath("$.[*].profilePicIUrl").value(hasItem(DEFAULT_PROFILE_PIC_I_URL)));
    }

    @Test
    @Transactional
    void getUsers() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        // Get the users
        restUsersMockMvc
            .perform(get(ENTITY_API_URL_ID, users.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(users.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.passWord").value(DEFAULT_PASS_WORD))
            .andExpect(jsonPath("$.profilePicIUrl").value(DEFAULT_PROFILE_PIC_I_URL));
    }

    @Test
    @Transactional
    void getNonExistingUsers() throws Exception {
        // Get the users
        restUsersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUsers() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the users
        Users updatedUsers = usersRepository.findById(users.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUsers are not directly saved in db
        em.detach(updatedUsers);
        updatedUsers
            .uid(UPDATED_UID)
            .email(UPDATED_EMAIL)
            .userName(UPDATED_USER_NAME)
            .passWord(UPDATED_PASS_WORD)
            .profilePicIUrl(UPDATED_PROFILE_PIC_I_URL);

        restUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUsers.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUsers))
            )
            .andExpect(status().isOk());

        // Validate the Users in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUsersToMatchAllProperties(updatedUsers);
    }

    @Test
    @Transactional
    void putNonExistingUsers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        users.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(put(ENTITY_API_URL_ID, users.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(users)))
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUsers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        users.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(users))
            )
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUsers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        users.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(users)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Users in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUsersWithPatch() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the users using partial update
        Users partialUpdatedUsers = new Users();
        partialUpdatedUsers.setId(users.getId());

        partialUpdatedUsers.uid(UPDATED_UID).userName(UPDATED_USER_NAME).profilePicIUrl(UPDATED_PROFILE_PIC_I_URL);

        restUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsers.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUsers))
            )
            .andExpect(status().isOk());

        // Validate the Users in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUsersUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedUsers, users), getPersistedUsers(users));
    }

    @Test
    @Transactional
    void fullUpdateUsersWithPatch() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the users using partial update
        Users partialUpdatedUsers = new Users();
        partialUpdatedUsers.setId(users.getId());

        partialUpdatedUsers
            .uid(UPDATED_UID)
            .email(UPDATED_EMAIL)
            .userName(UPDATED_USER_NAME)
            .passWord(UPDATED_PASS_WORD)
            .profilePicIUrl(UPDATED_PROFILE_PIC_I_URL);

        restUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsers.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUsers))
            )
            .andExpect(status().isOk());

        // Validate the Users in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUsersUpdatableFieldsEquals(partialUpdatedUsers, getPersistedUsers(partialUpdatedUsers));
    }

    @Test
    @Transactional
    void patchNonExistingUsers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        users.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, users.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(users))
            )
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUsers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        users.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(users))
            )
            .andExpect(status().isBadRequest());

        // Validate the Users in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUsers() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        users.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsersMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(users)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Users in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUsers() throws Exception {
        // Initialize the database
        insertedUsers = usersRepository.saveAndFlush(users);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the users
        restUsersMockMvc
            .perform(delete(ENTITY_API_URL_ID, users.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return usersRepository.count();
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

    protected Users getPersistedUsers(Users users) {
        return usersRepository.findById(users.getId()).orElseThrow();
    }

    protected void assertPersistedUsersToMatchAllProperties(Users expectedUsers) {
        assertUsersAllPropertiesEquals(expectedUsers, getPersistedUsers(expectedUsers));
    }

    protected void assertPersistedUsersToMatchUpdatableProperties(Users expectedUsers) {
        assertUsersAllUpdatablePropertiesEquals(expectedUsers, getPersistedUsers(expectedUsers));
    }
}
