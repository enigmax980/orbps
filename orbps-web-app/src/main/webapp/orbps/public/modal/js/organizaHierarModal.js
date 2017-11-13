/** 节点名称 */
com.orbps.common.treeNodeName = "";
com.orbps.common.treeNodeCount = 0;
com.orbps.common.addDialog = $("#btnSelectModel");
com.orbps.common.nodeType;
com.orbps.common.name;
com.orbps.common.organizaform = $("#organizaform");
com.orbps.common.index = 0;
com.orbps.common.treeNodeId;
com.orbps.common.parentId;
com.orbps.common.custNo;
com.orbps.common.parentName;
com.orbps.common.rootIsUpdateEdit=false;
com.orbps.common.grpInsurApplVo={};
//增加节点
com.orbps.common.addHoverDom = function (treeId, treeNode) {
	
	var sObj = $("#" + treeNode.tId + "_span");
	if (treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0){
		return;
	}
	var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
			+ "' title='add node' onfocus='this.blur();'></span>";
	sObj.after(addStr);
	com.orbps.common.name = 'NewNode';
	var btn = $("#addBtn_" + treeNode.tId);
	if (btn)
		btn.bind("click", function() {
			if(!treeNode.isEdit){
				lion.util.info("提示","节点数据完善后才能添加子节点");
				return false;
			}
			$("#groupSelectForm").reset();
			$("#organizaform").reset();
			$("#organizaform #bankIs").hide();
			$("#organizaform #bankNoIs").hide();
			// 增加单位团体名称只读
			$("#groupSelectForm #groupDep").attr("readOnly",true);
			if(treeNode.isNodeType === "1"){
				lion.util.info("提示","节点【"+treeNode.name+"】的节点类型为部门，不能添加子节点！");
				return false;
			}
			$("#groupSelectForm #groupDep").val("");
			$("#departmentName").hide();
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			var sNodes = zTree.getSelectedNodes();
			if(sNodes.length > 0){
				zTree.cancelSelectedNode(sNodes[0]);
			}
			treeNode.checked = false;
			zTree.addNodes(treeNode, {
				id : ((++com.orbps.common.index) + treeNode.id),
				pId : treeNode.id,
				name : com.orbps.common.name,
				checked : false,
				noEditBtn: false,
				noRemoveBtn: false,
				isEdit : false
			});
			com.orbps.common.zNodes = treeNode;
			return false;
		});
};

// 是否显示编辑按钮
com.orbps.common.showRenameBtn = function (treeId, treeNode) {
	// 获取节点所配置的noEditBtn属性值
	if (treeNode.noEditBtn != undefined && treeNode.noEditBtn) {
		return false;
	} else {
		return true;
	}
}
// 是否显示删除按钮
com.orbps.common.showRemoveBtn = function (treeId, treeNode) {
	// 获取节点所配置的noRemoveBtn属性值
	if (treeNode.noRemoveBtn != undefined && treeNode.noRemoveBtn) {
		return false;
	} else {
		return true;
	}
}

//展示投保单带过来的信息
com.orbps.common.showorgaNizeForm = function(){
	$("#organizaform #idCardNo").val(com.orbps.common.idNo);
	$("#organizaform #totalMembers").val(com.orbps.common.numOfEmp);
	$("#organizaform #ojEmpNum").val(com.orbps.common.ojEmpNum);
	$("#organizaform #town").val(com.orbps.common.appAddrTown);
	$("#organizaform #village").val(com.orbps.common.appAddrValige);
	$("#organizaform #detailAddress").val(com.orbps.common.appAddrHome);
	$("#organizaform #postCode").val(com.orbps.common.appPost);
	$("#organizaform #contactName").val(com.orbps.common.connName);
	$("#organizaform #phoneNum").val(com.orbps.common.connPhone);
	$("#organizaform #email").val(com.orbps.common.connPostcode);
	$("#organizaform #fixedPhones").val(com.orbps.common.appHomeTel);
	setTimeout(function(){
		$("#organizaform #industryClassification").combo("val",com.orbps.common.occDangerFactor);
		$("#organizaform #deptType").combo("val",com.orbps.common.idType);
		// 将省市县置为可以回显的状态
		$(".dist").attr("disabled",false);
		$(".city").attr("disabled",false);
		$(".prov").attr("disabled",false);
		$("#organizaform #province").combo("val",com.orbps.common.appAddrProv);
		$("#organizaform #city").combo("val",com.orbps.common.appAddrCity);
		$("#organizaform #county").combo("val",com.orbps.common.appAddrCountry);
		$("#organizaform #registeredNationality").combo("val", com.orbps.common.registerArea);
		$("#organizaform #securityOptions").combo("val", "N");
		$("#organizaform #serviceAssignment").combo("val", "N");
		$("#organizaform #invoiceOption").combo("val", "N");
	},1000);
}

