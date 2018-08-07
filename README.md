# FastDFSDemo
FastDFS最简单的基础框架搭建，包含如何配置文件以及封装了文件的上传/下载/删除，以及生成token的方法

# 配置的FastDFS服务器需要启动的服务
1.启动Tracker服务。命令：启动服务：service fdfs_trackerd start，查看监听：netstat -unltp|grep fdfs（具体FastDFS配置Tracker服务的步骤请参考：http://blog.mayongfa.cn/192.html）
2.启动Storage服务。命令：启动服务：service fdfs_storaged start，查看监听：netstat -unltp|grep fdfs（具体FastDFS配置Storage服务的步骤请参考：http://blog.mayongfa.cn/192.html）
3.启动nginx服务
启动 Nginx ，会打印出fastdfs模块的pid，看看日志是否报错，正常不会报错的。命令：/usr/local/nginx/sbin/nginx。（具体FastDFS配置Nginx模块的步骤请参考：http://blog.mayongfa.cn/193.html）
