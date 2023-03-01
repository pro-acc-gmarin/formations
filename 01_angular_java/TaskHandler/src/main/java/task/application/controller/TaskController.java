package task.application.controller;

import board.application.controller.BoardController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import task.application.dto.InTaskDto;
import task.application.helper.ResponseHelper;
import task.application.mapper.TaskDtoMapper;
import task.domain.data.Task;
import task.domain.ports.api.TaskServiceImpl;
import task.domain.ports.api.TaskServicePort;
import task.infrastructure.adapter.TaskRepository;
import utils.annotations.HandleException;
import utils.enumerations.MethodHTTPEnum;
import utils.helpers.LoggerHelper;

import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@WebServlet(name = "TaskServlet")
public class TaskController extends HttpServlet {

    private static final Logger LOGGER = LogManager.getLogger(TaskController.class);

    TaskServicePort service;
    private final TaskDtoMapper mapper;

    public TaskController() throws NamingException {
        this.service = new TaskServiceImpl(new TaskRepository());
        this.mapper = TaskDtoMapper.INSTANCE;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        this.processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        this.processRequest(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        this.processRequest(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        this.processRequest(request, response);
    }

    @HandleException
    private void processRequest(HttpServletRequest request, HttpServletResponse response) {
        String method = request.getMethod();
        Optional<MethodHTTPEnum> oCurrentMethodHTTPEnum = MethodHTTPEnum.fromString(method);
        if (oCurrentMethodHTTPEnum.isPresent()) {
            Optional<InTaskDto> oInTaskDto = (Optional<InTaskDto>) request.getAttribute("dto");
            Optional<String> oParameter = (Optional<String>) request.getAttribute("parameter");
            try {
                this.dispatchAction(response, oInTaskDto, oParameter, oCurrentMethodHTTPEnum);
            } catch (IOException  exception) {
                LoggerHelper.logError(LOGGER, LoggerHelper.TASK_CONTROLLER, exception);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (NoSuchMethodException exception){
                LoggerHelper.logError(LOGGER, LoggerHelper.TASK_CONTROLLER, exception);
                response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            } catch (SQLException exception) {
                LoggerHelper.logError(LOGGER, LoggerHelper.TASK_PERSISTENCE, exception);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    private void dispatchAction(HttpServletResponse response, Optional<InTaskDto> oInTaskDto, Optional<String> oParameter, Optional<MethodHTTPEnum> oMethodHTTPEnum) throws SQLException, IOException, NoSuchMethodException {
        if (oParameter.isPresent()) {
            this.dispatchActionWithParameter(response, oInTaskDto, oParameter.get(), oMethodHTTPEnum.get());
        } else {
            this.dispatchActionWithoutParameter(response, oInTaskDto, oMethodHTTPEnum.get());
        }
    }

    private void dispatchActionWithParameter(HttpServletResponse response, Optional<InTaskDto> oInTaskDto, String parameter, MethodHTTPEnum methodHTTPEnum) throws IOException, SQLException, NoSuchMethodException {
        switch (methodHTTPEnum) {
            case DELETE:
                this.service.delete(parameter);
                response.setStatus(HttpServletResponse.SC_FOUND);
                LoggerHelper.logInfo(LOGGER, LoggerHelper.TASK_CONTROLLER, String.format("Delete %s succeed.", parameter));
                break;
            case PUT:
                if (oInTaskDto.isPresent()) {
                    Task requestTask = this.mapper.inDtoToDomain(oInTaskDto.get());
                    ResponseHelper.processResponse(response, mapper, this.service.update(requestTask, parameter));
                    response.setStatus(HttpServletResponse.SC_FOUND);
                    LoggerHelper.logInfo(LOGGER, LoggerHelper.TASK_CONTROLLER, String.format("Update %s succeed.", parameter));
                } else {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
                break;
            case GET:
                Optional<Task> oTask = ofNullable(this.service.getById(parameter));
                if (oTask.isPresent()) {
                    ResponseHelper.processResponse(response, mapper, oTask.get());
                    response.setStatus(HttpServletResponse.SC_FOUND);
                    LoggerHelper.logInfo(LOGGER, LoggerHelper.TASK_CONTROLLER, String.format("Get %s succeed.", parameter));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
                break;
            default:
                throw new NoSuchMethodException("HTTP Method not allowed with parameter.");
        }

    }

    private void dispatchActionWithoutParameter(HttpServletResponse response, Optional<InTaskDto> oInTaskDto, MethodHTTPEnum methodHTTPEnum) throws SQLException, IOException, NoSuchMethodException {
        switch (methodHTTPEnum) {
            case GET:
                ResponseHelper.processResponse(response, mapper, this.service.getAll());
                response.setStatus(HttpServletResponse.SC_OK);
                break;
            case POST:
                if (oInTaskDto.isPresent()) {
                    Task requestTask = this.mapper.inDtoToDomain(oInTaskDto.get());
                    ResponseHelper.processResponse(response, mapper, this.service.add(requestTask));
                    response.setStatus(HttpServletResponse.SC_CREATED);
                } else {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
                break;
            default:
                throw new NoSuchMethodException("HTTP Method not allowed with parameter.");
        }
    }
}
