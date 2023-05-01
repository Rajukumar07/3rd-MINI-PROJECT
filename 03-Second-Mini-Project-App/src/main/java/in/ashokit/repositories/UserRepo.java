package in.ashokit.repositories;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.ashokit.entity.UserEntity;

public interface UserRepo extends JpaRepository<UserEntity, Integer> {

	// public UserEntity findByEmailAndPwd(String email,String pwd);

	// public UserEntity findByEmail(String email);
	public Optional<UserEntity> findByEmail(String email);

	@Transactional
	@Modifying // It means it's not a select statement
	@Query(value = "Update user_entity u set u.pwd = :password , u.acc_status=:status where u.email=:email", nativeQuery = true)
	public int changeUserPassword(@Param("password") String password, @Param("email") String email,
			@Param("status") String status);
}
