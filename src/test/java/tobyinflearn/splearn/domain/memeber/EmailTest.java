package tobyinflearn.splearn.domain.memeber;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tobyinflearn.splearn.domain.shared.Email;

import static org.assertj.core.api.Assertions.assertThat;


class EmailTest {

    @Test
    @DisplayName("동등성 비교")
    void eq() {
        var email1 = new Email("test@test.test");
        var email2 = new Email("test@test.test");

        assertThat(email1).isEqualTo(email2);

    }

}