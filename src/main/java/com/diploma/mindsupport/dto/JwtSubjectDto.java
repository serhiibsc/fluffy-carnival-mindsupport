package com.diploma.mindsupport.dto;

import com.diploma.mindsupport.model.UserRole;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Data
public class JwtSubjectDto {
    private String username;
    private Set<GrantedAuthority> userRoles;

    @Override
    public String toString() {
        return "username:" + username + "|userRoles:" +
                userRoles.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(","));
    }

    public static void main(String[] args) {
        JwtSubjectDto test = JwtSubjectDto.builder().username("lol").userRoles(Set.of(UserRole.PATIENT, UserRole.ADMIN)).build();
        System.out.println(
                test.toString()
                        .replaceAll("username:", "")
                        .replaceAll("\\|.+$", ""));
    }
}
