package org.smile.cz.postmaker.shared.result;

import java.io.Serializable;

public record ErrorCode(String code, String message,ErrorType type) implements Serializable {

    public static final ErrorCode INVALID_CREDENTIALS = new ErrorCode("identity.invalid_email_or_password","Invalid email or password.",ErrorType.VALIDATION);
    public static final ErrorCode USER_ALREADY_EXISTS = new ErrorCode("identity.user_already_exists","User with the given email already exists.",ErrorType.CONFLICT);
    public static final ErrorCode USER_NOT_ACTIVE = new ErrorCode("identity.user_not_active","User is not active. Please check your email",ErrorType.NONVALIDATE);
}
