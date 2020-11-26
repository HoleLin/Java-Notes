### Docker操作

#### 常用命令

*  列出所有的容器 ID

  ```sh
  docker ps -aq
  ```

* 停止所有容器

  ```shell
  docker stop $(docker ps -aq)
  ```

* 删除所有容器

  ```shell
  docker rm $(docker ps -aq)
  ```

* 删除所有镜像

  ```shell
  docker rmi $(docker images -q)
  ```

* 复制文件

  * 从容器中复制文件

    ```
    docker cp 容器ID: 源文件路径 目标路径
    docker cp container_id: /opt/file.txt /opt/local
    ```

  * 从容器外复制进容器

    ```
    docker cp 源文件路径 容器ID:目标路径
    docker cp /opt/file.txt container_id:/opt/local
    ```

* Docker 1.13 增加了 docker system prune

  * 针对container

    ```shell
    docker container prune
    
    docker container prune -f : 删除所有停止的容器
    ```

  * 针对image

    ```shell
    docker image prune
    docker image prune --force -all / docker image prune -f -a : 删除所有不用镜像
    ```

####  Docker日志文件导致磁盘满了,清理方法

* Docker容器在启动/重启的时候会往`/var/lib/docker`中写东西,若在启动docker容器遇到`No space left on device` 的问题,可使用以下步骤来解决

* 对`/var/lib/docker/containers`下的文件夹进行排序,看看哪个容器杂用太多的磁盘空间

  ```shell
  $ du -d1 -h /var/lib/docker/containers | sort -h
  ```

* 选择需要清理的容器进行清理

  ```shell
  $ cat /dev/null > /var/lib/docker/containers/container_id/container_log_name
  ```

* 限制日志文件的大小

  ```shell
  docker run -it --log-opt max-size=10m --log-opt max-file=33 alpine ash
  ```

  

