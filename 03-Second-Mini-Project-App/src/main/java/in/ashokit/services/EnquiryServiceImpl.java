package in.ashokit.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.ashokit.binding.AddEnquiryForm;
import in.ashokit.binding.ViewSearchCriteria;
import in.ashokit.entity.StudentEntity;
import in.ashokit.entity.UserEntity;
import in.ashokit.repositories.CourseRepo;
import in.ashokit.repositories.EnqStatusRepo;
import in.ashokit.repositories.StudentRepo;
import in.ashokit.repositories.UserRepo;

@Service
public class EnquiryServiceImpl implements EnquiryService {

	@Autowired
	private HttpSession session;

	@Autowired
	private StudentRepo studentRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private CourseRepo courseRepo;

	@Autowired
	private EnqStatusRepo statusRepo;

	@Override
	public List<String> getCourse() {
		return courseRepo.getCourceName();

	}

	@Override
	public List<String> getStatus() {
		return statusRepo.getEnqType();
	}

	@Override
	public Long getTotalCount() {

		Integer userId = (Integer) session.getAttribute("userId");
		Optional<UserEntity> user = userRepo.findById(userId);
		long countByUser = studentRepo.countByUser(user);
		return countByUser;
	}

	@Override
	public Long getEnrolledCount() {
		Integer userId = (Integer) session.getAttribute("userId");
		Optional<UserEntity> user = userRepo.findById(userId);
		long enrolledCount = studentRepo.countByUserAndEnqStatus(user, "Enrolled");
		return enrolledCount;
	}

	@Override
	public Long getLostCount() {
		Integer userId = (Integer) session.getAttribute("userId");
		Optional<UserEntity> user = userRepo.findById(userId);
		long countLost = studentRepo.countByUserAndEnqStatus(user, "Lost");
		return countLost;
	}

	@Override
	public boolean addEnquiry(AddEnquiryForm s) {

		boolean status = false;
		Integer userId = (Integer) session.getAttribute("userId");
		StudentEntity entity = new StudentEntity();

		UserEntity user = new UserEntity();
		user.setUserId(userId);
		BeanUtils.copyProperties(s, entity);
		entity.setUser(user);
		StudentEntity save = studentRepo.save(entity);
		if (save != null) {
			status = true;
		}
		return status;
	}

	@Override
	public StudentEntity editEnquiry(Integer id) {

		Optional<StudentEntity> findById = studentRepo.findById(id);
		if (findById.isPresent()) {
			StudentEntity studentEntity = findById.get();
			return studentEntity;
		}
		return null;

	}

	@Override
	public List<StudentEntity> viewEnquiry() {

		Integer userId = (Integer) session.getAttribute("userId");
		Optional<UserEntity> user = userRepo.findById(userId);

		if (user.isPresent()) {
			UserEntity userEntity = user.get();
			List<StudentEntity> students = userEntity.getStudents();

			return students;
		}
		return null;
	}

	@Override
	public List<StudentEntity> filterEnquiry(ViewSearchCriteria criteria, Integer uid) {

		Optional<UserEntity> findById = userRepo.findById(uid);
		if (findById.isPresent()) {
			List<StudentEntity> enquiries = findById.get().getStudents();

			if (null != criteria.getCourseName() && !"".equals(criteria.getCourseName())) {
				enquiries = enquiries.stream().filter(e -> e.getCourseName().equals(criteria.getCourseName()))
						.collect(Collectors.toList());

			}
			if (null != criteria.getClassMode() & !"".equals(criteria.getClassMode())) {
				enquiries = enquiries.stream().filter(e -> e.getClassMode().equals(criteria.getClassMode()))
						.collect(Collectors.toList());
			}

			if (null != criteria.getEnqStatus() & !"".equals(criteria.getEnqStatus())) {
				enquiries = enquiries.stream().filter(e -> e.getEnqStatus().equals(criteria.getEnqStatus()))
						.collect(Collectors.toList());
			}

			return enquiries;
		}
		return null;

	}

	@Override
	public boolean deleteEnq(Integer id) {
		studentRepo.deleteById(id);
		return true;
	}

}
