package cz.vutbr.fit.mis.dip.perfserver.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import cz.vutbr.fit.mis.dip.perfserver.enums.Fce;
import cz.vutbr.fit.mis.dip.perfserver.enums.TestRunStatus;
import cz.vutbr.fit.mis.dip.perfserver.controller.ComputeBean;
import cz.vutbr.fit.mis.dip.perfserver.controller.objects.ThresholdHelper;
import cz.vutbr.fit.mis.dip.perfserver.dao.AttrDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.AttrResultDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.BuildDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.HwDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.MethodDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.ProjectDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.TestRunDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.TestSuiteDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.TestSuiteRunDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.ThresholdDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.UnitDao;
import cz.vutbr.fit.mis.dip.perfserver.model.Attr;
import cz.vutbr.fit.mis.dip.perfserver.model.AttrResult;
import cz.vutbr.fit.mis.dip.perfserver.model.Build;
import cz.vutbr.fit.mis.dip.perfserver.model.Hw;
import cz.vutbr.fit.mis.dip.perfserver.model.Method;
import cz.vutbr.fit.mis.dip.perfserver.model.Project;
import cz.vutbr.fit.mis.dip.perfserver.model.Result;
import cz.vutbr.fit.mis.dip.perfserver.model.TestRun;
import cz.vutbr.fit.mis.dip.perfserver.model.TestSuite;
import cz.vutbr.fit.mis.dip.perfserver.model.TestSuiteRun;
import cz.vutbr.fit.mis.dip.perfserver.model.Unit;
import cz.vutbr.fit.mis.dip.perfobjects.constants.AttrUnitStatus;
import cz.vutbr.fit.mis.dip.perfobjects.constants.Format;
import cz.vutbr.fit.mis.dip.perfobjects.constants.RelURL;
import cz.vutbr.fit.mis.dip.perfobjects.objects.AttrResultData;
import cz.vutbr.fit.mis.dip.perfobjects.objects.AttrUnit;
import cz.vutbr.fit.mis.dip.perfobjects.objects.Attribute;
import cz.vutbr.fit.mis.dip.perfobjects.objects.ConfigProblem;
import cz.vutbr.fit.mis.dip.perfobjects.objects.LastTestRunData;
import cz.vutbr.fit.mis.dip.perfobjects.objects.LastTestSuiteData;
import cz.vutbr.fit.mis.dip.perfobjects.objects.TestSuiteData;

@Path("/rep")
@Singleton
public class RepRESTService {
	
	@EJB
	private AttrDao attrDao;
	@EJB
	private UnitDao unitDao;
	@EJB
	private ProjectDao projectDao;
	@EJB
	private BuildDao buildDao;
	@EJB
	private HwDao hwDao;
	@EJB
	private TestSuiteDao testSuiteDao;
	@EJB
	private MethodDao methodDao;
	@EJB
	private TestRunDao testRunDao;
	@EJB
	private AttrResultDao attrResultDao;
	@EJB
	private ThresholdDao thresholdDao;
	@EJB
	private TestSuiteRunDao testSuiteRunDao;
	@Inject
	private ComputeBean computeBean;
	
	private static final Logger logger = LoggerFactory.getLogger(RepRESTService.class);
	private Gson gson = new Gson();
	private SimpleDateFormat sdf = new SimpleDateFormat(Format.DATE_TIME);
	private TestSuiteRun baseTestSuiteRun;
	private TestSuiteRun testSuiteRun;
	

