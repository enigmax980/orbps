/*==============================================================*/
/* Table: DASC_CONTENT_PERSIST                                  */
/*==============================================================*/
create table DASC_CONTENT_PERSIST 
(
   PKID                 varchar2(50)         not null,
   MESSAGE_ID           varchar2(50)         not null,
   TRANSACTION_CODE     varchar2(128)        not null,
   PROVIDER_ID          varchar2(200)        not null,
   CALLER_ID            varchar2(200)        not null,
   CALL_SEQ             number(10)           not null,
   CONTENT_TYPE         varchar2(10)         not null,
   CONTENT_DATA         CLOB,
   OPERATE_TIME         TIMESTAMP            default SYSTIMESTAMP not null,
   constraint PK_DASC_CONTENT_PERSIST primary key (PKID)
);

comment on table DASC_CONTENT_PERSIST is
'DASC服务调用内容的持久化,包括参数列表和返回值';

comment on column DASC_CONTENT_PERSIST.PKID is
'流水号';

comment on column DASC_CONTENT_PERSIST.MESSAGE_ID is
'消息ID';

comment on column DASC_CONTENT_PERSIST.TRANSACTION_CODE is
'交易码';

comment on column DASC_CONTENT_PERSIST.PROVIDER_ID is
'服务ID';

comment on column DASC_CONTENT_PERSIST.CALLER_ID is
'调用方ID';

comment on column DASC_CONTENT_PERSIST.CALL_SEQ is
'相同服务的调用顺序号';

comment on column DASC_CONTENT_PERSIST.CONTENT_TYPE is
'内容类型:ARGS;RETURN';

comment on column DASC_CONTENT_PERSIST.CONTENT_DATA is
'JSON格式内容';

comment on column DASC_CONTENT_PERSIST.OPERATE_TIME is
'操作时间';

/*==============================================================*/
/* Index: ARGS_IDX                                              */
/*==============================================================*/
create unique index ARGS_IDX on DASC_CONTENT_PERSIST (
   MESSAGE_ID ASC,
   PROVIDER_ID ASC,
   CALL_SEQ ASC,
   CONTENT_TYPE ASC
);

/*==============================================================*/
/* Table: DASC_FAILD_INFO                                       */
/*==============================================================*/
create table DASC_FAILD_INFO 
(
   PKID                 varchar2(50)         not null,
   MESSAGE_ID           varchar2(50)         not null,
   TRANSACTION_CODE     varchar2(128)        not null,
   FAILD_TYPE           varchar2(50)         not null,
   PROVIDER_ID          varchar2(200)        not null,
   CALLER_ID            varchar2(200)        not null,
   CALL_SEQ             number(10)           not null,
   MESSAGE_DATA         CLOB,
   OPERATE_TIME         TIMESTAMP            default SYSTIMESTAMP not null,
   constraint PK_DASC_FAILD_INFO primary key (PKID)
);

comment on column DASC_FAILD_INFO.PKID is
'流水号';

comment on column DASC_FAILD_INFO.MESSAGE_ID is
'消息ID';

comment on column DASC_FAILD_INFO.TRANSACTION_CODE is
'交易码';

comment on column DASC_FAILD_INFO.FAILD_TYPE is
'失败触发场景';

comment on column DASC_FAILD_INFO.PROVIDER_ID is
'服务方ID';

comment on column DASC_FAILD_INFO.CALLER_ID is
'调用方ID';

comment on column DASC_FAILD_INFO.CALL_SEQ is
'相同服务的调用顺序号';

comment on column DASC_FAILD_INFO.MESSAGE_DATA is
'JSON格式消息内容';

comment on column DASC_FAILD_INFO.OPERATE_TIME is
'操作时间';

/*==============================================================*/
/* Index: FAILD_COUNT_IDX                                       */
/*==============================================================*/
create index FAILD_COUNT_IDX on DASC_FAILD_INFO (
   MESSAGE_ID ASC,
   FAILD_TYPE ASC,
   PROVIDER_ID ASC,
   CALL_SEQ ASC
);

/*==============================================================*/
/* Index: FAILD_IDX                                             */
/*==============================================================*/
create index FAILD_IDX on DASC_FAILD_INFO (
   TRANSACTION_CODE ASC,
   FAILD_TYPE ASC,
   CALL_SEQ ASC,
   OPERATE_TIME ASC
);
