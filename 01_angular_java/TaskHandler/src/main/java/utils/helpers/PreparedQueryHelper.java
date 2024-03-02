package utils.helpers;

import utils.annotations.PreparedQuery;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class

PreparedQueryHelper {

    static public String getPreparedQueryValueWithoutParameter(final String methodName, final Class enclosingClass) throws NoSuchMethodException {
        final Method currentMethod = enclosingClass.getDeclaredMethod(getNotWeavedMethodName(methodName));
        final PreparedQuery annotation = currentMethod.getAnnotation(PreparedQuery.class);
        return annotation.value();
    }

    static public String getPreparedQueryValueWithParameter(final String methodName, final Class enclosingClass, final Class... parameterClass) throws NoSuchMethodException {
        final Method currentMethod = enclosingClass.getDeclaredMethod(getNotWeavedMethodName(methodName), parameterClass);
        final PreparedQuery annotation = currentMethod.getAnnotation(PreparedQuery.class);
        return annotation.value();
    }

    static public void setPasswordAndSaltWithIndex(final PreparedStatement statement, final int passwordIndex, final int saltIndex) throws SQLException {
        final String salt = PasswordHelper.HASH.generateSalt();
        final String password = PasswordHelper.GENERATOR.generatePassword();
        final Optional<String> oHashPassword = PasswordHelper.HASH.hashPassword(password, salt);

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

