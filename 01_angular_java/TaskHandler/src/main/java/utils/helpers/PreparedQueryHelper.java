package utils.helpers;

import utils.annotations.PreparedQuery;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class

PreparedQueryHelper {

    static public String getPreparedQueryValueWithoutParameter(String methodName, Class enclosingClass) throws NoSuchMethodException {
        Method currentMethod = enclosingClass.getDeclaredMethod(getNotWeavedMethodName(methodName));
        PreparedQuery annotation = currentMethod.getAnnotation(PreparedQuery.class);
        return annotation.value();
    }

    static public String getPreparedQueryValueWithParameter(String methodName, final Class enclosingClass, final Class... parameterClass) throws NoSuchMethodException {
        Method currentMethod = enclosingClass.getDeclaredMethod(getNotWeavedMethodName(methodName), parameterClass);
        PreparedQuery annotation = currentMethod.getAnnotation(PreparedQuery.class);
        return annotation.value();
    }

    static public void setPasswordAndSaltWithIndex(PreparedStatement statement, int passwordIndex, int saltIndex) throws SQLException {
        String salt = PasswordHelper.HASH.generateSalt();
        String password = PasswordHelper.GENERATOR.generatePassword();
        Optional<String> oHashPassword = PasswordHelper.HASH.hashPassword(password, salt);

        if(oHashPassword.isPresent()){
            statement.setString(saltIndex, salt);
            statement.setString(passwordIndex, oHashPassword.get());
        }
    }

    private static String getNotWeavedMethodName(final String methodName){
        if(methodName.contains("_")){
            return methodName.split("_")[0];
        }
        return methodName;
    }
}

