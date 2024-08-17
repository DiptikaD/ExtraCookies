package rocks.zipcode.web.rest;

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
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import rocks.zipcode.domain.Customers;
import rocks.zipcode.repository.CustomersRepository;
import rocks.zipcode.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link rocks.zipcode.domain.Customers}.
 */
@RestController
@RequestMapping("/api/customers")
@Transactional
public class CustomersResource {

    private static final Logger log = LoggerFactory.getLogger(CustomersResource.class);

    private static final String ENTITY_NAME = "customers";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomersRepository customersRepository;

    public CustomersResource(CustomersRepository customersRepository) {
        this.customersRepository = customersRepository;
    }

    /**
     * {@code POST  /customers} : Create a new customers.
     *
     * @param customers the customers to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customers, or with status {@code 400 (Bad Request)} if the customers has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Customers> createCustomers(@Valid @RequestBody Customers customers) throws URISyntaxException {
        log.debug("REST request to save Customers : {}", customers);
        if (customers.getId() != null) {
            throw new BadRequestAlertException("A new customers cannot already have an ID", ENTITY_NAME, "idexists");
        }
        customers = customersRepository.save(customers);
        return ResponseEntity.created(new URI("/api/customers/" + customers.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, customers.getId().toString()))
            .body(customers);
    }

    /**
     * {@code PUT  /customers/:id} : Updates an existing customers.
     *
     * @param id the id of the customers to save.
     * @param customers the customers to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customers,
     * or with status {@code 400 (Bad Request)} if the customers is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customers couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Customers> updateCustomers(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Customers customers
    ) throws URISyntaxException {
        log.debug("REST request to update Customers : {}, {}", id, customers);
        if (customers.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customers.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        customers = customersRepository.save(customers);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customers.getId().toString()))
            .body(customers);
    }

    /**
     * {@code PATCH  /customers/:id} : Partial updates given fields of an existing customers, field will ignore if it is null
     *
     * @param id the id of the customers to save.
     * @param customers the customers to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customers,
     * or with status {@code 400 (Bad Request)} if the customers is not valid,
     * or with status {@code 404 (Not Found)} if the customers is not found,
     * or with status {@code 500 (Internal Server Error)} if the customers couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Customers> partialUpdateCustomers(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Customers customers
    ) throws URISyntaxException {
        log.debug("REST request to partial update Customers partially : {}, {}", id, customers);
        if (customers.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customers.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Customers> result = customersRepository
            .findById(customers.getId())
            .map(existingCustomers -> {
                if (customers.getUid() != null) {
                    existingCustomers.setUid(customers.getUid());
                }
                if (customers.getEmail() != null) {
                    existingCustomers.setEmail(customers.getEmail());
                }
                if (customers.getUserName() != null) {
                    existingCustomers.setUserName(customers.getUserName());
                }
                if (customers.getPassword() != null) {
                    existingCustomers.setPassword(customers.getPassword());
                }
                if (customers.getProfilePicIUrl() != null) {
                    existingCustomers.setProfilePicIUrl(customers.getProfilePicIUrl());
                }

                return existingCustomers;
            })
            .map(customersRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customers.getId().toString())
        );
    }

    /**
     * {@code GET  /customers} : get all the customers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customers in body.
     */
    @GetMapping("")
    public List<Customers> getAllCustomers() {
        log.debug("REST request to get all Customers");
        return customersRepository.findAll();
    }

    /**
     * {@code GET  /customers/:id} : get the "id" customers.
     *
     * @param id the id of the customers to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customers, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Customers> getCustomers(@PathVariable("id") Long id) {
        log.debug("REST request to get Customers : {}", id);
        Optional<Customers> customers = customersRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(customers);
    }

    /**
     * {@code DELETE  /customers/:id} : delete the "id" customers.
     *
     * @param id the id of the customers to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomers(@PathVariable("id") Long id) {
        log.debug("REST request to delete Customers : {}", id);
        customersRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
