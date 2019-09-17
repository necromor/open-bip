package net.jewczuk.openbip.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import net.jewczuk.openbip.entity.EditorEntity;
import net.jewczuk.openbip.repository.EditorRepository;

@Component
public class CustomUserDetailService implements UserDetailsService {
	
	@Autowired
	private EditorRepository editorRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		EditorEntity user = editorRepository.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException(email);
		}
		return new CustomUserDetails(user);
	}

}
