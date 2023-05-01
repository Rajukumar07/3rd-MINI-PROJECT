package in.ashokit.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.ashokit.binding.LoginForm;
import in.ashokit.binding.SignUpForm;
import in.ashokit.binding.UnlockForm;
import in.ashokit.services.UserService;

@Controller
public class UserController {

	@Autowired
	private HttpSession session;

	@Autowired
	private UserService service;

	@GetMapping("/login")
	public String loginForm(Model m) {

		m.addAttribute("login", new LoginForm());
		return "loginForm";
	}

	@PostMapping("/login")
	public String login(@ModelAttribute("login") LoginForm data, Model m) {
		System.out.println(data);

		String signIn = service.signIn(data);
		if ("OK".equals(signIn)) {
			m.addAttribute("successMsg", "successfully logged in..");
			return "redirect:/dashboard";

		} else {
			m.addAttribute("errorMsg", signIn);
			return "loginForm";
		}

	}

	@GetMapping("/register")
	public String signUpForm(Model m) {
		m.addAttribute("register", new SignUpForm());
		return "register";
	}

	@PostMapping("/register")
	public String signUp(@ModelAttribute("register") SignUpForm formData, Model m) {

		String signUp = service.signUp(formData);
		if ("created".equals(signUp)) {
			m.addAttribute("successMsg", "Successfully created, Please check your mail");
			return "register";
		} else {
			m.addAttribute("errorMsg", signUp);
			return "register";
		}

	}

	@GetMapping("/unlock")
	public String unlockForm(@RequestParam("email") String email, Model model) {
		UnlockForm form = new UnlockForm();
		form.setEmail(email);
		model.addAttribute("unlock", form);
		return "unlockform";
	}

	@PostMapping("/unlock")
	public String unlock(@ModelAttribute("unlock") UnlockForm form, Model model) {
		System.out.println(form);
		String msg = service.unlockAccount(form);
		if ("success".equals(msg)) {
			model.addAttribute("successMsg", "successfully updated");
			return "unlockForm";
		} else {
			model.addAttribute("errorMsg", msg);
			return "unlockForm";
		}

	}

	@GetMapping("/forgot")
	public String forgotForm(String email, Model model) {

		model.addAttribute("email", email);
		if (email != null && email != "") {
			boolean isPwd = service.forgotPwd(email);
			if (isPwd) {
				model.addAttribute("successMsg", "Password has been sent please check your mail");
			} else {
				{
					model.addAttribute("errorMsg", "please provide correct username/email");
				}
			}
		}
		return "forgotPwd";
	}

	@GetMapping("/logout")
	public String logout(Model model) {
		session.invalidate();
		model.addAttribute("message", "Logout successfully");
		return "home";
	}
}
