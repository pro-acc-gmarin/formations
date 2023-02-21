package board.application.helper;

import board.application.dto.InBoardDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import task.application.dto.InTaskDto;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class RequestHelper{

    static public Optional<InBoardDto> getDtoFromRequestBody(HttpServletRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(); // jackson
        String payload = getRequestBody(request);
        if(payload.isEmpty()){
            return Optional.empty();
        }else{
            return ofNullable(objectMapper.readValue(payload, InBoardDto.class));
        }
    }

    static public String getRequestBody(HttpServletRequest request) throws IOException {
        String line;
        BufferedReader bufferedReader = request.getReader();
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
}