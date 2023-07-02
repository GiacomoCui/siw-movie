package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    /**
     * Restituisce un User dal DB in corrispondenza del suo ID
     * @param id dello User
     * @return Il relativo User, oppure null se non trovato
     */
    @Transactional
    public User getUser(Long id){
        Optional<User> result = this.userRepository.findById(id);
        return result.orElse(null);
    }

    /**
     * Salva un User nel DB
     * @param user da salvare nel DB
     * @return il salvataggio
     * @throws DataIntegrityViolationException se esiste già uno User nel DB
     */
    @Transactional
    public User saveUser(User user){
        return this.userRepository.save(user);
    }

    /**
     * Restituisce tutti gli User dal DB
     * @return List di tutti gli User presenti
     */
    @Transactional
    public List<User> getAllUsers(){
        List<User> result = new ArrayList<>();
        Iterable<User> users = this.userRepository.findAll();
        for(User user: users)
            result.add(user);
        return result;
    }

    @Transactional
    public UserDetails getUserDetails() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Transactional
    public void setUser(Credentials credentials, User user){
        credentials.setUser(user);
    }
}