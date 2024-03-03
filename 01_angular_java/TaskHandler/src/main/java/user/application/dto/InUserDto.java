package user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InUserDto {
    private String firstname;
    private String lastname;
    private String email;


    public String toString(){
        return firstname+lastname+email;
    }
}