//初始化函数
$(function() {

	//省市县三级联动
    $("#citySelects").citySelect({
        url:"/resources/global/js/cityselect/js/city.min.json",
        required:false
    });

	// 增加单位团体名称只读
	$("#organizaform #companyName").val(com.orbps.common.companyName);
	com.orbps.common.showorgaNizeForm();
});

// ztree设置
var setting = {
	view : {
		addHoverDom : com.orbps.common.addHoverDom, // 当鼠标移动到节点上时，显示用户自定义控件
		removeHoverDom : com.orbps.common.removeHoverDom, // 离开节点时的操作
		selectedMulti : false
	},
	check : {
		enable : true,
		chkStyle: "radio",
		radioType : "all"
	},
	data : {
		simpleData : {
			enable : true,
			idKey : "id",
			pIdKey : "pId",
			system : "system",
			rootPId : ""
		}
	},
	edit : {
		enable : true, // 单独设置为true时，可加载修改、删除图标
		showRenameBtn:com.orbps.common.showRenameBtn,
		showRemoveBtn:com.orbps.common.showRemoveBtn,
		showAddBtn:true,
		editNameSelectAll : true
	},
	callback : {
		onCheck : function(event, treeId, treeNode) {
			var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
			var nodes = treeObj.getNodes();
			com.orbps.common.zTrees = nodes;
			com.orbps.common.parentId = treeNode.pId;
			com.orbps.common.treeNodeName = treeNode.name;
			com.orbps.common.treeNode = treeNode;
			com.orbps.common.treeNodeId = treeNode.id
			if(treeNode.checked){
				treeObj.selectNode(treeNode);
				com.orbps.common.showTreeData(treeNode.id);
				var nodeType = $("#groupSelectForm #nodeType").val();
				if(nodeType==="1"){
					var parentNode = treeNode.getParentNode();
					$("#organizaform #companyName").val(parentNode.name);
				}
			}else{
				$("#groupSelectForm").reset();
				$("#organizaform").reset();
			}
		},
		onRename: function (event, treeId, treeNode){
			com.orbps.common.treeNodeName = treeNode.name;
			if(treeNode.checked){
				var nodeType = $("#groupSelectForm #nodeType").val();
				if(nodeType === "1"){
					$("#groupSelectForm #groupDep").val(treeNode.name);
				}else{
					$("#organizaform #companyName").val(treeNode.name);
				}
			}
			
		},
		onRemove: function (event, treeId, treeNode){
			com.orbps.common.deleteTreeData(treeNode.id);
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			var nodes = zTree.getNodes();
			com.orbps.common.zTrees=nodes;
			zTree.removeNode(treeNode);
		}
	}

},zTree;

//选择节点信息
$("#groupSelectForm #nodeType").change(function(){
	var nodeType = $("#groupSelectForm #nodeType").val();
	if(!com.orbps.common.rootIsEdit && nodeType==="1"){
		lion.util.info("提示：", "根节点节点类型不能选择部门！");
		$(this).val("0");
		return false;
	}
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	var sNodes = treeObj.getSelectedNodes();
	if(lion.util.isNotEmpty(sNodes) && lion.util.isEmpty(sNodes[0].pId) && nodeType==="1"){
		lion.util.info("提示：", "根节点节点类型不能选择部门！");
		$(this).val("0");
		return false;
	}
	com.orbps.common.nodeType = nodeType;
	if(nodeType === "0"){
		com.orbps.common.oraganizaReadOnlyFalse();
		$("#organizaform").reset();
		$("#groupSelectForm #departmentName").hide();
		$("#groupSelectForm #groupDep").val("");
		if(lion.util.isEmpty(sNodes)){
			$("#organizaform #companyName").val("");
		}else{
			$("#organizaform #companyName").val(sNodes[0].name);
		}
	}else if(nodeType === "1"){
		com.orbps.common.oraganizaReadOnlyTrue();
		$("#groupSelectForm #departmentName").show();
		if(lion.util.isEmpty(sNodes)){
			lion.util.info("提示：", "请选择一个节点！");
			$("#groupSelectForm #groupDep").val("");
			$("#organizaform #companyName").val("");
		}else{
			var parentNode = sNodes[0].getParentNode();
			$("#groupSelectForm #groupDep").val(sNodes[0].name);
			$("#organizaform #companyName").val(parentNode.name);
		}
		com.orbps.common.showorgaNizeForm();
	}else{
		lion.util.info("提示","请选择节点类型");
		$("#groupSelectForm #departmentName").hide();
		$("#groupSelectForm #groupDep").val("");
		return false;
	}
	
	if(com.orbps.common.treeNodeName === "NewNode"){
		lion.util.info("提示","请先在左侧修改节点默认名称后,再点击该节点(如已修改完,请选择节点)");
		return false;
	}else if(com.orbps.common.treeNodeName === ""){
		lion.util.info("提示","请先在左侧修改节点默认名称后,再点击该节点(如已修改完,请选择节点)");
		return false;
	}
	$("#organizaform #bankIs").hide();
	$("#organizaform #bankNoIs").hide();
});


