package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
   // List<User> findAll();

  //  User save(User user);

    User update(Long id, User user);

  //  boolean deleteById(Long id);

 //   User findById(Long id);

    void checkAlreadyExistEmail(User user);

    void removeOldEmail(String oldEmail);
}