package board.application.controller;

import board.application.dto.InBoardDto;
import board.application.helper.ResponseHelper;
import board.application.mapper.BoardDtoMapper;
import board.domain.data.Board;
import board.domain.ports.api.BoardServiceImpl;
import board.domain.ports.api.BoardServicePort;
import board.infrastructure.adapter.BoardRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

@WebServlet(name = "BoardServlet")
public class BoardController extends HttpServlet {

    private static final Logger LOGGER = LogManager.getLogger(BoardController.class);

    BoardServicePort service;
    private final BoardDtoMapper mapper;

    public BoardController() throws NamingException {
        this.service = new BoardServiceImpl(new BoardRepository());
        this.mapper = BoardDtoMapper.INSTANCE;
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
            Optional<InBoardDto> oInBoardDto = (Optional<InBoardDto>) request.getAttribute("dto");
            Optional<String> oParameter = (Optional<String>) request.getAttribute("parameter");
            try {
                this.dispatchAction(response, oInBoardDto, oParameter, oCurrentMethodHTTPEnum);
            } catch (IOException exception) {
                LoggerHelper.logError(LOGGER, LoggerHelper.TASK_CONTROLLER, exception);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (NoSuchMethodException exception) {
                LoggerHelper.logError(LOGGER, LoggerHelper.TASK_CONTROLLER, exception);
                response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            } catch (SQLException exception) {
                LoggerHelper.logError(LOGGER, LoggerHelper.TASK_PERSISTENCE, exception);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    private void dispatchAction(HttpServletResponse response, Optional<InBoardDto> oInBoardDto, Optional<String> oParameter, Optional<MethodHTTPEnum> oMethodHTTPEnum) throws IOException, SQLException, NoSuchMethodException {
        if (oParameter.isPresent()) {
            this.dispatchActionWithParameter(response, oInBoardDto, oParameter.get(), oMethodHTTPEnum.get());
        } else {
            this.dispatchActionWithoutParameter(response, oInBoardDto, oMethodHTTPEnum.get());
        }
    }

    private void dispatchActionWithParameter(HttpServletResponse response, Optional<InBoardDto> oInBoardDto, String parameter, MethodHTTPEnum methodHTTPEnum) throws IOException, SQLException, NoSuchMethodException {
        switch (methodHTTPEnum) {
            case DELETE:
                this.service.delete(parameter);
                response.setStatus(HttpServletResponse.SC_FOUND);
                LoggerHelper.logInfo(LOGGER, LoggerHelper.BOARD_CONTROLLER, String.format("Delete %s succeed.", parameter));

                break;
            case PUT:
                if (oInBoardDto.isPresent()) {
                    Board requestBoard = this.mapper.inDtoToDomain(oInBoardDto.get());
                    ResponseHelper.processResponse(response, mapper, this.service.update(requestBoard, parameter));
                    response.setStatus(HttpServletResponse.SC_FOUND);
                    LoggerHelper.logInfo(LOGGER, LoggerHelper.BOARD_CONTROLLER, String.format("Update %s succeed.", parameter));

                } else {
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
                break;
            case GET:
                Optional<Board> oBoard = ofNullable(this.service.getById(parameter));
                if (oBoard.isPresent()) {
                    ResponseHelper.processResponse(response, mapper, oBoard.get());
                    response.setStatus(HttpServletResponse.SC_FOUND);
                    LoggerHelper.logInfo(LOGGER, LoggerHelper.BOARD_CONTROLLER, String.format("Get %s succeed.", parameter));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
                break;
            default:
                throw new NoSuchMethodException("HTTP Method not allowed with parameter.");
        }
    }

    private void dispatchActionWithoutParameter(HttpServletResponse
                                                        response, Optional<InBoardDto> oInBoardDto, MethodHTTPEnum methodHTTPEnum) throws SQLException, IOException, NoSuchMethodException {
        switch (methodHTTPEnum) {
            case GET:
                ResponseHelper.processResponse(response, mapper, this.service.getAll());
                response.setStatus(HttpServletResponse.SC_OK);
                break;
            case POST:
                if (oInBoardDto.isPresent()) {
                    Board requestBoard = this.mapper.inDtoToDomain(oInBoardDto.get());
                    ResponseHelper.processResponse(response, mapper, this.service.add(requestBoard));
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
