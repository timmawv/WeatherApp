package avlyakulov.timur.service;

import avlyakulov.timur.custom_exception.ModelAlreadyExistsException;
import avlyakulov.timur.custom_exception.UserCredentialsException;
import avlyakulov.timur.dao.UserDao;
import avlyakulov.timur.dto.UserRegistrationDto;
import avlyakulov.timur.model.User;
import avlyakulov.timur.util.BCryptPassword;
import avlyakulov.timur.util.ContextUtil;
import avlyakulov.timur.util.authentication.LoginRegistrationValidation;
import avlyakulov.timur.util.thymeleaf.ThymeleafUtilRespondHtmlView;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.context.Context;

@Slf4j
public class UserService extends LoginRegistrationValidation implements BCryptPassword {

    private UserDao userDao;


    public UserService(UserDao userDao) {
        this.userDao = userDao;

    }

    public void createUser(User user) {
        user.setPassword(encryptPassword(user.getPassword()));
        userDao.create(user);
    }

    public User getUserByLoginAndPassword(String login, String password) {
        User user = userDao.getUserByLogin(login);
        if (user == null) {
            throw new UserCredentialsException("Login or password isn't correct");
        }
        if (!isPasswordCorrect(password, user.getPassword())) {
            throw new UserCredentialsException("Login or password isn't correct");
        }
        return user;
    }

    public boolean isUserLoginAndPasswordAreValid(Context context, UserRegistrationDto userDto) {
        if (isUserLoginValid(userDto.getLogin(), context) && isPasswordTheSameAndStrong(userDto.getPassword(), userDto.getConfirmPassword(), context)) {
            context.setVariable("success_registration", true);
            return true;
        }
        return false;
    }
}