//鼠标移除事件
com.orbps.common.removeHoverDom = function (treeId, treeNode) {
	$("#addBtn_" + treeNode.tId).unbind().remove();
};

//客户号只读
$("#organizaform #custNo").attr("readOnly",true);

//新增信息
$("#organizaform #btnAdd").click(function() {
	console.log(1);
	// 保单验证
	if(com.orbps.common.organizaSubmitValidate()){
		 
	}else{
		return false
	}
	var orgList = {};
	if(com.orbps.common.organizaSelecetValidate()){
		var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		var nodes = treeObj.getNodes();
		var sNodes = treeObj.getSelectedNodes();
		var isNodeType;
		if(!com.orbps.common.rootIsEdit){
			isNodeType="0";
		}else{
			isNodeType = $("#groupSelectForm #nodeType").val();
			if(lion.util.isEmpty(sNodes)){
				lion.util.info("请选择一个子节点！");
				return false;
			}else if(!sNodes[0].isEdit){
				if(lion.util.isEmpty(isNodeType)){
					lion.util.info("提示：", "节点类型必选");
					return false;
				}
			}else if(sNodes[0].isEdit){
				lion.util.info("提示：", "节点已经添加，如改变请点击修改");
				return false;
			}
		}
		
		if(isNodeType==="0"){
			if (confirm("确认保存"+sNodes[0].name+"节点信息？")) {
				var nodeType = $("#groupSelectForm #nodeType").val();
				var nodePayAmnt = $("#organizaform #nodePayAmnt").val();
				if(nodePayAmnt===""){
					$("#organizaform #nodePayAmnt").val("0");
				}
				orgList = $("#organizaform").serializeObject();
				com.orbps.common.grpInsurApplVo.organizaHierarModalVos = [];
				com.orbps.common.grpInsurApplVo.organizaHierarModalVos.push(orgList);
				var grpInsurApplVo = com.orbps.common.grpInsurApplVo;
				//查询客户号
				lion.util.postjson(
		                '/orbps/web/orbps/contractEntry/search/getCustNo',
		                grpInsurApplVo,
		                function(data){
		                	com.orbps.common.custNo = data.organizaHierarModalVos[0].custNo;
		                	if(lion.util.isEmpty(com.orbps.common.custNo)){
		                		lion.util.info("提示","开户失败，请重新录入节点信息!");
		                		return false;
		                	}
		                	var orgList = {};
		                	// 客户号赋值给层级代码
							$("#organizaform #custNo").val(com.orbps.common.custNo);
							orgList = $("#organizaform").serializeObject();
							orgList.partyId = data.organizaHierarModalVos[0].partyId;
							orgList.levelCode = com.orbps.common.custNo;
							if(!com.orbps.common.rootIsEdit){
								orgList.isRoot = "Y";
								orgList.prioLevelCode = "#";
								com.orbps.common.zTrees[0].id = com.orbps.common.custNo;
								com.orbps.common.zTrees[0].isEdit = true;
								com.orbps.common.zTrees[0].isNodeType = isNodeType;
								com.orbps.common.zTrees[0].checked = false;
								com.orbps.common.rootIsEdit = true;
								var zTree = $.fn.zTree.init($("#treeDemo"), setting, com.orbps.common.zTrees);
								var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
								var nodes = treeObj.getNodes();
								com.orbps.common.zTrees = nodes;
							}else{
								orgList.isRoot = "N";
								orgList.prioLevelCode = sNodes[0].pId;
								var tid = sNodes[0].parentTId;
							    var nodesJson = JSON.stringify(com.orbps.common.zTrees);
							    nodesJson = nodesJson.replace('"id":"'+sNodes[0].id+'"', '"id":"'+com.orbps.common.custNo+'"');
							    var nodesObj = JSON.parse(nodesJson);
							    var zTree = $.fn.zTree.init($("#treeDemo"), setting, nodesObj);
							    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
							    var newPNode = treeObj.getNodeByTId(tid);
							    var childs = newPNode.children;
							    for (var i = 0; i < childs.length; i++) {
									var children = childs[i];
									if(children.id === com.orbps.common.custNo){
										newPNode.children[i].isEdit=true;
										newPNode.children[i].checked = false;
										newPNode.children[i].isNodeType = isNodeType;
									}
								}
							    treeObj.reAsyncChildNodes(newPNode, "refresh", false);
							    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
								var nodes = treeObj.getNodes();
								com.orbps.common.zTrees = nodes;
								var zTree = $.fn.zTree.init($("#treeDemo"), setting, nodes);
							}
							orgList.nodeType = $("#groupSelectForm #nodeType").val();
							com.orbps.common.oranLevelList.push(orgList);
							lion.util.info("提示", "添加成功");
							com.orbps.common.treeNodeNameList.push(sNodes[0].name);
		                });
			}
		}else if(isNodeType==="1"){
			if (confirm("确认保存"+sNodes[0].name+"节点信息？")) {
				var nodeType = $("#groupSelectForm #nodeType").val();
				var nodePayAmnt = $("#organizaform #nodePayAmnt").val();
				if(nodePayAmnt===""){
					$("#organizaform #nodePayAmnt").val("0");
				}
				var a = 0;
				for (var i = 0; i < com.orbps.common.oranLevelList.length; i++) {
					var array_element = com.orbps.common.oranLevelList[i];
					var custNo = array_element.custNo;
					var varstart = custNo.indexOf(sNodes[0].pId);
					if(varstart == 0){
						a++;
					}
				}
				if(a>0 && a<10){
					com.orbps.common.custNo = sNodes[0].pId + "00" + a;
				}
				if(a>9 && a<100){
					com.orbps.common.custNo = sNodes[0].pId + "0" + a;
				}
				if(a>99){
					com.orbps.common.custNo = sNodes[0].pId + a;
				}
            	var orgList = {};
            	// 客户号赋值给层级代码
				$("#organizaform #custNo").val(com.orbps.common.custNo);
				orgList = $("#organizaform").serializeObject();
				orgList.levelCode = com.orbps.common.custNo;
				orgList.isRoot = "N";
				orgList.prioLevelCode = sNodes[0].pId;
				var tid = sNodes[0].parentTId;
			    var nodesJson = JSON.stringify(com.orbps.common.zTrees);
			    nodesJson = nodesJson.replace('"id":"'+sNodes[0].id+'"', '"id":"'+com.orbps.common.custNo+'"');
			    var nodesObj = JSON.parse(nodesJson);
				var zTree = $.fn.zTree.init($("#treeDemo"), setting, nodesObj);
				var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
				var newPNode = treeObj.getNodeByTId(tid);
				var childs = newPNode.children;
				for (var i = 0; i < childs.length; i++) {
					var children = childs[i];
					if(children.id === com.orbps.common.custNo){
						newPNode.children[i].isEdit=true;
						newPNode.children[i].checked = false;
						newPNode.children[i].isNodeType = isNodeType;
					}
				}
				treeObj.reAsyncChildNodes(newPNode, "refresh", false);
				var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
				var nodes = treeObj.getNodes();
				com.orbps.common.zTrees = nodes;
				orgList.nodeType = $("#groupSelectForm #nodeType").val();
				orgList.groupDep = $("#groupSelectForm #groupDep").val();
				
				com.orbps.common.oranLevelList.push(orgList);
				lion.util.info("提示", "添加成功");
				com.orbps.common.treeNodeNameList.push(sNodes[0].name);
			}
		}
		var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		var nodes = treeObj.getNodes();
		com.orbps.common.zTrees = nodes;
	}
});

