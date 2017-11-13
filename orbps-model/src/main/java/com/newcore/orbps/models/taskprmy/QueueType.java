package com.newcore.orbps.models.taskprmy;


public enum QueueType {
	
	MANUAL_APPROVE_TASK_QUEUE,  //审批任务队列
	LIST_IMPORT_TASK_QUEUE,    //清单导入任务队列
	EV_CUST_SET_TASK_QUEUE,    //开户任务队列
	APPL_UDW_TASK_QUEUE,       //核保队列
	PROC_PLNMIO_TASK_QUEUE,    //生成应收任务队列
	MONEY_INCHECK_TASK_QUEUE,  //收费检查任务队列
	CNTR_INFORCE_TASK_QUEUE,  //保单生效任务队列
	CNTR_PRINT_TASK_QUEUE,    //打印任务队列
	RECEIPT_VERIFICA_TASK_QUEUE, //回执核销任务队列
	SMS_TASK_QUEUE,//短信息任务队列
	PROC_EARNEST_PAY_TASK, //暂缴费支取全部支取任务队列
	TASK_CNTR_DATA_LANDING_QUEUE;//保单落地任务表队列
	
}
