package cz.vutbr.fit.mis.dip.perfserver.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import cz.vutbr.fit.mis.dip.perfserver.controller.forms.ThresholdForm;
import cz.vutbr.fit.mis.dip.perfserver.dao.AttrDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.ProjectDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.ThresholdDao;
import cz.vutbr.fit.mis.dip.perfserver.model.Attr;
import cz.vutbr.fit.mis.dip.perfserver.model.Global;
import cz.vutbr.fit.mis.dip.perfserver.model.Local;
import cz.vutbr.fit.mis.dip.perfserver.model.Project;
import cz.vutbr.fit.mis.dip.perfserver.model.Threshold;



@ManagedBean
@ViewScoped
public class ThresholdBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private ThresholdDao thresholdDao;
	@EJB
	private AttrDao attrDao;
	@EJB
	private ProjectDao projectDao;
	
	@ManagedProperty(value="#{thresholdForm}")
	private ThresholdForm form;
	
	private Project project;
	
	@PostConstruct
	public void init() {
		FacesContext fc = FacesContext.getCurrentInstance();
		String id = fc.getExternalContext().getRequestParameterMap().get("projectid");
		// necessary for global threshold page
		if (id == null) {
			return;
		}
		long projectId = Long.parseLong(id);
		project = projectDao.getProjectById(projectId);	
	}
	
	public void setForm(ThresholdForm form) {
		this.form = form;
	}
	
	public Project getProject() {
		return project;
	}
	
	public List<Threshold> getGlobalThresholds() {
		return thresholdDao.getGlobalThresholds();
	} 
	
	public List<Threshold> getProjectLocalThresholds() {
		return thresholdDao.getProjectLocalThresholds(project.getName());
	}
	
	private void saveThreshold(boolean isGlobalThreshold) {
		String attribute = form.getAttribute();
		Threshold threshold;
		String thresholdType;
		
		if (isGlobalThreshold) {
			threshold = thresholdDao.getGlobalThresholdByAttrName(attribute);
			thresholdType = "Global";
		} else {
			threshold = thresholdDao.getLocalThresholdByProjectAndAttr(project.getId(), attribute);
			thresholdType = "Local";
		}
		
		FacesMessage msg;
		if (threshold != null) {
			// threshold exists
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, thresholdType + " Threshold for " + attribute + " already exists.", null);
		} else {
			Attr attr = attrDao.getAttrByName(attribute);
			threshold = new Threshold(Double.valueOf(form.getValue()), attr);
			if (isGlobalThreshold) {
				threshold.addGlobal(new Global(threshold));
			} else {
				threshold.addLocal(new Local(project, threshold));
			}
			
			thresholdDao.save(threshold);
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, thresholdType + " Threshold for " + attribute + " successfully saved.", null);	
			form.clear();
		}
		FacesContext.getCurrentInstance().addMessage("form_add:save", msg);
	}
	
	public void saveGlobalThreshold() {
		saveThreshold(true);
	}
	
	public void saveLocalThreshold() {
		saveThreshold(false);
	}
	
	private void deleteThreshold(Threshold threshold, String thresholdType) {
		thresholdDao.remove(threshold);
		FacesContext.getCurrentInstance().addMessage("form_list:delete",
				new FacesMessage(FacesMessage.SEVERITY_INFO, thresholdType + " Threshold for " + threshold.getAttr().getName() + " was removed.", null));	
	}
	
	public void deleteGlobalThreshold(Threshold threshold) {
		deleteThreshold(threshold, "Global");
	}	
	
	public void deleteLocalThreshold(Threshold threshold) {
		deleteThreshold(threshold, "Local");
	}
}
