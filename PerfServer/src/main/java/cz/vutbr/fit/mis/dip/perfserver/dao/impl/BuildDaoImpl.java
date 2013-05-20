package cz.vutbr.fit.mis.dip.perfserver.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import cz.vutbr.fit.mis.dip.perfserver.dao.BuildDao;
import cz.vutbr.fit.mis.dip.perfserver.model.Build;


@Stateless
public class BuildDaoImpl implements BuildDao {
	@Inject
	private EntityManager em;
	
	@Override
	public List<Build> getBuildsByProjectId(Long projectId) {
		return em.createNamedQuery("buildsByProjectId", Build.class)
					.setParameter("projectId", projectId)
					.getResultList();
	}
	
	@Override
	public Build getBuildByNameAndByProjectId(String build, Long projectId) {
		List<Build> builds = em.createNamedQuery("buildByNameAndByProjectId", Build.class)
								.setParameter("build", build)
								.setParameter("projectId", projectId)
								.getResultList();
		
		return builds.isEmpty()? null : builds.get(0);
	}
	
	@Override
	public Build getBuildByNameAndByProject(String build, String project) {
		List<Build> builds = em.createNamedQuery("buildByNameAndByProject", Build.class)
								.setParameter("build", build)
								.setParameter("project", project)
								.getResultList();
		
		return builds.isEmpty()? null : builds.get(0);
	}
	
	@Override
	public Build save(Build build) {
		return em.merge(build);
	}

	@Override
	public List<String> getUniqueBuildNamesByProjectIdAndByTestSuite(
			Long projectId, String testSuite) {
		return em.createNamedQuery("uniqueBuildNamesByProjectIdAndByTestSuite", String.class)
					.setParameter("projectId", projectId)
					.setParameter("testSuite", testSuite)
					.getResultList();
	}
}
