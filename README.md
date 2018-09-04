# FastDFSDemo
FastDFS最简单的基础框架搭建，包含如何配置文件以及封装了文件的上传/下载/删除，以及生成token的方法

# 配置的FastDFS服务器需要启动的服务
1.启动Tracker服务。命令：启动服务：service fdfs_trackerd start，查看监听：netstat -unltp|grep fdfs（具体FastDFS配置Tracker服务的步骤请参考：http://blog.mayongfa.cn/192.html）
2.启动Storage服务。命令：启动服务：service fdfs_storaged start，查看监听：netstat -unltp|grep fdfs（具体FastDFS配置Storage服务的步骤请参考：http://blog.mayongfa.cn/192.html）
3.启动nginx服务
启动 Nginx ，会打印出fastdfs模块的pid，看看日志是否报错，正常不会报错的。命令：/usr/local/nginx/sbin/nginx。（具体FastDFS配置Nginx模块的步骤请参考：http://blog.mayongfa.cn/193.html）

#linux centOs 配置FastDFS的时候遇到的坑
按照上面的实例搭建会有问题，主要原因是nginx和fastdfs moudle结合的时候存在版本冲突（解决方法：采用兼容版本https://blog.csdn.net/hhq163/article/details/46536895）

nginx版本使用1.8
wget http://nginx.org/download/nginx-1.8.0.tar.gz
fastdfs-nginx-module版本使用1.16
wget http://jaist.dl.sourceforge.net/project/fastdfs/FastDFS%20Nginx%20Module%20Source%20Code/fastdfs-nginx-module_v1.16.tar.gz

配置fastDFS和Nginx如果出现404解决方案：
cd /usr/local/nginx/conf/nginx.conf
在#user nobody 下添加user root;

注意：上面实例安装nginx的时候缺少make和make install，在执行了./configure --add-module=../fastdfs-nginx-module-master/src/之后 进行执行。
还要注意端口冲突问题
