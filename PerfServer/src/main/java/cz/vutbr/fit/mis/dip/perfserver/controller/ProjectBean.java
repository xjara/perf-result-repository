package cz.vutbr.fit.mis.dip.perfserver.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import cz.vutbr.fit.mis.dip.perfserver.dao.LocalDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.ProjectDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.ThresholdDao;
import cz.vutbr.fit.mis.dip.perfserver.model.Local;
import cz.vutbr.fit.mis.dip.perfserver.model.Project;



@ManagedBean
@ViewScoped
public class ProjectBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private ProjectDao projectDao;
	@EJB
	private LocalDao localDao;
	@EJB
	private ThresholdDao thresholdDao;
	@Named
	@Produces
	private List<Project> projects;
	
	@PostConstruct
	public void init() {
		projects = projectDao.getProjects();
	}
	
	public List<Project> getProjects() {
		return projects; 
	}
	
	public void deleteProject(Project project) {
		List<Local> locals = localDao.getLocalsByProjectId(project.getId());
		for(Local local: locals) {
			thresholdDao.remove(local.getThreshold());
		}
		projectDao.remove(project);
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Project " + project.getName() + " with all its data was deleted.", null);
		FacesContext.getCurrentInstance().addMessage("form:delete", msg);
	}
}
