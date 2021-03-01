## Maven

### 1.  Maven打包,跳过生成javadoc

* 有时候由于代码中注释错误（比如方法参数）或者maven javadoc插件版本有问题，导致打包报错，而我们着急打包验证问题，没有时间一一修改错误，这时候可以先跳过生成javadoc，继续下一步工作。

  命令：`mvn clean package -Dmaven.javadoc.skip=true`