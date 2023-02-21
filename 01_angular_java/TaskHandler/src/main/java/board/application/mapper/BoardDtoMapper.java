package board.application.mapper;

import board.application.dto.InBoardDto;
import board.application.dto.OutBoardDto;
import board.domain.data.Board;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import task.application.dto.InTaskDto;
import task.application.dto.OutTaskDto;
import task.domain.data.Task;

import java.util.List;

@Mapper
public interface BoardDtoMapper {
    BoardDtoMapper INSTANCE = Mappers.getMapper(BoardDtoMapper.class);

    Board inDtoToDomain(InBoardDto in);

    OutBoardDto domainToOutDto(Board domain);
    List<OutBoardDto> domainListToOutDto(List<Board> domainList);
}
