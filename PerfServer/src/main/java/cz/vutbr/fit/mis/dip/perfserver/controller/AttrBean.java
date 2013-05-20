package cz.vutbr.fit.mis.dip.perfserver.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import cz.vutbr.fit.mis.dip.perfserver.controller.forms.AttrForm;
import cz.vutbr.fit.mis.dip.perfserver.dao.AttrDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.AttrResultDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.ThresholdDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.UnitDao;
import cz.vutbr.fit.mis.dip.perfserver.model.Attr;
import cz.vutbr.fit.mis.dip.perfserver.model.Threshold;
import cz.vutbr.fit.mis.dip.perfserver.model.Unit;



@ManagedBean
@ViewScoped
public class AttrBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private AttrDao attrDao;
	@EJB
	private UnitDao unitDao;
	@EJB
	private AttrResultDao attrResultDao;
	@EJB
	private ThresholdDao thresholdDao;
	
	@ManagedProperty(value="#{attrForm}")
	private AttrForm form;
	
	@Named
	@Produces
	private List<Attr> attributes;
	
	@Named
	@Produces
	private List<Unit> attrUnits;
	
	@PostConstruct
	public void init() {
		attributes = attrDao.getAttrs();
		attrUnits = unitDao.getUnits();
	}
	
	public void setForm(AttrForm form) {
		this.form = form;
	}
	
	public List<Attr> getAttributes() {
		return attributes;
	}
	
	public List<Unit> getAttrUnits() {
		return attrUnits;
	}
	
	public void saveAttr() {
		FacesMessage msg;
		String name = form.getName().toUpperCase();
		if (attrDao.getAttrByName(name) != null) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Attribute " + name + " already exists.", null);
		} else {
			Unit unit = unitDao.getUnitByName(form.getUnit());
			attrDao.save(new Attr(name, unit));
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Attribute " + name + " successfully saved.", null);	
		}
		FacesContext.getCurrentInstance().addMessage("form_add:name", msg);
		form.clear();
	}
	
	private FacesMessage checkAttributeUsage(Attr attr) {
		FacesMessage msg = null;
		Threshold threshold = thresholdDao.getLocalThresholdByAttrId(attr.getId());
		if (threshold != null) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Attribute " + attr.getName() + " cannot be deleted. It is used for some local threshold.", null);	
		} else {
			threshold = thresholdDao.getGlobalThresholdByAttrId(attr.getId());
			if (threshold != null) {
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Attribute " + attr.getName() + " cannot be deleted. It is used for global threshold.", null);
			} else {
				if (attrResultDao.getFirstUsedAttrResultByAttrId(attr.getId()) != null) {
					msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Attribute " + attr.getName() + " cannot be deleted. It is already used for performance data.", null);	
				}
			}
		}
		return msg;
	}
	
	public void deleteAttr(Attr attr) {
		FacesMessage msg = checkAttributeUsage(attr);
		if (msg == null) {
			attrDao.remove(attr);
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Attribute " + attr.getName() + " was deleted.", null); 
		}
		FacesContext.getCurrentInstance().addMessage("form_list:delete", msg);
		form.clear();
	}
}
