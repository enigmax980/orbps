#!/usr/bin/env bash
. /Library/Java_runtime/zookeeper-3.4.9/bin/zkCli.sh -server 10.31.22.191:12181,10.31.22.191:12182,10.31.22.191:12183 <<!!!

create /orbps ''
create /orbps/db ''
create /orbps/db/version ''
create /orbps/db/version/global 1.0.0-SNAPSHOT
create /orbps/serviceapp ''
create /orbps/serviceapp/config ''
create /orbps/serviceapp/config/global ''
create /orbps/serviceapp/version ''
create /orbps/serviceapp/version/global 1.0.0-SNAPSHOT
create /orbps/serviceapp/heartbeat ''
create /orbps/webapp ''
create /orbps/webapp/config ''
create /orbps/webapp/config/global ''
create /orbps/webapp/version ''
create /orbps/webapp/version/global 1.0.0-SNAPSHOT
create /orbps/webapp/heartbeat ''
create /orbps/batchapp ''
create /orbps/batchapp/config ''
create /orbps/batchapp/config/global ''
create /orbps/batchapp/version ''
create /orbps/batchapp/version/global 1.0.0-SNAPSHOT
create /orbps/batchapp/heartbeat ''

#########batchapp cache.properties start#########
# 是否启用动态缓存服务
create /orbps/batchapp/config/global/cache.dynamic false
#该值使用场景：
# 1. 作为存入缓存中的Key值前缀
create /orbps/batchapp/config/global/cache.name orbps
# 所使用的缓存容器类型: REDIS
create /orbps/batchapp/config/global/cache.type REDIS
# 当容器类型为REDIS时,存在以下配置信息
# Redis运行模式: SINGLETON(单例), CLUSTER(集群), SHAREED(分片)
create /orbps/batchapp/config/global/cache.redis.default.mode SINGLETON
# Redis服务地址集合
# 写入格式: host1:port1;host2:port2
create /orbps/batchapp/config/global/cache.redis.default.addresses 10.31.57.70:6379
# Redis超时时长(毫秒)
create /orbps/batchapp/config/global/cache.redis.default.timeout 10000
# 当运行模式为CLUSTER时, 允许重定向的最大次数
create /orbps/batchapp/config/global/cache.redis.default.maxRedirections 6
# 自带的连接池的一些通用配置
create /orbps/batchapp/config/global/cache.redis.default.maxIdle 200
create /orbps/batchapp/config/global/cache.redis.default.maxActive 1024
create /orbps/batchapp/config/global/cache.redis.default.maxWait 1000
create /orbps/batchapp/config/global/cache.redis.default.testOnBorrow true
#########batchapp cache.properties end#########

#########batchapp dasc.properties start#########
# 作为dasc调用方，向服务方展示的名称
create /orbps/batchapp/config/global/dasc.name orbps-batch-app
# 声明DASC服务在开发测试模式下运行
create /orbps/batchapp/config/global/dasc.test.id dev8888
# 要求在发送dasc消息时，要收到ack回执
create /orbps/batchapp/config/global/dasc.send.ack true
# 在dasc.send.ack=true的情况下，等待ack的超时设定（毫秒）
create /orbps/batchapp/config/global/dasc.send.ack.timeout 10000
# 是否在缓存中暂存调用数据
create /orbps/batchapp/config/global/dasc.cache.enabled true
# 一次调用涉及到的数据在缓存中存放的时间（毫秒）
create /orbps/batchapp/config/global/dasc.cache.timeout 600000
# 一次调用涉及到的数据允许放入缓存的大小 (KB)
create /orbps/batchapp/config/global/dasc.cache.limit 1024
# 调用方提供参数列表服务的服务地址
create /orbps/batchapp/config/global/dasc.caller.args.address http://10.31.31.73:9002/orbps/serviceapp/services/rest/dasc/args
# 调用方提供参数列表服务的服务类型
create /orbps/batchapp/config/global/dasc.caller.args.protocols REST
# 调用方的服务参数列表能否放入MQ中的大小（KB）
create /orbps/batchapp/config/global/dasc.caller.args.limit 100
# 返回值服务的服务地址
create /orbps/batchapp/config/global/dasc.provider.return.address http://10.31.31.73:9002/orbps/serviceapp/services/rest/dasc/returnValue
# 返回值服务的服务类型
create /orbps/batchapp/config/global/dasc.provider.return.protocols REST
# 服务的返回值能否放入MQ中的大小（KB）
create /orbps/batchapp/config/global/dasc.provider.return.limit 100
#########batchapp dasc.properties end#########

