package in.ashokit.services;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.ashokit.binding.LoginForm;
import in.ashokit.binding.SignUpForm;
import in.ashokit.binding.UnlockForm;
import in.ashokit.entity.UserEntity;
import in.ashokit.repositories.UserRepo;
import in.ashokit.utils.EmailUtility;
import in.ashokit.utils.PasswordUtility;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private HttpSession session;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private EmailUtility email;

	@Override
	public String signIn(LoginForm data) {

		Optional<UserEntity> user = userRepo.findByEmail(data.getEmail());
		if (user.isPresent()) {
			boolean isTruePass = user.get().getPwd().equals(data.getPwd());
			String unlockStatus = user.get().getAccStatus();
			if ("UNLOCKED".equals(unlockStatus)) {
				if (isTruePass) {
					session.setAttribute("userId", user.get().getUserId());
					return "OK";
				}
				return "Bad credentials";
			}
			return "Please Unlock your account ";

		}
		return "user is not available";

	}

	@Override
	public String signUp(SignUpForm s) {
		String tempPwd = PasswordUtility.generatePassword();
		UserEntity user = new UserEntity();
		BeanUtils.copyProperties(s, user);
		user.setAccStatus("LOCKED");
		user.setPwd(tempPwd);

		if (checkMail(s.getEmail())) {
			userRepo.save(user);
			sendMail(s.getEmail(), "Unlock account", tempPwd);
			return "created";
		} else {
			return "user is already exists..!";
		}

	}

	@Override
	public boolean forgotPwd(String mail) {
		boolean sendEmail = false;
		Optional<UserEntity> user = userRepo.findByEmail(mail);
		if (user.isPresent()) {

			StringBuffer body = new StringBuffer(" <h2>please don't share your password </h2>");
			body.append("Your password : " + user.get().getPwd());
			body.append("<br>");
			String to = user.get().getEmail();
			String sub = "Recover Password";
			sendEmail = email.sendEmail(to, sub, body.toString());
		}

		return sendEmail;
	}

	@Override
	public boolean checkMail(String email) {

		Optional<UserEntity> user = userRepo.findByEmail(email);
		if (user.isPresent()) {
			return false;
		}
		return true;
	}

	@Override
	public String unlockAccount(UnlockForm data) {

		boolean isTruePwd = isTruePwd(data.getEmail(), data.getTempPwd());
		if (isTruePwd) {
			if (data.getConfirmPwd().equals(data.getNewPwd())) {
				UserEntity user = new UserEntity();
				user.setAccStatus("UNLOCKED");
				user.setPwd(data.getNewPwd());
				UserEntity updatedRow = userRepo.save(user);
				if (null != updatedRow) {
					return "success";
				}
				return "something went wrong";
			} else {
				return "new password and confirm password does not match..";
			}
		} else {
			return "invalid password please check your mail.";
		}
	}

	@Override
	public boolean sendMail(String to, String sub, String pwd) {

		StringBuffer body = new StringBuffer(" <h1>To open account click below link</h1>");
		body.append("Your password : " + pwd);
		body.append("<br></br>");
		body.append("<a href=\"http://localhost:8081/unlock?email=" + to + "\">click here to unlock</a>");

		email.sendEmail(to, sub, body.toString());

		return false;
	}

	public boolean isTruePwd(String email, String pwd) {

		Optional<UserEntity> user = userRepo.findByEmail(email);
		if (user != null) {
			return user.get().getPwd().equals(pwd);
		} else {
			return false;
		}
	}

}
