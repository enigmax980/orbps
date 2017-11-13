package com.newcore.orbps.models.taskprmy;

public enum TaskType {
	MANUAL_APPROVE("MANUAL_APPROVE_TASK_QUEUE", 100), LIST_IMPORT("LIST_IMPORT_TASK_QUEUE", 101), SET_EV_CUST(
			"SET_EV_CUST_TASK_QUEUE", 102), APPL_UDW("APPL_UDW_TASK_QUEUE", 103), PROC_PLNMIO("PROC_PLNMIO_TASK_QUEUE",
					104), MONEY_INCHECK("MONEY_INCHECK_TASK_QUEUE", 105), CNTR_INFORCE("CNTR_INFORCE_TASK_QUEUE",
							106), CNTR_PRINT("CNTR_PRINT_TASK_QUEUE", 107);

	private TaskType(String taskType, int index) {
		this.taskType = taskType;
		this.index = index;
	}

	private String taskType;
	private int index;

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public static String getTaskType(int index) {
		for (TaskType taskType : TaskType.values())
			if (taskType.getIndex() == index) {
				return taskType.getTaskType();
			}

		return null;
	}
}
