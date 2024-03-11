package avlyakulov.timur.dao;

import avlyakulov.timur.custom_exception.UserAlreadyExistsException;
import avlyakulov.timur.model.User;
import avlyakulov.timur.util.HibernateSingletonUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;

@Slf4j
public class UserDao {

    private final SessionFactory sessionFactory = HibernateSingletonUtil.getSessionFactory();

    public void create(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();//открываем транзакцию

            session.persist(user);

            session.getTransaction().commit();//закрываем транзакцию
        } catch (ConstraintViolationException e) {
            log.error("User with such login name {} already exists", user.getLogin());
            throw new UserAlreadyExistsException("User with such login name already exists");
        }
    }

    public User getById(int userId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, userId);
        }
    }

    public User getUserByLogin(String login) {
        try (Session session = sessionFactory.openSession()) {
            return session.createNamedQuery("HQL_FindUserByUsername", User.class)
                    .setParameter("userLogin", login)
                    .getSingleResult();
        }
    }
}