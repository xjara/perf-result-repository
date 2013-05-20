package cz.vutbr.fit.mis.dip.perfserver.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.validation.constraints.NotNull;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.XMLReaderXSDFactory;
import org.primefaces.model.UploadedFile;

import cz.vutbr.fit.mis.dip.perfobjects.constants.Format;
import cz.vutbr.fit.mis.dip.perfserver.dao.AttrDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.BuildDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.HwDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.MethodDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.ProjectDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.TestSuiteDao;
import cz.vutbr.fit.mis.dip.perfserver.dao.TestSuiteRunDao;
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


@ManagedBean
@RequestScoped
public class FileUploadBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotNull(message="File with TestSuite must be not Empty.")
	private UploadedFile file;
	private SimpleDateFormat sdf = new SimpleDateFormat(Format.DATE_TIME);
	
	@EJB
	private ProjectDao projectdao;
	@EJB
	private HwDao hwDao;
	@EJB
	private BuildDao buildDao;
	@EJB
	private AttrDao attrDao;
	@EJB
	private TestSuiteDao testSuiteDao;
	@EJB
	private MethodDao methodDao;
	@EJB
	private TestSuiteRunDao testSuiteRunDao;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    private File createFileFromStream(InputStream is) {
    	File file = new File("file");
    	try {
    		OutputStream os = new FileOutputStream(file);
    		byte[] buffer = new byte[1024];
    		int length;
    		while((length = is.read(buffer)) > 0) {
    			os.write(buffer, 0, length);
    		}
    		os.close();
    		is.close();
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    	return file;
    }
   
    public void upload() {
    	Project project = null;
    	TestSuiteRun testSuiteRun = null;
    	Build build = null;
    	boolean saveOnlyBuild = false;
    	boolean saveOnlyTestSuiteRun = false;
    	String error = null;
	  	try {
	  		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	  		File xsdFile = new File(ec.getRealPath("/resources/files/schema.xsd"));
	  		File xmlFile = createFileFromStream(file.getInputstream());
	    	XMLReaderJDOMFactory factory = new XMLReaderXSDFactory(xsdFile);
	    	SAXBuilder builder = new SAXBuilder(factory);
	  		Document document = (Document) builder.build(xmlFile);
	  		Element rootNode = document.getRootElement();
	  		String testSuiteName= rootNode.getAttributeValue("name");
	  		Element node = rootNode.getChild("properties");
	  		
	  		String projectName = node.getChildText("project");
	  		String buildName = node.getChildText("build");
	  		String hwName = node.getChildText("hw");
	  		String startTime = node.getChildText("starttime");
	  		String endTime = node.getChildText("endtime");
	  		
	  		node = rootNode.getChild("methods");
	  		Hw hw = hwDao.getHwByName(hwName);
	  		if (hw == null) {
	  			hw = hwDao.save(new Hw(hwName));
	  		}
	  		
	  		TestSuite testSuite = null;
	  		project = projectdao.getProjectByName(projectName);
	  		
	  		if (project != null) {
	  			// project already exists
	  			build = buildDao.getBuildByNameAndByProjectId(buildName, project.getId());
	  			if (build != null) {
	  				// build already exists
	  				testSuite = testSuiteDao.getTestSuiteByNameAndByBuildId(testSuiteName, build.getId());
	  				if (testSuite != null) {
	  					// testsuite already exists
	  					saveOnlyTestSuiteRun = true;
	  				} else {
	  					saveOnlyBuild = true;
	  					testSuite = new TestSuite(testSuiteName, build);
	  					
	  					// build.getTestSuites().add(testSuite);	doesn't work because of lazy initialization
	  					List<TestSuite> testSuites = new LinkedList<TestSuite>();
	  					testSuites.add(testSuite);
	  					build.setTestSuites(testSuites);
	  				}
	  			} else {
	  				// project exists but build and testSuite not
	  				build = new Build(buildName, project);
	  				testSuite = new TestSuite(testSuiteName, build);
	  				build.getTestSuites().add(testSuite);
	  				
	  				// project.getBuilds().add(build);		doesn't work because of lazy initialization
	  				List<Build> builds = new LinkedList<Build>();
			  		builds.add(build);
	  				project.setBuilds(builds);
	  				
	  			}
	  		} else {
	  			// project, build and testSuite don't exist
	  			project = new Project(projectName);
		  		build = new Build(buildName, project);
		  		testSuite = new TestSuite(testSuiteName, build);
		  		build.getTestSuites().add(testSuite);
		  		
		  		// project.getBuilds().add(build);		doesn't work because of lazy initialization
		  		List<Build> builds = new LinkedList<Build>();
		  		builds.add(build);
		  		project.setBuilds(builds);
	  		}
	  	
	  		testSuiteRun = new TestSuiteRun(sdf.parse(startTime), sdf.parse(endTime), hw, testSuite);
	  		List<TestRun> testRunList = new LinkedList<TestRun>();
	  		
	  		List<Element> methods = node.getChildren();
	  		for(int i = 0; i < methods.size(); i++) {	
	  			Element methodNode = methods.get(i);
	  			String methodName = methodNode.getAttributeValue("name");
	  			node = methodNode.getChild("info");
	  			Method method = methodDao.getMethodByName(methodName);
	  			if (method == null) {
	  				// method doesn't exist 
	  				method = methodDao.save(new Method(methodName));
	  			}
	  			
	  			String description = node.getChildText("description");
	  			int status = Integer.parseInt(node.getChildText("status"));
	  			String exception = node.getChildText("exception");
	  			String params = node.getChildText("params");
	  			
	  			TestRun testRun = new TestRun(status, 
	  											(exception == null || exception.equals(""))? null : exception, 
	  											(params == null || params.equals(""))? null : params,
	  											(description == null || description.equals(""))? null : description,
	  											method, 
	  											testSuiteRun);
	  			
	  			List<AttrResult> attrResultList = new LinkedList<AttrResult>();
	  			AttrResult attrResult;
	  			node = methodNode.getChild("data");
	  			List<Element> attributes = node.getChildren(); 
	  			for(int j = 0; j < attributes.size(); j++) {
	  				node = attributes.get(j);	
	  				String attrName = node.getAttributeValue("name");
	  				String unitName = node.getAttributeValue("unit");
	  				
	  				Attr attr = attrDao.getAttrByName(attrName);
	  				if (attr == null) {
	  					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Attribute " + attrName + " is not supported. TestSuite import was not successfull.");
	  		            FacesContext.getCurrentInstance().addMessage("form:fileupload", msg);
	  		            return;
	  				}
	  				
	  				if (!attr.getUnit().getName().equals(unitName)) {
	  					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Attribute " + attrName + " can be measured only in " + attr.getUnit().getName() + ". TestSuite import was not successfull.");
	  		            FacesContext.getCurrentInstance().addMessage("form:fileupload", msg);
	  		            return;
	  				}
	  				
	  				attrResult = new AttrResult(attr, testRun);
	  				List<Result> resultList = new LinkedList<Result>();
	  				
	  				List<Element> values = node.getChildren();
	  				for(int k = 0; k < values.size(); k++) {
	  					node = values.get(k);
	  					String time = node.getAttributeValue("time");
	  					String value = node.getText();
	  					Result resultValue = new Result(sdf.parse(time), Double.valueOf(value), attrResult);
	  					resultList.add(resultValue);
	  				}
	  				attrResult.setResults(resultList);
	  				attrResultList.add(attrResult);
	  			}
	  			testRun.setAttrResults(attrResultList);
	  			testRunList.add(testRun);
	  		}
	  		testSuiteRun.setTestRuns(testRunList);
	  		
	  		// testSuite.getTestSuiteRuns().add(testSuiteRun);	doesn't work because of lazy initialization
	  		List<TestSuiteRun> testSuiteRuns = new LinkedList<TestSuiteRun>();
	  		testSuiteRuns.add(testSuiteRun);
	  		testSuite.setTestSuiteRuns(testSuiteRuns);
	  		
	  	} catch (IOException io) {
	  		error = ((String[])io.getMessage().split("\n", 2))[0];
	  		io.printStackTrace();
	  	} catch (JDOMException jdomex) {
	  		error = ((String[])jdomex.getMessage().split("\n", 2))[0];
	  		jdomex.printStackTrace();
	  	} catch (ParseException e) {
	  		error = ((String[])e.getMessage().split("\n", 2))[0];
			e.printStackTrace();
		}
	  	
	  	FacesMessage msg;
	  	if (error != null) {
	  		msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "TestSuite from file " + file.getFileName() + " was not imported: " + error);    
	  	} else {
	  		if (saveOnlyBuild) {
	  			buildDao.save(build);
	  		} else if (saveOnlyTestSuiteRun) {
	  			testSuiteRunDao.save(testSuiteRun);
	  		} else {
	  			// save the whole project
		  		projectdao.save(project);
	  		}	
	        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, null, "TestSuite from file " + file.getFileName() + " was sucessfully imported.");
	  	}
	  	FacesContext.getCurrentInstance().addMessage("form:fileupload", msg);
    }
}
