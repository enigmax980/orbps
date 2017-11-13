/*==============================================================*/
/* Table: BATCH_ERR_LOG                                         */
/*==============================================================*/
create table BATCH_ERR_LOG 
(
   PKID                 VARCHAR2(50)         not null,
   TASK_ID              VARCHAR2(32)         not null,
   TASK_EXECUTION_ID    VARCHAR2(50)         not null,
   JOB_INSTANCE_ID      NUMBER(19,0)         not null,
   JOB_EXECUTION_ID     NUMBER(19,0)         not null,
   BUSINESS_TYPE        VARCHAR2(2),
   BUSINESS_KEY         VARCHAR2(255),
   ERROR_CODE           VARCHAR2(4)          not null,
   ERROR_INFO           VARCHAR2(255)        not null,
   ERROR_DETAIL         CLOB,
   CREATE_DATE          Date                 default sysdate not null,
   constraint PK_BATCH_ERR_LOG primary key (PKID)
);

comment on table BATCH_ERR_LOG is
'批作业定义表';

comment on column BATCH_ERR_LOG.PKID is
'PKID';

comment on column BATCH_ERR_LOG.TASK_ID is
'任务ID';

comment on column BATCH_ERR_LOG.TASK_EXECUTION_ID is
'任务执行ID，标记一次TaskId的执行';

comment on column BATCH_ERR_LOG.JOB_INSTANCE_ID is
'SpringBatch的JOB实例ID';

comment on column BATCH_ERR_LOG.JOB_EXECUTION_ID is
'SpringBatch的JOB执行ID';

comment on column BATCH_ERR_LOG.BUSINESS_TYPE is
'业务类型';

comment on column BATCH_ERR_LOG.BUSINESS_KEY is
'业务流水号';

comment on column BATCH_ERR_LOG.ERROR_CODE is
'错误代码';

comment on column BATCH_ERR_LOG.ERROR_INFO is
'异常信息';

comment on column BATCH_ERR_LOG.ERROR_DETAIL is
'错误明细';

comment on column BATCH_ERR_LOG.CREATE_DATE is
'创建时间';

/*==============================================================*/
/* Table: BATCH_TASK_DEF                                        */
/*==============================================================*/
create table BATCH_TASK_DEF 
(
   BAT_TX_NO            VARCHAR2(50)         not null,
   BAT_NAME             VARCHAR2(32)         not null,
   constraint PK_BATCH_TASK_DEF primary key (BAT_TX_NO)
);

comment on table BATCH_TASK_DEF is
'批作业定义表';

comment on column BATCH_TASK_DEF.BAT_TX_NO is
'批作业编号';

comment on column BATCH_TASK_DEF.BAT_NAME is
'批作业名称';

/*==============================================================*/
/* Table: BATCH_TASK_EXECUTION                                  */
/*==============================================================*/
create table BATCH_TASK_EXECUTION
(
   TASK_EXECUTION_ID    VARCHAR2(50)         not null,
   TASK_ID              VARCHAR2(32)         not null,
   START_TIME           DATE,
   END_TIME             DATE,
   constraint PK_BATCH_TASK_EXECUTION primary key (TASK_EXECUTION_ID)
);

comment on table BATCH_TASK_EXECUTION is
'批作业任务执行表';

comment on column BATCH_TASK_EXECUTION.TASK_EXECUTION_ID is
'任务执行ID，标记一次TaskId的执行';

comment on column BATCH_TASK_EXECUTION.TASK_ID is
'任务ID';

comment on column BATCH_TASK_EXECUTION.START_TIME is
'开始时间';

comment on column BATCH_TASK_EXECUTION.END_TIME is
'结束时间';

/*==============================================================*/
/* Index: TAK_ID_IDX                                            */
/*==============================================================*/
create index TAK_ID_IDX on BATCH_TASK_EXECUTION (
   TASK_ID ASC
);

/*==============================================================*/
/* Table: BATCH_TASK_EXECUTION_DETAIL                           */
/*==============================================================*/
create table BATCH_TASK_EXECUTION_DETAIL
(
   PKID                 VARCHAR2(50)         not null,
   TASK_EXECUTION_ID    VARCHAR2(50)         not null,
   JOB_INSTANCE_ID      NUMBER(19,0)         not null,
   JOB_EXECUTION_ID     NUMBER(19,0)         not null,
   JOB_NAME             VARCHAR2(100),
   constraint PK_BATCH_TASK_EXECUTION_DETAIL primary key (PKID)
);

comment on table BATCH_TASK_EXECUTION_DETAIL is
'批作业任务执行明细表';

comment on column BATCH_TASK_EXECUTION_DETAIL.PKID is
'PKID';

comment on column BATCH_TASK_EXECUTION_DETAIL.TASK_EXECUTION_ID is
'任务执行ID，标记一次TaskId的执行';

comment on column BATCH_TASK_EXECUTION_DETAIL.JOB_INSTANCE_ID is
'SpringBatch的JOB实例ID';

comment on column BATCH_TASK_EXECUTION_DETAIL.JOB_EXECUTION_ID is
'SpringBatch的JOB执行ID';

comment on column BATCH_TASK_EXECUTION_DETAIL.JOB_NAME is
'SpringBatch的JOB名称';

/*==============================================================*/
/* Index: TAK_EXE_ID_IDX                                        */
/*==============================================================*/
create index TAK_EXE_ID_IDX on BATCH_TASK_EXECUTION_DETAIL (
   TASK_EXECUTION_ID ASC
);

/*==============================================================*/
/* Table: BATCH_TASK_REG                                        */
/*==============================================================*/
create table BATCH_TASK_REG
(
   TASK_ID              VARCHAR2(32)         not null,
   BAT_TX_NO            VARCHAR2(50)         not null,
   DATA_DEAL_STATUS     VARCHAR2(2)          not null,
   CREATE_TIME          DATE                 default sysdate not null,
   UPDATE_TIMESTAMP     TIMESTAMP            not null,
   constraint PK_BATCH_TASK_REG primary key (TASK_ID)
);

comment on table BATCH_TASK_REG is
'批作业任务注册表';

comment on column BATCH_TASK_REG.TASK_ID is
'任务ID';

comment on column BATCH_TASK_REG.BAT_TX_NO is
'批作业编号';

comment on column BATCH_TASK_REG.DATA_DEAL_STATUS is
'数据处理状态';

comment on column BATCH_TASK_REG.CREATE_TIME is
'任务创建时间';

comment on column BATCH_TASK_REG.UPDATE_TIMESTAMP is
'更新时间戳';
