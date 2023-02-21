package board.application.controller;

import board.application.dto.InBoardDto;
import board.application.helper.ResponseHelper;
import board.application.mapper.BoardDtoMapper;
import board.domain.data.Board;
import board.domain.ports.api.BoardServiceImpl;
import board.domain.ports.api.BoardServicePort;
import board.infrastructure.adapter.BoardRepository;
import utils.enumerations.MethodHTTPEnum;

import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet(name="BoardServlet")
public class BoardController extends HttpServlet {

    BoardServicePort service;
    private BoardDtoMapper mapper;

    public BoardController() throws NamingException {
        this.service = new BoardServiceImpl(new BoardRepository());
        this.mapper = BoardDtoMapper.INSTANCE;
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
            Optional<InBoardDto> oInBoardDto = (Optional<InBoardDto>) request.getAttribute("dto");
            Optional<String> oParameter = (Optional<String>) request.getAttribute("parameter");
            if (oParameter.isPresent()) {
                this.dispatchActionWithParameter(response, oInBoardDto, oParameter.get(), oCurrentMethodHTTPEnum.get());
            } else {
                this.dispatchActionWithoutParameter(response, oInBoardDto, oCurrentMethodHTTPEnum.get());
            }
        }

    }

    private void dispatchActionWithParameter(HttpServletResponse response, Optional<InBoardDto> oInBoardDto, String parameter, MethodHTTPEnum methodHTTPEnum) throws SQLException, IOException {
        switch (methodHTTPEnum) {
            case DELETE:
                this.service.delete(parameter);
                response.setStatus(HttpServletResponse.SC_FOUND);
                break;
            case PUT:
                if (oInBoardDto.isPresent()) {
                    Board requestBoard = this.mapper.inDtoToDomain(oInBoardDto.get());
                    ResponseHelper.processResponse(response, mapper, this.service.update(requestBoard, parameter));
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

    private void dispatchActionWithoutParameter(HttpServletResponse response, Optional<InBoardDto> oInBoardDto, MethodHTTPEnum methodHTTPEnum) throws SQLException, IOException {
        switch (methodHTTPEnum) {
            case GET:
                ResponseHelper.processResponse(response, mapper, this.service.getAll());
                break;
            case POST:
                if (oInBoardDto.isPresent()) {
                    Board requestBoard = this.mapper.inDtoToDomain(oInBoardDto.get());
                    ResponseHelper.processResponse(response, mapper, this.service.add(requestBoard));
                }
                break;
            default:
                response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                break;
        }
    }
}
