package org.example.rest.dto;

import org.example.domain.enums.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}
