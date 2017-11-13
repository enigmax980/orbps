com.orbps.common = {};
com.orbps.common.rootIsEdit = false;
/* 根节点模型 */
com.orbps.common.OHMFootTemp = {
	id : null,
	pId : null,
	name : null,
	open : false,
	nocheck : false,
	checked : false,
	noRemoveBtn : true,
	noRenameBtn : true,
	noEditBtn : true,
	chkDisabled : false,
	isEdit : true,
	children:[]
}

/* 子节点模型 */
com.orbps.common.OHMTemp = {
	id : null,
	pId : null,
	name : null,
	checked : false,
	noEditBtn : false,
	noRemoveBtn : false,
	isEdit : true,
	children:[]
}

/* 组织层次信息递归遍历树   */
com.orbps.common.OHMFtn = function(organizaHierarModalVos){
	var ztreeVos = [];
	if(lion.util.isNotEmpty(organizaHierarModalVos)){
		com.orbps.common.rootIsEdit = true;
		for (var i = 0; i < organizaHierarModalVos.length; i++) {
			var organizaHierarModalVo = organizaHierarModalVos[i];
			if(organizaHierarModalVo.isRoot==="Y"){
				var ztreeVo = {
						id : organizaHierarModalVo.levelCode,
						pId : "#",
						name : organizaHierarModalVo.companyName,
						open : false,
						nocheck : false,
						checked : false,
						noRemoveBtn : true,
						noRenameBtn : true,
						noEditBtn : true,
						chkDisabled : false,
						isEdit : true,
						children:[]
					};
				com.orbps.common.OHMChildFtn(organizaHierarModalVos, ztreeVo);
				ztreeVos.push(ztreeVo);
			}
		}
	}
	return ztreeVos;
}

com.orbps.common.OHMChildFtn = function(organizaHierarModalVos, ztreeVo){
	for (var i = 0; i < organizaHierarModalVos.length; i++) {
		var organizaHierarModalVo = organizaHierarModalVos[i];
		if(organizaHierarModalVo.prioLevelCode === ztreeVo.id){
			var ztreeVoChild = {
					id : organizaHierarModalVo.levelCode,
					pId : organizaHierarModalVo.prioLevelCode,
					name : organizaHierarModalVo.companyName,
					checked : false,
					noEditBtn : false,
					noRemoveBtn : false,
					isEdit : true,
					children:[]
				};
			com.orbps.common.OHMChildFtn(organizaHierarModalVos, ztreeVoChild);
			ztreeVo.children.push(ztreeVoChild);
		}
	}
}