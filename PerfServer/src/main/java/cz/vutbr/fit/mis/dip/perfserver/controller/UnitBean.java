package cz.vutbr.fit.mis.dip.perfserver.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import cz.vutbr.fit.mis.dip.perfserver.controller.forms.UnitForm;
import cz.vutbr.fit.mis.dip.perfserver.dao.AttrDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.AttrResultDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.ThresholdDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.UnitDao;
import cz.vutbr.fit.mis.dip.perfserver.model.Attr;
import cz.vutbr.fit.mis.dip.perfserver.model.Threshold;
import cz.vutbr.fit.mis.dip.perfserver.model.Unit;



@ManagedBean
@ViewScoped
public class UnitBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private UnitDao unitDao;
	@EJB
	private AttrResultDao attrResultDao;
	@EJB
	private AttrDao attrDao;
	@EJB
	private ThresholdDao thresholdDao;
	
	@ManagedProperty(value="#{unitForm}")
	private UnitForm form;
	
	@Named
	@Produces
	private List<Unit> units;

	@PostConstruct
	public void init() {
		units = unitDao.getUnits();
	}
	
	public void setForm(UnitForm form) {
		this.form = form;
	}
	
	public List<Unit> getUnits() {
		return units;
	}
	
	public void setUnit(List<Unit> units) {
		this.units = units;
	}
		
	public void saveUnit() {
		FacesMessage msg;
		String name = form.getName().toUpperCase();
		if (unitDao.getUnitByName(name) != null) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unit " + name + " already exists.", null);
		} else {
			unitDao.save(new Unit(name));
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Unit " + name + " successfully saved.", null);
		}
		FacesContext.getCurrentInstance().addMessage("form_add:name", msg);
		form.clear();
	}
	
	private FacesMessage checkUnitUsage(Unit unit) {
		FacesMessage msg = null;
		Threshold threshold = thresholdDao.getLocalThresholdByUnitId(unit.getId());
		if (threshold != null) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unit " + unit.getName() + " cannot be deleted. It is used for some local threshold.", null);
		} else {
			threshold = thresholdDao.getGlobalThresholdByUnitId(unit.getId());
			if (threshold != null) {
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unit " + unit.getName() + " cannot be deleted. It is used for global threshold.", null);
			} else {
				if (attrResultDao.getFirstUsedAttrResultByUnitId(unit.getId()) != null) {
					msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unit " + unit.getName() + " cannot be deleted. It is already used for performance data.", null);	
				} else {
					Attr attr = attrDao.getAttrByUnit(unit.getName());
					if (attr != null) {
						msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unit " + unit.getName() + " cannot be deleted. It is used for attribute " + attr.getName() + ".", null);
					}
				}
			}
		}
		return msg;
	}
	
	public void deleteUnit(Unit unit) {
		FacesMessage msg = checkUnitUsage(unit);
		if (msg == null) {
			unitDao.remove(unit);
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Unit " + unit.getName() + " was deleted.", null);
		}
		FacesContext.getCurrentInstance().addMessage("form_list:delete", msg);
		form.clear();
	}
}