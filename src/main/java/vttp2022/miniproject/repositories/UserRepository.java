package vttp2022.miniproject.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2022.miniproject.models.User;

import static vttp2022.miniproject.repositories.Queries.*;
import static vttp2022.miniproject.models.ConversionUtils.*;

import java.util.Optional;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate template;

    public Optional<User> findUserByEmail (String email) {
        final SqlRowSet rs = template.queryForRowSet(SQL_FIND_USER_BY_EMAIL, email);
        if (!rs.next()) {
            return Optional.empty();
        }
        return Optional.of(convert(rs));
    }

    public boolean insertUser (String email, String password, String name) {
        int count = template.update(SQL_INSERT_NEW_USER, email, password, name);
        return count == 1;
    }
    
}