// 清空属组表单
com.orbps.common.clearOranLevelListInfoForm = function (){
	// form清空
	$("#organizaform input[type='text']").val("");
	$("#organizaform select").combo("val","");
	$("#organizaform #province").combo("val","请选择");
	$("#organizaform #county").combo("val","请选择");
	$("#organizaform #city").combo("val","请选择");
	$("#organizaform #province").combo("val","请选择");
	$(".fa").removeClass("fa-warning");
	$(".fa").removeClass("fa-check");
	$(".fa").removeClass("has-success");
	$(".fa").removeClass("has-error");
}

//修改信息
$("#organizaform #btnUpdate").click(function() {
	console.log(1);
	// 保单验证
	if(com.orbps.common.organizaSubmitValidate()){
		
	}else{
		return false;
	}
	if(com.orbps.common.organizaSelecetValidate()){
		var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		var sNodes = treeObj.getSelectedNodes();
		var isNodeType = $("#groupSelectForm #nodeType").val();
		if(lion.util.isEmpty(sNodes)){
			lion.util.info("提示：", "请选择你要修改的节点！");
			return false;
		}
		if(!sNodes[0].isEdit){
			lion.util.info("提示","先添加节点信息才能修改！");
			return false;
		}
		if(sNodes[0].isNodeType === "0"){
			var child = sNodes[0].children;
			if(isNodeType === "1" && lion.util.isNotEmpty(child)){
				lion.util.info("提示","节点类型为部门时，不能有子节点；请删除节点【"+sNodes[0].name+"】下所有子节点后，再做修改操作！");
				return false;
			}
		}
		var orgList = {};
		if(isNodeType==="0"){
			if (confirm("确认修改"+sNodes[0].name+"节点信息？")) {
				var nodeType = $("#groupSelectForm #nodeType").val();
				var nodePayAmnt = $("#organizaform #nodePayAmnt").val();
				if(nodePayAmnt===""){
					$("#organizaform #nodePayAmnt").val("0");
				}
				orgList = $("#organizaform").serializeObject();
				com.orbps.common.grpInsurApplVo.organizaHierarModalVos = [];
				com.orbps.common.grpInsurApplVo.organizaHierarModalVos.push(orgList);
				var grpInsurApplVo = com.orbps.common.grpInsurApplVo;
				//查询客户号
				lion.util.postjson(
		                '/orbps/web/orbps/contractEntry/search/getCustNo',
		                grpInsurApplVo,
		                function(data){
		                	com.orbps.common.custNo = data.organizaHierarModalVos[0].custNo;
		                	if(lion.util.isEmpty(com.orbps.common.custNo)){
		                		lion.util.info("提示","开户失败，请重新录入节点信息!");
		                		return false;
		                	}
		                	var orgList = {};
		                	// 客户号赋值给层级代码
							$("#organizaform #custNo").val(com.orbps.common.custNo);
							orgList = $("#organizaform").serializeObject();
							orgList.levelCode = com.orbps.common.custNo;
							orgList.partyId = data.organizaHierarModalVos[0].partyId;
							if(sNodes[0].pId===""){
								orgList.isRoot = "Y";
								orgList.prioLevelCode = "#";
								com.orbps.common.zTrees[0].isEdit = true;
								com.orbps.common.zTrees[0].isNodeType = isNodeType;
								com.orbps.common.zTrees[0].checked = false;
								for (var i = 0; i < com.orbps.common.oranLevelList.length; i++) {
									var array_element = com.orbps.common.oranLevelList[i];
									if (array_element.levelCode === sNodes[0].id) {
										orgList.nodeType = $("#groupSelectForm #nodeType").val();
										orgList.groupDep = $("#groupSelectForm #groupDep").val();
										com.orbps.common.oranLevelList.splice(i,1,orgList);
										break;
									} 
								}
								com.orbps.common.zTrees[0].id = com.orbps.common.custNo;
								var zTree = $.fn.zTree.init($("#treeDemo"), setting, com.orbps.common.zTrees);
								var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
								var nodes = treeObj.getNodes();
								com.orbps.common.zTrees = nodes;
							}else{
								orgList.isRoot = "N";
								orgList.prioLevelCode = sNodes[0].pId;
								for (var i = 0; i < com.orbps.common.oranLevelList.length; i++) {
									var array_element = com.orbps.common.oranLevelList[i];
									if (array_element.levelCode === sNodes[0].id) {
										orgList.nodeType = $("#groupSelectForm #nodeType").val();
										orgList.groupDep = $("#groupSelectForm #groupDep").val();
										com.orbps.common.oranLevelList.splice(i,1,orgList);
										break;
									} 
								}
								var tid = sNodes[0].parentTId;
							    var nodesJson = JSON.stringify(com.orbps.common.zTrees);
							    nodesJson = nodesJson.replace('"id":"'+sNodes[0].id+'"', '"id":"'+com.orbps.common.custNo+'"');
							    var nodesObj = JSON.parse(nodesJson);
							    var zTree = $.fn.zTree.init($("#treeDemo"), setting, nodesObj);
							    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
							    var newPNode = treeObj.getNodeByTId(tid);
							    var childs = newPNode.children;
							    for (var i = 0; i < childs.length; i++) {
									var children = childs[i];
									if(children.id === com.orbps.common.custNo){
										newPNode.children[i].isEdit=true;
										newPNode.children[i].checked = false;
										newPNode.children[i].isNodeType = isNodeType;
									}
								}
							    treeObj.reAsyncChildNodes(newPNode, "refresh", false);
							    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
								var nodes = treeObj.getNodes();
								com.orbps.common.zTrees = nodes;
								var zTree = $.fn.zTree.init($("#treeDemo"), setting, nodes);
							}
							orgList.nodeType = $("#groupSelectForm #nodeType").val();
							lion.util.info("提示", "修改成功");
							com.orbps.common.treeNodeNameList.push(com.orbps.common.treeNodeName);
		                });
			}
		}else if(isNodeType==="1"){
			if (confirm("确认修改"+sNodes[0].name+"节点信息？")) {
				var nodeType = $("#groupSelectForm #nodeType").val();
				var nodePayAmnt = $("#organizaform #nodePayAmnt").val();
				if(nodePayAmnt===""){
					$("#organizaform #nodePayAmnt").val("0");
				}
				for (var i = 0; i < com.orbps.common.oranLevelList.length; i++) {
					var array_element = com.orbps.common.oranLevelList[i];
					if (array_element.levelCode === sNodes[0].id) {
						var orgList = $("#organizaform").serializeObject();
						orgList.levelCode = sNodes[0].id;
						orgList.prioLevelCode = sNodes[0].pId;
						orgList.isRoot = "N";
						orgList.nodeType = $("#groupSelectForm #nodeType").val();
						orgList.groupDep = $("#groupSelectForm #groupDep").val();
						com.orbps.common.oranLevelList.splice(i,1,orgList);
						break;
					} 
				}
            	var orgList = {};
            	// 客户号赋值给层级代码
				$("#organizaform #custNo").val(sNodes[0].id);
				orgList = $("#organizaform").serializeObject();
				orgList.levelCode = com.orbps.common.custNo;
				orgList.isRoot = "N";
				orgList.prioLevelCode = sNodes[0].pId;
				var tid = sNodes[0].parentTId;
			    var nodesJson = JSON.stringify(com.orbps.common.zTrees);
			    nodesJson = nodesJson.replace('"id":"'+sNodes[0].id+'"', '"id":"'+com.orbps.common.custNo+'"');
			    var nodesObj = JSON.parse(nodesJson);
				var zTree = $.fn.zTree.init($("#treeDemo"), setting, nodesObj);
				var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
				var newPNode = treeObj.getNodeByTId(tid);
				var childs = newPNode.children;
				for (var i = 0; i < childs.length; i++) {
					var children = childs[i];
					if(children.id === com.orbps.common.custNo){
						newPNode.children[i].isEdit=true;
						newPNode.children[i].checked = false;
						newPNode.children[i].isNodeType = isNodeType;
					}
				}
				treeObj.reAsyncChildNodes(newPNode, "refresh", false);
				var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
				var nodes = treeObj.getNodes();
				com.orbps.common.zTrees = nodes;
				orgList.nodeType = $("#groupSelectForm #nodeType").val();
				orgList.groupDep = $("#groupSelectForm #groupDep").val();
				lion.util.info("提示", "修改成功");
				com.orbps.common.treeNodeNameList.push(sNodes[0].name);
			}
		}
		
	}
});

