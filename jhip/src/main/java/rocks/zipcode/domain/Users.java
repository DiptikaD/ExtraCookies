package rocks.zipcode.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Users.
 */
@Entity
@Table(name = "users")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "uid", nullable = false)
    private Long uid;

    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "user_name", nullable = false)
    private String userName;

    @NotNull
    @Column(name = "pass_word", nullable = false)
    private String passWord;

    @Column(name = "profile_pic_i_url")
    private String profilePicIUrl;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pid")
    @JsonIgnoreProperties(value = { "pid", "customers" }, allowSetters = true)
    private Set<Posts> uids = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Users id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return this.uid;
    }

    public Users uid(Long uid) {
        this.setUid(uid);
        return this;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return this.email;
    }

    public Users email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return this.userName;
    }

    public Users userName(String userName) {
        this.setUserName(userName);
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return this.passWord;
    }

    public Users passWord(String passWord) {
        this.setPassWord(passWord);
        return this;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getProfilePicIUrl() {
        return this.profilePicIUrl;
    }

    public Users profilePicIUrl(String profilePicIUrl) {
        this.setProfilePicIUrl(profilePicIUrl);
        return this;
    }

    public void setProfilePicIUrl(String profilePicIUrl) {
        this.profilePicIUrl = profilePicIUrl;
    }

    public Set<Posts> getUids() {
        return this.uids;
    }

    public void setUids(Set<Posts> posts) {
        if (this.uids != null) {
            this.uids.forEach(i -> i.setPid(null));
        }
        if (posts != null) {
            posts.forEach(i -> i.setPid(this));
        }
        this.uids = posts;
    }

    public Users uids(Set<Posts> posts) {
        this.setUids(posts);
        return this;
    }

    public Users addUid(Posts posts) {
        this.uids.add(posts);
        posts.setPid(this);
        return this;
    }

    public Users removeUid(Posts posts) {
        this.uids.remove(posts);
        posts.setPid(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Users)) {
            return false;
        }
        return getId() != null && getId().equals(((Users) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Users{" +
            "id=" + getId() +
            ", uid=" + getUid() +
            ", email='" + getEmail() + "'" +
            ", userName='" + getUserName() + "'" +
            ", passWord='" + getPassWord() + "'" +
            ", profilePicIUrl='" + getProfilePicIUrl() + "'" +
            "}";
    }
}
