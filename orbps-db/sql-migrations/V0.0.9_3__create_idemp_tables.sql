/*==============================================================*/
/* Table: DASC_IDEMP_KEEPING                                    */
/*==============================================================*/
create table DASC_IDEMP_KEEPING 
(
   TRANSACTION_CODE     varchar2(128),
   IDEMPOTENT_FATOR     varchar2(200)        not null,
   SERVICE_ID           varchar2(200)        not null,
   OPERATE_TIME         TIMESTAMP            default SYSDATE not null
);

comment on column DASC_IDEMP_KEEPING.TRANSACTION_CODE is
'交易码';

comment on column DASC_IDEMP_KEEPING.IDEMPOTENT_FATOR is
'幂等要素信息';

comment on column DASC_IDEMP_KEEPING.SERVICE_ID is
'服务ID';

comment on column DASC_IDEMP_KEEPING.OPERATE_TIME is
'操作时间';

/*==============================================================*/
/* Index: COUNT1_IDX                                            */
/*==============================================================*/
create index COUNT1_IDX on DASC_IDEMP_KEEPING (
   TRANSACTION_CODE ASC,
   IDEMPOTENT_FATOR ASC,
   SERVICE_ID ASC
);

/*==============================================================*/
/* Index: COUNT2_IDX                                            */
/*==============================================================*/
create index COUNT2_IDX on DASC_IDEMP_KEEPING (
   IDEMPOTENT_FATOR ASC,
   SERVICE_ID ASC
);
