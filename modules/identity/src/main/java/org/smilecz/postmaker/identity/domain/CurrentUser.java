package org.smilecz.postmaker.identity.domain;

import java.util.Set;

public record CurrentUser(long id, String email, Set<String> roles) { }
