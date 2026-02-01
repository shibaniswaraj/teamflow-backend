package com.teamflow.teamflow.controller;

import com.teamflow.teamflow.dto.UserResponse;
import com.teamflow.teamflow.dto.UserSummaryResponse;
import com.teamflow.teamflow.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * ADMIN only – paginated users list
     */
    @GetMapping
    public Page<UserResponse> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            Authentication authentication
    ) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Only ADMIN can access users"
            );
        }

        return userService.getUsersPaged(page, size);
    }

    /**
     * ADMIN / MANAGER – list of members
     */
    @GetMapping("/members")
    public java.util.List<UserSummaryResponse> getMembers(Authentication auth) {
        return userService.getAllMembers(auth);
    }
}
