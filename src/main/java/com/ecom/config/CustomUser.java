package com.ecom.config;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ecom.model.UserDtls;

public class CustomUser implements UserDetails{

	private UserDtls user;
	

	public CustomUser(UserDtls user) {
		super();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority=new SimpleGrantedAuthority(user.getRole());
		return Arrays.asList(authority);
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}
	
	 @Override
	    public boolean isAccountNonExpired() {
	        // Return true if the account is not expired.
	        return true;
	    }

	    @Override
	    public boolean isAccountNonLocked() {
	        // Return true if the account is not locked.
	        return user.getAccountNonLocked();
	    }

	    @Override
	    public boolean isCredentialsNonExpired() {
	        // Return true if the credentials are not expired.
	        return true;
	    }

	    @Override
	    public boolean isEnabled() {
			return user.getIsEnable();
		}
	    // Getter for UserDtls if needed
	    public UserDtls getUser() {
	        return user;
	    }
}
