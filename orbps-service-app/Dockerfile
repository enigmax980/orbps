FROM 10.31.57.177/dev1-ci/pinpoint-agent:1.6.1

# port 9002
EXPOSE 9002

# jvm option
ENV JAVA_OPTS="-server -Xms4g -Xmx4g -XX:MaxNewSize=1g -Dfile.encoding=UTF-8 -Djava.security.egd=file:/dev/urandom"

# orbps add
ENV LANG=en_US.UTF-8

RUN mkdir -p /usr/local/apache-tomcat-8.5.14/conf/Catalina/localhost

# war and config
ADD target/orbps-service-app-1.0.0_P8-SNAPSHOT.war /
ADD orbps#serviceapp.xml /usr/local/apache-tomcat-8.5.14/conf/Catalina/localhost

# chown and change port
RUN set -x \
    && chown tomcat:tomcat /orbps-service-app-1.0.0_P8-SNAPSHOT.war \
    && chown -R tomcat:tomcat /usr/local/apache-tomcat-8.5.14/conf/Catalina \
    && sed -i 's/Connector port="8080"/Connector port="9002"/' /usr/local/apache-tomcat-8.5.14/conf/server.xml \
    && sed -i 's/autoDeploy="true"/autoDeploy="false"/' /usr/local/apache-tomcat-8.5.14/conf/server.xml

# change user
USER tomcat

# boot command
CMD ["/usr/local/apache-tomcat-8.5.14/bin/catalina.sh","run"]
