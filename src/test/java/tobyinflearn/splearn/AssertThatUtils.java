package tobyinflearn.splearn;

import org.assertj.core.api.AssertProvider;
import org.springframework.test.json.JsonPathValueAssert;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;


public class AssertThatUtils {

    public static Consumer<AssertProvider<JsonPathValueAssert>> notNull() {
        return value -> {
            assertThat(value).isNotNull();
        };
    }

    public static <T> Consumer<AssertProvider<JsonPathValueAssert>> eqTo(T target) {
        return value -> {
            assertThat(value).isEqualTo(target);
        };
    }
}
