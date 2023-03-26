# 基础镜像使用java
FROM java:8
# VOLUME 指定了临时文件目录为/tmp。
#VOLUME 指令创建了一个名为 /tmp 的挂载点，用于将主机上的目录或文件挂载到容器内部。当你使用 docker run 命令启动容器时，可以使用 -v 选项将主机上的目录挂载到 /data 挂载点中
VOLUME /tmp
# 将jar包添加到容器中并更名为app.jar
ADD senbei-forum-main-1.0-SNAPSHOT.jar /tmp/senbei-forum-main-1.0-SNAPSHOT.jar.jar
# 运行jar包
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/tmp/senbei-forum-main-1.0-SNAPSHOT.jar","--spring.profiles.active=prod","--server.port=8800"]
