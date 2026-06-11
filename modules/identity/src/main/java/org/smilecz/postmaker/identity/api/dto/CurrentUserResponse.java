package org.smilecz.postmaker.identity.api.dto;

import java.util.Set;

public record CurrentUserResponse(long  id, String email, Set<String> roles) {}