#########batchapp filestore.properties start#########
# 是否启用资源服务
create /orbps/batchapp/config/global/fs.enabled true
#该值使用场景：
# 1.当访问协议为MONGO时，对应的是数据库名
# 2.当访问协议为DISK时，对应的是文件夹名称
# 3.当访问协议为FTP时，对应的是FTP最后的路径
create /orbps/batchapp/config/global/fs.name orbps
# 资源访问协议：DISK, MONGO，FTP
create /orbps/batchapp/config/global/fs.protocol DISK
# 本地临时文件夹路径
create /orbps/batchapp/config/global/fs.tmpdir /datafs/share/file_tmp
# 当进行资源更新操作时，进行备份操作
create /orbps/batchapp/config/global/fs.backupOnUpdate true
# 当访问协议为FTP时,存在以下配置信息
# 地址
create /orbps/batchapp/config/global/fs.ftp.address 10.31.31.73:21
# 用户名和密码
create /orbps/batchapp/config/global/fs.ftp.username test
create /orbps/batchapp/config/global/fs.ftp.password xyd2016
# 用户名和密码
create /orbps/batchapp/config/global/fs.ftp.filedir /datafs/ftp_file/
# 当访问协议为FILE时，访问仓库的基础路径
create /orbps/batchapp/config/global/fs.file.basedir /datafs/share/file_base
#错误清单目录
create /orbps/batchapp/config/global/fs.ipsn.err.path /datafs/share/file_ipsnerr
#########batchapp filestore.properties end#########

#########batchapp global.properties start#########
#webServiceClient.xml url地址
create /orbps/batchapp/config/global/webService.url /datafs/fs/domains/orbps-service-app/resources/xml/webServiceClient.xml
#創建投保单字体路径  data\fsshare\einsurappl\font
create /orbps/batchapp/config/global/CreateGrpIsurApplService.font.url /datafs/fs/domains/orbps-service-app/resources/font/SIMSUN.TTC
#端口号
create /orbps/batchapp/config/global/CreateGrpIsurApplService.img.url /datafs/fs/domains/orbps-service-app/resources/img/shuiyin.png
#开户批作业异步、同步开关
create /orbps/batchapp/config/global/batch.ipsn.opencust.switch asy
#清单导入批作业异步、同步开关
create /orbps/batchapp/config/global/batch.ipsn.import.switch asy
#########batchapp global.properties end#########


#########batchapp jdbc.properties start#########
#############################
#
#对于数据库访问动态路由的支持
#
############################
#是否打开动态路由开关
create /orbps/batchapp/config/global/jdbc.dynamic false
#动态路由的数据源列表Bean标识, 多个以逗号分割
create /orbps/batchapp/config/global/jdbc.dynamic.list sss
#对jdbctemplate中各类操作的方法拦截, 每个配置项可以填写唯一个路由数据源.多填无效,不填默认走default数据源
create /orbps/batchapp/config/global/jdbc.dynamic.interceptor.query ''
create /orbps/batchapp/config/global/jdbc.dynamic.interceptor.update ''
create /orbps/batchapp/config/global/jdbc.dynamic.interceptor.call ''
create /orbps/batchapp/config/global/jdbc.dynamic.interceptor.batch ''
#############################
#
#所有jdbc驱动连接的通用配置
#
############################
# 需要在logback.xml中打开DEBUG级别才能生效
create /orbps/batchapp/config/global/jdbc.debug.logging true
# 数据库连接的最大等待时间，默认设置1分钟
create /orbps/batchapp/config/global/jdbc.maxWait '60000'
# 连接池初始化时的连接数
create /orbps/batchapp/config/global/jdbc.initConnectionCount 3
# 连接池保持的最小连接数
create /orbps/batchapp/config/global/jdbc.minConnectionCount 3
#最大连接数
create /orbps/batchapp/config/global/jdbc.maxConnectionCount 50
#密码是否加密
create /orbps/batchapp/config/global/jdbc.password.decrypt false
# 分库分表较多的数据库，建议配置为false
create /orbps/batchapp/config/global/jdbc.poolPreparedStatements true
create /orbps/batchapp/config/global/jdbc.maxPoolPreparedStatementPerConnectionSize 20
# 数据库事务超时时长（秒）
create /orbps/batchapp/config/global/jdbc.transaction.timeout 60
#默认数据源的配置
#  当defaultDataSource的Bean实例为Provider时, 该值为指定真实数据源的Bean名称
create /orbps/batchapp/config/global/jdbc.ds.default.name defaultDataSource
create /orbps/batchapp/config/global/jdbc.ds.default.url jdbc:oracle:thin:@10.31.31.74:1521:orcl
create /orbps/batchapp/config/global/jdbc.ds.default.user orbps
create /orbps/batchapp/config/global/jdbc.ds.default.password orbps123
create /orbps/batchapp/config/global/jdbc.ds.default.password.publickey ''
#########batchapp jdbc.properties end#########

#########batchapp mongodb.properties start#########
create /orbps/batchapp/config/global/mongo.host database-dev.orbps.cbps.clic
create /orbps/batchapp/config/global/mongo.port 27017
create /orbps/batchapp/config/global/mongo.database test
create /orbps/batchapp/config/global/mongo.uname sa
create /orbps/batchapp/config/global/mongo.upass sufeinet.com
create /orbps/batchapp/config/global/mongo.connectionsPerHost 8
create /orbps/batchapp/config/global/mongo.threadsAllowedToBlockForConnectionMultiplier 4
create /orbps/batchapp/config/global/mongo.connectTimeout 1500
create /orbps/batchapp/config/global/mongo.maxWaitTime 1500
create /orbps/batchapp/config/global/mongo.autoConnectRetry true
create /orbps/batchapp/config/global/mongo.socketKeepAlive true
create /orbps/batchapp/config/global/mongo.socketTimeout 1500
create /orbps/batchapp/config/global/mongo.slaveOk true
#########batchapp mongodb.properties end#########

