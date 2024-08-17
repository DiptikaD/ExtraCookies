package zipcode.rocks.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import zipcode.rocks.domain.enumeration.Tags;

/**
 * A Posts.
 */
@Entity
@Table(name = "posts")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Posts implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "price")
    private Double price;

    @Column(name = "title")
    private String title;

    @Column(name = "location")
    private String location;

    @Column(name = "availability")
    private Instant availability;

    @Column(name = "rating")
    private Integer rating;

    @Enumerated(EnumType.STRING)
    @Column(name = "tag")
    private Tags tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "uids" }, allowSetters = true)
    private Customers customers;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Posts id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return this.postId;
    }

    public Posts postId(Long postId) {
        this.setPostId(postId);
        return this;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Double getPrice() {
        return this.price;
    }

    public Posts price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getTitle() {
        return this.title;
    }

    public Posts title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return this.location;
    }

    public Posts location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Instant getAvailability() {
        return this.availability;
    }

    public Posts availability(Instant availability) {
        this.setAvailability(availability);
        return this;
    }

    public void setAvailability(Instant availability) {
        this.availability = availability;
    }

    public Integer getRating() {
        return this.rating;
    }

    public Posts rating(Integer rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Tags getTag() {
        return this.tag;
    }

    public Posts tag(Tags tag) {
        this.setTag(tag);
        return this;
    }

    public void setTag(Tags tag) {
        this.tag = tag;
    }

    public Customers getCustomers() {
        return this.customers;
    }

    public void setCustomers(Customers customers) {
        this.customers = customers;
    }

    public Posts customers(Customers customers) {
        this.setCustomers(customers);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Posts)) {
            return false;
        }
        return getId() != null && getId().equals(((Posts) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Posts{" +
            "id=" + getId() +
            ", postId=" + getPostId() +
            ", price=" + getPrice() +
            ", title='" + getTitle() + "'" +
            ", location='" + getLocation() + "'" +
            ", availability='" + getAvailability() + "'" +
            ", rating=" + getRating() +
            ", tag='" + getTag() + "'" +
            "}";
    }
}
