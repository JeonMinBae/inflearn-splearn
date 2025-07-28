package tobyinflearn.splearn.domain;

import java.util.regex.Pattern;


public record Email(
    String address
) {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);

    public Email {
        if (!EMAIL_PATTERN.matcher(address).matches()) {
            throw new IllegalArgumentException("유효하지 않은 이메일 형식입니다.");
        }
    }

}
