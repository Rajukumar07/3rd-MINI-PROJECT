package in.ashokit.services;

import java.util.List;

import in.ashokit.binding.AddEnquiryForm;
import in.ashokit.binding.ViewSearchCriteria;
import in.ashokit.entity.StudentEntity;

public interface EnquiryService {

	public List<String> getCourse();

	public List<String> getStatus();

	public Long getTotalCount();

	public Long getLostCount();

	public Long getEnrolledCount();

	public boolean addEnquiry(AddEnquiryForm s);

	public List<StudentEntity> viewEnquiry();

	public List<StudentEntity> filterEnquiry(ViewSearchCriteria searchCriteria, Integer userid);

	public StudentEntity editEnquiry(Integer id);
	
	public boolean deleteEnq(Integer integer); 

}
