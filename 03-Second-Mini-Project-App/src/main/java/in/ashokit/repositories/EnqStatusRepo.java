package in.ashokit.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.ashokit.entity.EnquiryEntity;

public interface EnqStatusRepo extends JpaRepository<EnquiryEntity, Integer> {

	@Query("select enquiryType from EnquiryEntity")
	public List<String> getEnqType();
}
