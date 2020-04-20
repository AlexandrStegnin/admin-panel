package com.ddkolesnik.adminpanel.service;

import com.ddkolesnik.adminpanel.model.AppToken;
import com.ddkolesnik.adminpanel.repository.AppTokenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AppTokenService {

    AppTokenRepository appTokenRepository;

    public AppToken create(AppToken token) {
        return appTokenRepository.save(token);
    }

    public AppToken update(AppToken token) {
        return appTokenRepository.save(token);
    }

    public void delete(AppToken token) {
        appTokenRepository.delete(token);
    }

    public List<AppToken> findAll() {
        return appTokenRepository.findAll();
    }

    public Long count() {
        return appTokenRepository.count();
    }

}