//清空信息
$("#organizaform #btnClear").click(function() {
	com.orbps.common.clearOranLevelListInfoForm();
});

// 删除节点信息
com.orbps.common.deleteTreeData = function (arg){
	console.log(1);
	$("#groupSelectForm #groupDep").val("");
	$("#groupSelectForm #nodeType").val("0");
	$("#organizaform").reset();
	for (var i = 0; i < com.orbps.common.oranLevelList.length; i++) {
		var array_element = com.orbps.common.oranLevelList[i];
		if (array_element.levelCode === arg) {
			com.orbps.common.oranLevelList.splice(i,1);
			$("#groupSelectForm").reset();
			$("#organizaform #bankIs").hide();
			$("#organizaform #bankNoIs").hide();
			break;
		}
	}
}

// 显示已添加的信息
com.orbps.common.showTreeData = function(arg) {
	console.log(1);
	$(".fa").removeClass("has-error");
	$(".fa").removeClass("has-success");
	$(".fa").removeClass("fa-warning");
	$(".fa").removeClass("fa-check");
	var array_element;
	for (var i = 0; i < com.orbps.common.oranLevelList.length; i++) {
		if (com.orbps.common.oranLevelList[i].levelCode === arg) {
			array_element = com.orbps.common.oranLevelList[i];
			break;
		}
	}
	if(lion.util.isNotEmpty(array_element)){
		com.orbps.common.showOrganiza(array_element);
	}else{
		$("#groupSelectForm").reset();
		$("#organizaform").reset();
		$("#organizaform #bankIs").hide();
		$("#organizaform #bankNoIs").hide();
	}
	$("#organizaform #companyName").val(com.orbps.common.treeNodeName);
	$("#organizaform #custNo").attr("readOnly",true);
}