#########batchapp mqconfig.properties start#########
#多个mq时候需要配置queue.list,多个配置以“,”分割，如queue.list=message,XXX
#queue.list=message
# 消息队列broker的地址.用;分割
create /orbps/batchapp/config/global/queue.addresses 10.31.31.61:5672
# 消息队列上的虚拟主机标识
create /orbps/batchapp/config/global/queue.vhost /
# 连接时的用户名
create /orbps/batchapp/config/global/queue.username tsms
# 连接时的密码
create /orbps/batchapp/config/global/queue.password tsms
# 是否自动恢复连接
create /orbps/batchapp/config/global/queue.recovery.enabled true
# 连接自动恢复的执行间隔(毫秒)
create /orbps/batchapp/config/global/queue.recovery.interval 10000
# 对broker的心跳检测间隔(秒)
create /orbps/batchapp/config/global/queue.heartbeat.interval 60
# 消息队列broker的地址.用,分割
#message.queue.addresses=localhost:5672
# 消息队列上的虚拟主机标识
#message.queue.vhost=/myname
# 连接时的用户名
#message.queue.username=suncp
# 连接时的密码
#message.queue.password=suncp
# 是否自动恢复连接 不写默认取queue.recovery.enabled
#message.queue.recovery.enabled=true
# 连接自动恢复的执行间隔(毫秒) 不写默认取queue.recovery.interval
#message.queue.recovery.interval=10000
# 对broker的心跳检测间隔(秒) 不写默认取queue.heartbeat.interval
#message.queue.heartbeat.interval=60
#########batchapp mqconfig.properties end#########

#########batchapp webconfig.properties start#########
create /orbps/batchapp/config/global/web.message.business.0001 "投保单号为空"
create /orbps/batchapp/config/global/web.message.business.0002 "被保人编号为空"
create /orbps/batchapp/config/global/web.message.business.0003 "被保人信息为空"
create /orbps/batchapp/config/global/web.message.business.0004 "保单基本信息为空"
create /orbps/batchapp/config/global/web.message.business.0005 "订正传入参数为空"
create /orbps/batchapp/config/global/web.message.business.0006 "调用服务失败，服务名[%s]"
create /orbps/batchapp/config/global/web.message.business.0007 "InsurApplRegist为空"
create /orbps/batchapp/config/global/web.message.business.0008 录入受理信息为空
create /orbps/batchapp/config/global/web.message.business.0009 单证号码为空
create /orbps/batchapp/config/global/web.message.business.0010 单证号码已存在
create /orbps/batchapp/config/global/web.message.business.0011 对应受理信息为空，请进行投保受理操作
create /orbps/batchapp/config/global/web.message.business.0012 对应团单信息不处于受理状态，无法修改
create /orbps/batchapp/config/global/web.message.business.0013 请输入查询条件
create /orbps/batchapp/config/global/web.message.business.0014 当前页数为空
create /orbps/batchapp/config/global/web.message.business.0015 查询操作轨迹空
create /orbps/batchapp/config/global/web.message.business.0016 保单基本信息不存在,投保单号[%s]
create /orbps/batchapp/config/global/web.message.business.0017 业务逻辑处理错误,详细信息[%s]
create /orbps/batchapp/config/global/web.message.business.0018 [%s]为空
# 执行异步批作业Job的线程池大小
create /orbps/batchapp/config/global/batch.asynjob.threadLimit 1000
#########batchapp webconfig.properties end#########

#########batchapp wsconfig.properties start#########
# 该值使用场景：
# 1.Header.ORISYS
# 2.Header.FROMSYS
create /orbps/batchapp/config/global/ws.common.name ORBPS
# 日志配置
create /orbps/batchapp/config/global/ws.common.logging true
# REST协议地址前缀
create /orbps/batchapp/config/global/ws.protocol.rest.path rest
# SOAP协议地址前缀
create /orbps/batchapp/config/global/ws.protocol.soap.path soap
# 调用SOAP服务时，是否启用Header
create /orbps/batchapp/config/global/ws.protocol.soap.header true
# 调用restful服务时，是否启用Header
create /orbps/batchapp/config/global/ws.protocol.rest.header true
# 客户端超时(毫秒)
create /orbps/batchapp/config/global/ws.client.timeout 60000
#调用orbps系统服务的地址
create /orbps/batchapp/config/global/ws.client.orbps.address localhost:9002/orbps/serviceapp/services
create /orbps/batchapp/config/global/ws.client.orbps.esb.address 10.31.58.110:16604/ESB/ORBPS
#调用pcms系统服务的地址
create /orbps/batchapp/config/global/ws.client.pcms.esb.address dev.ehub.fs.clic:16604/ESB/PCMS
#调用产品ipms系统 服务地址
create /orbps/batchapp/config/global/ws.client.ipms.esb.address dev.ehub.fs.clic:16604/ESB/IPMS
#调用组合服务
create /orbps/batchapp/config/global/ws.client.proxy.esb.address dev.ehub.fs.clic:16604/ESB/PROXY
#远程调用字典系统服务的地址
create /orbps/batchapp/config/global/ws.client.dicts.address 10.31.31.52:9002/ipbps/serviceapp/services
create /orbps/batchapp/config/global/ws.client.cmds.esb.address dev.ehub.fs.clic:16604/ESB/CMDS
# 服务超时(毫秒)
create /orbps/batchapp/config/global/ws.service.timeout 60000
# SOAP服务的命名空间
create /orbps/batchapp/config/global/ws.service.soap.namespace http://www.e-chinalife.com/soa/
# 调用打印
create /orbps/batchapp/config/global/ws.client.ems.esb.address 10.253.128.53:7001/uop_api/RS/rest/eprint
#########batchapp wsconfig.properties end#########

