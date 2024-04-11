package avlyakulov.timur.service;

import avlyakulov.timur.custom_exception.UserCredentialsException;
import avlyakulov.timur.dao.UserDao;
import avlyakulov.timur.dto.UserRegistrationDto;
import avlyakulov.timur.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.context.Context;

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


    @Nested
    @Tag("login_password_validation")
    @DisplayName("Test user login validation functionality")
    class LoginPasswordValidation {

        private final Context context = new Context();

        private UserRegistrationDto userLoginValid = new UserRegistrationDto("timur", "cJ3SKJ2mb8BVkt", "cJ3SKJ2mb8BVkt");

        private UserRegistrationDto userLoginInvalid = new UserRegistrationDto("timur 1", "cJ3SKJ2mb8BVkt", "cJ3SKJ2mb8BVkt");

        private UserRegistrationDto userEmailLoginValid = new UserRegistrationDto("timur@gmail.com", "cJ3SKJ2mb8BVkt", "cJ3SKJ2mb8BVkt");

        private UserRegistrationDto userEmailLoginInvalid = new UserRegistrationDto("timur@.com", "cJ3SKJ2mb8BVkt", "cJ3SKJ2mb8BVkt");

        private UserRegistrationDto userPasswordInvalid = new UserRegistrationDto("timur", "dummy", "cJ3SKJ2mb8BVkt");

        private UserRegistrationDto userConfirmPasswordInvalid = new UserRegistrationDto("timur", "cJ3SKJ2mb8BVkt", "dummy");

        @Test
        void isUserLoginAndPasswordAreValid_returnTrue_userHasValidLoginAndPassword() {
            boolean userLoginAndPasswordAreValid = userService.isUserLoginAndPasswordAreValid(context, userLoginValid);

            assertThat(userLoginAndPasswordAreValid).isTrue();
        }

        @Test
        void isUserLoginAndPasswordAreValid_returnFalse_userHasInvalidLogin() {
            boolean userLoginAndPasswordAreValid = userService.isUserLoginAndPasswordAreValid(context, userLoginInvalid);

            assertThat(userLoginAndPasswordAreValid).isFalse();
        }

        @Test
        void isUserLoginAndPasswordAreValid_returnTrue_userHasValidEmailLogin() {
            boolean userLoginAndPasswordAreValid = userService.isUserLoginAndPasswordAreValid(context, userEmailLoginValid);

            assertThat(userLoginAndPasswordAreValid).isTrue();
        }

        @Test
        void isUserLoginAndPasswordAreValid_returnFalse_userHasInvalidEmailLogin() {
            boolean userLoginAndPasswordAreValid = userService.isUserLoginAndPasswordAreValid(context, userEmailLoginInvalid);

            assertThat(userLoginAndPasswordAreValid).isFalse();
        }

        @Test
        void isUserLoginAndPasswordAreValid_returnFalse_userHasWeakPassword() {
            boolean userLoginAndPasswordAreValid = userService.isUserLoginAndPasswordAreValid(context, userPasswordInvalid);

            assertThat(userLoginAndPasswordAreValid).isFalse();
        }

        @Test
        void isUserLoginAndPasswordAreValid_returnFalse_userHasInvalidConfirmPassword() {
            boolean userLoginAndPasswordAreValid = userService.isUserLoginAndPasswordAreValid(context, userConfirmPasswordInvalid);

            assertThat(userLoginAndPasswordAreValid).isFalse();
        }
    }
}