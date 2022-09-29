package KSUTech.demo.auth.repository;

import KSUTech.demo.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
