package zipcode.rocks.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static zipcode.rocks.domain.PostsAsserts.*;
import static zipcode.rocks.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import zipcode.rocks.domain.Posts;
import zipcode.rocks.domain.enumeration.Tags;
import zipcode.rocks.repository.PostsRepository;

/**
 * Integration tests for the {@link PostsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PostsResourceIT {

    private static final Long DEFAULT_POST_ID = 1L;
    private static final Long UPDATED_POST_ID = 2L;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Instant DEFAULT_AVAILABILITY = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_AVAILABILITY = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;

    private static final Tags DEFAULT_TAG = Tags.PRODUCE;
    private static final Tags UPDATED_TAG = Tags.READY_MADE;

    private static final String ENTITY_API_URL = "/api/posts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPostsMockMvc;

    private Posts posts;

    private Posts insertedPosts;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Posts createEntity(EntityManager em) {
        Posts posts = new Posts()
            .postId(DEFAULT_POST_ID)
            .price(DEFAULT_PRICE)
            .title(DEFAULT_TITLE)
            .location(DEFAULT_LOCATION)
            .availability(DEFAULT_AVAILABILITY)
            .rating(DEFAULT_RATING)
            .tag(DEFAULT_TAG);
        return posts;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Posts createUpdatedEntity(EntityManager em) {
        Posts posts = new Posts()
            .postId(UPDATED_POST_ID)
            .price(UPDATED_PRICE)
            .title(UPDATED_TITLE)
            .location(UPDATED_LOCATION)
            .availability(UPDATED_AVAILABILITY)
            .rating(UPDATED_RATING)
            .tag(UPDATED_TAG);
        return posts;
    }

    @BeforeEach
    public void initTest() {
        posts = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPosts != null) {
            postsRepository.delete(insertedPosts);
            insertedPosts = null;
        }
    }

    @Test
    @Transactional
    void createPosts() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Posts
        var returnedPosts = om.readValue(
            restPostsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(posts)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Posts.class
        );

        // Validate the Posts in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPostsUpdatableFieldsEquals(returnedPosts, getPersistedPosts(returnedPosts));

        insertedPosts = returnedPosts;
    }

    @Test
    @Transactional
    void createPostsWithExistingId() throws Exception {
        // Create the Posts with an existing ID
        posts.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(posts)))
            .andExpect(status().isBadRequest());

        // Validate the Posts in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPostIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        posts.setPostId(null);

        // Create the Posts, which fails.

        restPostsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(posts)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPosts() throws Exception {
        // Initialize the database
        insertedPosts = postsRepository.saveAndFlush(posts);

        // Get all the postsList
        restPostsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(posts.getId().intValue())))
            .andExpect(jsonPath("$.[*].postId").value(hasItem(DEFAULT_POST_ID.intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].availability").value(hasItem(DEFAULT_AVAILABILITY.toString())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].tag").value(hasItem(DEFAULT_TAG.toString())));
    }

    @Test
    @Transactional
    void getPosts() throws Exception {
        // Initialize the database
        insertedPosts = postsRepository.saveAndFlush(posts);

        // Get the posts
        restPostsMockMvc
            .perform(get(ENTITY_API_URL_ID, posts.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(posts.getId().intValue()))
            .andExpect(jsonPath("$.postId").value(DEFAULT_POST_ID.intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.availability").value(DEFAULT_AVAILABILITY.toString()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.tag").value(DEFAULT_TAG.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPosts() throws Exception {
        // Get the posts
        restPostsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPosts() throws Exception {
        // Initialize the database
        insertedPosts = postsRepository.saveAndFlush(posts);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the posts
        Posts updatedPosts = postsRepository.findById(posts.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPosts are not directly saved in db
        em.detach(updatedPosts);
        updatedPosts
            .postId(UPDATED_POST_ID)
            .price(UPDATED_PRICE)
            .title(UPDATED_TITLE)
            .location(UPDATED_LOCATION)
            .availability(UPDATED_AVAILABILITY)
            .rating(UPDATED_RATING)
            .tag(UPDATED_TAG);

        restPostsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPosts.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPosts))
            )
            .andExpect(status().isOk());

        // Validate the Posts in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPostsToMatchAllProperties(updatedPosts);
    }

    @Test
    @Transactional
    void putNonExistingPosts() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        posts.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostsMockMvc
            .perform(put(ENTITY_API_URL_ID, posts.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(posts)))
            .andExpect(status().isBadRequest());

        // Validate the Posts in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPosts() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        posts.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(posts))
            )
            .andExpect(status().isBadRequest());

        // Validate the Posts in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPosts() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        posts.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(posts)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Posts in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePostsWithPatch() throws Exception {
        // Initialize the database
        insertedPosts = postsRepository.saveAndFlush(posts);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the posts using partial update
        Posts partialUpdatedPosts = new Posts();
        partialUpdatedPosts.setId(posts.getId());

        partialUpdatedPosts
            .postId(UPDATED_POST_ID)
            .title(UPDATED_TITLE)
            .location(UPDATED_LOCATION)
            .availability(UPDATED_AVAILABILITY)
            .rating(UPDATED_RATING);

        restPostsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPosts.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPosts))
            )
            .andExpect(status().isOk());

        // Validate the Posts in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPostsUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPosts, posts), getPersistedPosts(posts));
    }

    @Test
    @Transactional
    void fullUpdatePostsWithPatch() throws Exception {
        // Initialize the database
        insertedPosts = postsRepository.saveAndFlush(posts);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the posts using partial update
        Posts partialUpdatedPosts = new Posts();
        partialUpdatedPosts.setId(posts.getId());

        partialUpdatedPosts
            .postId(UPDATED_POST_ID)
            .price(UPDATED_PRICE)
            .title(UPDATED_TITLE)
            .location(UPDATED_LOCATION)
            .availability(UPDATED_AVAILABILITY)
            .rating(UPDATED_RATING)
            .tag(UPDATED_TAG);

        restPostsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPosts.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPosts))
            )
            .andExpect(status().isOk());

        // Validate the Posts in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPostsUpdatableFieldsEquals(partialUpdatedPosts, getPersistedPosts(partialUpdatedPosts));
    }

    @Test
    @Transactional
    void patchNonExistingPosts() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        posts.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, posts.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(posts))
            )
            .andExpect(status().isBadRequest());

        // Validate the Posts in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPosts() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        posts.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(posts))
            )
            .andExpect(status().isBadRequest());

        // Validate the Posts in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPosts() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        posts.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPostsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(posts)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Posts in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePosts() throws Exception {
        // Initialize the database
        insertedPosts = postsRepository.saveAndFlush(posts);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the posts
        restPostsMockMvc
            .perform(delete(ENTITY_API_URL_ID, posts.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return postsRepository.count();
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

    protected Posts getPersistedPosts(Posts posts) {
        return postsRepository.findById(posts.getId()).orElseThrow();
    }

    protected void assertPersistedPostsToMatchAllProperties(Posts expectedPosts) {
        assertPostsAllPropertiesEquals(expectedPosts, getPersistedPosts(expectedPosts));
    }

    protected void assertPersistedPostsToMatchUpdatableProperties(Posts expectedPosts) {
        assertPostsAllUpdatablePropertiesEquals(expectedPosts, getPersistedPosts(expectedPosts));
    }
}
