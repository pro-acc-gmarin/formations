package user.application.controller;

import user.application.dto.InUserDto;
import user.application.helper.ResponseHelper;
import user.application.mapper.UserDtoMapper;
import user.domain.data.User;
import user.domain.ports.api.UserServiceImpl;
import user.domain.ports.api.UserServicePort;
import user.infrastructure.adapter.UserRepository;
import utils.enumerations.MethodHTTPEnum;

import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet(name = "UserServlet")
public class UserController extends HttpServlet {

    UserServicePort service;

    public UserController() throws NamingException {
        this.service = new UserServiceImpl(new UserRepository());
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
            Optional<InUserDto> oInUserDto = (Optional<InUserDto>) request.getAttribute("dto");
            Optional<String> oParameter = (Optional<String>) request.getAttribute("parameter");
            if (oParameter.isPresent()) {
                this.dispatchActionWithParameter(response, oInUserDto, oParameter.get(), oCurrentMethodHTTPEnum.get());
            } else {
                this.dispatchActionWithoutParameter(response, oInUserDto, oCurrentMethodHTTPEnum.get());
            }
        }

    }

    private void dispatchActionWithParameter(HttpServletResponse response, Optional<InUserDto> oInUserDto, String parameter, MethodHTTPEnum methodHTTPEnum) throws SQLException, IOException {
        switch (methodHTTPEnum) {
            case DELETE:
                this.service.delete(parameter);
                response.setStatus(HttpServletResponse.SC_FOUND);
                break;
            case PUT:
                if (oInUserDto.isPresent()) {
                    User requestUser = UserDtoMapper.INSTANCE.inUserDtoToUser(oInUserDto.get());
                    ResponseHelper.processResponse(response, this.service.update(requestUser, parameter));
                }

                break;
            case GET:
                ResponseHelper.processResponse(response, this.service.getById(parameter));
                break;
            default:
                response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                break;
        }
    }

    private void dispatchActionWithoutParameter(HttpServletResponse response, Optional<InUserDto> oInUserDto, MethodHTTPEnum methodHTTPEnum) throws SQLException, IOException {
        switch (methodHTTPEnum) {
            case GET:
                ResponseHelper.processResponse(response, this.service.getAll());
                break;
            case POST:
                if (oInUserDto.isPresent()) {
                    User requestUser = UserDtoMapper.INSTANCE.inUserDtoToUser(oInUserDto.get());
                    ResponseHelper.processResponse(response, this.service.add(requestUser));
                }
                break;
            default:
                response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                break;
        }
    }
}
