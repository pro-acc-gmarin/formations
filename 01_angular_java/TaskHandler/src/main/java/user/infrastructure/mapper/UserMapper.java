package user.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import task.infrastructure.mapper.TaskMapper;
import user.domain.data.User;
import user.infrastructure.entity.UserPersistence;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User persistenceToDomain(UserPersistence persistence);

    UserPersistence domainToPersistence(User domain);

    List<User> persistenceListToDomainList(List<UserPersistence> persistenceList);
}
