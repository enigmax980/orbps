// 新建contractEntry命名空间
com.orbps.contractEntry={};
// 新建contractEntry.cardEntry命名空间
com.orbps.contractEntry.cardEntry={};

$(function(){
	//datagrid控件初始化表格 与bootstrap的表格样式不兼容
//	$('#applListTab').datagrid();
//	$("#treegridId").datagrid();
	//日期初始化插件
//	$(".date-picker").datepicker({
//		autoclose : true,
//		language : 'zh-CN'
//	});	
	
//显示tab页面信息	
//	$('#tab1').show(function(){
//		$("#tab1").load("orbps/contractEntry/cardEntry/html/applInfo.html");
//	});
//	
//	$('#applInfo').click(function(){
//		$('#tab2').hide();
//		$('#tab3').hide();
//		$('#tab4').hide();
//		$('#tab5').hide();
//		$("#tab1").load("orbps/contractEntry/cardEntry/html/applInfo.html");
//	});
//	$('#ipsnInfo').click(function(){
//		$('#tab3').hide();
//		$('#tab4').hide();
//		$('#tab5').hide();
//		$('#tab2').show(function(){
//		$("#tab2").load("orbps/contractEntry/cardEntry/html/ipsnInfo.html");
//		});	
//	});
//	$('#bnfrInfo').click(function(){
//		$('#tab2').hide();
//		$('#tab4').hide();
//		$('#tab5').hide();
//		$('#tab3').show(function(){
//		$("#tab3").load("orbps/contractEntry/cardEntry/html/bnfrInfo.html");
//		});	
//	});
//	$('#bsInfo').click(function(){
//		$('#tab2').hide();
//		$('#tab3').hide();
//		$('#tab5').hide();
//		$('#tab4').show(function(){
//		$("#tab4").load("orbps/contractEntry/cardEntry/html/bsInfo.html");
//		});	
//	});
//	$('#accInfo').click(function(){
//		$('#tab2').hide();
//		$('#tab3').hide();
//		$('#tab4').hide();
//		$('#tab5').show(function(){
//		$("#tab5").load("orbps/contractEntry/cardEntry/html/accInfo.html");
//		});	
//	});
	
	
	$("#tab1").load("orbps/contractEntry/cardEntry/html/applInfo.html");
	$('#applInfo').click(function(){
		$("#tab1").load("orbps/contractEntry/cardEntry/html/applInfo.html");
	});
	$('#ipsnInfo').click(function(){
		$("#tab2").load("orbps/contractEntry/cardEntry/html/ipsnInfo.html");
	});
	$('#bnfrInfo').click(function(){
		$("#tab3").load("orbps/contractEntry/cardEntry/html/bnfrInfo.html");
	});
	$('#bsInfo').click(function(){
		$("#tab4").load("orbps/contractEntry/cardEntry/html/bsInfo.html");
	});
	$('#accInfo').click(function(){
		$("#tab5").load("orbps/contractEntry/cardEntry/html/accInfo.html");
	});


//要约信息、特约信息需要查询数据库回显，不可更改
});

