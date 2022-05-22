package vttp2022.miniproject.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import vttp2022.miniproject.models.User;
import vttp2022.miniproject.repositories.UserRepository;

import static vttp2022.miniproject.repositories.Queries.*;
import static vttp2022.miniproject.models.ConversionUtils.*;

@Service
public class UserService {

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private UserRepository userRepo;

    public Optional<User> findUserByEmailAndPassword(String email, String password) {
        
        final SqlRowSet rs = template.queryForRowSet(
            SQL_SELECT_USER_BY_EMAIL_AND_PASSWORD, email, password);
        if (!rs.next())
            return Optional.empty();
        
        return Optional.of(convert(rs));
        
    }

    public void addNewUser(String email, String password, String name) {
        
        Optional<User> opt = userRepo.findUserByEmail(email);
        try {
            if (opt.isPresent()) 
                throw new IllegalArgumentException("%s is already used, please login or use other email".formatted(email));
                
            if (!userRepo.insertUser(email, password, name))
                throw new IllegalArgumentException("Unable to create user! Please try again!");
        } catch (Exception ex) {
            ex.getStackTrace();
            throw ex;
        } 
    }
}
