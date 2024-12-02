package eredua.bean;

import businessLogic.*;

public class SelectModeBean {
	BLFacade facadeBL;

	public SelectModeBean() {
		facadeBL = FacadeBean.getBusinessLogic();
	}

}