#########serviceapp cache.properties start#########
# 是否启用动态缓存服务
create /orbps/serviceapp/config/global/cache.dynamic false
#该值使用场景：
# 1. 作为存入缓存中的Key值前缀
create /orbps/serviceapp/config/global/cache.name orbps
# 所使用的缓存容器类型: REDIS
create /orbps/serviceapp/config/global/cache.type REDIS
# 当容器类型为REDIS时,存在以下配置信息
# Redis运行模式: SINGLETON(单例), CLUSTER(集群), SHAREED(分片)
create /orbps/serviceapp/config/global/cache.redis.default.mode SINGLETON
# Redis服务地址集合
# 写入格式: host1:port1;host2:port2
create /orbps/serviceapp/config/global/cache.redis.default.addresses localhost:6379
# Redis超时时长(毫秒)
create /orbps/serviceapp/config/global/cache.redis.default.timeout 10000
# 当运行模式为CLUSTER时, 允许重定向的最大次数
create /orbps/serviceapp/config/global/cache.redis.default.maxRedirections 6
# 自带的连接池的一些通用配置
create /orbps/serviceapp/config/global/cache.redis.default.maxIdle 200
create /orbps/serviceapp/config/global/cache.redis.default.maxActive 1024
create /orbps/serviceapp/config/global/cache.redis.default.maxWait 1000
create /orbps/serviceapp/config/global/cache.redis.default.testOnBorrow true
#########serviceapp cache.properties end#########

#########serviceapp dasc.properties start#########
# 作为dasc调用方，向服务方展示的名称
create /orbps/serviceapp/config/global/dasc.name orbps-service-app
# 声明DASC服务在开发测试模式下运行
create /orbps/serviceapp/config/global/dasc.test.id dev8888
# 要求在发送dasc消息时，要收到ack回执
create /orbps/serviceapp/config/global/dasc.send.ack true
# 在dasc.send.ack=true的情况下，等待ack的超时设定（毫秒）
create /orbps/serviceapp/config/global/dasc.send.ack.timeout 10000
# 是否在缓存中暂存调用数据
create /orbps/serviceapp/config/global/dasc.cache.enabled true
# 一次调用涉及到的数据在缓存中存放的时间（毫秒）
create /orbps/serviceapp/config/global/dasc.cache.timeout 600000
# 一次调用涉及到的数据允许放入缓存的大小 (KB)
create /orbps/serviceapp/config/global/dasc.cache.limit 1024
# 调用方提供参数列表服务的服务地址
create /orbps/serviceapp/config/global/dasc.caller.args.address http://10.31.31.73:9002/orbps/serviceapp/services/rest/dasc/args
# 调用方提供参数列表服务的服务类型
create /orbps/serviceapp/config/global/dasc.caller.args.protocols REST
# 调用方的服务参数列表能否放入MQ中的大小（KB）
create /orbps/serviceapp/config/global/dasc.caller.args.limit 100
# 返回值服务的服务地址
create /orbps/serviceapp/config/global/dasc.provider.return.address http://10.31.31.73:9002/orbps/serviceapp/services/rest/dasc/returnValue
# 返回值服务的服务类型
create /orbps/serviceapp/config/global/dasc.provider.return.protocols REST
# 服务的返回值能否放入MQ中的大小（KB）
create /orbps/serviceapp/config/global/dasc.provider.return.limit 100
#########serviceapp dasc.properties end#########

#########serviceapp filestore.properties start#########
# 是否启用资源服务
create /orbps/serviceapp/config/global/fs.enabled true
#该值使用场景：
# 1.当访问协议为MONGO时，对应的是数据库名
# 2.当访问协议为DISK时，对应的是文件夹名称
# 3.当访问协议为FTP时，对应的是FTP最后的路径
create /orbps/serviceapp/config/global/fs.name orbps
# 资源访问协议：DISK, MONGO，FTP
create /orbps/serviceapp/config/global/fs.protocol DISK
# 本地临时文件夹路径
create /orbps/serviceapp/config/global/fs.tmpdir /datafs/share/file_tmp
# 当进行资源更新操作时，进行备份操作
create /orbps/serviceapp/config/global/fs.backupOnUpdate true
# 当访问协议为FTP时,存在以下配置信息
# 地址
create /orbps/serviceapp/config/global/fs.ftp.address 10.31.31.73:21
# 用户名和密码
create /orbps/serviceapp/config/global/fs.ftp.username test
create /orbps/serviceapp/config/global/fs.ftp.password Test2016!
# 用户名和密码
create /orbps/serviceapp/config/global/fs.ftp.filedir /datafs/ftp_file/
create /orbps/serviceapp/config/global/fs.ftp.filedir.custnodir /datafs/ftp_file/custno_file/
# 当访问协议为FILE时，访问仓库的基础路径
create /orbps/serviceapp/config/global/fs.file.basedir /datafs/share/file_base
#错误清单目录
create /orbps/serviceapp/config/global/fs.ipsn.err.path /datafs/share/file_ipsnerr
#清单目录
create /orbps/serviceapp/config/global/fs.ipsnlst.ipsn.dir /datafs/share/file_ipsn
#########serviceapp filestore.properties end#########

