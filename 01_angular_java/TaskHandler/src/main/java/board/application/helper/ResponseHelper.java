package board.application.helper;

import board.application.dto.InBoardDto;
import board.application.dto.OutBoardDto;
import board.application.mapper.BoardDtoMapper;
import board.domain.data.Board;
import com.fasterxml.jackson.databind.ObjectMapper;
import task.application.dto.OutTaskDto;
import task.application.mapper.TaskDtoMapper;
import task.domain.data.Task;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ResponseHelper {

    static public String serializeOutBoardDtoListToJson(List<OutBoardDto> outBoardDtoList) throws IOException {
        return new ObjectMapper().writeValueAsString(outBoardDtoList);
    }

    static public String serializeOutBoardDtoToJson(OutBoardDto outBoardDto) throws IOException {
        return new ObjectMapper().writeValueAsString(outBoardDto);
    }

    static public void processResponse(HttpServletResponse response, BoardDtoMapper mapper, Board board) throws IOException {
        OutBoardDto outBoardDto = mapper.domainToOutDto(board);
        String taskJson = ResponseHelper.serializeOutBoardDtoToJson(outBoardDto);
        ResponseHelper.sendJson(response, taskJson);
    }

    static public void processResponse(HttpServletResponse response, BoardDtoMapper mapper, List<Board> boardList) throws IOException {
        List<OutBoardDto> outBoardDtoList = mapper.domainListToOutDto(boardList);
        String taskJson = ResponseHelper.serializeOutBoardDtoListToJson(outBoardDtoList);
        ResponseHelper.sendJson(response, taskJson);
    }

    static public void sendJson(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}
