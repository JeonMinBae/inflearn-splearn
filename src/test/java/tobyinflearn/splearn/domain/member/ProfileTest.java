package tobyinflearn.splearn.domain.member;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class ProfileTest {

    @Test
    void profile() {
        new Profile("qweeq");
        new Profile("qweeq12332");
        new Profile("12414");
    }


    @Test
    void profileFail() {
        assertThatThrownBy(() -> new Profile("UpperCase")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Profile("tooooooooooooolongprofile")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Profile("한국어")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void url(){
        final Profile profile = new Profile("test");

        assertThat(profile.url()).isEqualTo("@test");
    }

}