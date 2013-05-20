package cz.vutbr.fit.mis.dip.perfserver.controller.objects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.vutbr.fit.mis.dip.perfserver.enums.ThresholdStatus;
import cz.vutbr.fit.mis.dip.perfserver.model.Threshold;


public class ThresholdHelper {
	private Map<String, Double> globalThresholdMap;
	private Map<String, Double> localThresholdMap;
	
	private Map<String, Double> convertThresholdListToMap(List<Threshold> thresholds) {
		Map<String, Double> thresholdMap = new HashMap<String, Double>();
		for(Threshold threshold: thresholds) {
			thresholdMap.put(threshold.getAttr().getName(), threshold.getValue());
		}
		return thresholdMap;
	}
	
	public ThresholdHelper(List<Threshold> globalThresholds, List<Threshold> localThresholds) {
		globalThresholdMap = convertThresholdListToMap(globalThresholds);
		localThresholdMap = convertThresholdListToMap(localThresholds);
	}
	
	public ThresholdStatus determineThresholdStatus(Double value, String attrName) {
		ThresholdStatus thStatus = ThresholdStatus.NONE;
		if (value == null) {
			return thStatus;
		}
		
		Double threshold = globalThresholdMap.get(attrName);
		if (threshold != null && value >= threshold) {
			thStatus = ThresholdStatus.GLOBAL;
		}
		
		threshold = localThresholdMap.get(attrName);
		if (threshold != null && value >= threshold) {
			thStatus = ThresholdStatus.LOCAL;
		}
		return thStatus;
	}
}
