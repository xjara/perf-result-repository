package cz.vutbr.fit.mis.dip.perfserver.dao;

import java.util.List;

import cz.vutbr.fit.mis.dip.perfserver.model.Project;


public interface ProjectDao {
	public Project save(Project p);
	public void remove(Project p);
	public List<Project> getProjects();
	public Project getProjectByName(String project);
	public Project getProjectById(long id);
}
