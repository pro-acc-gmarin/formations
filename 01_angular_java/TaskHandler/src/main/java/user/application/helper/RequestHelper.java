package user.application.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import user.application.dto.InUserDto;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class RequestHelper {

    static public Optional<Object> getDtoFromRequestBody(HttpServletRequest request, Class dtoClass) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(); // jackson
        String payload = getRequestBody(request);
        if(payload.isEmpty()){
            return Optional.empty();
        }else{
            return ofNullable(objectMapper.readValue(payload, dtoClass));
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
