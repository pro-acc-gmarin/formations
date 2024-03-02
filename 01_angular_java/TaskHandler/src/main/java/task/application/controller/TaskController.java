package task.application.controller;

import board.utils.LogsHelper;
import org.picocontainer.MutablePicoContainer;
import task.application.dto.InTaskDto;
import task.application.helper.ResponseHelper;
import task.application.mapper.TaskDtoMapper;
import task.domain.data.Task;
import task.domain.ports.api.TaskServiceImpl;
import task.domain.ports.api.TaskServicePort;
import utils.annotations.HandleException;
import utils.enumerations.MethodHTTPEnum;
import utils.exception.RecordNotFoundException;
import utils.helpers.LoggerHelper;
import utils.helpers.ServletContextHelper;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.sql.SQLException;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static utils.enumerations.ServletContextKey.TASK_CONTAINER;

@WebServlet(name = "TaskServlet")
public class TaskController extends HttpServlet {

    private TaskServicePort service;
    private final TaskDtoMapper mapper;

    public TaskController() {
        this.mapper = TaskDtoMapper.INSTANCE;
    }

    @Override
    public void init() throws ServletException {
        super.init();
        final ServletContext servletContext = getServletContext();
        final MutablePicoContainer container = (MutablePicoContainer) ServletContextHelper.getAttribute(servletContext, TASK_CONTAINER);
        this.service = container.getComponent(TaskServiceImpl.class);
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

    private void processRequest(HttpServletRequest request, HttpServletResponse response) {
        String method = request.getMethod();
        Optional<MethodHTTPEnum> oCurrentMethodHTTPEnum = MethodHTTPEnum.fromString(method);
        if (oCurrentMethodHTTPEnum.isPresent()) {
            Optional<InTaskDto> oInTaskDto = (Optional<InTaskDto>) request.getAttribute("dto");
            Optional<String> oParameter = (Optional<String>) request.getAttribute("parameter");
            try {
                this.dispatchAction(response, oInTaskDto, oParameter, oCurrentMethodHTTPEnum);
            } catch (IOException exception) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (NoSuchMethodException exception){
                response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            } catch (SQLException exception) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } catch (RecordNotFoundException exception) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    @HandleException
    private void dispatchAction(HttpServletResponse response, Optional<InTaskDto> oInTaskDto, Optional<String> oParameter, Optional<MethodHTTPEnum> oMethodHTTPEnum) throws SQLException, IOException, NoSuchMethodException, RecordNotFoundException {
        if (oParameter.isPresent()) {
            this.dispatchActionWithParameter(response, oInTaskDto, oParameter.get(), oMethodHTTPEnum.get());
        } else {
            this.dispatchActionWithoutParameter(response, oInTaskDto, oMethodHTTPEnum.get());
        }
    }

    private void dispatchActionWithParameter(HttpServletResponse response, Optional<InTaskDto> oInTaskDto, String parameter, MethodHTTPEnum methodHTTPEnum) throws IOException, SQLException, NoSuchMethodException, RecordNotFoundException {
        switch (methodHTTPEnum) {
            case DELETE:
                this.service.delete(parameter);
                response.setStatus(HttpServletResponse.SC_FOUND);
                LogsHelper.info(LoggerHelper.TASK_CONTROLLER, String.format("Delete %s succeed.", parameter));
                break;
            case PUT:
                if (oInTaskDto.isPresent()) {
                    Task requestTask = this.mapper.inDtoToDomain(oInTaskDto.get());
                    ResponseHelper.processResponse(response, mapper, this.service.update(requestTask, parameter));
                    response.setStatus(HttpServletResponse.SC_FOUND);
                    LogsHelper.info(LoggerHelper.TASK_CONTROLLER, String.format("Update %s succeed.", parameter));
                } else {
                    throw new InvalidObjectException("Input datas are not valid.");
                }
                break;
            case GET:
                Optional<Task> oTask = ofNullable(this.service.getById(parameter));
                if (oTask.isPresent()) {
                    ResponseHelper.processResponse(response, mapper, oTask.get());
                    response.setStatus(HttpServletResponse.SC_FOUND);
                    LogsHelper.info(LoggerHelper.TASK_CONTROLLER, String.format("Get %s succeed.", parameter));
                } else {
                    throw new RecordNotFoundException(String.format("Record with id %s was not found.", parameter));
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
                    throw new InvalidObjectException("Input datas are not valid.");
                }
                break;
            default:
                throw new NoSuchMethodException("HTTP Method not allowed with parameter.");
        }
    }
}
