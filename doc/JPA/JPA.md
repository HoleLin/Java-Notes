## JPA

### 1. 基础使用

* 市场上的ORM框架对比

  ![](https://s0.lgstatic.com/i/image/M00/4E/B1/Ciqc1F9fBfeANrGuAAOa8Y2E5fU233.png)

  ![Drawing 9.png](https://s0.lgstatic.com/i/image/M00/4E/AF/Ciqc1F9fA1uARUnvAAB2ZNS1UXc485.png)

* Spring Data Common的依赖关系

  * ![Drawing 0.png](https://s0.lgstatic.com/i/image/M00/50/6F/Ciqc1F9i18OABIgzAAGVeUj3uCU674.png)
    * 数据库连接用的是**JDBC**
    * 连接池用的是**HikariCP**
    * 强依赖**Hibernate**
    * Spring Boot Starter Data JPA 依赖Spring Data JPA 而Spring Data JPA依赖Spring Data Commons

* **Repository接口**

  * Repository是Spring Date Common里面的顶级父类接口,操作DB的入口类.

* **Respository类层次关系**

  * ReactiveCrudRepository 这条线是响应式编程，主要支持当前 NoSQL 方面的操作，因为这方面大部分操作都是分布式的，所以由此我们可以看出 Spring Data 想统一数据操作的“野心”，即想提供关于所有 Data 方面的操作。目前 Reactive 主要有 Cassandra、MongoDB、Redis 的实现。

  * RxJava2CrudRepository 这条线是为了支持 RxJava 2 做的标准响应式编程的接口。

  * CoroutineCrudRepository 这条继承关系链是为了支持 Kotlin 语法而实现的。

  * CrudRepository

* **7 个大 Repository 接口：**

  * **Repository(**org.springframework.data.repository)，没有暴露任何方法；

  * **CrudRepository**(org.springframework.data.repository)，简单的 Curd 方法；

  * **PagingAndSortingRepository**(org.springframework.data.repository)，带分页和排序的方法；

  * **QueryByExampleExecutor**(org.springframework.data.repository.query)，简单 Example 查询；

  * **JpaRepository**(org.springframework.data.jpa.repository)，JPA 的扩展方法；

  * **JpaSpecificationExecutor**(org.springframework.data.jpa.repository)，JpaSpecification 扩展查询；

  * **QueryDslPredicateExecutor**(org.springframework.data.querydsl)，QueryDsl 的封装。

* **两大 Repository 实现类：**

  * **SimpleJpaRepository**(org.springframework.data.jpa.repository.support)，JPA 所有接口的默认实现类；
  * **QueryDslJpaRepository**(org.springframework.data.jpa.repository.support)，QueryDsl 的实现类。

* **CrudRepository 接口**

  ```java
  @NoRepositoryBean
  public interface CrudRepository<T, ID> extends Repository<T, ID> {
      // 保存实体方法，参数和返回结果可以是实体的子类；
      <S extends T> S save(S entity);
  	// 批量保存，原理和 save方法相同，我们去看实现的话，就是 for 循环调用上面的 save 方法。
      <S extends T> Iterable<S> saveAll(Iterable<S> entities);
  	// 根据主键查询实体，返回 JDK 1.8 的 Optional，这可以避免 null exception
      Optional<T> findById(ID id);
  	// 根据主键判断实体是否存在
      boolean existsById(ID id);
  	//  查询实体的所有列表
      Iterable<T> findAll();
  	// 根据主键列表查询实体列表
      Iterable<T> findAllById(Iterable<ID> ids);
      // 查询总数返回 long 类型
      long count();
  	// 根据主键删除，查看源码会发现，其是先查询出来再进行删除
      void deleteById(ID id);
  	// 根据 entity 进行删除
      void delete(T entity);
  	// 批量删除
      void deleteAll(Iterable<? extends T> entities);
  	// 删除所有
      void deleteAll();
  }
  ```

* **PagingAndSortingRepository 接口**

  * 该接口也是 Repository 接口的子类，主要用于分页查询和排序查询。
  
  ```
  @NoRepositoryBean
  public interface PagingAndSortingRepository<T, ID> extends CrudRepository<T, ID> {
  
     /**
      * Returns all entities sorted by the given options.
      * 根据排序参数，实现不同的排序规则获取所有的对象的集合；
      * @param sort
      * @return all entities sorted by the given options
      */
     Iterable<T> findAll(Sort sort);
  
     /**
      * Returns a {@link Page} of entities meeting the paging restriction provided in the {@code Pageable} object.
      * 根据分页和排序进行查询，并用 Page 对返回结果进行封装。而 Pageable 对象包含 Page 和 Sort 对象。
      * @param pageable
      * @return a page of entities
      */
     Page<T> findAll(Pageable pageable);
  }
  ```

* **JpaRepository 接口**

  ```java
  @NoRepositoryBean
  public interface JpaRepository<T, ID> extends PagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T> {
  	/*
  	 * (non-Javadoc)
  	 * @see org.springframework.data.repository.CrudRepository#findAll()
  	 */
  	@Override
  	List<T> findAll();
  	/*
  	 * (non-Javadoc)
  	 * @see org.springframework.data.repository.PagingAndSortingRepository#findAll(org.springframework.data.domain.Sort)
  	 */
  	@Override
  	List<T> findAll(Sort sort);
  	/*
  	 * (non-Javadoc)
  	 * @see org.springframework.data.repository.CrudRepository#findAll(java.lang.Iterable)
  	 */
  	@Override
  	List<T> findAllById(Iterable<ID> ids);
  	/*
  	 * (non-Javadoc)
  	 * @see org.springframework.data.repository.CrudRepository#save(java.lang.Iterable)
  	 */
  	@Override
  	<S extends T> List<S> saveAll(Iterable<S> entities);
  	/**
  	 * Flushes all pending changes to the database.
  	 */
  	void flush();
  	/**
  	 * Saves an entity and flushes changes instantly.
  	 *
  	 * @param entity
  	 * @return the saved entity
  	 */
  	<S extends T> S saveAndFlush(S entity);
  	/**
  	 * Deletes the given entities in a batch which means it will create a single {@link Query}. Assume that we will clear
  	 * the {@link javax.persistence.EntityManager} after the call.
  	 *
  	 * @param entities
  	 */
  	void deleteInBatch(Iterable<T> entities);
  	/**
  	 * Deletes all entities in a batch call.
  	 */
  	void deleteAllInBatch();
  	/**
  	 * Returns a reference to the entity with the given identifier. Depending on how the JPA persistence provider is
  	 * implemented this is very likely to always return an instance and throw an
  	 * {@link javax.persistence.EntityNotFoundException} on first access. Some of them will reject invalid identifiers
  	 * immediately.
  	 *
  	 * @param id must not be {@literal null}.
  	 * @return a reference to the entity with the given identifier.
  	 * @see EntityManager#getReference(Class, Object) for details on when an exception is thrown.
  	 */
  	T getOne(ID id);
  	/*
  	 * (non-Javadoc)
  	 * @see org.springframework.data.repository.query.QueryByExampleExecutor#findAll(org.springframework.data.domain.Example)
  	 */
  	@Override
  	<S extends T> List<S> findAll(Example<S> example);
  	/*
  	 * (non-Javadoc)
  	 * @see org.springframework.data.repository.query.QueryByExampleExecutor#findAll(org.springframework.data.domain.Example, org.springframework.data.domain.Sort)
  	 */
  	@Override
  	<S extends T> List<S> findAll(Example<S> example, Sort sort);
  }
  
  ```

* **Repository 的实现类 SimpleJpaRepository**

  * 关系数据库的所有 Repository 接口的实现类就是 SimpleJpaRepository

* Spring Data JPA 的最大特色是利用**方法名定义查询方法（Defining Query Methods）**来做 CRUD 操作

  * 一种是直接通过方法名就可以实现;

  * 另一种是 @Query 手动在方法上定义;

  * **方法的查询策略设置**

    * `@EnableJpaRepositories(queryLookupStrategy= QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND)`
    * QueryLookupStrategy.Key 的值共 3 个
      * **Create**：直接根据方法名进行创建，规则是根据方法名称的构造进行尝试，一般的方法是从方法名中删除给定的一组已知前缀，并解析该方法的其余部分。如果方法名不符合规则，启动的时候会报异常，这种情况可以理解为，即使配置了 @Query 也是没有用的。
      * **USE_DECLARED_QUERY**：声明方式创建，启动的时候会尝试找到一个声明的查询，如果没有找到将抛出一个异常，可以理解为必须配置 @Query。
      * **CREATE_IF_NOT_FOUND**：这个是默认的，除非有特殊需求，可以理解为这是以上 2 种方式的兼容版。先用声明方式（@Query）进行查找，如果没有找到与方法相匹配的查询，那用 Create 的方法名创建规则创建一个查询；这两者都不满足的情况下，启动就会报错。

  * Defining Query Method（DQM）语法

    * 该语法是：带查询功能的方法名由查询策略（关键字）+ 查询字段 + 一些限制性条件组成，具有语义清晰、功能完整的特性
    * org.springframework.data.repository.query.parser.PartTree 
    * org.springframework.data.repository.query.parser.Part
    * ![Lark20200918-182821.png](https://s0.lgstatic.com/i/image/M00/51/31/Ciqc1F9ki9CAPfoLAAMOpmuNPDY563.png)

  * **特定类型的参数：Sort 排序和 Pageable 分页**

    ```java
    Page<User> findByLastname(String lastname, Pageable pageable);//根据分页参数查询User，返回一个带分页结果的Page对象（方法一）
    方法一：允许将 org.springframework.data.domain.Pageable 实例传递给查询方法，将分页参数添加到静态定义的查询中，通过 Page 返回的结果得知可用的元素和页面的总数。这种分页查询方法可能是昂贵的（会默认执行一条 count 的 SQL 语句），所以用的时候要考虑一下使用场景。
    
    Slice<User> findByLastname(String lastname, Pageable pageable);//我们根据分页参数返回一个Slice的user结果（方法二）
    方法二：返回结果是 Slice，因为只知道是否有下一个 Slice 可用，而不知道 count，所以当查询较大的结果集时，只知道数据是足够的，也就是说用在业务场景中时不用关心一共有多少页。
    
    List<User> findByLastname(String lastname, Sort sort);//根据排序结果返回一个List（方法三）
    方法三：如果只需要排序，需在 org.springframework.data.domain.Sort 参数中添加一个参数，正如上面看到的，只需返回一个 List 也是有可能的。
    
    List<User> findByLastname(String lastname, Pageable pageable);//根据分页参数返回一个List对象（方法四）
    方法四：排序选项也通过 Pageable 实例处理，在这种情况下，Page 将不会创建构建实际实例所需的附加元数据（即不需要计算和查询分页相关数据），而仅仅用来做限制查询给定范围的实体。
    
    
    //查询user里面的lastname=jk的第一页，每页大小是20条；并会返回一共有多少页的信息
    Page<User> users = userRepository.findByLastname("jk",PageRequest.of(1, 20));
    //查询user里面的lastname=jk的第一页的20条数据，不知道一共多少条
    Slice<User> users = userRepository.findByLastname("jk",PageRequest.of(1, 20));
    //查询出来所有的user里面的lastname=jk的User数据，并按照name正序返回List
    List<User> users = userRepository.findByLastname("jk",new Sort(Sort.Direction.ASC, "name"))
    //按照createdAt倒序，查询前一百条User数据
    List<User> users = userRepository.findByLastname("jk",PageRequest.of(0, 100, Sort.Direction.DESC, "createdAt"));
    ```

  * **限制查询结果 First 和 Top**

    ```java
    User findFirstByOrderByLastnameAsc();
    User findTopByOrderByAgeDesc();
    List<User> findDistinctUserTop3ByLastname(String lastname, Pageable pageable);
    List<User> findFirst10ByLastname(String lastname, Sort sort);
    List<User> findTop10ByLastname(String lastname, Pageable pageable);
    ```

    * 查询方法在使用 First 或 Top 时，数值可以追加到 First 或 Top 后面，指定返回最大结果的大小；

    * 如果数字被省略，则假设结果大小为 1；

    * 限制表达式也支持 Distinct 关键字；

    * 支持将结果包装到 Optional 中。

    * 如果将 Pageable 作为参数，以 Top 和 First 后面的数字为准，即分页将在限制结果中应用。
    
  * @NonNull、@NonNullApi、@Nullable
  
    * 从 Spring Data 2.0 开始，JPA 新增了@NonNull @NonNullApi @Nullable，是对 null 的参数和返回结果做的支持。
  
    * @NonNullApi：在包级别用于声明参数，以及返回值的默认行为是不接受或产生空值的。
  
      ```java
      @org.springframework.lang.NonNullApi
      package com.myrespository;
      ```
  
    * @NonNull：用于不能为空的参数或返回值（在 @NonNullApi 适用的参数和返回值上不需要）
  
    * @Nullable：用于可以为空的参数或返回值。
  
      ```java
      //当我们添加@Nullable 注解之后，参数和返回结果这个时候就都会允许为 null 了；          
      @Nullable
      User findByEmailAddress(@Nullable EmailAddress emailAdress);
      //返回结果允许为 null,参数不允许为 null 的情况
      Optional<User> findOptionalByEmailAddress(EmailAddress emailAddress); 
      ```
  
* **Repository 的返回结果**

  * 它实现的方法，以及父类接口的方法和返回类型包括：Optional、Iterable、List、Page、Long、Boolean、Entity 对象等;

  * Spring Data 里面定义了一个特殊的子类 **Steamable**，Streamable 可以替代 Iterable 或任何集合类型。它还提供了方便的方法来访问 Stream，可以直接在元素上进行 ….filter(…) 和 ….map(…) 操作，并将 Streamable 连接到其他元素;

    ```java
    class Product { (1)
      MonetaryAmount getPrice() { … }
    }
    
    @RequiredArgConstructor(staticName = "of")
    class Products implements Streamable<Product> { (2)
      private Streamable<Product> streamable;
      public MonetaryAmount getTotal() { (3)
        return streamable.stream() //
          .map(Priced::getPrice)
          .reduce(Money.of(0), MonetaryAmount::add);
      }
    }
    
    interface ProductRepository implements Repository<Product, Long> {
      Products findAllByDescriptionContaining(String text); (4)
    }
    
    ```

    ```java
    //  Page<User>
    {
       "content":[],
       "pageable":{
          "sort":{
             "sorted":false,
             "unsorted":true,
             "empty":true
          },
          "pageNumber":0,当前页码
          "pageSize":3,页码大小
          "offset":0,偏移量
          "paged":true,是否分页了
          "unpaged":false
       },
       "totalPages":3,一共有多少页
       "last":false,是否是到最后
       "totalElements":7,一共多少调数
       "numberOfElements":3,当前数据下标
       "sort":{
          "sorted":false,
          "unsorted":true,
          "empty":true
       },
       "size":3,当前content大小
       "number":0,当前页面码的索引
       "first":true,是否是第一页
       "empty":false是否有数据
    }
    
    查询分页数据
    Hibernate: select user0_.id as id1_0_, user0_.address as address2_0_, user0_.email as email3_0_, user0_.name as name4_0_, user0_.sex as sex5_0_ from user user0_ limit ?
    计算分页数据
    Hibernate: select count(user0_.id) as col_0_0_ from user user0_
    ```

    ```java
    // Slice<User>
    {
       "content":[],
       "pageable":{
          "sort":{
             "sorted":false,
             "unsorted":true,
             "empty":true
          },
          "pageNumber":1,
          "pageSize":3,
          "offset":3,
          "paged":true,
          "unpaged":false
       },
       "numberOfElements":3,
       "sort":{
          "sorted":false,
          "unsorted":true,
          "empty":true
       },
       "size":3,
       "number":1,
       "first":false,
       "last":false,
       "empty":false
    }
    Hibernate: select user0_.id as id1_0_, user0_.address as address2_0_, user0_.email as email3_0_, user0_.name as name4_0_, user0_.sex as sex5_0_ from user user0_ limit ? offset ?
    
    ```

    * 只查询偏移量，不计算分页数据，这就是 Page 和 Slice 的主要区别。

  * **Repository 对 Feature/CompletableFuture 异步返回结果的支持：**
  
    * 可以使用 Spring 的异步方法执行Repository查询，这意味着方法将在调用时立即返回，并且实际的查询执行将发生在已提交给 Spring TaskExecutor 的任务中，比较适合定时任务的实际场景。异步使用起来比较简单，直接加@Async 注解即可，如下所示：
  
    ```java
    // 使用 java.util.concurrent.Future 的返回类型；
    @Async
    Future<User> findByFirstname(String firstname);
    // 使用 java.util.concurrent.CompletableFuture 作为返回类型；
    @Async
    CompletableFuture<User> findOneByFirstname(String firstname); 
    // 使用 org.springframework.util.concurrent.ListenableFuture 作为返回类型。
    @Async
    ListenableFuture<User> findOneByLastname(String lastname);
    ```
  
    * 以上是对 @Async 的支持，关于实际使用需要注意以下三点内容：
      - 在实际工作中，直接在 Repository 这一层使用异步方法的场景不多，一般都是把异步注解放在 Service 的方法上面，这样的话，可以有一些额外逻辑，如发短信、发邮件、发消息等配合使用；
      - 使用异步的时候一定要配置线程池，这点切记，否则“死”得会很难看；
      - 万一失败我们会怎么处理？关于事务是怎么处理的呢？这种需要重点考虑的;
  
  * ![Drawing 5.png](https://s0.lgstatic.com/i/image/M00/56/1D/Ciqc1F9rDAiARh9tAAQVFWlht1s532.png)
  
* Projections 的概念

  * 从字面意思上理解就是映射，指的是和 DB 的查询结果的字段映射关系。一般情况下，返回的字段和 DB 的查询结果的字段是一一对应的；但有的时候，需要返回一些指定的字段，或者返回一些复合型的字段，而不需要全部返回。

    ```java
    @Entity
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class User {
       @Id
       @GeneratedValue(strategy= GenerationType.AUTO)
       private Long id;
       private String name;
       private String email;
       private String sex;
       private String address;
    }
    
    ```

  * 如果我们只想返回 User 对象里面的 name 和 email，应该怎么做？下面我们介绍三种方法。

    * 第一种方法：新建一张表的不同 Entity

      ```java
      首先，我们新增一个Entity类：通过 @Table 指向同一张表，这张表和 User 实例里面的表一样都是 user，完整内容如下：
      @Entity
      @Table(name = "user")
      @Data
      @Builder
      @AllArgsConstructor
      @NoArgsConstructor
      public class UserOnlyNameEmailEntity {
         @Id
         @GeneratedValue(strategy= GenerationType.AUTO)
         private Long id;
         private String name;
         private String email;
      }
      
      然后，新增一个 UserOnlyNameEmailEntityRepository，做单独的查询：
      package com.example.jpa.example1;
      import org.springframework.data.jpa.repository.JpaRepository;
      public interface UserOnlyNameEmailEntityRepository extends JpaRepository<UserOnlyNameEmailEntity,Long> {
      }
      
      最后，我们的测试用例里面的写法如下：
      @Test
      public void testProjections() {
        userRepository.save(User.builder().id(1L).name("jack12").email("123456@126.com").sex("man").address("shanghai").build());
          List<User> users= userRepository.findAll();
          System.out.println(users);
          UserOnlyNameEmailEntity uName = userOnlyNameEmailEntityRepository.getOne(1L);
          System.out.println(uName);
      }
      
      缺点就是通过两个实体都可以进行 update 操作，如果同一个项目里面这种实体比较多，到时候就容易不知道是谁更新的，从而导致出 bug 不好查询，实体职责划分不明确。我们来看第二种返回 DTO 的做法。
      ```

    * 第二种方法：直接定义一个 UserOnlyNameEmailDto

      ```java
      首先，我们新建一个 DTO 类来返回我们想要的字段，它是 UserOnlyNameEmailDto，用来接收 name、email 两个字段的值，具体如下：
      @Data
      @Builder
      @AllArgsConstructor
      public class UserOnlyNameEmailDto {
          private String name,email;
      }
      
      其次，在 UserRepository 里面做如下用法：
      public interface UserRepositoryByDTO extends JpaRepository<User,Long> {
          //测试只返回name和email的DTO
          UserOnlyNameEmailDto findByEmail(String email);
      }
      
      然后，测试用例里面写法如下：
      @Test
      public void testProjections() {
      userRepository.save(User.builder().id(1L).name("jack12").email("123456@126.com").sex("man").address("shanghai").build());
              UserOnlyNameEmailDto userOnlyNameEmailDto =  userRepository.findByEmail("123456@126.com");
              System.out.println(userOnlyNameEmailDto);
          }
      
      这里需要注意的是，如果我们去看源码的话，看关键的 PreferredConstructorDiscoverer 类时会发现，UserDTO 里面只能有一个全参数构造方法
      
      Constructor 选择的时候会帮我们做构造参数的选择，如果 DTO 里面有多个构造方法，就会报转化错误的异常，这一点需要注意，异常是这样的：
      No converter found capable of converting from type [com.example.jpa.example1.User] to type [com.example.jpa.example1.UserOnlyNameEmailDto
      ```

      ![Drawing 7.png](https://s0.lgstatic.com/i/image/M00/56/28/CgqCHl9rDD6AWKs9AAE8kGFOxmo130.png)

    * 第三种方法：返回结果是一个 POJO 的接口

      ```java
      这种方式与上面两种的区别是只需要定义接口，它的好处是只读，不需要添加构造方法，我们使用起来非常灵活，一般很难产生 Bug，
      
      首先，定义一个 UserOnlyName 的接口：
      package com.example.jpa.example1;
      public interface UserOnlyName {
          String getName();
          String getEmail();
      }
      
      其次，我们的 UserRepository 写法如下：
      package com.example.jpa.example1;
      import org.springframework.data.jpa.repository.JpaRepository;
      public interface UserRepository extends JpaRepository<User,Long> {
          /**
           * 接口的方式返回DTO
           * @param address
           * @return
           */
          UserOnlyName findByAddress(String address);
      }
      然后，测试用例的写法如下：
          @Test
          public void testProjections() {
      userRepository.save(User.builder().name("jack12").email("123456@126.com").sex("man").address("shanghai").build());
              UserOnlyName userOnlyName = userRepository.findByAddress("shanghai");
              System.out.println(userOnlyName);
          }
      
      这个时候会发现我们的 userOnlyName 接口成了一个代理对象，里面通过 Map 的格式包含了我们的要返回字段的值（如：name、email），我们用的时候直接调用接口里面的方法即可，如 userOnlyName.getName() 即可；这种方式的优点是接口为只读，并且语义更清晰。
      ```

      ![Drawing 8.png](https://s0.lgstatic.com/i/image/M00/56/28/CgqCHl9rDEWAdoxmAARwd1XSUzo704.png)

* **@Query**

  * JpaQueryLookupStrategy 关键源码剖析

    * 先打开 QueryExecutorMethodInterceptor 类，默认的策略是CreateIfNotFound,找到如下代码：

      ![image-20210130223105474](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210130223105474.png)

  * @Query 的基本用法

    ```java
    package org.springframework.data.jpa.repository;
    public @interface Query {
       /**
        * 指定JPQL的查询语句。（nativeQuery=true的时候，是原生的Sql语句）
    	*/
       String value() default "";
       /**
    	* 指定count的JPQL语句，如果不指定将根据query自动生成。
        * （如果当nativeQuery=true的时候，指的是原生的Sql语句）
        */
       String countQuery() default "";
       /**
        * 根据哪个字段来count，一般默认即可。
    	*/
       String countProjection() default "";
       /**
        * 默认是false，表示value里面是不是原生的sql语句
    	*/
       boolean nativeQuery() default false;
       /**
        * 可以指定一个query的名字，必须唯一的。
    	* 如果不指定，默认的生成规则是：
        * {$domainClass}.${queryMethodName}
        */
       String name() default "";
       /*
        * 可以指定一个count的query的名字，必须唯一的。
    	* 如果不指定，默认的生成规则是：
        * {$domainClass}.${queryMethodName}.count
        */
       String countName() default "";
    }
    ```

  * @Query 用法是使用 JPQL 为实体创建声明式查询方法。我们一般只需要关心 @Query 里面的 value 和 nativeQuery、countQuery 的值即可

  * JPQL的语法

    ```
    查询:
    SELECT ... FROM ...
    [WHERE ...]
    [GROUP BY ... [HAVING ...]]
    [ORDER BY ...]
    它的语法结构有点类似我们 SQL,唯一的区别就是 JPQL FROM 后面跟的是对象，而 SQL 里面的字段对应的是对象里面的属性字段。
    
    update 和 delete 的语法结构
    DELETE FROM ... [WHERE ...]
    UPDATE ... SET ... [WHERE ...]
    ```

  * 语法参考文档: https://docs.oracle.com/html/E13946_04/ejb3_langref.html

  * 示例

    ```java
    案例 1： 要在 Repository 的查询方法上声明一个注解，这里就是 @Query 注解标注的地方。
    public interface UserRepository extends JpaRepository<User, Long>{
      @Query("select u from User u where u.emailAddress = ?1")
      User findByEmailAddress(String emailAddress);
    }
    
    案例 2： LIKE 查询，注意 firstname 不会自动加上“%”关键字。
    public interface UserRepository extends JpaRepository<User, Long> {
      @Query("select u from User u where u.firstname like %?1")
      List<User> findByFirstnameEndsWith(String firstname);
    }
    
    案例 3： 直接用原始 SQL，nativeQuery = true 即可。
    public interface UserRepository extends JpaRepository<User, Long> {
      @Query(value = "SELECT * FROM USERS WHERE EMAIL_ADDRESS = ?1", nativeQuery = true)
      User findByEmailAddress(String emailAddress);
    }
    注意：nativeQuery 不支持直接 Sort 的参数查询。
    
    案例 4： 下面是nativeQuery 的排序错误的写法，会导致无法启动。
    public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "select * from user_info where first_name=?1",nativeQuery = true)
    List<UserInfoEntity> findByFirstName(String firstName,Sort sort);
    }
    
    案例 5： nativeQuery 排序的正确写法。
    @Query(value = "select * from user_info where first_name=?1 order by ?2",nativeQuery = true)
    List<UserInfoEntity> findByFirstName(String firstName,String sort);
    //调用的地方写法last_name是数据里面的字段名，不是对象的字段名
    repository.findByFirstName("jackzhang","last_name");
    ```

  * @Query 的排序

    * @Query中在用JPQL的时候，想要实现排序，方法上直接用 PageRequest 或者 Sort 参数都可以做到。

    * `state_field_path_expression JPQL`的定义，并且 Sort 的对象支持一些特定的函数。

      ```java
      案例 6： Sort and JpaSort 的使用，它可以进行排序。
      public interface UserRepository extends JpaRepository<User, Long> {
        @Query("select u from User u where u.lastname like ?1%")
        List<User> findByAndSort(String lastname, Sort sort);
      
        @Query("select u.id, LENGTH(u.firstname) as fn_len from User u where u.lastname like ?1%")
        List<Object[]> findByAsArrayAndSort(String lastname, Sort sort);
      
      }
      
      //调用方的写法，如下：
      repo.findByAndSort("lannister", new Sort("firstname"));
      repo.findByAndSort("stark", new Sort("LENGTH(firstname)"));
      repo.findByAndSort("targaryen", JpaSort.unsafe("LENGTH(firstname)"));
      repo.findByAsArrayAndSort("bolton", new Sort("fn_len"));
      
      ```

  * @Query 的分页

    * @Query 的分页分为两种情况，分别为 JPQL 的排序和 nativeQuery 的排序。

      ```java
      案例 7：直接用 Page 对象接受接口，参数直接用 Pageable 的实现类即可。
      public interface UserRepository extends JpaRepository<User, Long> {
        @Query(value = "select u from User u where u.lastname = ?1")
        Page<User> findByLastname(String lastname, Pageable pageable);
      }
      //调用者的写法
      repository.findByFirstName("jackzhang",new PageRequest(1,10));
      
      案例 8：@Query 对原生 SQL 的分页支持，并不是特别友好，因为这种写法比较“骇客”，可能随着版本的不同会有所变化。我们以 MySQL 为例。
      这里需要注意：这个注释 / #pageable# / 必须有。
       public interface UserRepository extends JpaRepository<UserInfoEntity, Integer>, JpaSpecificationExecutor<UserInfoEntity> {
      
         @Query(value = "select * from user_info where first_name=?1 /* #pageable# */",
               countQuery = "select count(*) from user_info where first_name=?1",
               nativeQuery = true)
         Page<UserInfoEntity> findByFirstName(String firstName, Pageable pageable);
      
      }
      
      //调用者的写法
      return userRepository.findByFirstName("jackzhang",new PageRequest(1,10, Sort.Direction.DESC,"last_name"));
      
      //打印出来的sql
      
      select  *   from  user_info  where  first_name=? /* #pageable# */  order by  last_name desc limit ?, ?
      
      ```

  * @Param 用法

    * @Param 注解指定方法参数的具体名称，通过绑定的参数名字指定查询条件，这样不需要关心参数的顺序。

  * @Query 之 Projections 应用返回指定 DTO

    ```java
    @Entity
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class UserExtend { //用户扩展信息表
       @Id
       @GeneratedValue(strategy= GenerationType.AUTO)
       private Long id;
       private Long userId;
       private String idCard;
       private Integer ages;
       private String studentNumber;
    }
    
    @Entity
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class User { //用户基本信息表
       @Id
       @GeneratedValue(strategy= GenerationType.AUTO)
       private Long id;
       private String name;
       private String email;
       @Version
       private Long version;
       private String sex;
       private String address;
    }
    如果我们想定义一个 DTO 对象，里面只要 name、email、idCard，这个时候我们怎么办呢？这种场景非常常见
        
    一 利用 `class UserDto` 获取我们想要的结果,首先，我们新建一个 UserDto 类的内容。
    package com.example.jpa.example1;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    @Data
    @Builder
    @AllArgsConstructor
    public class UserDto {
        private String name,email,idCard;
    } 
    public interface UserDtoRepository extends JpaRepository<User, Long> {
       @Query("select new com.example.jpa.example1.UserDto(CONCAT(u.name,'JK123'),u.email,e.idCard) from User u,UserExtend e where u.id= e.userId and u.id=:id")
       UserDto findByUserDtoId(@Param("id") Long id);
    }
    @Test
    public void testQueryAnnotationDto() {
      userDtoRepository.save(User.builder().name("jack").email("123456@126.com").sex("man").address("shanghai").build());
      userExtendRepository.save(UserExtend.builder().userId(1L).idCard("shengfengzhenghao").ages(18).studentNumber("xuehao001").build());
       UserDto userDto = userDtoRepository.findByUserDtoId(1L);
       System.out.println(userDto);
    }
    
    二 利用 UserDto 接口获得我们想要的结果
    首先，新增一个 UserSimpleDto 接口来得到我们想要的 name、email、idCard 信息。
    package com.example.jpa.example1;
    public interface UserSimpleDto {
       String getName();
       String getEmail();
       String getIdCard();
    }
    其次，在 UserDtoRepository 里面新增一个方法，返回结果是 UserSimpleDto 接口。
    public interface UserDtoRepository extends JpaRepository<User, Long> {
    //利用接口DTO获得返回结果，需要注意的是每个字段需要as和接口里面的get方法名字保持一样
    @Query("select CONCAT(u.name,'JK123') as name,UPPER(u.email) as email ,e.idCard as idCard from User u,UserExtend e where u.id= e.userId and u.id=:id")
    UserSimpleDto findByUserSimpleDtoId(@Param("id") Long id);
    }
    然后，测试用例写法如下。
    @Test
    public void testQueryAnnotationDto() {
       userDtoRepository.save(User.builder().name("jack").email("123456@126.com").sex("man").address("shanghai").build());
      userExtendRepository.save(UserExtend.builder().userId(1L).idCard("shengfengzhenghao").ages(18).studentNumber("xuehao001").build());
       UserSimpleDto userDto = userDtoRepository.findByUserSimpleDtoId(1L);
       System.out.println(userDto);  System.out.println(userDto.getName()+":"+userDto.getEmail()+":"+userDto.getIdCard());
    }
    
    ```

    * 打开 `org.hibernate.query.criteria.internal.expression.function.ParameterizedFunctionExpression` 会发现 Hibernate 支持的关键字有这么多

  * @Query 动态查询解决方法

    ```java
    实现 @Query 的动态参数查询。
    首先，新增一个 UserOnlyName 接口，只查询 User 里面的 name 和 email 字段。
    package com.example.jpa.example1;
    //获得返回结果
    public interface UserOnlyName {
        String getName();
        String getEmail();
    }
    其次，在我们的 UserDtoRepository 里面新增两个方法：一个是利用 JPQL 实现动态查询，一个是利用原始 SQL 实现动态查询。
    package com.example.jpa.example1;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;
    import java.util.List;
    public interface UserDtoRepository extends JpaRepository<User, Long> {
       /**
        * 利用JQPl动态查询用户信息
        * @param name
        * @param email
        * @return UserSimpleDto接口
        */
       @Query("select u.name as name,u.email as email from User u where (:name is null or u.name =:name) and (:email is null or u.email =:email)")
       UserOnlyName findByUser(@Param("name") String name,@Param("email") String email);
       /**
        * 利用原始sql动态查询用户信息
        * @param user
        * @return
        */
       @Query(value = "select u.name as name,u.email as email from user u where (:#{#user.name} is null or u.name =:#{#user.name}) and (:#{#user.email} is null or u.email =:#{#user.email})",nativeQuery = true)
       UserOnlyName findByUser(@Param("user") User user);
    }
    
    然后，我们新增一个测试类，测试一下上面方法的结果。
    @Test
    public void testQueryDinamicDto() {
     userDtoRepository.save(User.builder().name("jack").email("123456@126.com").sex("man").address("shanghai").build());
       UserOnlyName userDto = userDtoRepository.findByUser("jack", null);
       System.out.println(userDto.getName() + ":" + userDto.getEmail());
       UserOnlyName userDto2 = userDtoRepository.findByUser(User.builder().email("123456@126.com").build());
       System.out.println(userDto2.getName() + ":" + userDto2.getEmail());
    }
    
    ```

    ![Drawing 5.png](https://s0.lgstatic.com/i/image/M00/56/57/CgqCHl9rKauAfQmmAAHMquqHBVg909.png)

    ![Drawing 6.png](https://s0.lgstatic.com/i/image/M00/56/4C/Ciqc1F9rKbGAT_6FAAL_YUkV6AQ306.png)
  
* **@Entity**

  * JPA 协议中关于 Entity 的相关规定

    * 实体是直接进行数据库持久化操作的领域对象（即一个简单的 POJO，可以按照业务领域划分），必须通过 @Entity 注解进行标示。

    * 实体必须有一个 public 或者 protected 的无参数构造方法。

    * 持久化映射的注解可以标示在 Entity 的字段 field 上

      ```java
      @Column(length = 20, nullable = false)
      private String userName;
      ```

    * 也可以将持久化注解运用在 Entity 里面的 get/set 方法上，通常我们是放在 get 方法中，如下所示：

      ```java
      @Column(length = 20, nullable = false)
      public String getUserName(){
          return userName;
      }
      ```

  * **概括起来，就是 Entity 里面的注解生效只有两种方式：将注解写在字段上或者将注解写在方法上（JPA 里面称 Property）。需要注意的是，在同一个 Entity 里面只能有一种方式生效，也就是说，注解要么全部写在 field 上面，要么就全部写在 Property 上面**

    * 只要是在 @Entity 的实体里面被注解标注的字段，都会被映射到数据库中，除了使用 @Transient 注解的字段之外。
    * 实体里面必须要有一个主键，主键标示的字段可以是单个字段，也可以是复合主键字段。

  * **@Entity** 用于定义对象将会成为被 JPA 管理的实体，必填，将字段映射到指定的数据库表中，使用起来很简单，直接用在实体类上面即可，通过源码表达的语法如下:

    ```java
    @Target(TYPE) //表示此注解只能用在class上面
    public @interface Entity {
       //可选，默认是实体类的名字，整个应用里面全局唯一。
       String name() default "";
    }
    ```

  * **@Table** 用于指定数据库的表名，表示此实体对应的数据库里面的表名，非必填，默认表名和 entity 名字一样。

    ```java
    @Target(TYPE) //一样只能用在类上面
    public @interface Table {
       //表的名字，可选。如果不填写，系统认为好实体的名字一样为表名。
       String name() default "";
       //此表所在schema，可选
       String schema() default "";
       //唯一性约束，在创建表的时候有用，表创建之后后面就不需要了。
       UniqueConstraint[] uniqueConstraints() default { };
       //索引，在创建表的时候使用，表创建之后后面就不需要了。
       Index[] indexes() default {};
    }
    ```

  * **@Access** 用于指定 entity 里面的注解是写在字段上面，还是 get/set 方法上面生效，非必填。在默认不填写的情况下，当实体里面的第一个注解出现在字段上或者 get/set 方法上面，就以第一次出现的方式为准；也就是说，一个实体里面的注解既有用在 field 上面，又有用在 properties 上面的时候.

    ```java
    @Id
    private Long id;
    @Column(length = 20, nullable = false)
    public String getUserName(){
        return userName;
    }
    那么由于 @Id 是实体里面第一个出现的注解，并且作用在字段上面，所以所有写在 get/set 方法上面的注解就会失效。而 @Access 可以干预默认值，指定是在 fileds 上面生效还是在 properties 上面生效
    
    //表示此注解可以运用在class上(那么这个时候就可以指定此实体的默认注解生效策略了)，也可以用在方法上或者字段上(表示可以独立设置某一个字段或者方法的生效策略)；
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Access {
        //指定是字段上面生效还是方法上面生效
        AccessType value();
    }
    public enum AccessType {
        FIELD,
        PROPERTY;
    
        private AccessType() {
        }
    }
    ```

  * **@Id** 定义属性为数据库的主键，一个实体里面必须有一个主键，但不一定是这个注解，可以和 @GeneratedValue 配合使用或成对出现。

  * **@GeneratedValue** 主键生成策略

    ```java
    public @interface GeneratedValue {
        //Id的生成策略
        GenerationType strategy() default AUTO;
        //通过Sequences生成Id,常见的是Orcale数据库ID生成规则，这个时候需要配合@SequenceGenerator使用
        String generator() default "";
    }
    public enum GenerationType {
        //通过表产生主键，框架借由表模拟序列产生主键，使用该策略可以使应用更易于数据库移植。
        TABLE,
        //通过序列产生主键，通过 @SequenceGenerator 注解指定序列名， MySql 不支持这种方式；
        SEQUENCE,
        //采用数据库ID自增长， 一般用于mysql数据库
        IDENTITY,
    	//JPA 自动选择合适的策略，是默认选项；
        AUTO
    }
    ```

  * **@Enumerated** 这个注解很好用，因为它对 enum 提供了下标和 name 两种方式，用法直接映射在 enum 枚举类型的字段上。

    ```java
    //作用在方法和字段上
    @Target({METHOD, FIELD})
    public @interface Enumerated {
    //枚举映射的类型，默认是ORDINAL（即枚举字段的下标）。
        EnumType value() default ORDINAL;
    }
    public enum EnumType {
        //映射枚举字段的下标
        ORDINAL,
        //映射枚举的Name
        STRING
    }
    经验分享： 如果我们用 @Enumerated（EnumType.ORDINAL），这时候数据库里面的值是 0、1。但是实际工作中，不建议用数字下标，因为枚举里面的属性值是会不断新增的，如果新增一个，位置变化了就惨了。并且 0、1、2 这种下标在数据库里面看着非常痛苦，时间长了就会一点也看不懂了。
    ```

  * **@Basic** 表示属性是到数据库表的字段的映射。如果实体的字段上没有任何注解，默认即为 @Basic。也就是说默认所有的字段肯定是和数据库进行映射的，并且默认为 Eager 类型。

    ```java
    public @interface Basic {
        //可选，EAGER（默认）：立即加载；LAZY：延迟加载。（LAZY主要应用在大字段上面）
        FetchType fetch() default EAGER;
        //可选。这个字段是否可以为null，默认是true。
        boolean optional() default true;
    }
    ```

  * **@Transient** 表示该属性并非一个到数据库表的字段的映射，表示非持久化属性。JPA 映射数据库的时候忽略它，与 @Basic 有相反的作用。也就是每个字段上面 @Transient 和 @Basic 必须二选一，而什么都不指定的话，默认是 @Basic。

  * **@Column** 定义该属性对应数据库中的列名。

    ```java
    public @interface Column {
        //数据库中的表的列名；可选，如果不填写认为字段名和实体属性名一样。
        String name() default "";
        //是否唯一。默认flase，可选。
        boolean unique() default false;
        //数据字段是否允许空。可选，默认true。
        boolean nullable() default true;
        //执行insert操作的时候是否包含此字段，默认，true，可选。
        boolean insertable() default true;
        //执行update的时候是否包含此字段，默认，true，可选。
        boolean updatable() default true;
        //表示该字段在数据库中的实际类型。
        String columnDefinition() default "";
       //数据库字段的长度，可选，默认255
        int length() default 255;
    }
    ```

  * **@Temporal** 用来设置 Date 类型的属性映射到对应精度的字段，存在以下三种情况

    * @Temporal(TemporalType.DATE)映射为日期 // date （**只有日期**）

    * @Temporal(TemporalType.TIME)映射为日期 // time （**只有时间**）

    * @Temporal(TemporalType.TIMESTAMP)映射为日期 // date time （**日期+时间**）

  * ```java
    package com.example.jpa.example1;
    
    import lombok.Data;
    import javax.persistence.*;
    import java.util.Date;
    
    @Entity
    @Table(name = "user_topic")
    @Access(AccessType.FIELD)
    @Data
    public class UserTopic {
    
       @Id
       @Column(name = "id", nullable = false)
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Integer id;
       
       @Column(name = "title", nullable = true, length = 200)
       private String title;
    
       @Basic
       @Column(name = "create_user_id", nullable = true)
       private Integer createUserId;
    
       @Basic(fetch = FetchType.LAZY)
       @Column(name = "content", nullable = true, length = -1)
       @Lob
       private String content;
    
       @Basic(fetch = FetchType.LAZY)
       @Column(name = "image", nullable = true)
       @Lob
       private byte[] image;
    
       @Basic
       @Column(name = "create_time", nullable = true)
       @Temporal(TemporalType.TIMESTAMP)
       private Date createTime;
       
       @Basic
       @Column(name = "create_date", nullable = true)
       @Temporal(TemporalType.DATE)
       private Date createDate;
    
       @Enumerated(EnumType.STRING)
       @Column(name = "topic_type")
       private Type type;
       
       @Transient
       private String transientSimple;
       //非数据库映射字段，业务类型的字段
       public String getTransientSimple() {
          return title + "auto:jack" + type;
       }
       //有一个枚举类，主题的类型
       public enum Type {
          EN("英文"), CN("中文");
          private final String des;
          Type(String des) {
             this.des = des;
          }
       }
    }
    ```

  * 联合主键

    * 可以通过 javax.persistence.EmbeddedId 和 javax.persistence.IdClass 两个注解实现联合主键的效果。

      ```java
      第一步：新建一个 UserInfoID 类里面是联合主键。
      package com.example.jpa.example1;
      import lombok.AllArgsConstructor;
      import lombok.Builder;
      import lombok.Data;
      import lombok.NoArgsConstructor;
      import java.io.Serializable;
      
      @Data
      @Builder
      @AllArgsConstructor
      @NoArgsConstructor
      public class UserInfoID implements Serializable {
         private String name,telephone;
      }
      
      第二步：再新建一个 UserInfo 的实体，采用 @IdClass 引用联合主键类。
      @Entity
      @Data
      @Builder
      @IdClass(UserInfoID.class)
      @AllArgsConstructor
      @NoArgsConstructor
      public class UserInfo {
         private Integer ages;
         @Id
         private String name;
         @Id
         private String telephone;
      }
      
      第三步：新增一个 UserInfoReposito 类来做 CRUD 操作。
      package com.example.jpa.example1;
      import org.springframework.data.jpa.repository.JpaRepository;
      public interface UserInfoRepository extends JpaRepository<UserInfo,UserInfoID> {
      }
      
      第四步：写一个测试用例，测试一下。
      package com.example.jpa.example1;
      import org.junit.jupiter.api.Test;
      import org.springframework.beans.factory.annotation.Autowired;
      import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
      import java.util.Optional;
      @DataJpaTest
      public class UserInfoRepositoryTest {
         @Autowired
         private UserInfoRepository userInfoRepository;
         @Test
         public void testIdClass() {
         userInfoRepository.save(UserInfo.builder().ages(1).name("jack").telephone("123456789").build());
            Optional<UserInfo> userInfo = 		userInfoRepository.findById(UserInfoID.builder().name("jack").telephone("123456789").build());
            System.out.println(userInfo.get());
         }
      }
      Hibernate: create table user_info (name varchar(255) not null, telephone varchar(255) not null, ages integer, primary key (name, telephone))
      Hibernate: select userinfo0_.name as name1_3_0_, userinfo0_.telephone as telephon2_3_0_, userinfo0_.ages as ages3_3_0_ from user_info userinfo0_ where userinfo0_.name=? and userinfo0_.telephone=?
      UserInfo(ages=1, name=jack, telephone=123456789)
      ```

    * @Embeddable 与 @EmbeddedId 注解使用

      ```java
      第一步：在我们上面例子中的 UserInfoID 里面添加 @Embeddable 注解。
      @Data
      @Builder
      @AllArgsConstructor
      @NoArgsConstructor
      @Embeddable
      public class UserInfoID implements Serializable {
         private String name,telephone;
      }
      
      第二步：改一下我们刚才的 User 对象，删除 @IdClass，添加 @EmbeddedId 注解，如下：
      @Entity
      @Data
      @Builder
      @AllArgsConstructor
      @NoArgsConstructor
      public class UserInfo {
         private Integer ages;
         @EmbeddedId
         private UserInfoID userInfoID;
         @Column(unique = true)
         private String uniqueNumber;
      }
      
      第三步：UserInfoRepository 不变，我们直接修改一下测试用例。
      @Test
      public void testIdClass() {
       userInfoRepository.save(UserInfo.builder().ages(1).userInfoID(UserInfoID.builder().name("jack").telephone("123456789").build()).build());
         Optional<UserInfo> userInfo = userInfoRepository.findById(UserInfoID.builder().name("jack").telephone("123456789").build());
         System.out.println(userInfo.get());
      }
      ```

    * @IdClass 和 @EmbeddedId 的区别是什么？

      * 如上面测试用例，在使用的时候，Embedded 用的是对象，而 IdClass 用的是具体的某一个字段；

      * 二者的JPQL 也会不一样：

        *  用 @IdClass JPQL 的写法：SELECT u.name FROM UserInfo u
        * 用 @EmbeddedId 的 JPQL 的写法：select u.userInfoId.name FROM UserInfo u

      * 联合主键还有需要注意的就是，它与唯一性索引约束的区别是写法不同，如上面所讲，唯一性索引的写法如下：

        ```java
        @Column(unique = true)
        private String uniqueNumber;
        ```

  * 实体之间的继承关系如何实现？

    * 在 Java 面向对象的语言环境中，@Entity 之间的关系多种多样，而根据 JPA 的规范，我们大致可以将其分为以下几种：

      * 纯粹的继承，和表没关系，对象之间的字段共享。利用注解 @MappedSuperclass，协议规定父类不能是 @Entity。
      * 单表多态问题，同一张 Table，表示了不同的对象，通过一个字段来进行区分。利用`@Inheritance(strategy = InheritanceType.SINGLE_TABLE)`注解完成，只有父类有 @Table。
      * 多表多态，每一个子类一张表，父类的表拥有所有公用字段。通过`@Inheritance(strategy = InheritanceType.JOINED)`注解完成，父类和子类都是表，有公用的字段在父表里面。
      * Object 的继承，数据库里面每一张表是分开的，相互独立不受影响。通过`@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)`注解完成，父类（可以是一张表，也可以不是）和子类都是表，相互之间没有关系。

    * **@Inheritance(strategy = InheritanceType.SINGLE_TABLE)**

      * 父类实体对象与各个子实体对象共用一张表，通过一个字段的不同值代表不同的对象。

        ```java
        我们抽象一个 Book 对象，如下所示：
        package com.example.jpa.example1.book;
        import lombok.Data;
        import javax.persistence.*;
        @Entity(name="book")
        @Data
        @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
        @DiscriminatorColumn(name="color", discriminatorType = DiscriminatorType.STRING)
        public class Book {
           @Id
           @GeneratedValue(strategy= GenerationType.AUTO)
           private Long id;
           private String title;
        }
        
        再新建一个 BlueBook 对象，作为 Book 的子对象。
        package com.example.jpa.example1.book;
        import lombok.Data;
        import lombok.EqualsAndHashCode;
        import javax.persistence.DiscriminatorValue;
        import javax.persistence.Entity;
        @Entity
        @Data
        @EqualsAndHashCode(callSuper=false)
        @DiscriminatorValue("blue")
        public class BlueBook extends Book{
           private String blueMark;
        }
        
        再新建一个 RedBook 对象，作为 Book 的另一子对象。
        //红皮书
        @Entity
        @DiscriminatorValue("red")
        @Data
        @EqualsAndHashCode(callSuper=false)
        public class RedBook extends Book {
           private String redMark;
        }
        
        这时，我们一共新建了三个 Entity 对象，其实都是指 book 这一张表，通过 book 表里面的 color 字段来区分红书还是绿书。我们继续做一下测试看看结果。
        我们再新建一个 RedBookRepositor 类，操作一下 RedBook 会看到如下结果：
        package com.example.jpa.example1.book;
        import org.springframework.data.jpa.repository.JpaRepository;
        public interface RedBookRepository extends JpaRepository<RedBook,Long>{
        }
        然后再新建一个测试用例。
        package com.example.jpa.example1;
        import com.example.jpa.example1.book.RedBook;
        import com.example.jpa.example1.book.RedBookRepository;
        import org.junit.jupiter.api.Test;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
        @DataJpaTest
        public class RedBookRepositoryTest {
           @Autowired
           private RedBookRepository redBookRepository;
           @Test
           public void testRedBook() {
              RedBook redBook = new RedBook();
              redBook.setTitle("redbook");
              redBook.setRedMark("redmark");
              redBook.setId(1L);
              redBookRepository.saveAndFlush(redBook);
              RedBook r = redBookRepository.findById(1L).get();
            System.out.println(r.getId()+":"+r.getTitle()+":"+r.getRedMark());
           }
        }
        Hibernate: create table book (color varchar(31) not null, id bigint not null, title varchar(255), blue_mark varchar(255), red_mark varchar(255), primary key (id))
        Hibernate: insert into book (title, red_mark, color, id) values (?, ?, 'red', ?)
        ```

    * **@Inheritance(strategy = InheritanceType.JOINED)**

      * 在这种映射策略里面，继承结构中的每一个实体（entity）类都会映射到数据库里一个单独的表中。也就是说，每个实体（entity）都会被映射到数据库中，一个实体（entity）类对应数据库中的一个表。

      * 其中根实体（root entity）对应的表中定义了主键（primary key），所有的子类对应的数据库表都要共同使用 Book 里面的 @ID 这个主键。

        ```java
        首先，我们改一下上面的三个实体，测试一下InheritanceType.JOINED，改动如下：
        package com.example.jpa.example1.book;
        import lombok.Data;
        import javax.persistence.*;
        @Entity(name="book")
        @Data
        @Inheritance(strategy = InheritanceType.JOINED)
        public class Book {
           @Id
           @GeneratedValue(strategy= GenerationType.AUTO)
           private Long id;
           private String title;
        }
        
        其次，我们 Book 父类、改变 Inheritance 策略、删除 DiscriminatorColumn，你会看到如下结果。
        package com.example.jpa.example1.book;
        import lombok.Data;
        import lombok.EqualsAndHashCode;
        import javax.persistence.Entity;
        import javax.persistence.PrimaryKeyJoinColumn;
        @Entity
        @Data
        @EqualsAndHashCode(callSuper=false)
        @PrimaryKeyJoinColumn(name = "book_id", referencedColumnName = "id")
        public class BlueBook extends Book{
           private String blueMark;
        }
        package com.example.jpa.example1.book;
        import lombok.Data;
        import lombok.EqualsAndHashCode;
        import javax.persistence.Entity;
        import javax.persistence.PrimaryKeyJoinColumn;
        @Entity
        @PrimaryKeyJoinColumn(name = "book_id", referencedColumnName = "id")
        @Data
        @EqualsAndHashCode(callSuper=false)
        public class RedBook extends Book {
           private String redMark;
        }
        然后，BlueBook和RedBook也删除DiscriminatorColumn，新增@PrimaryKeyJoinColumn(name = "book_id", referencedColumnName = "id")，和 book 父类共用一个主键值，而 RedBookRepository 和测试用例不变
        Hibernate: create table blue_book (blue_mark varchar(255), book_id bigint not null, primary key (book_id))
        Hibernate: create table book (id bigint not null, title varchar(255), primary key (id))
        Hibernate: create table red_book (red_mark varchar(255), book_id bigint not null, primary key (book_id))
        Hibernate: alter table blue_book add constraint FK9uuwgq7a924vtnys1rgiyrlk7 foreign key (book_id) references book
        Hibernate: alter table red_book add constraint FKk8rvl61bjy9lgsr9nhxn5soq5 foreign key (book_id) references book
        ```

    * **@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)**

      * 我们在使用 @MappedSuperClass 主键的时候，如果不指定 @Inhertance，默认就是此种TABLE_PER_CLASS模式。当然了，我们也显示指定，要求继承基类的都是一张表，而父类不是表，是 java 对象的抽象类。我们看一个例子。

        ```java
        首先，还是改一下上面的三个实体。
        package com.example.jpa.example1.book;
        import lombok.Data;
        import javax.persistence.*;
        @Entity(name="book")
        @Data
        @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
        public class Book {
           @Id
           @GeneratedValue(strategy= GenerationType.AUTO)
           private Long id;
           private String title;
        }
        
        其次，Book 表采用 TABLE_PER_CLASS 策略，其子实体类都代表各自的表，实体代码如下：
        package com.example.jpa.example1.book;
        import lombok.Data;
        import lombok.EqualsAndHashCode;
        import javax.persistence.Entity;
        @Entity
        @Data
        @EqualsAndHashCode(callSuper=false)
        public class RedBook extends Book {
           private String redMark;
        }
        package com.example.jpa.example1.book;
        import lombok.Data;
        import lombok.EqualsAndHashCode;
        import javax.persistence.Entity;
        @Entity
        @Data
        @EqualsAndHashCode(callSuper=false)
        public class BlueBook extends Book{
           private String blueMark;
        }
        
        这时，从 RedBook 和 BlueBook 里面去掉 PrimaryKeyJoinColumn，而 RedBookRepository 和测试用例不变，我们执行看一下结果。
        Hibernate: create table blue_book (id bigint not null, title varchar(255), blue_mark varchar(255), primary key (id))
        Hibernate: create table book (id bigint not null, title varchar(255), primary key (id))
        Hibernate: create table red_book (id bigint not null, title varchar(255), red_mark varchar(255), primary key (id))
        ```

  * 实体与实体之间的关联关系

    * 实体与实体之间的关联关系一共分为四种，分别为 OneToOne、OneToMany、ManyToOne 和 ManyToMany；而实体之间的关联关系又分为双向的和单向的。

    * **@OneToOne 关联关系**

      * @OneToOne 一般表示对象之间一对一的关联关系，它可以放在 field 上面，也可以放在 get/set 方法上面。其中 JPA 协议有规定，如果是配置双向关联，维护关联关系的是拥有外键的一方，而另一方必须配置 mappedBy；如果是单项关联，直接配置在拥有外键的一方即可。

        ```java
        举个例子：user 表是用户的主信息，user_info 是用户的扩展信息，两者之间是一对一的关系。user_info 表里面有一个 user_id 作为关联关系的外键，如果是单项关联，我们的写法如下
        package com.example.jpa.example1;
        import lombok.AllArgsConstructor;
        import lombok.Builder;
        import lombok.Data;
        import lombok.NoArgsConstructor;
        import javax.persistence.*;
        @Entity
        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public class User {
           @Id
           @GeneratedValue(strategy= GenerationType.AUTO)
           private Long id;
           private String name;
           private String email;
           private String sex;
           private String address;
        }
        
        User 实体里面什么都没变化，不需要添加 @OneToOne 注解。我们只需要在拥有外键的一方配置就可以，所以 UserInfo 的代码如下：
        package com.example.jpa.example1;
        import lombok.*;
        import javax.persistence.*;
        @Entity
        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        @ToString(exclude = "user")
        public class UserInfo {
           @Id
           @GeneratedValue(strategy= GenerationType.AUTO)
           private Long id;
           private Integer ages;
           private String telephone;
           @OneToOne //维护user的外键关联关系，配置一对一
           private User user;
        }
        我们看到，UserInfo 实体对象里面添加了 @OneToOne 注解，这时我们写一个测试用例跑一下看看有什么效果：
        Hibernate: create table user (id bigint not null, address varchar(255), email varchar(255), name varchar(255), sex varchar(255), primary key (id))
        Hibernate: create table user_info (id bigint not null, ages integer, telephone varchar(255), user_id bigint, primary key (id))
        Hibernate: alter table user_info add constraint FKn8pl63y4abe7n0ls6topbqjh2 foreign key (user_id) references user
        
        那么双向关联应该怎么配置呢？我们保持 UserInfo 不变，在 User 实体对象里面添加这一段代码即可。
        @OneToOne(mappedBy = "user")
        private UserInfo userInfo;
        
        完整的 User 实体对象就会变成如下模样。
        @Entity
        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public class User {
           @Id
           @GeneratedValue(strategy= GenerationType.AUTO)
           private Long id;
           private String name;
           private String email;
           @OneToOne(mappedBy = "user")
           private UserInfo userInfo;//变化之处
           private String sex;
           private String address;
        }
        ```

    * @interface OneToOne 源码解读

      ```java
      public @interface OneToOne {
          //表示关系目标实体，默认该注解标识的返回值的类型的类。
          Class targetEntity() default void.class;
          //cascade 级联操作策略，就是我们常说的级联操作
          CascadeType[] cascade() default {};
          //数据获取方式EAGER(立即加载)/LAZY(延迟加载)
          FetchType fetch() default EAGER;
          //是否允许为空，默认是可选的，也就表示可以为空；
          boolean optional() default true;
          //关联关系被谁维护的一方对象里面的属性名字。 双向关联的时候必填
          String mappedBy() default "";
          //当被标识的字段发生删除或者置空操作之后，是否同步到关联关系的一方，即进行通过删除操作，默认flase，注意与CascadeType.REMOVE 级联删除的区别
          boolean orphanRemoval() default false;
      }
      ```

    * mappedBy 注意事项

      > 只有关联关系的维护方才能操作两个实体之间外键的关系。被维护方即使设置了维护方属性进行存储也不会更新外键关联。
      >
      > mappedBy 不能与 @JoinColumn 或者 @JoinTable 同时使用，因为没有意义，关联关系不在这里面维护。
      >
      > 此外，mappedBy 的值是指另一方的实体里面属性的字段，而不是数据库字段，也不是实体的对象的名字。也就是维护关联关系的一方属性字段名称，或者加了 @JoinColumn / @JoinTable 注解的属性字段名称。如上面的 User 例子 user 里面 mappedBy 的值，就是 UserInfo 里面的 user 字段的名字。

    * CascadeType用法

      * 在 CascadeType 的用法中，CascadeType 的枚举值只有五个，分别如下：

        * CascadeType.PERSIST 级联新建
        * CascadeType.REMOVE 级联删除
        * CascadeType.REFRESH 级联刷新
        * CascadeType.MERGE 级联更新
        * CascadeType.ALL 四项全选

      * 其中，默认是没有级联操作的，关系表不会产生任何影响。此外，JPA 2.0 还新增了 CascadeType.DETACH，即级联实体到 Detach 状态。

        ```java
        public class UserInfo {
            ...
           @OneToOne(cascade ={CascadeType.PERSIST,CascadeType.REMOVE})
           private User user;
        }
        ```

    * orphanRemoval 属性用法

      * orphanRemoval 表示当关联关系被删除的时候，是否应用级联删除，默认 false。

        ```
        public class UserInfo {
           @OneToOne(cascade = {CascadeType.PERSIST},orphanRemoval = true)
           private User user;
           ....其他没变的代码省了
        }
        ```

        

### 2. 进阶用法

### 3. 原理

### 4. 扩展



