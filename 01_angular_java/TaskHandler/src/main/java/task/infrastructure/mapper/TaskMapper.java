package task.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import task.domain.data.Task;
import task.infrastructure.entity.TaskPersistence;
import user.domain.data.User;
import user.infrastructure.entity.UserPersistence;

import java.util.List;

@Mapper
public interface TaskMapper {
    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    Task persistenceToDomain(final TaskPersistence persistence);
    TaskPersistence domainToPersistence(final Task domain);
    List<Task> persistenceListToDomainList(final List<TaskPersistence> persistenceList);
}
