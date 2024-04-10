package avlyakulov.timur.service;

import avlyakulov.timur.custom_exception.UserCredentialsException;
import avlyakulov.timur.dao.UserDao;
import avlyakulov.timur.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private final User TIMUR = new User("timur", "111");

    private final User TIMUR_CREATED = new User("timur", "$2a$10$WNiCiKGnmhWxuwWfRFtZZeze7vMDsHgnPy2fEqrLyyqtsuIv6KpkG");

    private final User INVALID_USER = new User("dummy", "dummy");

    @Mock
    private UserDao userDao;
    @Captor
    private ArgumentCaptor<User> argumentCaptor;
    @InjectMocks
    private UserService userService;


    //MethodName_ExpectedBehavior_StateUnderTest
    @Test
    void createUser_userWasCreated() {
        userService.createUser(TIMUR);


        Mockito.verify(userDao, times(1)).create(argumentCaptor.capture());

        String encryptedPasswordUser = argumentCaptor.getValue().getPassword();
        assertThat(userService.isPasswordCorrect("111", encryptedPasswordUser)).isTrue();
    }

    @Test
    void logUserByCredentials_userWasLogged_validCredentials() {
        doReturn(TIMUR_CREATED).when(userDao).getUserByLogin(TIMUR.getLogin());

        User maybeUser = userService.getUserByLoginAndPassword(TIMUR.getLogin(), TIMUR.getPassword());

        verify(userDao, times(1)).getUserByLogin(TIMUR.getLogin());
        assertThat(maybeUser.getLogin()).isEqualTo(TIMUR.getLogin());
        assertThat(maybeUser.getPassword()).isEqualTo(TIMUR_CREATED.getPassword());
    }

    @Test
    void logUserByCredentials_userWasNotLogged_invalidLogin() {
        doReturn(null).when(userDao).getUserByLogin(INVALID_USER.getLogin());

        UserCredentialsException userCredentialsException = assertThrows(UserCredentialsException.class, () -> userService.getUserByLoginAndPassword(INVALID_USER.getLogin(), INVALID_USER.getPassword()));

        verify(userDao, times(1)).getUserByLogin(INVALID_USER.getLogin());
        assertThat(userCredentialsException.getMessage()).isEqualTo("Login or password isn't correct");
    }

    @Test
    void logUserByCredentials_userWasNotLogged_invalidPassword() {
        doReturn(TIMUR_CREATED).when(userDao).getUserByLogin(TIMUR.getLogin());

        UserCredentialsException userCredentialsException = assertThrows(UserCredentialsException.class, () -> userService.getUserByLoginAndPassword(TIMUR.getLogin(), INVALID_USER.getPassword()));

        verify(userDao, times(1)).getUserByLogin(TIMUR.getLogin());
        assertThat(userCredentialsException.getMessage()).isEqualTo("Login or password isn't correct");
    }
}