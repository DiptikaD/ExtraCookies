package zipcode.rocks.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import zipcode.rocks.domain.Posts;
import zipcode.rocks.repository.PostsRepository;
import zipcode.rocks.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link zipcode.rocks.domain.Posts}.
 */
@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "http://localhost:3000/")
@Transactional
public class PostsResource {

    private static final Logger log = LoggerFactory.getLogger(PostsResource.class);

    private static final String ENTITY_NAME = "posts";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PostsRepository postsRepository;

    public PostsResource(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    }

    /**
     * {@code POST  /posts} : Create a new posts.
     *
     * @param posts the posts to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new posts, or with status {@code 400 (Bad Request)} if the posts has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Posts> createPosts(@Valid @RequestBody Posts posts) throws URISyntaxException {
        log.debug("REST request to save Posts : {}", posts);
        if (posts.getId() != null) {
            throw new BadRequestAlertException("A new posts cannot already have an ID", ENTITY_NAME, "idexists");
        }
        posts = postsRepository.save(posts);
        return ResponseEntity.created(new URI("/api/posts/" + posts.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, posts.getId().toString()))
            .body(posts);
    }

    /**
     * {@code PUT  /posts/:id} : Updates an existing posts.
     *
     * @param id the id of the posts to save.
     * @param posts the posts to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated posts,
     * or with status {@code 400 (Bad Request)} if the posts is not valid,
     * or with status {@code 500 (Internal Server Error)} if the posts couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Posts> updatePosts(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Posts posts)
        throws URISyntaxException {
        log.debug("REST request to update Posts : {}, {}", id, posts);
        if (posts.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, posts.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        posts = postsRepository.save(posts);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, posts.getId().toString()))
            .body(posts);
    }

    /**
     * {@code PATCH  /posts/:id} : Partial updates given fields of an existing posts, field will ignore if it is null
     *
     * @param id the id of the posts to save.
     * @param posts the posts to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated posts,
     * or with status {@code 400 (Bad Request)} if the posts is not valid,
     * or with status {@code 404 (Not Found)} if the posts is not found,
     * or with status {@code 500 (Internal Server Error)} if the posts couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Posts> partialUpdatePosts(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Posts posts
    ) throws URISyntaxException {
        log.debug("REST request to partial update Posts partially : {}, {}", id, posts);
        if (posts.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, posts.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Posts> result = postsRepository
            .findById(posts.getId())
            .map(existingPosts -> {
                if (posts.getPrice() != null) {
                    existingPosts.setPrice(posts.getPrice());
                }
                if (posts.getTitle() != null) {
                    existingPosts.setTitle(posts.getTitle());
                }
                if (posts.getLocation() != null) {
                    existingPosts.setLocation(posts.getLocation());
                }
                if (posts.getAvailability() != null) {
                    existingPosts.setAvailability(posts.getAvailability());
                }
                if (posts.getTag() != null) {
                    existingPosts.setTag(posts.getTag());
                }
                if (posts.getImage() != null) {
                    existingPosts.setImage(posts.getImage());
                }

                        return existingPosts;
            })
            .map(postsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, posts.getId().toString())
        );
    }

    /**
     * {@code GET  /posts} : get all the posts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of posts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Posts>> getAllPosts(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Posts");
        Page<Posts> page = postsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /posts/:id} : get the "id" posts.
     *
     * @param id the id of the posts to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the posts, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Posts> getPosts(@PathVariable("id") Long id) {
        log.debug("REST request to get Posts : {}", id);
        Optional<Posts> posts = postsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(posts);
    }

    /**
     * {@code DELETE  /posts/:id} : delete the "id" posts.
     *
     * @param id the id of the posts to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePosts(@PathVariable("id") Long id) {
        log.debug("REST request to delete Posts : {}", id);
        postsRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
