package task.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import task.application.dto.InTaskDto;
import task.application.dto.OutTaskDto;
import task.domain.data.Task;
import user.application.dto.InUserDto;
import user.application.dto.OutUserDto;
import user.domain.data.User;

import java.util.List;

@Mapper
public interface TaskDtoMapper{
    TaskDtoMapper INSTANCE = Mappers.getMapper(TaskDtoMapper.class);

    Task inDtoToDomain(InTaskDto in);

    OutTaskDto domainToOutDto(Task domain);
    List<OutTaskDto> domainListToOutDto(List<Task> domainList);
}
