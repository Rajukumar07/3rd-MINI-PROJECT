package in.ashokit.services;

import in.ashokit.binding.LoginForm;
import in.ashokit.binding.SignUpForm;
import in.ashokit.binding.UnlockForm;

public interface UserService {

	public String signIn(LoginForm data);

	public String signUp(SignUpForm data);

	public boolean forgotPwd(String email);

	public String unlockAccount(UnlockForm data);

	public boolean checkMail(String email);

	public boolean sendMail(String to, String sub, String pwd);

}
