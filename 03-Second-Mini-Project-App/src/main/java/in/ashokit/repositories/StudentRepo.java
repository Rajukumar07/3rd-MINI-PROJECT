package in.ashokit.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.ashokit.entity.StudentEntity;
import in.ashokit.entity.UserEntity;

public interface StudentRepo extends JpaRepository<StudentEntity, Integer> {

	//public List<StudentEntity> findAllByUser(UserEntity entity);

	public List<StudentEntity> findByUserAndClassMode(UserEntity user, String mode);

	public List<StudentEntity> findByUserAndEnqStatus(UserEntity user, String status);

	public List<StudentEntity> findByUserAndCourseName(UserEntity user, String course);

	public long countByUser(Optional<UserEntity> user);

	public long countByUserAndEnqStatus(Optional<UserEntity> user, String status);
	
    @Modifying
    @Query("DELETE FROM StudentEntity s WHERE s.id = :id")
    void deleteById(@Param("id") Integer id);


}