	@Path(RelURL.TEST_SUITE + "{name}")
	@POST 
	@Consumes("text/json")
	@Produces("text/json")
	public Response processTestSuiteData(@PathParam("name") final String testSuiteName, final TestSuiteData testSuiteData) {
		TestSuite savedTestSuite;
		Project projectDb = projectDao.getProjectByName(testSuiteData.getProject());
				
		if (projectDb == null) {
			// save the complete project, it doesn't exist
			Project project = new Project(testSuiteData.getProject());
			Build build = new Build(testSuiteData.getBuild(), project);
			build.getTestSuites().add(new TestSuite(testSuiteName, build));
			project.getBuilds().add(build);
			projectDao.save(project);
			savedTestSuite = testSuiteDao.getTestSuiteByNameAndByProjectAndByBuild(testSuiteName, testSuiteData.getProject(), testSuiteData.getBuild());
		} else {
			// the project already exists, save only its build and testsuite
			Build buildDb = buildDao.getBuildByNameAndByProjectId(testSuiteData.getBuild(), projectDb.getId()); 
			if (buildDb == null) {
				// the build doesn't exist, save it with testsuite
				Build build = new Build(testSuiteData.getBuild(), projectDb); 
				build.getTestSuites().add(new TestSuite(testSuiteName, build));
				buildDb = buildDao.save(build);
			} 
			
			savedTestSuite = testSuiteDao.getTestSuiteByNameAndByBuildId(testSuiteName, buildDb.getId());
			if (savedTestSuite == null) {
				savedTestSuite = testSuiteDao.save(new TestSuite(testSuiteName, buildDb));
			}
		} 
	   
		Hw hw = hwDao.getHwByName(testSuiteData.getHw());			
		if (hw == null) {
			hw = hwDao.save(new Hw(testSuiteData.getHw()));
		}
	   
	   	Date startTime = null;
		try {
			startTime = sdf.parse(testSuiteData.getStartTime());
		} catch (ParseException e) {
			logger.error("TestSuite start time has incorrect format.");
			e.printStackTrace();
		}
		
		TestSuiteRun testSuiteRun = testSuiteRunDao.save(new TestSuiteRun(startTime, hw, savedTestSuite));
		return Response.ok(gson.toJson(testSuiteRun.getId())).header("Message", "TestSuite " + testSuiteName + " was created.").status(201).build();
	}
	
	@Path(RelURL.TEST_SUITE_RUN + "{id}")
	@POST
	@Consumes("text/json")
	@Produces("text/json")
	public Response processTestSuiteRun(@PathParam("id") final Long testSuiteRunId, String methodName) {
		methodName = methodName.substring(1, methodName.length() - 1);
		Method method = methodDao.getMethodByName(methodName);
		if (method == null) {
			method = methodDao.save(new Method(methodName));
		}
		
		TestSuiteRun testSuiteRun = testSuiteRunDao.getTestSuiteRunById(testSuiteRunId);
		TestRun testRun = testRunDao.save(new TestRun(TestRunStatus.TESTED.getCode(), method, testSuiteRun));
		return Response.ok(gson.toJson(testRun.getId())).header("Message", "Method " + methodName + " was created.").status(201).build();
	}
	
	private AttrUnit createAttrUnit(String attrItem, Attr attr, Attribute a, Unit unit, AttrResultData data) {
		AttrUnit attrUnit = new AttrUnit(attrItem, a.getUnit());
		if (attr == null) {
			if (unit == null) {
				attrUnit.setStatus(AttrUnitStatus.ATTR_UNIT_ERR);
			} else {
				attrUnit.setStatus(AttrUnitStatus.ATTR_ERR);
			}
		} else {
			if (unit == null) {
				attrUnit.setStatus(AttrUnitStatus.UNIT_ERR);
			} else if (attr.getUnit().getName().equals(unit.getName())) {
				attrUnit.setStatus(AttrUnitStatus.ATTR_UNIT_OK);
			} else {
				attrUnit.setStatus(AttrUnitStatus.UNIT_ERR_ASSIGNED);
			}
		}
		return attrUnit;
	}
	
	private ConfigProblem processAttrResultData(TestRun testRun, final AttrResultData data) {
		ConfigProblem configProblem = new ConfigProblem();
		List<AttrResult> attrResultList = new LinkedList<AttrResult>();	
		for(String attrItem: data.getAttrResult().keySet()) {
			Attr attr = attrDao.getAttrByName(attrItem);
			Attribute a = data.getAttrResult().get(attrItem);
			Unit unit = unitDao.getUnitByName(a.getUnit());
			
			AttrUnit attrUnit = createAttrUnit(attrItem, attr, a, unit, data);
			if (attrUnit.getStatus() != AttrUnitStatus.ATTR_UNIT_OK) {
				// other processing is useless, because these data is irrelevant
				configProblem.getProblems().add(attrUnit);
				continue;
			}
			
			AttrResult attrResult = attrResultDao.getAttrResultByTestRunIdAndByAttrId(testRun.getId(), attr.getId());
			if (attrResult == null) {
				attrResult = new AttrResult(attr, testRun);
			}
			
			List<Result> resultList = new LinkedList<Result>();
			Date datetime = null;
			for (int i = 0; i < a.getResults().size(); i++) {
				try {
					datetime = sdf.parse(a.getResults().get(i).getDate());
				} catch (ParseException e) {
					logger.error("Performance data use incorrect date time.");
					e.printStackTrace();
				}
				Result result = new Result(datetime, a.getResults().get(i).getValue(), attrResult);
				resultList.add(result);
			}
			attrResult.setResults(resultList);
			attrResultList.add(attrResult);
		}
		
		if (configProblem.getProblems().isEmpty()) {
			// attributes and units are OK, let store the data
			for(AttrResult attrResult: attrResultList) {
				attrResultDao.save(attrResult);
			}
			return null;
		} else {
			return configProblem;
		}
	}
	
