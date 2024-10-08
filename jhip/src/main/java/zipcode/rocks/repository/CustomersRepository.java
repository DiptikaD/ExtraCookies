package zipcode.rocks.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import zipcode.rocks.domain.Customers;

/**
 * Spring Data JPA repository for the Customers entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomersRepository extends JpaRepository<Customers, Long> {}
