package task.application.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import task.application.dto.InTaskDto;
import task.application.dto.OutTaskDto;
import task.application.mapper.TaskDtoMapper;
import task.domain.data.Task;
import user.application.dto.InUserDto;
import user.application.dto.OutUserDto;
import user.application.mapper.UserDtoMapper;
import user.domain.data.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ResponseHelper {

    static public String serializeOutTaskDtoListToJson(List<OutTaskDto> outTaskDtoList) throws IOException {
        return new ObjectMapper().writeValueAsString(outTaskDtoList);
    }

    static public String serializeOutTaskDtoToJson(OutTaskDto outTaskDto) throws IOException {
        return new ObjectMapper().writeValueAsString(outTaskDto);
    }

    static public void processResponse(HttpServletResponse response, TaskDtoMapper mapper, Task task) throws IOException {
        OutTaskDto outTaskDto = mapper.domainToOutDto(task);
        String taskJson = ResponseHelper.serializeOutTaskDtoToJson(outTaskDto);
        ResponseHelper.sendJson(response, taskJson);
    }

    static public void processResponse(HttpServletResponse response, TaskDtoMapper mapper, List<Task> taskList) throws IOException {
        List<OutTaskDto> outTaskDtoList = mapper.domainListToOutDto(taskList);
        String taskJson = ResponseHelper.serializeOutTaskDtoListToJson(outTaskDtoList);
        ResponseHelper.sendJson(response, taskJson);
    }

    static public void sendJson(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}
