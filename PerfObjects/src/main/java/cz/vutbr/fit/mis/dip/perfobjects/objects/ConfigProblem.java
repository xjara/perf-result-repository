package cz.vutbr.fit.mis.dip.perfobjects.objects;

import java.util.LinkedList;
import java.util.List;

public class ConfigProblem {
	private List<AttrUnit> problems = new LinkedList<AttrUnit>();
	
	public ConfigProblem() {
	}
	
	public ConfigProblem(List<AttrUnit> problems) {
		this.problems = problems;
	}

	public List<AttrUnit> getProblems() {
		return problems;
	}

	public void setProblems(List<AttrUnit> problems) {
		this.problems = problems;
	}
}
