package in.ashokit.runner;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import in.ashokit.entity.CourseEntity;
import in.ashokit.entity.EnquiryEntity;
import in.ashokit.repositories.CourseRepo;
import in.ashokit.repositories.EnqStatusRepo;

@Component
public class DataLoader implements ApplicationRunner {

	@Autowired
	private CourseRepo courseRepo;
	@Autowired
	private EnqStatusRepo statusRepo;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		courseRepo.deleteAll();
		statusRepo.deleteAll();
		CourseEntity c1 = new CourseEntity();
		c1.setCorseName("JRTP");
		CourseEntity c2 = new CourseEntity();
		c2.setCorseName("DevOps");
		CourseEntity c3 = new CourseEntity();
		c3.setCorseName("Spring Boot");
		CourseEntity c4 = new CourseEntity();
		c4.setCorseName("AWS");
		CourseEntity c5 = new CourseEntity();
		c5.setCorseName("Testing");

		List<CourseEntity> courses = Arrays.asList(c1, c2, c3, c4, c5);
		courseRepo.saveAll(courses);

		EnquiryEntity e1 = new EnquiryEntity();
		e1.setEnquiryType("New");
		EnquiryEntity e2 = new EnquiryEntity();
		e2.setEnquiryType("Enrolled");
		EnquiryEntity e3 = new EnquiryEntity();
		e3.setEnquiryType("Lost");

		List<EnquiryEntity> enquiries = Arrays.asList(e1, e2, e3);
		statusRepo.saveAll(enquiries);

	}

}