	@Path(RelURL.REMOTE_TEST_SUITE_RUN + "{id}")
	@POST
	@Consumes("text/json")
	@Produces("text/json")
	public Response processDataFromRemotePerfMonitor(@PathParam("id") final Long testSuiteRunId, final AttrResultData data) {
		// get actually tested method for our testSuiteRunId and save the received performance data
		TestRun testRun = testRunDao.getTestedTestRunByTestSuiteRunId(testSuiteRunId);
		if (testRun != null) {
			// test is running so data can be stored
			ConfigProblem configProblem = processAttrResultData(testRun, data);
			if (configProblem == null) {
				logger.info("PerfServer processes data from remote PerfMonitor which belong to TestSuiteRunID: {}", testSuiteRunId);
				return Response.noContent().header("Message", "Data from remote PerfMonitor which belong to TestSuiteRunID: " + testSuiteRunId + " were stored.").status(201).build();
			} else {
				return Response.ok(gson.toJson(configProblem)).header("Message", "Remote PerfMonitor is not correctly configured.").status(333).build();
			}
		} else {
			// no method is running
			return Response.noContent().header("Message", "Data from remote PerfMonitor which belong to TestSuiteRunID: " + testSuiteRunId + " were not stored. No method is running.").status(399).build();
		}
	}
	
	@Path(RelURL.TEST_RUN + "{id}")
	@POST
	@Consumes("text/json")
	@Produces("text/json")
	public Response processTestRunData(@PathParam("id") final Long testRunId, final AttrResultData data) throws ParseException {
		TestRun testRun = testRunDao.getTestRunById(testRunId);
		ConfigProblem configProblem = processAttrResultData(testRun, data);
		if (configProblem == null) {
			return Response.noContent().header("Message", "Data from PerfMonitor were stored.").status(201).build();
		} else {
			return Response.ok(gson.toJson(configProblem)).header("Message", "PerfMonitor is not correctly configured.").status(333).build();
		}
	}
	
	@Path(RelURL.TEST_RUN_FINISH + "{id}")
	@POST
	@Consumes("text/json")
	public Response processLastTestRunData(@PathParam("id") final Long testRunId, final LastTestRunData data) {
		TestRun testRun = testRunDao.getTestRunById(testRunId);
		testRun.setDescription(data.getDescription());
		testRun.setStatus(data.isSuccess()? TestRunStatus.SUCCESSFULL.getCode() : TestRunStatus.FAILED.getCode());
		testRun.setError(data.getError());
		testRun.setParameters(data.getParameters());
		testRunDao.save(testRun);
		return Response.ok().header("Message", "Last data for TestRunID: " + testRunId + " were stored").status(201).build();
	}

	@Path(RelURL.TEST_SUITE_FINISH + "{testSuiteRunId}")
	@POST
	@Consumes("text/json")
	public Response processLastTestSuiteData(@PathParam("testSuiteRunId") final Long testSuiteRunId, final LastTestSuiteData data) {
		TestSuiteRun testSuiteRun = testSuiteRunDao.getTestSuiteRunById(testSuiteRunId);
		List<TestRun> testRuns = new LinkedList<TestRun>();
		for(String method: data.getExcludedMethods()) {
			Method m = methodDao.getMethodByName(method);
			if (m == null) {
				m = methodDao.save(new Method(method));
			}
			testRuns.add(new TestRun(TestRunStatus.EXCLUDED.getCode(), m, testSuiteRun));
		}
		
		Date endTime = null;
		try {
			endTime = sdf.parse(data.getEndTime());
		} catch (ParseException e) {
			logger.debug("TestSuite end time has incorrect format.");
			e.printStackTrace();
		}
		
		testSuiteRun.setEndTime(endTime);
		testSuiteRun.setTestRuns(testRuns);
		testSuiteRunDao.save(testSuiteRun);
		return Response.ok().header("Message", "Last data for TestSuiteRunID: " + testSuiteRunId + " were stored.").status(201).build();
	}
	
