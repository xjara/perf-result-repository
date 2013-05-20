package cz.vutbr.fit.mis.dip.perfserver.dao;

import java.util.List;

import cz.vutbr.fit.mis.dip.perfserver.model.Method;


public interface MethodDao {
	public Method save(Method method);
	public Method getMethodByName(String method);
	public Method getMethodById(long methodId);
	public List<Method> getAllMethodsByTestSuiteRunId(Long testSuiteRunId);
}
