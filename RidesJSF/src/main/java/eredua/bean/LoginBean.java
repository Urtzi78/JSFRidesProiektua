package eredua.bean;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import businessLogic.BLFacade;
import domain.SessionManager;

public class LoginBean {
	BLFacade facadeBL;
	private String email;
	private String password;

	public LoginBean() {
		facadeBL = FacadeBean.getBusinessLogic();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String login() {

		boolean isValid = facadeBL.loginEgin(email, password);
		if (isValid) {
			return "in";
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email edo pasahitza okerra.", null));
			return null;
		}
	}

	public String logout() {
		SessionManager.logout();
		return "logout";
	}
}
