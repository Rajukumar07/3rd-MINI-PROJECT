package in.ashokit.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import in.ashokit.binding.AddEnquiryForm;
import in.ashokit.binding.UserDashoard;
import in.ashokit.binding.ViewSearchCriteria;
import in.ashokit.entity.StudentEntity;
import in.ashokit.services.EnquiryService;

@Controller
public class EnquiryController {
	@Autowired
	private HttpSession session;

	@Autowired
	private EnquiryService enquiryService;

	@GetMapping("/add")
	public String enqForm(Model model) {

		model.addAttribute("enqForm", new AddEnquiryForm());
		initForm(model);
		return "addStudentEnquiry";
	}

	private void initForm(Model model) {

		List<String> course = enquiryService.getCourse();
		List<String> status = enquiryService.getStatus();
		model.addAttribute("courses", course);
		model.addAttribute("enqStatus", status);
	}

	@PostMapping("/add")
	public String enqFormHandle(@ModelAttribute("enqForm") AddEnquiryForm form, Model model) {

		initForm(model);
		boolean addEnquiry = enquiryService.addEnquiry(form);
		if (addEnquiry) {
			model.addAttribute("successMsg", "successfully added");
		} else {
			model.addAttribute("errorMsg", "Unsuccessfully added");
		}

		return "addStudentEnquiry";

	}

	@GetMapping("/dashboard")
	public String dashboardPage(Model m) {

		UserDashoard dashboard = new UserDashoard();
		m.addAttribute("dashboard", dashboard);

		Long count = enquiryService.getTotalCount();
		// System.out.println("total::"+count);
		dashboard.setTotalEnquiry(count);
		Long lost = enquiryService.getLostCount();
		// System.out.println("lost ::"+lost);
		dashboard.setLostEnquiry(lost);
		Long enrolled = enquiryService.getEnrolledCount();
		// System.out.println("enrolled ::"+enrolled);
		dashboard.setEnrolledEnquiry(enrolled);

		return "dashboard";
	}

	@GetMapping("/view")
	public String viewStudent(RedirectAttributes attributes, Model model) {
		model.addAttribute("courses", enquiryService.getCourse());
		model.addAttribute("enqStatus", enquiryService.getStatus());
		List<StudentEntity> list = enquiryService.viewEnquiry();
		model.addAttribute("students", list);

		return "ViewEnquiryForm";
	}

	@GetMapping("/viewfilter")
	public String viewStudentHandle(@RequestParam String cname, @RequestParam String status, @RequestParam String cmode,
			Model model) {

		ViewSearchCriteria criteria = new ViewSearchCriteria();
		criteria.setCourseName(cname);
		criteria.setEnqStatus(status);
		criteria.setClassMode(cmode);
		model.addAttribute("courses", enquiryService.getCourse());
		model.addAttribute("enqStatus", enquiryService.getStatus());
		Integer userId = (Integer) session.getAttribute("userId");
		List<StudentEntity> enquiries = enquiryService.filterEnquiry(criteria, userId);
		model.addAttribute("students", enquiries);

		return "view-table";

	}

	@GetMapping("/edit")
	public String editEnquiry(@RequestParam("id") Integer id, Model model) {
		initForm(model);
		StudentEntity editEnquiry = enquiryService.editEnquiry(id);
		model.addAttribute("enqForm", editEnquiry);

		return "addStudentEnquiry";

	}

	@GetMapping("/delete/{id}")
	public String deleteRecord(@PathVariable("id") Integer id, Model model) {

		boolean isDelete = enquiryService.deleteEnq(id);
		if (isDelete) {
			model.addAttribute("successMsg", "Successfully deleted");
		} else {
			model.addAttribute("errorMsg", "Not deleted");
		}

		return "redirect:/view";
	}

}
