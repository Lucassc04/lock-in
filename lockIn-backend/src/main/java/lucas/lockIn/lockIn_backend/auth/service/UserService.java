package lucas.lockIn.lockIn_backend.auth.service;

import lombok.AllArgsConstructor;
import lucas.lockIn.lockIn_backend.auth.dto.request.LoginRequest;
import lucas.lockIn.lockIn_backend.auth.dto.request.RegisterRequest;
import lucas.lockIn.lockIn_backend.auth.dto.request.UserDomainDetailsRequest;
import lucas.lockIn.lockIn_backend.auth.entity.Role;
import lucas.lockIn.lockIn_backend.auth.entity.User;
import lucas.lockIn.lockIn_backend.auth.entity.UserPrincipal;
import lucas.lockIn.lockIn_backend.auth.repository.UserRepository;
import lucas.lockIn.lockIn_backend.workout.exceptions.EntityNotFoundException;
import lucas.lockIn.lockIn_backend.auth.exceptions.InvalidCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserPrincipal createUser(RegisterRequest registerRequest) {
        User user = new User();

        setters(registerRequest, user);

        User savedUser = userRepository.save(user);
        return new UserPrincipal(savedUser);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow( () -> new EntityNotFoundException("User", id));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User", email, "email"));
    }

    public User verifyLogIn(LoginRequest loginRequest) {
        User user = findByEmail(loginRequest.email());
        if(!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Email or password is invalid");
        }
        return user;
    }

    public User updateUserDetails(Long userId, RegisterRequest registerRequest) {
        User savedUser = userRepository.findById(userId)
                .orElseThrow( () -> new EntityNotFoundException("User", userId));

        setters(registerRequest, savedUser);
        return userRepository.save(savedUser);
    }

    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

    private void setters(RegisterRequest registerRequest, User user) {
        user.setEmail(registerRequest.email());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setFirstName(registerRequest.firstName());
        user.setLastName(registerRequest.lastName());
        user.setRole(Role.USER);
    }

    public User updateUserDomainDetails(Long userId, UserDomainDetailsRequest userDetails) {
        User user = findById(userId);

        if(userDetails.exercises()!= null) user.getExercises().addAll(userDetails.exercises());
        if(userDetails.workoutPlans() != null) user.getWorkoutPlans().addAll(userDetails.workoutPlans());
        if(userDetails.workouts() != null) user.getWorkouts().addAll(userDetails.workouts());

        return userRepository.save(user);
    }
}
