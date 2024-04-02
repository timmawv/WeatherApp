package avlyakulov.timur.dao;

import avlyakulov.timur.custom_exception.ModelAlreadyExistsException;
import avlyakulov.timur.model.User;
import avlyakulov.timur.util.hibernate.HibernateSingletonUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

@Slf4j
public class UserDao extends HibernateDao {


    public List<User> findAll() {
        return executeNotInTransaction(session -> session.createQuery("from User", User.class).getResultList());
    }

    public void create(User user) {
        try {
            executeInTransaction(session -> session.persist(user));
        } catch (ConstraintViolationException e) {
            log.error("User with such login name {} already exists", user.getLogin());
            throw new ModelAlreadyExistsException("User with such login name already exists");
        }
    }

    public User getUserByLogin(String login) {
        return executeNotInTransaction(session -> session.createNamedQuery("HQL_FindUserByUsername", User.class)
                .setParameter("userLogin", login)
                .getSingleResult());
    }
}