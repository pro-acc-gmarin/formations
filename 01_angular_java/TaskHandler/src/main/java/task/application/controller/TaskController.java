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
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) {
        this.processRequest(request, response);
    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response) {
        this.processRequest(request, response);
    }

    @Override
    protected void doPut(final HttpServletRequest request, final HttpServletResponse response) {
        this.processRequest(request, response);
    }

    @Override
    protected void doDelete(final HttpServletRequest request, final HttpServletResponse response) {
        this.processRequest(request, response);
    }

    private void processRequest(final HttpServletRequest request, final HttpServletResponse response) {
        final String method = request.getMethod();
        final Optional<MethodHTTPEnum> oCurrentMethodHTTPEnum = MethodHTTPEnum.fromString(method);
        if (oCurrentMethodHTTPEnum.isPresent()) {
            final Optional<InTaskDto> oInTaskDto = (Optional<InTaskDto>) request.getAttribute("dto");
            final Optional<String> oParameter = (Optional<String>) request.getAttribute("parameter");
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
    private void dispatchAction(final HttpServletResponse response, final Optional<InTaskDto> oInTaskDto, final Optional<String> oParameter, final Optional<MethodHTTPEnum> oMethodHTTPEnum) throws SQLException, IOException, NoSuchMethodException, RecordNotFoundException {
        if (oParameter.isPresent()) {
            this.dispatchActionWithParameter(response, oInTaskDto, oParameter.get(), oMethodHTTPEnum.get());
        } else {
            this.dispatchActionWithoutParameter(response, oInTaskDto, oMethodHTTPEnum.get());
        }
    }

    private void dispatchActionWithParameter(final HttpServletResponse response, final Optional<InTaskDto> oInTaskDto, final String parameter, final MethodHTTPEnum methodHTTPEnum) throws IOException, SQLException, NoSuchMethodException, RecordNotFoundException {
        switch (methodHTTPEnum) {
            case DELETE:
                this.service.delete(parameter);
                response.setStatus(HttpServletResponse.SC_FOUND);
                LogsHelper.info(LoggerHelper.TASK_CONTROLLER, String.format("Delete %s succeed.", parameter));
                break;
            case PUT:
                if (oInTaskDto.isPresent()) {
                    final Task requestTask = this.mapper.inDtoToDomain(oInTaskDto.get());
                    ResponseHelper.processResponse(response, mapper, this.service.update(requestTask, parameter));
                    response.setStatus(HttpServletResponse.SC_FOUND);
                    LogsHelper.info(LoggerHelper.TASK_CONTROLLER, String.format("Update %s succeed.", parameter));
                } else {
                    throw new InvalidObjectException("Input datas are not valid.");
                }
                break;
            case GET:
                final Optional<Task> oTask = ofNullable(this.service.getById(parameter));
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

    private void dispatchActionWithoutParameter(final HttpServletResponse response, final Optional<InTaskDto> oInTaskDto, final MethodHTTPEnum methodHTTPEnum) throws SQLException, IOException, NoSuchMethodException {
        switch (methodHTTPEnum) {
            case GET:
                ResponseHelper.processResponse(response, mapper, this.service.getAll());
                response.setStatus(HttpServletResponse.SC_OK);
                break;
            case POST:
                if (oInTaskDto.isPresent()) {
                    final Task requestTask = this.mapper.inDtoToDomain(oInTaskDto.get());
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
