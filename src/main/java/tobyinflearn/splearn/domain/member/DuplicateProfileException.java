package tobyinflearn.splearn.domain.member;

public class DuplicateProfileException extends RuntimeException{
    public DuplicateProfileException(String message) {
        super(message);
    }

    public DuplicateProfileException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateProfileException(Throwable cause) {
        super(cause);
    }
}
