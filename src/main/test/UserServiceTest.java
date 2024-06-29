import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.User;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.repository.UserRepository;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.service.UserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpSession session;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private User existingUser;
    private SecurityContext securityContext;
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .userName("newUser")
                .password("newPassword")
                .build();

        existingUser = User.builder()
                .id(1L)
                .userName("existingUser")
                .password("existingPassword")
                .build();

        securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(
                new UsernamePasswordAuthenticationToken("existingUser", "existingPassword")
        );
        SecurityContextHolder.setContext(securityContext);

        authentication = new UsernamePasswordAuthenticationToken("existingUser", "existingPassword");
    }

    @Test
    public void testRegisterUserSuccess() {
        when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });


        User registeredUser = userService.register(user);

        assertThat(registeredUser).isNotNull();
        assertThat(registeredUser.getId()).isNotNull();
        assertThat(registeredUser.getUserName()).isEqualTo(user.getUserName());
        assertThat(registeredUser.getPassword()).isEqualTo("encodedPassword");
        assertThat(registeredUser.getMoney()).isEqualTo(0.0);
        assertThat(registeredUser.getOverallTransactionsProfit()).isEqualTo(0.0);
        verify(userRepository).findByUserName(user.getUserName());
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(user.getPassword());
    }

    @Test
    public void testRegisterUserAlreadyExists() {
        when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.register(user))
                .isInstanceOf(DataIntegrityViolationException.class);

        verify(userRepository).findByUserName(user.getUserName());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testLoginSuccess() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        userService.login(user, session);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isEqualTo(authentication);
    }


    @Test
    public void testLoginFailure() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        assertThatThrownBy(() -> userService.login(user, session))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Authentication failed");
    }

    @Test
    public void testUpdateCredentialsSuccess() {
        when(userRepository.findByUserName("existingUser")).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUserName("newUser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });

        User updatedUser = userService.updateCredentials(user);

        assertThat(updatedUser.getUserName()).isEqualTo("newUser");
        assertThat(updatedUser.getPassword()).isEqualTo("encodedNewPassword");
    }

    @Test
    public void testUpdateCredentialsUserNotFound() {
        when(userRepository.findByUserName("existingUser")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateCredentials(user))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User with name 'existingUser' is not existed");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testUpdateCredentialsNewUserNameAlreadyExists() {
        when(userRepository.findByUserName("existingUser")).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUserName("newUser")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.updateCredentials(user))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("User with name 'newUser' already exists");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testUpdateCredentialsInvalidNewUserName() {
        when(userRepository.findByUserName("existingUser")).thenReturn(Optional.of(existingUser));
        user.setUserName("");

        assertThatThrownBy(() -> userService.updateCredentials(user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("userName need to have minimum 1 non-white space character");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testUpdateCredentialsInvalidNewPassword() {
        when(userRepository.findByUserName("existingUser")).thenReturn(Optional.of(existingUser));
        user.setPassword(" ");

        assertThatThrownBy(() -> userService.updateCredentials(user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("password need to have minimum 1 non-white space character and maximum 256 symbols");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testLogout() {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        userService.logout();

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
}