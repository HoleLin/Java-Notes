### Spring循环依赖

#### 参考文献

* [spring：我是如何解决循环依赖的？](https://mp.weixin.qq.com/s/mPTMWybMvMDjbfTjvikyAw)

#### 循环依赖

> 循环依赖：说白是一个或多个对象实例之间存在直接或间接的依赖关系，这种依赖关系构成了构成一个环形调用.
>
> 第一种情况：自己依赖自己的直接依赖: A<==>A
>
> 第二种情况：两个对象之间的直接依赖: A<==>B<==>A
>
> 第三种情况：多个对象之间的间接依赖: A<==>B<==>C<==>A

#### 出现循环依赖的场景

* **单例的Setter注入(能解决)**

  ```
  @Service
  publicclass TestService1 {
  
      @Autowired
      private TestService2 testService2;
  
      public void test1() {
      }
  }
  
  @Service
  publicclass TestService2 {
  
      @Autowired
      private TestService1 testService1;
  
      public void test2() {
      }
  }
  ```

  > 这是一个经典的循环依赖，但是它能正常运行，得益于spring的内部机制，让我们根本无法感知它有问题，因为spring默默帮我们解决了。
  > spring内部有三级缓存：
  >
  > * singletonObjects:  一级缓存，用于保存实例化、注入、初始化完成的bean实例
  >
  > * earlySingletonObjects: 二级缓存，用于保存实例化完成的bean实例
  >
  > * singletonFactories: 三级缓存，用于保存bean创建工厂，以便于后面扩展有机会创建代理对象。

  ```
  testService1-->从一级缓存中获取不到实例-->创建实例-->提前暴露,添加到三级缓存
  -->依赖注入-->testService2-->从一级缓存中获取不到实例-->创建实例-->提前暴露,添加到三级缓存
  -->依赖注入-->testService1-->从三级缓存获取到实例,并添加到二级缓存
  -->testService2注入成功-->testService2初始化完成-->testService2添加到一级缓存
  -->testService1依赖注入成功-->testService1初始化完成-->testService1添加到一级缓存-->结束
  ```

  

* **多例的Setter注入(不能解决)**

* **构造器注入(不能解决)**

* **单例的代理对象Setter注入(有可能解决)**

  > 

* **DependsOn循环依赖(不能解决)**

  > 这类循环依赖问题要找到`@DependsOn`注解循环依赖的地方，迫使它不循环依赖就可以解决问题。