package zipcode.rocks.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Customers.
 */
@Entity
@Table(name = "customers")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Customers implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "user_name", nullable = false)
    private String userName;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "profile_pic_i_url")
    private String profilePicIUrl;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Customers id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public Customers email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return this.userName;
    }

    public Customers userName(String userName) {
        this.setUserName(userName);
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public Customers password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicIUrl() {
        return this.profilePicIUrl;
    }

    public Customers profilePicIUrl(String profilePicIUrl) {
        this.setProfilePicIUrl(profilePicIUrl);
        return this;
    }

    public void setProfilePicIUrl(String profilePicIUrl) {
        this.profilePicIUrl = profilePicIUrl;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customers)) {
            return false;
        }
        return getId() != null && getId().equals(((Customers) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Customers{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", userName='" + getUserName() + "'" +
            ", password='" + getPassword() + "'" +
            ", profilePicIUrl='" + getProfilePicIUrl() + "'" +
            "}";
    }
}
