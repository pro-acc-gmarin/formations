package task.application.controller;

import task.application.dto.InTaskDto;
import task.application.helper.ResponseHelper;
import task.application.mapper.TaskDtoMapper;
import task.domain.data.Task;
import task.domain.ports.api.TaskServiceImpl;
import task.domain.ports.api.TaskServicePort;
import task.infrastructure.adapter.TaskRepository;
import utils.enumerations.MethodHTTPEnum;

import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet(name="TaskServlet")
public class TaskController extends HttpServlet {

    TaskServicePort service;
    private TaskDtoMapper mapper;

    public TaskController() throws NamingException {
        this.service = new TaskServiceImpl(new TaskRepository());
        this.mapper = TaskDtoMapper.INSTANCE;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            this.processRequest(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            this.processRequest(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            this.processRequest(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            this.processRequest(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String method = request.getMethod();
        Optional<MethodHTTPEnum> oCurrentMethodHTTPEnum = MethodHTTPEnum.fromString(method);
        if (oCurrentMethodHTTPEnum.isPresent()) {
            Optional<InTaskDto> oInTaskDto = (Optional<InTaskDto>) request.getAttribute("dto");
            Optional<String> oParameter = (Optional<String>) request.getAttribute("parameter");
            if (oParameter.isPresent()) {
                this.dispatchActionWithParameter(response, oInTaskDto, oParameter.get(), oCurrentMethodHTTPEnum.get());
            } else {
                this.dispatchActionWithoutParameter(response, oInTaskDto, oCurrentMethodHTTPEnum.get());
            }
        }

    }

    private void dispatchActionWithParameter(HttpServletResponse response, Optional<InTaskDto> oInTaskDto, String parameter, MethodHTTPEnum methodHTTPEnum) throws SQLException, IOException {
        switch (methodHTTPEnum) {
            case DELETE:
                this.service.delete(parameter);
                response.setStatus(HttpServletResponse.SC_FOUND);
                break;
            case PUT:
                if (oInTaskDto.isPresent()) {
                    Task requestTask = this.mapper.inDtoToDomain(oInTaskDto.get());
                    ResponseHelper.processResponse(response, mapper, this.service.update(requestTask, parameter));
                }

                break;
            case GET:
                ResponseHelper.processResponse(response, mapper, this.service.getById(parameter));
                break;
            default:
                response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                break;
        }
    }

    private void dispatchActionWithoutParameter(HttpServletResponse response, Optional<InTaskDto> oInTaskDto, MethodHTTPEnum methodHTTPEnum) throws SQLException, IOException {
        switch (methodHTTPEnum) {
            case GET:
                ResponseHelper.processResponse(response, mapper, this.service.getAll());
                break;
            case POST:
                if (oInTaskDto.isPresent()) {
                    Task requestTask = this.mapper.inDtoToDomain(oInTaskDto.get());
                    ResponseHelper.processResponse(response, mapper, this.service.add(requestTask));
                }
                break;
            default:
                response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                break;
        }
    }
}
