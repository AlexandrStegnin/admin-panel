package com.ddkolesnik.adminpanel.service;

import com.ddkolesnik.adminpanel.model.User;
import com.ddkolesnik.adminpanel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static com.ddkolesnik.adminpanel.configuration.support.Constant.ROLE_USER;


/**
 * @author Alexandr Stegnin
 */

@Service
@Transactional(readOnly = true)
public class UserService {

//    @Value("${spring.config.file-upload-directory}")
//    private String FILE_UPLOAD_DIRECTORY;

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder,
                       RoleService roleService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.roleService = roleService;
    }

    @Transactional
    public User create(User user) {
        user.setPasswordHash(encoder.encode(user.getPassword()));
        if (Objects.equals(null, user.getRoles()) || user.getRoles().isEmpty())
            user.setRoles(Collections.singleton(roleService.findByTitle(ROLE_USER)));
        return userRepository.save(user);
    }

    public User findOne(Long id) {
        return userRepository.getOne(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public User update(User user) {
        return save(user);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void delete(User user) {
        delete(user.getId());
    }

    /**
     * Рассчитываем хэш пароля
     *
     * @param password - пароль для расчёта хэша
     * @return String hash
     */
    private String passwordToHash(String password) {
        if (Objects.equals(null, password) || StringUtils.isEmpty(password)) {
            return null;
        }
        return encoder.encode(password);
    }

    /**
     * Сохраняет пользователя, не меняя пароля.
     *
     * @param secUser - пользователь, которому надо поменять пароль
     * @return User
     */
    @Transactional
    public User save(User secUser) {
        User dbUser = userRepository.findByLogin(secUser.getLogin());
        // Пароль не сохраняется (transient !), сохраняется только HASH
        if (!StringUtils.isEmpty(secUser.getPassword())) {
            secUser.setPasswordHash(passwordToHash(secUser.getPassword()));
        } else {
            // нам нужно сохранить пароль (если он не задан)
            // подставляем старый пароль из базы
            secUser.setPasswordHash(dbUser.getPasswordHash());
        }
        if (Objects.equals(null, secUser.getRoles())) secUser.setRoles(dbUser.getRoles());
        return userRepository.save(secUser);
    }

    @Transactional
    public void registerNewUser(User newUser) {
        newUser.setPasswordHash(passwordToHash(newUser.getPassword()));
        newUser.setRoles(new HashSet<>(Collections.singletonList(roleService.getDefaultUserRole())));
        userRepository.save(newUser);
    }

    private User getById(Long useId) {
        return userRepository.getOne(useId);
    }

    @Transactional
    public void changePassword(long userId, String passwordNew) {
        User userDb = getById(userId);
        userDb.setPasswordHash(passwordToHash(passwordNew));
        userRepository.save(userDb);
    }

    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public boolean isLoginFree(String login) {
        return Objects.equals(null, findByLogin(login));
    }

    public boolean matchesPasswords(String oldPass, String dbPass) {
        return encoder.matches(oldPass, dbPass);
    }

    public Long count() {
        return userRepository.count();
    }

}
