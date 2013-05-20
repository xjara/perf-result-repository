package cz.vutbr.fit.mis.dip.perfserver.controller.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("perf.result.rep.controller.validators.DoubleValueValidator")
public class DoubleValueValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		try {
			Double.parseDouble((String) value);
		} catch(NumberFormatException e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Value is not correct decimal number.", null);
			throw new ValidatorException(msg);
		}
	}

}