//回显方法
com.orbps.common.showOrganiza = function(msg) {
	console.log(1);
	$("#groupSelectForm #nodeType").find("option[value='"+msg.nodeType+"']").attr("selected", true); 
	if(msg.pay==="Y"){
		$("#organizaform #bankIs").show();
		$("#organizaform #bankNoIs").show();
	}else if(msg.pay==="N"){
		$("#organizaform #bankIs").hide();
		$("#organizaform #bankNoIs").hide();
	}
	jsonStr = JSON.stringify(msg);
	var obj = eval("(" + jsonStr + ")");
	var key, value, tagName, type, arr;
	setTimeout(function() {
		for (x in obj) {
			key = x;
			value = obj[x];
			$("[name='" + key + "'],[name='" + key + "[]']").each(function() {
				tagName = $(this)[0].tagName;
				type = $(this).attr('type');
				if (tagName === 'INPUT') {
					if (type === 'radio') {
						$(this).attr('checked', $(this).val() === value);
					} else if (type === 'checkbox') {
						arr = value.split(',');
						for (var i = 0; i < arr.length; i++) {
							if ($(this).val() === arr[i]) {
								$(this).attr('checked', true);
								break;
							}
						}
					} else {
						$("#organizaform #" + key).val(value);
					}
				} else if (tagName === 'SELECT' || tagName === 'TEXTAREA') {
					$("#organizaform #" + key).combo("val", value);
					if(key==="unitCharacter"){
						$("#organizaform #"+key).find("option[value='"+value+"']").attr("selected", true); 
					}
				}
			});
		}
	},400);
	if(msg.legalCode!==""){
		$("#nature").hide();
		$("#legal").show();
	}else if(msg.natureCode!==""){
		$("#legal").hide();
		$("#nature").show();
	}
	// 将省市县置为可以回显的状态
	$(".dist").attr("disabled",false);
	$(".city").attr("disabled",false);
	$(".prov").attr("disabled",false);
	$("#organizaform #province").combo("val",msg.province);
	$("#organizaform #city").combo("val",msg.city);
	$("#organizaform #county").combo("val",msg.county);
	// 取到隐藏值判断只读或者可写
	var nodeType = $("#groupSelectForm #nodeType").val();
	if(nodeType === "0"){
		$("#groupSelectForm #departmentName").hide();
		com.orbps.common.oraganizaReadOnlyFalse();
	}else if(nodeType === "1"){
		$("#groupSelectForm #departmentName").show();
		$("#groupSelectForm #groupDep").val(msg.groupDep);
		com.orbps.common.oraganizaReadOnlyTrue();
	}
}
// 提交修改时候校验
com.orbps.common.organizaSubmitValidate = function () {
	var pay = $("#organizaform #pay").val();
	if(pay==="Y"){
		var bankaccNo = $("#organizaform #bankaccNo").val();
		if(bankaccNo===""){
			lion.util.info("提示","请输入银行账号");
			return false;
		}
		var bankaccName = $("#organizaform #bankaccName").val();
		if(bankaccName===""){
			lion.util.info("提示","请输入开户名称");
			return false;
		}
		var bankCode = $("#organizaform #bankCode").val();
		if(bankCode===""){
			lion.util.info("提示","请选择开户银行");
			return false;
		}
		var nodePayAmnt = $("#organizaform #nodePayAmnt").val();
		if(nodePayAmnt===""){
			lion.util.info("提示","请输入节点交费金额");
			return false;
		}
	}
	if(com.orbps.common.nodeType === "0"){
		var companyName=$("#organizaform #companyName").val();
        if (companyName === null || "" === companyName
                || "undefined" === companyName) {
            lion.util.info("警告", "单位/团体名称不能为空");
            return false;
        }
	}
		var email=$("#organizaform #email").val();
        if (email !== "") {
        	var isEmail = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/;
        	if(isEmail.test(email)){

        	}else{
        		lion.util.info("提示","请输入正确邮件地址");
        		return false;
        	}
        }
        var idCardNo=$("#organizaform #idCardNo").val();
        if (idCardNo === "") {
    		lion.util.info("提示","请输入正确的证件号码");
    		return false;
        }
        var totalMembers=$("#organizaform #totalMembers").val();
        if (totalMembers === "") {
    		lion.util.info("提示","请输入成员总数");
    		return false;
        }
	if(com.orbps.common.nodeType === "0"){
		var ojEmpNum=$("#organizaform #ojEmpNum").val();
        if (ojEmpNum !== "") {
        	var isIntGteZero = /^\+?[1-9]\d*$/;
        	if(isIntGteZero.test(ojEmpNum)){

        	}else{
        		lion.util.info("提示","在职人数请输入大于0的整数");
        		return false;
        	}
        }
	}
	if(com.orbps.common.nodeType === "0"){
		var totalMembers=$("#organizaform #totalMembers").val();
        if (totalMembers !== "") {
        	var isZipCode = /^\+?[1-9]\d*$/; 
        	if(isZipCode.test(totalMembers)){

        	}else{
        		lion.util.info("提示","成员总数请输入大于0的整数");
        		return false;
        	}
        }
	}
	return true;
}

