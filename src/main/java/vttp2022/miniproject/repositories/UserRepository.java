package vttp2022.miniproject.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import vttp2022.miniproject.models.User;

import static vttp2022.miniproject.repositories.Queries.*;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate template;

    public boolean insertUser (String email, String password, String name) {
        int count = template.update(SQL_INSERT_NEW_USER, email, password, name);
        return count == 1;
    }
    
}