	private Response checkParameters(final String project, final String baseBuild, final String build, final String testSuite) {
		Project projectDb = projectDao.getProjectByName(project);
		if (projectDb == null) {
			return Response.noContent().header("Message", "Project " + project + " does not exist.").status(340).build();
		}
		
		Build buildDb = buildDao.getBuildByNameAndByProject(build, project);
		if (buildDb == null) {
			return Response.noContent().header("Message", "Build " + build + " from project " + project + " does not exist.").status(341).build();
		}
		
		Build baseBuildDb = buildDao.getBuildByNameAndByProject(baseBuild, project);
		if (baseBuildDb == null) {
			return Response.noContent().header("Message", "Base build " + baseBuild + " from project " + project + " does not exist.").status(342).build();
		}
		
		testSuiteRun = testSuiteRunDao.getLastFinishedTestSuiteRunByTestSuiteAndByProjectAndByBuild(testSuite, project, build);
		if (testSuiteRun == null) {
			return Response.noContent().header("Message", "TestSuite " + testSuite + " from project " + project + " and build " + build + " does not exist.").status(343).build();
		}
		
		if (build.equals(baseBuild)) {
			baseTestSuiteRun = testSuiteRunDao.getBeforeLastFinishedTestSuiteRunByTestSuiteAndByProjectAndByBuild(testSuite, project, build);
			if (baseTestSuiteRun == null) {
				return Response.noContent().header("Message", "TestSuite " + testSuite + " from project " + project + " and build " + build + " finished before last TestSuite does not exist.").status(344).build();
			}
		} else {
			baseTestSuiteRun = testSuiteRunDao.getLastFinishedTestSuiteRunByTestSuiteAndByProjectAndByBuild(testSuite, project, baseBuild);
			if (baseTestSuiteRun == null) {
				return Response.noContent().header("Message", "TestSuite " + testSuite + " from project " + project + " and base build " + baseBuild + " does not exist.").status(344).build();
			}
		} 
		
		return null;
	}
	
	@Path(RelURL.EXPORT + "{project}")
	@GET
	@Produces("text/json")
	public Response getLastFinishedTestSuiteForPortal(@PathParam("project") final String project, @MatrixParam("testsuite") final String testSuite, @MatrixParam("basebuild") final String baseBuild, @MatrixParam("build") final String build) {
		Response response = checkParameters(project, baseBuild, build, testSuite);
		if (response != null) {
			return response;
		}
		
		ThresholdHelper thresholdHelper = new ThresholdHelper(thresholdDao.getGlobalThresholds(), thresholdDao.getProjectLocalThresholds(project));
		List<Method> methods = methodDao.getAllMethodsByTestSuiteRunId(testSuiteRun.getId());
		List<Attr> attributes = attrDao.getAttrs();
		
		List<MData> data = new LinkedList<MData>();
		for(Method method: methods) {
			Map<String, PData> attrData = new HashMap<String, PData>();
			for(Attr attr: attributes) {
				computeBean.setBaseTestSuiteValue(baseTestSuiteRun.getId(), method.getId(), attr.getId(), Fce.MEAN);
				computeBean.setComparedTestSuiteValue(testSuiteRun.getId(), method.getId(), attr.getId(), Fce.MEAN);
				computeBean.computeDiffAndShare();
				
				PData pData = new PData(attr.getUnit().getName(), 
										computeBean.formatValue(computeBean.getBaseTestSuiteValue()), 
										computeBean.formatValue(computeBean.getComparedTestSuiteValue()),  
										computeBean.formatValue(computeBean.getDiff()), 
										computeBean.formatPercentage(computeBean.getShare()), 
										thresholdHelper.determineThresholdStatus(computeBean.getDiff(), attr.getName()).getFlag());
				attrData.put(attr.getName(), pData);
			}
			data.add(new MData(method.getName(), attrData));
		}
		return Response.ok(gson.toJson(data)).header("Message", "Export data for project " + project + ", TestSuite " + testSuite + ", base build " + baseBuild + " and build " + build + " was successfull.").build();
	}
}
