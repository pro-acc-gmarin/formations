package board.infrastructure.mapper;

import board.domain.data.Board;
import board.infrastructure.entity.BoardPersistence;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import task.domain.data.Task;
import task.infrastructure.entity.TaskPersistence;

import java.util.List;

@Mapper
public interface BoardMapper {
    BoardMapper INSTANCE = Mappers.getMapper(BoardMapper.class);

    Board persistenceToDomain(final BoardPersistence persistence);
    BoardPersistence domainToPersistence(final Board domain);
    List<Board> persistenceListToDomainList(final List<BoardPersistence> persistenceList);
}
