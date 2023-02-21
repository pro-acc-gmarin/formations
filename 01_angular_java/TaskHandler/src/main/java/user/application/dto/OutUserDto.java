package user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OutUserDto {
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String role;
}
