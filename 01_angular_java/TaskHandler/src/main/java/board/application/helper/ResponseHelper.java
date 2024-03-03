package board.application.helper;

import board.application.dto.OutBoardDto;
import board.application.mapper.BoardDtoMapper;
import board.domain.data.Board;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ResponseHelper {

    static public String serializeOutBoardDtoListToJson(final List<OutBoardDto> outBoardDtoList) throws IOException {
        return new ObjectMapper().writeValueAsString(outBoardDtoList);
    }

    static public String serializeOutBoardDtoToJson(final OutBoardDto outBoardDto) throws IOException {
        return new ObjectMapper().writeValueAsString(outBoardDto);
    }

    static public void processResponse(final HttpServletResponse response, final OutBoardDto outBoardDto) throws IOException {
        final String taskJson = ResponseHelper.serializeOutBoardDtoToJson(outBoardDto);
        ResponseHelper.sendJson(response, taskJson);
    }

    static public void processResponse(final HttpServletResponse response, final BoardDtoMapper mapper, final List<Board> boardList) throws IOException {
        final List<OutBoardDto> outBoardDtoList = mapper.domainListToOutDto(boardList);
        final String taskJson = ResponseHelper.serializeOutBoardDtoListToJson(outBoardDtoList);
        ResponseHelper.sendJson(response, taskJson);
    }

    static public void sendJson(final HttpServletResponse response, final String json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}
