package rocks.zipcode.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static rocks.zipcode.domain.PostsTestSamples.*;
import static rocks.zipcode.domain.UsersTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import rocks.zipcode.web.rest.TestUtil;

class UsersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Users.class);
        Users users1 = getUsersSample1();
        Users users2 = new Users();
        assertThat(users1).isNotEqualTo(users2);

        users2.setId(users1.getId());
        assertThat(users1).isEqualTo(users2);

        users2 = getUsersSample2();
        assertThat(users1).isNotEqualTo(users2);
    }

    @Test
    void uidTest() {
        Users users = getUsersRandomSampleGenerator();
        Posts postsBack = getPostsRandomSampleGenerator();

        users.addUid(postsBack);
        assertThat(users.getUids()).containsOnly(postsBack);
        assertThat(postsBack.getPid()).isEqualTo(users);

        users.removeUid(postsBack);
        assertThat(users.getUids()).doesNotContain(postsBack);
        assertThat(postsBack.getPid()).isNull();

        users.uids(new HashSet<>(Set.of(postsBack)));
        assertThat(users.getUids()).containsOnly(postsBack);
        assertThat(postsBack.getPid()).isEqualTo(users);

        users.setUids(new HashSet<>());
        assertThat(users.getUids()).doesNotContain(postsBack);
        assertThat(postsBack.getPid()).isNull();
    }
}
