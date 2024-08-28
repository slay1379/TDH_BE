package ToDo.example.authentication;

public class TokenExtractor {
    public static String extract(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new IllegalStateException("유효하지 않은 토큰입니다.");
    }
}
