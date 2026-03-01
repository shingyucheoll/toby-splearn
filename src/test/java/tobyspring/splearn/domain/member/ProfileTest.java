package tobyspring.splearn.domain.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ProfileTest {

    @Test
    void profile() {
        new Profile("test1");
        new Profile("test");
        new Profile("test1666");
        new Profile("");
    }

    @Test
    void profileFail() {
        assertThatThrownBy(() -> new Profile("gkjhjhetgirerjge"))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Profile("A"))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Profile("프로필"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void url() {
        Profile profile = new Profile("test");
        assertThat(profile.url()).isEqualTo("@test");
    }
}