#########serviceapp global.properties start#########
create /orbps/serviceapp/config/global/webServiceXml.address /datafs/fs/domains/orbps-service-app/resources/xml/webServiceClient.xml
create /orbps/serviceapp/config/global/CreateGrpIsurApplService.font.url /datafs/fs/domains/orbps-service-app/resources/font/SIMSUN.TTC
create /orbps/serviceapp/config/global/CreateGrpIsurApplService.img.url /datafs/fs/domains/orbps-service-app/resources/img/shuiyin.png
#########serviceapp global.properties end#########

#########serviceapp jdbc.properties start#########
#是否打开动态路由开关
create /orbps/serviceapp/config/global/jdbc.dynamic false
#动态路由的数据源列表Bean标识, 多个以逗号分割
create /orbps/serviceapp/config/global/jdbc.dynamic.list sss
#对jdbctemplate中各类操作的方法拦截, 每个配置项可以填写唯一个路由数据源.多填无效,不填默认走default数据源
create /orbps/serviceapp/config/global/jdbc.dynamic.interceptor.query ''
create /orbps/serviceapp/config/global/jdbc.dynamic.interceptor.update ''
create /orbps/serviceapp/config/global/jdbc.dynamic.interceptor.call ''
create /orbps/serviceapp/config/global/jdbc.dynamic.interceptor.batch ''
#############################
#
#所有jdbc驱动连接的通用配置
#
#############################
# 需要在logback.xml中打开DEBUG级别才能生效
create /orbps/serviceapp/config/global/jdbc.debug.logging true
# 数据库连接的最大等待时间，默认设置1分钟
create /orbps/serviceapp/config/global/jdbc.maxWait '60000'
# 连接池初始化时的连接数
create /orbps/serviceapp/config/global/jdbc.initConnectionCount 3
# 连接池保持的最小连接数
create /orbps/serviceapp/config/global/jdbc.minConnectionCount 3
#最大连接数
create /orbps/serviceapp/config/global/jdbc.maxConnectionCount 50
#密码是否加密
create /orbps/serviceapp/config/global/jdbc.password.decrypt false
# 分库分表较多的数据库，建议配置为false
create /orbps/serviceapp/config/global/jdbc.poolPreparedStatements true
create /orbps/serviceapp/config/global/jdbc.maxPoolPreparedStatementPerConnectionSize 100
# 数据库事务超时时长（秒）
create /orbps/serviceapp/config/global/jdbc.transaction.timeout 60
#默认数据源的配置
#  当defaultDataSource的Bean实例为Provider时, 该值为指定真实数据源的Bean名称
create /orbps/serviceapp/config/global/jdbc.ds.default.name defaultDataSource
create /orbps/serviceapp/config/global/jdbc.ds.default.url jdbc:oracle:thin:@10.31.31.74:1521:orcl
create /orbps/serviceapp/config/global/jdbc.ds.default.user orbps
create /orbps/serviceapp/config/global/jdbc.ds.default.password orbps123
create /orbps/serviceapp/config/global/jdbc.ds.default.password.publickey ''
#########serviceapp jdbc.properties end#########

#########serviceapp mongodb.properties start#########
create /orbps/serviceapp/config/global/mongo.host 10.31.31.74
create /orbps/serviceapp/config/global/mongo.port 27017
create /orbps/serviceapp/config/global/mongo.database test
create /orbps/serviceapp/config/global/mongo.uname sa
create /orbps/serviceapp/config/global/mongo.upass sufeinet.com
create /orbps/serviceapp/config/global/mongo.connectionsPerHost 8
create /orbps/serviceapp/config/global/mongo.threadsAllowedToBlockForConnectionMultiplier 4
create /orbps/serviceapp/config/global/mongo.connectTimeout 1500
create /orbps/serviceapp/config/global/mongo.maxWaitTime 1500
create /orbps/serviceapp/config/global/mongo.autoConnectRetry true
create /orbps/serviceapp/config/global/mongo.socketKeepAlive true
create /orbps/serviceapp/config/global/mongo.socketTimeout 1500
create /orbps/serviceapp/config/global/mongo.slaveOk true
#########serviceapp mongodb.properties end#########

#########serviceapp mqconfig.properties start#########
# 消息队列broker的地址.用;分割
create /orbps/serviceapp/config/global/queue.addresses 10.31.31.61:5672
# 消息队列上的虚拟主机标识
create /orbps/serviceapp/config/global/queue.vhost /
# 连接时的用户名
create /orbps/serviceapp/config/global/queue.username tsms
# 连接时的密码
create /orbps/serviceapp/config/global/queue.password tsms
# 是否自动恢复连接
create /orbps/serviceapp/config/global/queue.recovery.enabled true
# 连接自动恢复的执行间隔(毫秒)
create /orbps/serviceapp/config/global/queue.recovery.interval 10000
# 对broker的心跳检测间隔(秒)
create /orbps/serviceapp/config/global/queue.heartbeat.interval 60
#########serviceapp mqconfig.properties end#########

