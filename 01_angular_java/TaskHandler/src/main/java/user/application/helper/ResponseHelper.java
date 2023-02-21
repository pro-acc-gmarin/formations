package user.application.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import user.application.dto.InUserDto;
import user.application.dto.OutUserDto;
import user.application.mapper.UserDtoMapper;
import user.domain.data.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class ResponseHelper {

    static public String serializeOutUserDtoListToJson(List<OutUserDto> outUserDtoList) throws IOException {
        return new ObjectMapper().writeValueAsString(outUserDtoList);
    }

    static public String serializeOutUserDtoToJson(OutUserDto outUserDto) throws IOException {
        return new ObjectMapper().writeValueAsString(outUserDto);
    }

    static public void processResponse(HttpServletResponse response, User user) throws IOException {
        OutUserDto outUserDto = UserDtoMapper.INSTANCE.userToOutUserDto(user);
        String userJson = ResponseHelper.serializeOutUserDtoToJson(outUserDto);
        ResponseHelper.sendJson(response, userJson);
    }

    static public void processResponse(HttpServletResponse response, List<User> userList) throws IOException {
        List<OutUserDto> outUserDtoList = UserDtoMapper.INSTANCE.userListToOutUserDto(userList);
        String userJson = ResponseHelper.serializeOutUserDtoListToJson(outUserDtoList);
        ResponseHelper.sendJson(response, userJson);
    }

    static public void sendJson(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}
