package com.project.web.event;

import com.project.web.model.User;
import com.project.web.repo.UserRep;
import com.project.web.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class AuthenticationEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private UserRep userRep;

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        // Ottieni i dettagli dell'utente autenticato
        UserDetails userDetails = (UserDetails) event.getAuthentication().getPrincipal();

        // Recupera l'utente dal database e aggiorna l'ultimo accesso
        Optional<User> optionalUser = userRep.findByEmail(userDetails.getUsername());
        // Aggiorna lastLogin se l'utente è presente
        optionalUser.ifPresent(user -> userService.updateUserLastLogin(user.getId(), LocalDateTime.now()));
    }
}