#########serviceapp webconfig.properties start#########
create /orbps/serviceapp/config/global/web.message.business.0001 "投保单号为空"
create /orbps/serviceapp/config/global/web.message.business.0002 "被保人编号为空"
create /orbps/serviceapp/config/global/web.message.business.0003 "被保人信息为空"
create /orbps/serviceapp/config/global/web.message.business.0004 "保单基本信息为空"
create /orbps/serviceapp/config/global/web.message.business.0005 "订正传入参数为空"
create /orbps/serviceapp/config/global/web.message.business.0006 "调用服务失败，服务名[%s]"
create /orbps/serviceapp/config/global/web.message.business.0007 "InsurApplRegist为空"
create /orbps/serviceapp/config/global/web.message.business.0008 录入受理信息为空
create /orbps/serviceapp/config/global/web.message.business.0009 单证号码为空
create /orbps/serviceapp/config/global/web.message.business.0010 单证号码已存在
create /orbps/serviceapp/config/global/web.message.business.0011 对应受理信息为空，请进行投保受理操作
create /orbps/serviceapp/config/global/web.message.business.0012 对应团单信息不处于受理状态，无法修改
create /orbps/serviceapp/config/global/web.message.business.0013 请输入查询条件
create /orbps/serviceapp/config/global/web.message.business.0014 当前页数为空
create /orbps/serviceapp/config/global/web.message.business.0015 查询操作轨迹空
create /orbps/serviceapp/config/global/web.message.business.0016 保单基本信息不存在,投保单号[%s]
create /orbps/serviceapp/config/global/web.message.business.0017 业务逻辑处理错误,详细信息[%s]
create /orbps/serviceapp/config/global/web.message.business.0018 [%s]为空
create /orbps/serviceapp/config/global/web.message.business.0019 操作员信息为空,投保单号[%s]
create /orbps/serviceapp/config/global/web.message.business.0020 已撤销的契约,投保单号[%s]
create /orbps/serviceapp/config/global/web.message.business.0021 调用服务失败，错误描述[%s]
create /orbps/serviceapp/config/global/web.message.business.0023 读取XML文件异常
#########serviceapp webconfig.properties end#########

#########serviceapp wsconfig.properties start#########
# 该值使用场景：
# 1.Header.ORISYS
# 2.Header.FROMSYS
create /orbps/serviceapp/config/global/ws.common.name ORBPS
# 日志配置
create /orbps/serviceapp/config/global/ws.common.logging true
# REST协议地址前缀
create /orbps/serviceapp/config/global/ws.protocol.rest.path rest
# SOAP协议地址前缀
create /orbps/serviceapp/config/global/ws.protocol.soap.path soap
# 调用SOAP服务时，是否启用Header
create /orbps/serviceapp/config/global/ws.protocol.soap.header true
# 调用restful服务时，是否启用Header
create /orbps/serviceapp/config/global/ws.protocol.rest.header true
# 客户端超时(毫秒)
create /orbps/serviceapp/config/global/ws.client.timeout 60000
#调用orbps系统服务的地址
create /orbps/serviceapp/config/global/ws.client.orbps.address 10.31.31.73:9002/orbps/serviceapp/services/rest
#调用orbps系统服务的地址
create /orbps/serviceapp/config/global/ws.client.orbps.esb.address dev.ehub.fs.clic:16604/ESB/ORBPS
create /orbps/serviceapp/config/global/ws.client.cmds.esb.address dev.ehub.fs.clic:16604/ESB/CMDS
#调用pcms系统服务的地址
create /orbps/serviceapp/config/global/ws.client.pcms.esb.address dev.ehub.fs.clic:16604/ESB/PCMS
#调用柜面ipbps系统服务地址
create /orbps/serviceapp/config/global/ws.client.ipbps.esb.address dev.ehub.fs.clic:16604/ESB/IPBPS
#调用柜面ipms系统服务地址
create /orbps/serviceapp/config/global/ws.client.ipms.esb.address dev.ehub.fs.clic:16604/ESB/IPMS
#远程调用字典系统服务的地址
create /orbps/serviceapp/config/global/ws.client.dicts.address service-dev.ipbps.cbps.clic:9002/ipbps/serviceapp/services
#调用核保接口地址
create /orbps/serviceapp/config/global/ws.client.uwbps.esb.address dev.ehub.fs.clic:16604/ESB/UWBPS
#调用打印系统服务地址
create /orbps/serviceapp/config/global/ws.client.ems.esb.address 10.253.128.53:7001/uop_api/RS/rest/eprint
# 服务超时(毫秒)
create /orbps/serviceapp/config/global/ws.service.timeout 60000
# SOAP服务的命名空间
create /orbps/serviceapp/config/global/ws.service.soap.namespace http://www.e-chinalife.com/soa/
#########serviceapp wsconfig.properties end#########

