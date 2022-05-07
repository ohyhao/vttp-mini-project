package vttp2022.miniproject.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import vttp2022.miniproject.models.User;

import static vttp2022.miniproject.repositories.Queries.*;
import static vttp2022.miniproject.models.ConversionUtils.*;

@Service
public class UserService {

    @Autowired
    private JdbcTemplate template;

    public Optional<User> findUserByEmailAndPassword(String email, String password) {
        
        final SqlRowSet rs = template.queryForRowSet(
            SQL_SELECT_USER_BY_EMAIL_AND_PASSWORD, email, password);
        if (!rs.next())
            return Optional.empty();
        
        return Optional.of(convert(rs));
        
    }  
}
