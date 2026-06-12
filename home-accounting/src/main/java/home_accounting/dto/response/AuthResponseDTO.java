package home_accounting.dto.response;

public record AuthResponseDTO(

        String token,
        String tokenType,

        Long id,
        String username,
        String email

) {
    public static AuthResponseDTO of(String token, Long id, String username, String email) {
        return new AuthResponseDTO(token, "Bearer", id, username, email);
    }
}