#########webapp cache.properties start#########
# 是否启用动态缓存服务
create /orbps/webapp/config/global/cache.dynamic true
#该值使用场景：
# 1. 作为存入缓存中的Key值前缀
create /orbps/webapp/config/global/cache.name orbps
# 所使用的缓存容器类型: REDIS
create /orbps/webapp/config/global/cache.type REDIS
# 当容器类型为REDIS时,存在以下配置信息
# Redis运行模式: SINGLETON(单例), CLUSTER(集群), SHAREED(分片)
create /orbps/webapp/config/global/cache.redis.default.mode SINGLETON
# Redis服务地址集合
# 写入格式: host1:port1;host2:port2
create /orbps/webapp/config/global/cache.redis.default.addresses 10.31.57.70:6379
# Redis超时时长(毫秒)
create /orbps/webapp/config/global/cache.redis.default.timeout 10000
# 当运行模式为CLUSTER时, 允许重定向的最大次数
create /orbps/webapp/config/global/cache.redis.default.maxRedirections 6
# 自带的连接池的一些通用配置
create /orbps/webapp/config/global/cache.redis.default.maxIdle 200
create /orbps/webapp/config/global/cache.redis.default.maxActive 1024
create /orbps/webapp/config/global/cache.redis.default.maxWait 1000
create /orbps/webapp/config/global/cache.redis.default.testOnBorrow true
# Redis运行模式: SINGLETON(单例), CLUSTER(集群), SHAREED(分片)
create /orbps/webapp/config/global/cache.redis.session.mode SINGLETON
# Redis服务地址集合
# 写入格式: host1:port1;host2:port2
create /orbps/webapp/config/global/cache.redis.session.addresses 10.31.57.70:6379
# Redis超时时长(毫秒)
create /orbps/webapp/config/global/cache.redis.session.timeout 10000
# 当运行模式为CLUSTER时, 允许重定向的最大次数
create /orbps/webapp/config/global/cache.redis.session.maxRedirections 6
#########webapp cache.properties end#########

#########webapp filestore.properties start#########
# 是否启用资源服务
create /orbps/webapp/config/global/fs.enabled true
#该值使用场景：
# 1.当访问协议为MONGO时，对应的是数据库名
# 2.当访问协议为DISK时，对应的是文件夹名称
# 3.当访问协议为FTP时，对应的是FTP最后的路径
create /orbps/webapp/config/global/fs.name orbps
# 资源访问协议：DISK, MONGO，FTP
create /orbps/webapp/config/global/fs.protocol DISK
# 本地临时文件夹路径
create /orbps/webapp/config/global/fs.tmpdir /datafs/share/file_tmp
# 当进行资源更新操作时，进行备份操作
create /orbps/webapp/config/global/fs.backupOnUpdate true
# 当访问协议为FTP时,存在以下配置信息
# 地址
create /orbps/webapp/config/global/fs.ftp.address 10.31.31.73:21
# 用户名和密码
create /orbps/webapp/config/global/fs.ftp.username test
create /orbps/webapp/config/global/fs.ftp.password xyd2016
# 用户名和密码
create /orbps/webapp/config/global/fs.ftp.filedir /datafs/ftp_file/
# 当访问协议为FILE时，访问仓库的基础路径
create /orbps/webapp/config/global/fs.file.basedir /datafs/share/file_base
#错误清单目录
create /orbps/webapp/config/global/fs.ipsn.err.path /datafs/share/file_ipsnerr
#清单目录
create /orbps/webapp/config/global/fs.ipsnlst.ipsn.dir /datafs/share/file_ipsn
create /orbps/webapp/config/global/fs.ipsnlst.template.dir /datafs/share/file_template
create /orbps/webapp/config/global/fs.file.exceldir /datafs/file_excel
#########webapp filestore.properties end#########

#########webapp global.properties start#########
create /orbps/webapp/config/global/webService.url /datafs/fs/domains/orbps-service-app/resources/xml/webServiceClient.xml
#########webapp global.properties end#########

