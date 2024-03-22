package avlyakulov.timur.util.api.converter;

import avlyakulov.timur.model.User;
import com.fasterxml.jackson.databind.util.StdConverter;

public class UserIdConverter extends StdConverter<Integer, User> {
    @Override
    public User convert(Integer userId) {
        return new User(userId);
    }
}
