package org.smilecz.postmaker.identity.api.dto;

import javax.management.relation.Role;
import java.util.Set;

public record CurrentUserResponse(long  id, String email, Set<String> roles) {}