#########webapp readbusiprdcodeconfig.properties start#########
create /orbps/webapp/config/global/healthPublicAmountBusiprdCode polCode:895,921,608,610,611,623,642,691,726,731,738,780,782,813,820,833,838,857,D51,D63,D73,D87,DW4,924,933,935,943,D12,D43,D47,DY6,FD2,FD6,FD8,FD9,FX2,864,865,867,892,607,628,641,654,695,706,721,736,801,804,845,D48,D64,D68,D89,925,945,D31,D37,D39,SB3,863,866,873,883,894,622,632,644,648,657,672,688,705,715,724,784,797,818,828,858,D67,D92,926,931,D32,D35,F67,F70,S42,S62,874,907,919,616,647,678,696,707,718,720,732,740,785,786,807,808,830,862,D61,D70,D88,929,936,D34,D41,DY8,F11,FK3,FX3,888,889,918,626,634,638,645,646,655,686,689,733,803,814,827,831,840,842,843,853,D53,D54,D74,D79,D94,D96,932,D05,D45,DY4,F08,FD7,FK6,FY7,S26,S32,S72,S79,SB2,869,876,887,893,902,909,609,614,635,636,692,783,798,811,826,832,835,839,848,D85,D95,944,946,D11,D33,DX2,DY2,F07,F69,FD4,FD5,FS3,FX1,FY2,FY3,FY5,S13,S19,S33,879,885,908,920,605,615,625,627,629,630,637,639,649,656,676,690,708,710,739,802,806,810,817,822,841,D69,D78,942,D36,D46,DY7,F09,F10,FD1,FK5,FY6,S12,882,886,890,891,911,915,606,612,613,624,631,633,643,661,677,713,716,719,727,787,791,793,805,809,922,927,D38,D40,D42,DY5,DY9,F68,F71,FK4,S20,S25,S43,S78
create /orbps/webapp/config/global/constructionbBusiprdCode polCode:D04,D24,815,859,685,704,D14
create /orbps/webapp/config/global/healthInsurFlag1 605,629,630,631,632,649,659,707,708,D87,D88,736
create /orbps/webapp/config/global/healthInsurFlag2 YF4,737,794
create /orbps/webapp/config/global/healthInsurFlagOption1 00-非专项标识业务,A2-新农合补充,A4-城镇职工补充医疗,A6-城镇居民补充医疗,A7-医疗救助,A8-企事业团体补充医疗
create /orbps/webapp/config/global/healthInsurFlagOption2 00-非专项标识业务,A1-新农合,A2-新农合补充,A3-城镇职工基本医疗,A4-城镇职工补充医疗,A5-城镇居民基本医疗,A6-城镇居民补充医疗,A7-医疗救助,A8-企事业团体补充医疗,A9-其他委托管理业务
#########webapp readbusiprdcodeconfig.properties end#########

#########webapp session_test.properties start#########
create /orbps/webapp/config/global/web.auth.test.enabled true
create /orbps/webapp/config/global/web.auth.test.user testuser
create /orbps/webapp/config/global/web.auth.test.authcode testuser
create /orbps/webapp/config/global/web.auth.test.accesstoken testuser
#########webapp session_test.properties end#########

#########webapp webconfig.properties start#########
create /orbps/webapp/config/global/web.message.business.0001 "applNo is null"
create /orbps/webapp/config/global/web.message.business.0004 "提交失败，该操作员只能录入本省机构的共保协议"
create /orbps/webapp/config/global/web.message.business.0002 "ipsnNo is null"
create /orbps/webapp/config/global/web.message.business.0003 "grpInsuredInfo is null"
#session超时时间[毫秒]
create /orbps/webapp/config/global/web.session.timeout 1800000
#上传的最大容量(MB)
create /orbps/webapp/config/global/web.maxUploadSize 2000
create /orbps/webapp/config/global/web.message.business.0018 [%s]为空
create /orbps/webapp/config/global/web.message.business.0023 读取XML文件异常
#########webapp webconfig.properties end#########

#########webapp wsconfig.properties start#########
# 该值使用场景：
# 1.Header.ORISYS
# 2.Header.FROMSYS
create /orbps/webapp/config/global/ws.common.name ORBPS
create /orbps/webapp/config/global/ws.common.batch.name ORBPS_BATCH
# 日志配置
create /orbps/webapp/config/global/ws.common.logging true
# REST协议地址前缀
create /orbps/webapp/config/global/ws.protocol.rest.path rest
# SOAP协议地址前缀
create /orbps/webapp/config/global/ws.protocol.soap.path soap
# 调用SOAP服务时，是否启用Header
create /orbps/webapp/config/global/ws.protocol.soap.header true
# 调用restful服务时，是否启用Header
create /orbps/webapp/config/global/ws.protocol.rest.header true
# 客户端超时(毫秒)
create /orbps/webapp/config/global/ws.client.timeout 60000
#调用orbps系统服务的地址
create /orbps/webapp/config/global/ws.client.orbps.address localhost:9002/orbps/serviceapp/services/rest
create /orbps/webapp/config/global/ws.client.orbps.esb.address dev.ehub.fs.clic:16604/ESB/ORBPS
create /orbps/webapp/config/global/ws.client.ipbps.esb.address dev.ehub.fs.clic:16604/ESB/IPBPS
create /orbps/webapp/config/global/ws.client.ipms.esb.address dev.ehub.fs.clic:16604/ESB/IPMS
create /orbps/webapp/config/global/ws.client.cmds.esb.address dev.ehub.fs.clic:16604/ESB/CMDS
create /orbps/webapp/config/global/ws.client.ims.esb.address 10.31.58.7:7001/cims-viewcapture/WS/GetCMSAppUrlRS
#调用pcms系统服务的地址
create /orbps/webapp/config/global/ws.client.pcms.esb.address dev.ehub.fs.clic:16604/ESB/PCMS
#远程调用字典系统服务的地址
create /orbps/webapp/config/global/ws.client.dicts.address service-dev.ipbps.cbps.clic:9002/ipbps/serviceapp/services
#调用批作业管理的地址
create /orbps/webapp/config/global/ws.client.batch.address dev.ehub.fs.clic:16604/ESB/ORBPS
# 服务超时(毫秒)
create /orbps/webapp/config/global/ws.service.timeout 60000
# SOAP服务的命名空间
create /orbps/webapp/config/global/ws.service.soap.namespace http://www.e-chinalife.com/soa/
#########webapp wsconfig.properties end#########

close
!!!
