package task.application.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import task.application.dto.OutTaskDto;
import task.application.mapper.TaskDtoMapper;
import task.domain.data.Task;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ResponseHelper {

    static public String serializeOutTaskDtoListToJson(final List<OutTaskDto> outTaskDtoList) throws IOException {
        return new ObjectMapper().writeValueAsString(outTaskDtoList);
    }

    static public String serializeOutTaskDtoToJson(final OutTaskDto outTaskDto) throws IOException {
        return new ObjectMapper().writeValueAsString(outTaskDto);
    }

    static public void processResponse(final HttpServletResponse response, final OutTaskDto outTaskDto) throws IOException {
        final String taskJson = ResponseHelper.serializeOutTaskDtoToJson(outTaskDto);
        ResponseHelper.sendJson(response, taskJson);
    }

    static public void processResponse(final HttpServletResponse response, final TaskDtoMapper mapper, final List<Task> taskList) throws IOException {
        final List<OutTaskDto> outTaskDtoList = mapper.domainListToOutDto(taskList);
        final String taskJson = ResponseHelper.serializeOutTaskDtoListToJson(outTaskDtoList);
        ResponseHelper.sendJson(response, taskJson);
    }

    static public void sendJson(final HttpServletResponse response, final String json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}