// 选择是否交费
$("#organizaform #pay").change(
		function(){
			var pay = $("#organizaform #pay").val();
			if(pay==="Y"){
				$("#bankIs").show();
				$("#bankNoIs").show();
			}else if(pay==="N"){
				$("#bankIs").hide();
				$("#bankNoIs").hide();
				setTimeout(function(){
					$("#organizaform #bankCode").combo("refresh");
					$("#organizaform #bankCode").combo("clear");
				},400);
				$("#bankaccName").val("");
				$("#bankaccNo").val("");
				$("#nodePayAmnt").val("");
			}
		});


$("#organizaform #bankaccNo").blur(
		function(){
			com.orbps.common.organizaBankAccNoValidate();
		});

// 校验银行账号
com.orbps.common.organizaBankAccNoValidate = function () {
	var bankaccNo = $("#organizaform #bankaccNo").val();
	if(bankaccNo===""){
		lion.util.info("提示","请输入银行账号");
	}
}

$("#organizaform #companyName").blur(
		function(){
			com.orbps.common.organizaCompanyNameValidate();
		});

// 校验单位/团体名称
com.orbps.common.organizaCompanyNameValidate = function () {
	if(com.orbps.common.nodeType === "0"){
		var companyName=$("#organizaform #companyName").val();
        if (companyName === null || "" === companyName
                || "undefined" === companyName) {
            lion.util.info("警告", "单位/团体名称不能为空");
            return false;
        }
	}
}

$("#organizaform #email").blur(
		function(){
			com.orbps.common.organizaEmailValidate();
		});

