package avlyakulov.timur.service;

import avlyakulov.timur.custom_exception.SessionNotValidException;
import avlyakulov.timur.dao.SessionDao;
import avlyakulov.timur.dto.UserDto;
import avlyakulov.timur.model.Session;
import avlyakulov.timur.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    private final User TIMUR = new User(1, "timur", "123");

    private final Session validUserSession = new Session(UUID.randomUUID().toString(), LocalDateTime.now().plusMinutes(30), TIMUR);

    private final Session invalidUserSession = new Session(UUID.randomUUID().toString(), LocalDateTime.now().minusMinutes(30), TIMUR);

    @Captor
    ArgumentCaptor<Session> argumentCaptor;

    @Mock
    private SessionDao sessionDao;

    @InjectMocks
    private SessionService sessionService;

    @Test
    void createSessionAndGetItsId_sessionCreated() {
        String maybeSessionId = sessionService.createSessionAndGetItsId(TIMUR);

        verify(sessionDao, times(1)).create(argumentCaptor.capture());

        Session maybeSession = argumentCaptor.getValue();
        assertThat(maybeSessionId).isNotBlank()
                .isNotEmpty()
                .isNotNull();
        assertThat(maybeSession.getId()).isNotNull();
        assertThat(maybeSession.getUser()).isEqualTo(TIMUR);
    }

    @Test
    void getUserSessionIfItNotExpired_userSessionNotExpiredAndExist() {
        doReturn(validUserSession).when(sessionDao).getById(any());

        Session maybeSession = sessionService.getUserSessionIfItNotExpired(validUserSession.getId());

        assertThat(maybeSession).isNotNull();
        assertThat(maybeSession).isEqualTo(validUserSession);
    }

    @Test
    void getUserSessionIfItNotExpired_userSessionNotExistInDB() {
        doReturn(null).when(sessionDao).getById(any());

        SessionNotValidException sessionNotValidException = assertThrows(SessionNotValidException.class, () -> sessionService.getUserSessionIfItNotExpired(validUserSession.getId()));
        assertThat(sessionNotValidException.getMessage()).isEqualTo("User session was expired or it doesn't exist");
    }

    @Test
    void getUserSessionIfItNotExpired_userSessionWasExpired() {
        doReturn(invalidUserSession).when(sessionDao).getById(any());

        SessionNotValidException sessionNotValidException = assertThrows(SessionNotValidException.class, () -> sessionService.getUserSessionIfItNotExpired(invalidUserSession.getId()));
        assertThat(sessionNotValidException.getMessage()).isEqualTo("User session was expired");
    }

    @Test
    void getUserDtoByHisSession() {
        UserDto userDto = sessionService.getUserDtoByHisSession(validUserSession);

        assertThat(userDto.getUserId()).isEqualTo(TIMUR.getId());
        assertThat(userDto.getLogin()).isEqualTo(TIMUR.getLogin());
    }

    @Test
    void isUserSessionValid_userSessionValid() {
        doReturn(true).when(sessionDao).isSessionValid(anyString());

        Boolean userSessionValid = sessionService.isUserSessionValid(anyString());

        assertThat(userSessionValid).isTrue();
    }

    @Test
    void isUserSessionValid_userSessionNotExistInDB() {
        doReturn(null).when(sessionDao).isSessionValid(anyString());

        assertThrows(SessionNotValidException.class, () -> sessionService.isUserSessionValid(anyString()));
    }

    @Test
    void isUserSessionValid_userSessionIsExpired() {
        doReturn(false).when(sessionDao).isSessionValid(anyString());

        Boolean userSessionValid = sessionService.isUserSessionValid(anyString());

        assertThat(userSessionValid).isFalse();
    }
}