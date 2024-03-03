package user.application.dto;

import lombok.*;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OutUserDto {
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String role;
    public String toString(){
        return firstname+lastname+email;
    }
}
