package zipcode.rocks.domain;

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

    @Column(name = "price")
    private Double price;

    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "availability")
    private Instant availability;

    @Enumerated(EnumType.STRING)
    @Column(name = "tag")
    private Tags tag;

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
            ", price=" + getPrice() +
            ", title='" + getTitle() + "'" +
            ", location='" + getLocation() + "'" +
            ", availability='" + getAvailability() + "'" +
            ", tag='" + getTag() + "'" +
            "}";
    }
}
