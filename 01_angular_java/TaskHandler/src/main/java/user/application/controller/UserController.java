package user.application.controller;

import board.utils.LogsHelper;
import org.picocontainer.MutablePicoContainer;
import user.application.dto.InUserDto;
import user.application.helper.ResponseHelper;
import user.application.mapper.UserDtoMapper;
import user.domain.data.User;
import user.domain.ports.api.UserServiceImpl;
import user.domain.ports.api.UserServicePort;
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
import static utils.enumerations.ServletContextKey.USER_CONTAINER;

@WebServlet(name = "UserServlet")
public class UserController extends HttpServlet {

    private UserServicePort service;
    private final UserDtoMapper mapper;

    public UserController() {
        this.mapper = UserDtoMapper.INSTANCE;
    }

    @Override
    public void init() throws ServletException {
        super.init();
        final ServletContext servletContext = getServletContext();
        final MutablePicoContainer container = (MutablePicoContainer) ServletContextHelper.getAttribute(servletContext, USER_CONTAINER);
        this.service = container.getComponent(UserServiceImpl.class);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        this.processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        this.processRequest(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response){
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
            Optional<InUserDto> oInUserDto = (Optional<InUserDto>) request.getAttribute("dto");
            Optional<String> oParameter = (Optional<String>) request.getAttribute("parameter");
            try {
                this.dispatchAction(response, oInUserDto, oParameter, oCurrentMethodHTTPEnum);
            } catch (IOException exception) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (NoSuchMethodException exception) {
                response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            } catch (SQLException exception) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } catch (RecordNotFoundException exception) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }
    @HandleException
    private void dispatchAction(HttpServletResponse response, Optional<InUserDto> oInUserDto, Optional<String> oParameter, Optional<MethodHTTPEnum> oMethodHTTPEnum) throws IOException, SQLException, NoSuchMethodException, RecordNotFoundException {
        if (oParameter.isPresent()) {
            this.dispatchActionWithParameter(response, oInUserDto, oParameter.get(), oMethodHTTPEnum.get());
        } else {
            this.dispatchActionWithoutParameter(response, oInUserDto, oMethodHTTPEnum.get());
        }
    }

    private void dispatchActionWithParameter(HttpServletResponse response, Optional<InUserDto> oInUserDto, String parameter, MethodHTTPEnum methodHTTPEnum) throws SQLException, IOException, NoSuchMethodException, RecordNotFoundException {
        switch (methodHTTPEnum) {
            case DELETE:
                this.service.delete(parameter);
                response.setStatus(HttpServletResponse.SC_FOUND);
                LogsHelper.info(LoggerHelper.USER_CONTROLLER, String.format("Delete %s succeed.", parameter));
                break;
            case PUT:
                if (oInUserDto.isPresent()) {
                    User requestUser = this.mapper.inUserDtoToUser(oInUserDto.get());
                    ResponseHelper.processResponse(response, this.service.update(requestUser, parameter));
                    response.setStatus(HttpServletResponse.SC_FOUND);
                    LogsHelper.info(LoggerHelper.USER_CONTROLLER, String.format("Update %s succeed.", parameter));
                } else {
                    throw new InvalidObjectException("Input datas are not valid.");
                }
                break;
            case GET:
                Optional<User> oUser = ofNullable(this.service.getById(parameter));
                if (oUser.isPresent()) {
                    ResponseHelper.processResponse(response, oUser.get());
                    response.setStatus(HttpServletResponse.SC_FOUND);
                    LogsHelper.info(LoggerHelper.USER_CONTROLLER, String.format("Get %s succeed.", parameter));
                } else {
                    throw new RecordNotFoundException(String.format("Record with id %s was not found.", parameter));
                }
                break;
            default:
                throw new NoSuchMethodException("HTTP Method not allowed with parameter.");
        }
    }

    private void dispatchActionWithoutParameter(HttpServletResponse response, Optional<InUserDto> oInUserDto, MethodHTTPEnum methodHTTPEnum) throws SQLException, IOException, NoSuchMethodException {
        switch (methodHTTPEnum) {
            case GET:
                ResponseHelper.processResponse(response, this.service.getAll());
                response.setStatus(HttpServletResponse.SC_OK);
                break;
            case POST:
                if (oInUserDto.isPresent()) {
                    User requestUser = this.mapper.inUserDtoToUser(oInUserDto.get());
                    ResponseHelper.processResponse(response, this.service.add(requestUser));
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
