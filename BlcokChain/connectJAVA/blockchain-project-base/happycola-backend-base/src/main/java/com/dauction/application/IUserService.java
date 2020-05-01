package com.dauction.application;

import com.dauction.domain.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IUserService {
    List<User> list();
    User get(long id);
    User get(String 이메일);

    @Transactional
    User add(User 회원);

    @Transactional
    User update(User 회원);

    @Transactional
    void delete(long id);
}
