package user.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import user.application.dto.InUserDto;
import user.application.dto.OutUserDto;
import user.domain.data.User;

import java.util.List;

@Mapper
public interface UserDtoMapper {
    UserDtoMapper INSTANCE = Mappers.getMapper(UserDtoMapper.class);

    User inUserDtoToUser(InUserDto userDto);

    OutUserDto userToOutUserDto(User user);
    List<OutUserDto> userListToOutUserDto(List<User> userList);
}
