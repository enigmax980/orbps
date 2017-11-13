package com.newcore.orbps.util;

import java.util.ArrayList;
import java.util.List;

import com.newcore.authority_support.models.Branch;

public class BranchNoUtils {
	public static List<String> getAllSubBranchNo(Branch branch){
		List<String> subBranchNos = new ArrayList<>();
		if(branch.getChildren()==null){
			branch.setChildren(new ArrayList<>());
		}
		for(Branch branch1:branch.getChildren()){
			subBranchNos.add(branch1.getBranchNo());
			subBranchNos.addAll(getAllSubBranchNo(branch1));
		}
		return subBranchNos;
	}
}
