package com.moongoeun.project.user.infrastructure.userdetails;

import com.moongoeun.project.user.domain.entity.UserEntity;
import com.moongoeun.project.user.domain.vo.RoleType;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class UserDetailsImpl implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final RoleType[] roles;
    private final String nickname;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, UserEntity user) {
        this.id = id;
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.roles = user.getRoles();
        this.nickname = user.getNickname();
        this.authorities = generateAuthorities(roles);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    private Collection<GrantedAuthority> generateAuthorities(RoleType[] roles) {
        return Arrays.stream(roles)
            .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
            .collect(Collectors.toList());
    }
}