//校验邮件地址
com.orbps.common.organizaEmailValidate = function () {
	var email=$("#organizaform #email").val();
    if (email !== "") {
    	var isEmail = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/;
    	if(isEmail.test(email)){

    	}else{
    		lion.util.info("提示","请输入正确邮件地址");
    		return false;
    	}
    }
}

$("#organizaform #fixedPhones").blur(
		function(){
			com.orbps.common.organizaFixedPhonesValidate();
		});

//校验电话号码
com.orbps.common.organizaFixedPhonesValidate = function () {
	var fixedPhones=$("#organizaform #fixedPhones").val();
    if (fixedPhones !== "") {
    	var isPhone = /^\d{3,4}-?\d{7,9}$/;
    	if(isPhone.test(fixedPhones)){

    	}else{
    		lion.util.info("提示","请输入正确电话号码");
    		return false;
    	}
    }
}

$("#organizaform #ojEmpNum").blur(
		function(){
			com.orbps.common.organizaOjEmpNumValidate();
		});

//校验在职人数
com.orbps.common.organizaOjEmpNumValidate = function () {
	if(com.orbps.common.nodeType === "0"){
		var ojEmpNum=$("#organizaform #ojEmpNum").val();
        if (ojEmpNum !== "") {
        	var isIntGteZero = /^\+?[1-9]\d*$/;
        	if(isIntGteZero.test(ojEmpNum)){

        	}else{
        		lion.util.info("提示","在职人数请输入大于0的整数");
        		return false;
        	}
        }
	}
}

$("#organizaform #totalMembers").blur(
		function(){
			com.orbps.common.organizaTotalMembersValidate();
		});

//校验成员总数
com.orbps.common.organizaTotalMembersValidate = function () {
	if(com.orbps.common.nodeType === "0"){
		var totalMembers=$("#organizaform #totalMembers").val();
        if (totalMembers !== "") {
        	var isZipCode = /^\+?[1-9]\d*$/; 
        	if(isZipCode.test(totalMembers)){

        	}else{
        		lion.util.info("提示","成员总数请输入大于0的整数");
        		return false;
        	}
        }
	}
}

// 选择框判断
com.orbps.common.organizaSelecetValidate = function (){
	
	var industryClassification=$("#organizaform #industryClassification").val();
    if (industryClassification === null || "" === industryClassification
            || "undefined" === industryClassification) {
        lion.util.info("警告", "请选择行业类别");
        return false;
    }
    
    var deptType=$("#organizaform #deptType").val();
    if (deptType === null || "" === deptType
            || "undefined" === deptType) {
        lion.util.info("警告", "请选择证件类型");
        return false;
    }

	var pay=$("#organizaform #pay").val();
    if (pay === null || "" === pay
            || "undefined" === pay) {
        lion.util.info("警告", "请选择是否交费");
        return false;
    }

    var securityOptions=$("#organizaform #securityOptions").val();
	if (securityOptions === null || "" === securityOptions
	        || "undefined" === securityOptions) {
	    lion.util.info("警告", "请选择保全选项");
	    return false;
	}
	
	var serviceAssignment=$("#organizaform #serviceAssignment").val();
	if (serviceAssignment === null || "" === serviceAssignment
	        || "undefined" === serviceAssignment) {
	    lion.util.info("警告", "请选择服务指派");
	    return false;
	}
	
	var invoiceOption=$("#organizaform #invoiceOption").val();
	if (invoiceOption === null || "" === invoiceOption
	        || "undefined" === invoiceOption) {
	    lion.util.info("警告", "请选择发票选项");
	    return false;
	}
	return true;
}

// 只读变可写
com.orbps.common.oraganizaReadOnlyFalse = function (){
	$("#organizaform #custNo").attr("readOnly",false);
	$("#organizaform #oldName").attr("readOnly",false);
	$("#organizaform #town").attr("readOnly",false);
	$("#organizaform #village").attr("readOnly",false);
	$("#organizaform #detailAddress").attr("readOnly",false);
	$("#organizaform #vatNum").attr("readOnly",false);
	$("#organizaform #unitCharacter").attr("readOnly",false);
	$("#organizaform #province").attr("readOnly",false);
	$("#organizaform #registeredNationality").attr("readOnly",false);
	
}

//可写变只读
com.orbps.common.oraganizaReadOnlyTrue = function (){
	$("#organizaform #custNo").attr("readOnly",true);
	$("#organizaform #oldName").attr("readOnly",true);
	$("#organizaform #town").attr("readOnly",true);
	$("#organizaform #village").attr("readOnly",true);
	$("#organizaform #detailAddress").attr("readOnly",true);
	$("#organizaform #vatNum").attr("readOnly",true);
	$("#organizaform #unitCharacter").attr("readOnly",true);
	$("#organizaform #province").attr("readOnly",true);
	$("#organizaform #registeredNationality").attr("readOnly",true);
}