# CSS

### 1. 层叠

* Stylesheets **cascade（样式表层叠）** — 简单的说，css规则的顺序很重要；当应用两条同级别的规则到一个元素的时候，写在后面的就是实际使用的规则。

* 优先级
  * 浏览器是根据优先级来决定当多个规则有不同选择器对应相同的元素的时候需要使用哪个规则。它基本上是一个衡量选择器具体选择哪些区域的尺度：
    * 一个元素选择器不是很具体 — 会选择页面上该类型的所有元素 — 所以它的优先级就会低一些。
    * 一个类选择器稍微具体点 — 它会选择该页面中有特定 `class` 属性值的元素 — 所以它的优先级就要高一点。
    * **优先级: 类选择器>元素选择器**
  
* 继承
  
* 继承也需要在上下文中去理解 —— 一些设置在父元素上的css属性是可以被子元素继承的，有些则不能。
  
* 控制继承
  * CSS 为控制继承提供了四个特殊的通用属性值。每个css属性都接收这些值。
    * **inherit**: 设置该属性会使子元素属性和父元素相同。实际上，就是 "开启继承".
    * **initial**: 设置属性值和浏览器默认样式相同。如果浏览器默认样式中未设置且该属性是自然继承的，那么会设置为 `inherit` 。
    * **unset**: 将属性重置为自然值，也就是如果属性是自然继承那么就是 `inherit`，否则和 `initial`一样
  * 重设所有属性值
    * CSS 的 shorthand 属性 `all` 可以用于同时将这些继承值中的一个应用于（几乎）所有属性。它的值可以是其中任意一个(`inherit`, `initial`, `unset`, or `revert`)。这是一种撤销对样式所做更改的简便方法，以便回到之前已知的起点。

* 理解层叠

  * 有三个因素需要考虑，根据重要性排序如下，前面的更重要:

    * **重要程度**

    * **优先级**

      * 一个元素选择器比类选择器的优先级更低会被其覆盖。本质上，不同类型的选择器有不同的分数值，把这些分数相加就得到特定选择器的权重，然后就可以进行匹配。

      * 一个选择器的优先级可以说是由四个部分相加 (分量)，可以认为是个十百千 — 四位数的四个位数：

        * **千位**： 如果声明在 `style` 的属性（内联样式）则该位得一分。这样的声明没有选择器，所以它得分总是1000。

        * **百位**： 选择器中包含ID选择器则该位得一分。

        * **十位**： 选择器中包含类选择器、属性选择器或者伪类则该位得一分。

        * **个位**：选择器中包含元素、伪元素选择器则该位得一分。

        * **通用选择器 (`*`)，组合符 (`+`, `>`, `~`, ' ')，和否定伪类 (`:not`) 不会影响优先级。**

          | 选择器                                  | 千位 | 百位 | 十位 | 个位 | 优先级 |
          | --------------------------------------- | ---- | ---- | ---- | ---- | ------ |
          | h1                                      | 0    | 0    | 0    | 1    | 0001   |
          | h1 + p::first-letter                    | 0    | 0    | 0    | 3    | 0003   |
          | li > a[href*="en-US"] > .inline-warning | 0    | 0    | 2    | 2    | 0022   |
          | #identifier                             | 0    | 1    | 0    | 0    | 0100   |
          | 内联样式                                | 1    | 0    | 0    | 0    | 1000   |
    
       * **!important**: 有一个特殊的 CSS 可以用来覆盖所有上面所有优先级计算，不过需要很小心的使用 — `!important`。用于修改特定属性的值， 能够覆盖普通规则的层叠。

          * 覆盖 `!important` 唯一的办法就是另一个 `!important` 具有 相同*优先级* 而且顺序靠后，或者更高优先级。
    
    * **资源顺序**: 如果你有超过一条规则，而且都是相同的权重，那么最后面的规则会应用。可以理解为后面的规则覆盖前面的规则，直到最后一个开始设置样式。
    
  * CSS位置的影响

    * 相互冲突的声明将按以下顺序适用，后一种声明将覆盖前一种声明：
      * 用户代理样式表中的声明(例如，浏览器的默认样式，在没有设置其他样式时使用)。
      * 用户样式表中的常规声明(由用户设置的自定义样式)。
      * 作者样式表中的常规声明(这些是我们web开发人员设置的样式)。
      * 作者样式表中的`!important`声明
      * 用户样式表中的`!important` 声明

### 2.选择器

  * **类型** :**类型选择器**有时也叫做“标签名选择器*”*或者是”元素选择器“，因为它在文档中选择了一个HTML标签/元素的缘故。

    ```css
    h1 { }
    ```

* **全局选择器**: 全局选择器，是由一个星号（`*`）代指的，它选中了文档中的所有内容（或者是父元素中的所有内容，比如，它紧随在其他元素以及邻代运算符之后的时候）;

  ```css
  * {
      margin: 0;
  }
  article :first-child {
  
  }
  article *:first-child {
  
  } 
  ```

  * **类**:类选择器以一个句点（`.`）开头，会选择文档中应用了这个类的所有物件.

    ```css
    .box { }
    ```

    * 指定特定元素的类 

      * 建立一个指向应用一个类的特定元素
      * 通过使用附加了类的欲选元素的选择器做到这点，其间没有空格。

      ```css
      span.highlight {
          background-color: yellow;
      }
      
      h1.highlight {
          background-color: pink;
      }
      ```

    * 多个类被应用的时候指向一个元素

      ```css
      .notebox {
        border: 4px solid #666;
        padding: .5em;
      }
      
      .notebox.warning {
        border-color: orange;
        font-weight: bold;
      }
      
      .notebox.danger {
        border-color: red;
        font-weight: bold;
      }
      <div class="notebox">
          This is an informational note.
      </div>
      
      <div class="notebox warning">
          This note shows a warning.
      </div>
      
      <div class="notebox danger">
          This note shows danger!
      </div>
      
      <div class="danger">
          This won't get styled — it also needs to have the notebox class
      </div>
      ```

  * **ID选择器**: ID选择器开头为`#`而非句点，不过基本上和类选择器是同种用法

    ```css
    #one {
        background-color: yellow;
    }
    
    h1#heading {
        color: rebeccapurple;
    }
        
    ```

  * **标签属性选择器**

    * 这组选择器根据一个元素上的某个标签的属性的存在以选择元素的不同方式：

    ```css
    a[title] { }
    ```

    * 或者根据一个有特定值的标签属性是否存在来选择：

    ```css
    a[href="https://example.com"] { }
    ```

    * **存否和值选择器**: 这些选择器允许基于一个元素自身是否存在（例如`href`）或者基于各式不同的按属性值的匹配，来选取元素。

    | 选择器             | 示例                          | 描述                                                         |
    | ------------------ | ----------------------------- | ------------------------------------------------------------ |
    | [*attr*]           | a[title]                      | 匹配带有一个名为*attr*的属性的元素——方括号里的值。           |
    | [*attr*=*value*]   | a[href="https://example.com"] | 匹配带有一个名为*attr*的属性的元素，其值正为*value*——引号中的字符串。 |
    | [*attr*~=*value*]  | `p[class~="special"]`         | 匹配带有一个名为*attr*的属性的元素 ，其值正为*value*，或者匹配带有一个*attr*属性的元素，其值有一个或者更多，至少有一个和*value*匹配。注意，在一列中的好几个值，是用空格隔开的。 |
    | [*attr*\|=*value*] | div[lang\|="zh"]              | 匹配带有一个名为*attr*的属性的元素，其值可正为*value*，或者开始为*value*，后面紧随着一个连字符。 |

    * **字符串匹配选择器**:这些选择器让更高级的属性的值的子字符串的匹配变得可行。

    | 选择器            | 示例              | 描述                                                         |
    | ----------------- | ----------------- | ------------------------------------------------------------ |
    | [*attr*^=*value*] | li[class^="box-"] | 匹配带有一个名为*attr*的属性的元素，其值开头为*value*子字符串。 |
    | [*attr*$=*value*] | li[class$="-box"] | 匹配带有一个名为*attr*的属性的元素，其值结尾为*value*子字符串 |
    | [*attr**=*value*] | li[class*="box"]  | 匹配带有一个名为*attr*的属性的元素，其值的字符串中的任何地方，至少出现了一次*value*子字符串。 |

    * 大小写敏感

      * 如果你想在大小写不敏感的情况下，匹配属性值的话，你可以在闭合括号之前，使用`i`值。这个标记告诉浏览器，要以大小写不敏感的方式匹配ASCII字符。没有了这个标记的话，值会按照文档语言对大小写的处理方式，进行匹配——HTML中是大小写敏感的。

      ```css
      li[class^="a"] {
          background-color: yellow;
      }
      
      li[class^="a" i] {
          color: red;
      }
      ```

      * 此外还有一个更加新的`s`值，它会强制在上下文的匹配正常为大小写不敏感的时候，强行要求匹配时大小写敏感。不过，在浏览器中它不太受支持，而且在上下文为HTML时也没啥用

  * **伪类和伪元素**

    * 这组选择器包含了伪类，用来样式化一个元素的特定状态。例如`:hover`伪类会在鼠标指针悬浮到一个元素上的时候选择这个元素：

    ```css
    a:hover { }
    ```

    * 它还可以包含了伪元素，选择一个元素的某个部分而不是元素自己。例如，`::first-line`是会选择一个元素（下面的情况中是`<p>`）中的第一行，类似`<span>`包在了第一个被格式化的行外面，然后选择这个`<span>`。

    ```css
    p::first-line { }
    ```

    * 伪类是选择器的一种，它用于选择处于特定状态的元素，比如当它们是这一类型的第一个元素时，或者是当鼠标指针悬浮在元素上面的时候。

    ```css
    伪类就是开头为冒号的关键字：
    :pseudo-class-name
    
    :first-child 
    :last-child 代表父元素的最后一个子元素。
    :only-child  匹配没有任何兄弟元素的元素.等效的选择器还可以写成 :first-child:last-child或者:nth-child(1):nth-last-child(1),当然,前者的权重会低一点
    :invalid 表示任意内容未通过验证的 <input> 或其他 <form> 元素 .
    ```

    * 用户行为伪类: 一些伪类只会在用户以某种方式和文档交互的时候应用。这些**用户行为伪类**，有时叫做**动态伪类**，表现得就像是一个类在用户和元素交互的时候加到了元素上一样.

    ```css
    :hover——上面提到过，只会在用户将指针挪到元素上的时候才会激活，一般就是链接元素。
    :focus——只会在用户使用键盘控制，选定元素的时候激活。
    ```

    * 伪元素: 伪元素以类似方式表现，不过表现得是像你往标记文本中加入全新的HTML元素一样，而不是向现有的元素上应用类。伪元素开头为双冒号`::`。

    ```css
    ::pseudo-element-name
    article p::first-line {
        font-size: 140%;
        font-weight: bold;
    } 
    ```

    * 伪类和伪元素组合起来

    ```css
    article p:first-child::first-line {
      font-size: 120%;
      font-weight: bold;
    }
    ```

    * 生成带有::before和::after的内容 https://cssarrowplease.com/

    ```css
    .box::before {
        content: "This should show before the other content."
    }   
    <p class="box">Content in the box in my HTML page.</p>
    ==>This should show before the other content.Content in the box in my HTML page.   
    
    ::before和::after伪元素与content属性的共同使用
    ```

    * 伪类

    | 选择器                                                       | 描述                                                         |
    | :----------------------------------------------------------- | :----------------------------------------------------------- |
    | [`:active`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:active) | 在用户激活（例如点击）元素的时候匹配。                       |
    | [`:any-link`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:any-link) | 匹配一个链接的`:link`和`:visited`状态。                      |
    | [`:blank`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:blank) | 匹配空输入值的[``元素](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input)。 |
    | [`:checked`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:checked) | 匹配处于选中状态的单选或者复选框。                           |
    | [`:current`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:current) | 匹配正在展示的元素，或者其上级元素。                         |
    | [`:default`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:default) | 匹配一组相似的元素中默认的一个或者更多的UI元素。             |
    | [`:dir`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:dir) | 基于其方向性（HTML`dir`属性或者CSS`direction`属性的值）匹配一个元素。 |
    | [`:disabled`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:disabled) | 匹配处于关闭状态的用户界面元素                               |
    | [`:empty`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:empty) | 匹配除了可能存在的空格外，没有子元素的元素。                 |
    | [`:enabled`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:enabled) | 匹配处于开启状态的用户界面元素。                             |
    | [`:first`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:first) | 匹配[分页媒体](https://developer.mozilla.org/en-US/docs/Web/CSS/Paged_Media)的第一页。 |
    | [`:first-child`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:first-child) | 匹配兄弟元素中的第一个元素。                                 |
    | [`:first-of-type`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:first-of-type) | 匹配兄弟元素中第一个某种类型的元素。                         |
    | [`:focus`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:focus) | 当一个元素有焦点的时候匹配。                                 |
    | [`:focus-visible`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:focus-visible) | 当元素有焦点，且焦点对用户可见的时候匹配。                   |
    | [`:focus-within`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:focus-within) | 匹配有焦点的元素，以及子代元素有焦点的元素。                 |
    | [`:future`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:future) | 匹配当前元素之后的元素。                                     |
    | [`:hover`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:hover) | 当用户悬浮到一个元素之上的时候匹配。                         |
    | [`:indeterminate`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:indeterminate) | 匹配未定态值的UI元素，通常为[复选框](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input/checkbox)。 |
    | [`:in-range`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:in-range) | 用一个区间匹配元素，当值处于区间之内时匹配。                 |
    | [`:invalid`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:invalid) | 匹配诸如`<input>`的位于不可用状态的元素。                    |
    | [`:lang`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:lang) | 基于语言（HTML[lang](https://developer.mozilla.org/zh-CN/docs/Web/HTML/Global_attributes/lang)属性的值）匹配元素。 |
    | [`:last-child`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:last-child) | 匹配兄弟元素中最末的那个元素。                               |
    | [`:last-of-type`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:last-of-type) | 匹配兄弟元素中最后一个某种类型的元素。                       |
    | [`:left`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:left) | 在[分页媒体](https://developer.mozilla.org/zh-CN/docs/Web/CSS/CSS_Pages)中，匹配左手边的页。 |
    | [`:link`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:link) | 匹配未曾访问的链接。                                         |
    | [`:local-link`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:local-link) | 匹配指向和当前文档同一网站页面的链接。                       |
    | [`:is()`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:is) | 匹配传入的选择器列表中的任何选择器。                         |
    | [`:not`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:not) | 匹配作为值传入自身的选择器未匹配的物件。                     |
    | [`:nth-child`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:nth-child) | 匹配一列兄弟元素中的元素——兄弟元素按照an+b形式的式子进行匹配（比如2n+1匹配元素1、3、5、7等。即所有的奇数个）。 |
    | [`:nth-of-type`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:nth-of-type) | 匹配某种类型的一列兄弟元素（比如，`<p>`元素）——兄弟元素按照an+b形式的式子进行匹配（比如2n+1匹配元素1、3、5、7等。即所有的奇数个）。 |
    | [`:nth-last-child`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:nth-last-child) | 匹配一列兄弟元素，从后往前倒数。兄弟元素按照an+b形式的式子进行匹配（比如2n+1匹配按照顺序来的最后一个元素，然后往前两个，再往前两个，诸如此类。从后往前数的所有奇数个）。 |
    | [`:nth-last-of-type`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:nth-last-of-type) | 匹配某种类型的一列兄弟元素（比如，`<p>`元素），从后往前倒数。兄弟元素按照an+b形式的式子进行匹配（比如2n+1匹配按照顺序来的最后一个元素，然后往前两个，再往前两个，诸如此类。从后往前数的所有奇数个）。 |
    | [`:only-child`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:only-child) | 匹配没有兄弟元素的元素。                                     |
    | [`:only-of-type`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:only-of-type) | 匹配兄弟元素中某类型仅有的元素。                             |
    | [`:optional`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:optional) | 匹配不是必填的form元素。                                     |
    | [`:out-of-range`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:out-of-range) | 按区间匹配元素，当值不在区间内的的时候匹配。                 |
    | [`:past`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:past) | 匹配当前元素之前的元素。                                     |
    | [`:placeholder-shown`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:placeholder-shown) | 匹配显示占位文字的input元素。                                |
    | [`:playing`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:playing) | 匹配代表音频、视频或者相似的能“播放”或者“暂停”的资源的，且正在“播放”的元素。 |
    | [`:paused`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:paused) | 匹配代表音频、视频或者相似的能“播放”或者“暂停”的资源的，且正在“暂停”的元素。 |
    | [`:read-only`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:read-only) | 匹配用户不可更改的元素。                                     |
    | [`:read-write`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:read-write) | 匹配用户可更改的元素。                                       |
    | [`:required`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:required) | 匹配必填的form元素。                                         |
    | [`:right`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:right) | 在[分页媒体](https://developer.mozilla.org/zh-CN/docs/Web/CSS/CSS_Pages)中，匹配右手边的页。 |
    | [`:root`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:root) | 匹配文档的根元素。                                           |
    | [`:scope`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:scope) | 匹配任何为参考点元素的的元素。                               |
    | [`:valid`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:valid) | 匹配诸如`<input>`元素的处于可用状态的元素。                  |
    | [`:target`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:target) | 匹配当前URL目标的元素（例如如果它有一个匹配当前[URL分段](https://en.wikipedia.org/wiki/Fragment_identifier)的元素）。 |
    | [`:visited`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/:visited) | 匹配已访问链接。                                             |

    * 伪元素

    | 选择器                                                       | 描述                                                 |
    | :----------------------------------------------------------- | :--------------------------------------------------- |
    | [`::after`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/::after) | 匹配出现在原有元素的实际内容之后的一个可样式化元素。 |
    | [`::before`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/::before) | 匹配出现在原有元素的实际内容之前的一个可样式化元素。 |
    | [`::first-letter`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/::first-letter) | 匹配元素的第一个字母。                               |
    | [`::first-line`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/::first-line) | 匹配包含此伪元素的元素的第一行。                     |
    | [`::grammar-error`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/::grammar-error) | 匹配文档中包含了浏览器标记的语法错误的那部分。       |
    | [`::selection`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/::selection) | 匹配文档中被选择的那部分。                           |
    | [`::spelling-error`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/::spelling-error) | 匹配文档中包含了浏览器标记的拼写错误的那部分。       |

  * **后代选择器**

    * 后代选择器——典型用单个空格（` `）字符——组合两个选择器，比如，第二个选择器匹配的元素被选择，如果他们有一个祖先（父亲，父亲的父亲，父亲的父亲的父亲，等等）元素匹配第一个选择器。选择器利用后代组合符被称作后代选择器。

    ```css
    body article p
    ```

  * **子代关系选择器**

    * 子代关系选择器是个大于号（`>`），只会在选择器选中直接子元素的时候匹配。继承关系上更远的后代则不会匹配.

    ```css
    article > p
    ```

  * **邻接兄弟**

    * 邻接兄弟选择器（`+`）用来选中恰好处于另一个在继承关系上同级的元素旁边的物件。例如，选中所有紧随`<p>`元素之后的`<img>`元素：

    ```css
    p + img
    ```

    * 常见的使用场景是， 改变紧跟着一个标题的段的某些表现方面，就像是我下面的示例那样。

  * **通用兄弟**

    * 如果你想选中一个元素的兄弟元素，即使它们不直接相邻，你还是可以使用通用兄弟关系选择器（`~`）。要选中所有的`<p>`元素后*任何地方*的`<img>`元素，我们会这样做：

    ```css
    p ~ img
    ```

  *  **使用关系选择器**

    * 你能用关系选择器，将任何在我们前面的学习过程中学到的选择器组合起来，选出你的文档中的一部分。例如如果我们想选中为`<ul>`的直接子元素的带有“a”类的列表项的话，我可以用下面的代码

    ```
    ul > li[class="a"]  {  }
    ```

    

  * **运算符**

    * 最后一组选择器可以将其他选择器组合起来，更复杂的选择元素。下面的示例用运算符（`>`）选择了`<article>`元素的初代子元素。

    ```css
    article > p { }
    ```

    | 选择器         | 示例              |
    | -------------- | ----------------- |
    | 类型选择器     | h1 { }            |
    | 通配选择器     | * { }             |
    | 类选择器       | .box { }          |
    | ID选择器       | \#unique { }      |
    | 标签属性选择器 | a[title] { }      |
    | 伪类选择器     | p:first-child { } |
    | 伪元素选择器   | p::first-line { } |
    | 后代选择器     | article p         |
    | 子代选择器     | article > p       |
    | 相邻兄弟选择器 | h1 + p            |
    | 通用兄弟选择器 | h1 ~ p            |
    
### 3.CSS盒子模型

* 块级盒子(block box)和内联盒子(inline box),这两种盒子会在**页面流**（page flow）和**元素之间的关系**方面表现出不同的行为

* **块级盒子(block box)**
  * 一个被定义成块级的（block）盒子会表现出以下行为:
    * 盒子会在内联的方向上扩展并占据父容器在该方向上的所有可用空间，在绝大数情况下意味着盒子会和父容器一样宽
    * 每个盒子都会换行
    * width和height属性发挥作用
    * 内边距（padding）, 外边距（margin） 和 边框（border） 会将其他元素从当前盒子周围“推开”
    * 除非特殊指定，诸如标题(`<h1>`等)和段落(`<p>`)默认情况下都是块级的盒子。
  
* **内联盒子(inline box)**
  * 如果一个盒子对外显示为 `inline`，那么他的行为如下:
    * 盒子不会产生换行。
    * width和height属性将不起作用。
    * 垂直方向的内边距、外边距以及边框会被应用但是不会把其他处于 `inline` 状态的盒子推开。
    * 水平方向的内边距、外边距以及边框会被应用且会把其他处于 `inline` 状态的盒子推开。
    * 用做链接的 `<a>` 元素、 `<span>`、 `<em>` 以及 `<strong>` 都是默认处于 `inline` 状态的。
    * 我们通过对盒子`display` 属性的设置，比如 `inline` 或者 `block` ，来控制盒子的外部显示类型。

* **display**
  * **`display: inline`**
  * **`display: block`**
  * **`display: inline-flex`**
  * **`display: flex`**
* **`display: inline-block`**
    * isplay有一个特殊的值，它在内联和块之间提供了一个中间状态。这对于以下情况非常有用:您不希望一个项切换到新行，但希望它可以设定宽度和高度，并避免上面看到的重叠。
    * 一个元素使用 `display: inline-block`，实现我们需要的块级的部分效果：
      - 设置`width` 和`height` 属性会生效。
      - `padding`, `margin`, 以及`border` 会推开其他元素。
  
* **CSS盒模型**

  * 完整的 CSS 盒模型应用于块级盒子，内联盒子只使用盒模型中定义的部分内容。模型定义了盒的每个部分 —— margin, border, padding, and content —— 合在一起就可以创建我们在页面上看到的内容。为了增加一些额外的复杂性，有一个标准的和替代（IE）的盒模型。

  * CSS中组成一个块级盒子需要:

    * **Content box**: 这个区域是用来显示内容，大小可以通过设置 [`width`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/width) 和 [`height`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/height).

    * **Padding box**: 包围在内容区域外部的空白区域； 大小通过 [`padding`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/padding) 相关属性设置。

    * **Border box**: 边框盒包裹内容和内边距。大小通过 [`border`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/border) 相关属性设置。

    * **Margin box**: 这是最外面的区域，是盒子和其他元素之间的空白区域。大小通过 [`margin`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/margin) 相关属性设置。

      ![Diagram of the box model](https://mdn.mozillademos.org/files/16558/box-model.png)

* 标准盒模型

  ```css
  .box {
    width: 350px;
    height: 150px;
    margin: 25px;
    padding: 25px;
    border: 5px solid black;
  }
  如果使用标准模型宽度 = 410px (350 + 25 + 25 + 5 + 5)，高度 = 210px (150 + 25 + 25 + 5 + 5)，padding 加 border 再加 content box。
  ```

* 替代盒模型

  ```css
  .box {
    box-sizing: border-box;
  } 
  ```

* 外边距

  * 外边距是盒子周围一圈看不到的空间。它会把其他元素从盒子旁边推开。 外边距属性值可以为正也可以为负。设置负值会导致和其他内容重叠。无论使用标准模型还是替代模型，外边距总是在计算可见部分后额外添加。
  * [`margin-top`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/margin-top)
  * [`margin-right`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/margin-right)
  * [`margin-bottom`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/margin-bottom)
  * [`margin-left`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/margin-left)
  * 外边距折叠: 理解外边距的一个关键是外边距折叠的概念。如果你有两个外边距相接的元素，这些外边距将合并为一个外边距，即最大的单个外边距的大小。

* 边框 边框是在边距和填充框之间绘制的

  * 可以使用[`border`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/border)属性一次设置所有四个边框的宽度、颜色和样式。

  * 分别设置每边的宽度、颜色和样式，可以使用：
    - [`border-top`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/border-top)
    - [`border-right`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/border-right)
    - [`border-bottom`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/border-bottom)
    - [`border-left`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/border-left)
    
   *  设置所有边的颜色、样式或宽度，请使用以下属性：
      - [`border-width`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/border-width)
      - [`border-style`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/border-style)
      - [`border-color`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/border-color)
      
  * 设置单边的颜色、样式或宽度，可以使用最细粒度的普通属性之一：

    * [`border-top-width`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/border-top-width)
    * [`border-top-style`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/border-top-style)
    * [`border-top-color`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/border-top-color)
    * [`border-right-width`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/border-right-width)
    * [`border-right-style`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/border-right-style)
    * [`border-right-color`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/border-right-color)
    * [`border-bottom-width`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/border-bottom-width)
    * [`border-bottom-style`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/border-bottom-style)
    * [`border-bottom-color`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/border-bottom-color)
    * [`border-left-width`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/border-left-width)
    * [`border-left-style`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/border-left-style)
    * [`border-left-color`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/border-left-color)

* 内边距

  * 内边距位于边框和内容区域之间。与外边距不同，您不能有负数量的内边距，所以值必须是0或正的值。应用于元素的任何背景都将显示在内边距后面，内边距通常用于将内容推离边框。

  * 我们可以使用[`padding`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/padding)简写属性控制元素所有边，或者每边单独使用等价的普通属性：

    * [`padding-top`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/padding-top)
  * [`padding-right`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/padding-right)
    * [`padding-bottom`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/padding-bottom)
    * [`padding-left`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/padding-left)
  
### 4. 背景样式

* 背景颜色`background-color`

  * [`background-color`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/background-color)属性定义了CSS中任何元素的背景颜色。属性接受任何有效的`<color>值`。背景色扩展到元素的内容和内边距的下面。

* 背景图片 `background-image`
  
* [`background-image`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/background-image)属性允许在元素的背景中显示图像。在下面的例子中，我们有两个方框——一个是比方框大的背景图像，另一个是星星的小图像。
  
* 控制背景平铺

  * [`background-repeat`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/background-repeat)属性用于控制图像的平铺行为。可用的值是:
    - `no-repeat` — 不重复。
    - `repeat-x` —水平重复。
    - `repeat-y` —垂直重复。
    - `repeat` — 在两个方向重复。

* 调整背景图像大小

  * 使用 [`background-size`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/background-size)属性，它可以设置长度或百分比值，来调整图像的大小以适应背景。
  * 也可以使用关键字:
    - `cover` —浏览器将使图像足够大，使它完全覆盖了盒子区，同时仍然保持其高宽比。在这种情况下，有些图像可能会跳出盒子外
    - `contain` — 浏览器将使图像的大小适合盒子内。在这种情况下，如果图像的长宽比与盒子的长宽比不同，则可能在图像的任何一边或顶部和底部出现间隙。

* 背景图像定位

  * [`background-position`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/background-position)属性允许您选择背景图像显示在其应用到的盒子中的位置。它使用的坐标系中，框的左上角是(0,0)，框沿着水平(x)和垂直(y)轴定位。
  * **注意：**默认的背景位置值是(0,0)。
  * 最常见的背景位置值有两个单独的值——一个水平值后面跟着一个垂直值。
  * **注意：**`background-position`是[`background-position-x`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/background-position-x)和[`background-position-y`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/background-position-y)的简写，它们允许您分别设置不同的坐标轴的值。

  ```css
  .box {
    background-image: url(star.png);
    background-repeat: no-repeat;
    background-position: top center;
    //  background-position: 20px 10%;
    //  background-position: top 20px;  
    //  background-position: top 20px right 10px;
  } 
  ```

* 渐变背景

  ```css
  .a {
    background-image: linear-gradient(105deg, rgba(0,249,255,1) 3%, rgba(51,56,57,1) 50%);
  }
  
  .b {
    background-image: radial-gradient(circle, rgba(0,249,255,1) 39%, rgba(51,56,57,1) 96%);
    background-size: 100px 50px;
  }
  ```

* 多个背景图像

  * 在单个属性值中指定多个`background-image`值，用逗号分隔每个值。

  * 背景图像会以互相重叠而告终。背景将与最后列出的背景图像层在堆栈的底部，背景图像在代码列表中最先出现的在顶端。

  * **注意：**渐变可以与常规的背景图像很好地混合在一起。

  * 不同属性的每个值，将与其他属性中相同位置的值匹配。

    ```css
    background-image: url(image1.png), url(image2.png), url(image3.png), url(image1.png);
    background-repeat: no-repeat, repeat-x, repeat;
    background-position: 10px 20px,  top right;
    ```

* 背景附加

  * 另一个可供选择的背景是指定他们如何滚动时，内容滚动。这是由[`background-attachment`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/background-attachment)属性控制的，它可以接受以下值:
    * `scroll`: 使元素的背景在页面滚动时滚动。如果滚动了元素内容，则背景不会移动。实际上，背景被固定在页面的相同位置，所以它会随着页面的滚动而滚动。
    * `fixed`: 使元素的背景固定在视图端口上，这样当页面或元素内容滚动时，它就不会滚动。它将始终保持在屏幕上相同的位置。
    * `local`: 这个值是后来添加的(它只在Internet Explorer 9+中受支持，而其他的在IE4+中受支持)，因为滚动值相当混乱，在很多情况下并不能真正实现您想要的功能。局部值将背景固定在设置的元素上，因此当您滚动元素时，背景也随之滚动。
  * [`background-attachment`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/background-attachment)属性只有在有内容要滚动时才会有效果

### 5. 书写模式

* CSS中的书写模式是指文本的排列方向是横向还是纵向的。[`writing-mode`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/writing-mode) 属性使我们从一种模式切换到另一种模式
* `writing-mode`的三个值分别是：
  - `horizontal-tb`: 块流向从上至下。对应的文本方向是横向的。
  - `vertical-rl`: 块流向从右向左。对应的文本方向是纵向的。
  - `vertical-lr`: 块流向从左向右。对应的文本方向是纵向的。

### 6. 溢出

* overflow属性
  * [`overflow`](https://developer.mozilla.org/zh-CN/docs/Web/CSS/overflow)属性是你控制一个元素溢出的方式，它告诉浏览器你想怎样处理溢出。`overflow`的默认值为`visible`，这就是我们的内容溢出的时候，我们在默认情况下看到它们的原因。
    * `overflow: hidden` 隐藏
    * `overflow: visible` 显示
    * `overflow: scroll `滚动
      * `overflow-y: scroll`
      * `overflow-x: scroll`
    * `overflow: auto`
  * **注意：** 你可以用`overflow`属性指定x轴和y轴方向的滚动，同时使用两个值进行传递。如果指定了两个关键字，第一个对`overflow-x`生效而第二个对`overflow-y`生效。否则，`overflow-x`和`overflow-y`将会被设置成同样的值。例如，`overflow: scroll hidden`会把`overflow-x`设置成`scroll`，而`overflow-y`则为`hidden`。

### 7.  css的值

* 数字，长度和百分比

  | 数值类型       | 描述                                                         |
  | :------------- | :----------------------------------------------------------- |
  | `<integer>`    | `<integer>`是一个整数，比如1024或-55。                       |
  | `<number>`     | `<number>`表示一个小数——它可能有小数点后面的部分，也可能没有，例如0.255、128或-1.2。 |
  | `<dimension>`  | `<dimension>`是一个`<number>`，它有一个附加的单位，例如45deg、5s或10px。`<dimension>`是一个伞形类别，包括`<length>`、`<angle>`、`<time>`和`<resolution>`类型。 |
  | `<percentage>` | `<percentage>`表示一些其他值的一部分，例如50%。百分比值总是相对于另一个量，例如，一个元素的长度相对于其父元素的长度。 |

* 长度

  * 绝对长度单位

    * 以下都是**绝对**长度单位——它们与其他任何东西都没有关系，通常被认为总是相同的大小。

    | 单位 | 名称         | 等价换算            |
    | :--- | :----------- | :------------------ |
    | `cm` | 厘米         | 1cm = 96px/2.54     |
    | `mm` | 毫米         | 1mm = 1/10th of 1cm |
    | `Q`  | 四分之一毫米 | 1Q = 1/40th of 1cm  |
    | `in` | 英寸         | 1in = 2.54cm = 96px |
    | `pc` | 十二点活字   | 1pc = 1/16th of 1in |
    | `pt` | 点           | 1pt = 1/72th of 1in |
    | `px` | 像素         | 1px = 1/96th of 1in |

  * 相对长度单位

    | 单位   | 相对于                                                       |
    | :----- | :----------------------------------------------------------- |
    | `em`   | 在 font-size 中使用是相对于父元素的字体大小，在其他属性中使用是相对于自身的字体大小，如 width |
    | `ex`   | 字符“x”的高度                                                |
    | `ch`   | 数字“0”的宽度                                                |
    | `rem`  | 根元素的字体大小                                             |
    | `lh`   | 元素的line-height                                            |
    | `vw`   | 视窗宽度的1%                                                 |
    | `vh`   | 视窗高度的1%                                                 |
    | `vmin` | 视窗较小尺寸的1%                                             |
    | `vmax` | 视图大尺寸的1%                                               |

* 颜色

  * 关键字

    ```css
    color: white
    ```

  * 十六进制RGB

    ```css
    color: #02798b
    ```

  * RGB和RGBA

    ```css
    background-color: rgb(2, 121, 139);
    background-color: rgba(2, 121, 139, .3);
    ```

  * HSL和HSLA

    * `hsl()` 函数接受色调、饱和度和亮度值作为参数，而不是红色、绿色和蓝色值，这些值的不同方式组合，可以区分1670万种颜色：
      - **色调**： 颜色的底色。这个值在0和360之间，表示色轮周围的角度。
      - **饱和度**： 颜色有多饱和？ 它的值为0 - 100%，其中0为无颜色(它将显示为灰色阴影)，100%为全色饱和度
      - **亮度**：颜色有多亮？ 它从0 - 100%中获取一个值，其中0表示没有光(它将完全显示为黑色)，100%表示完全亮(它将完全显示为白色)

      ```css
        background-color: hsl(188, 97%, 28%);
        background-color: hsla(188, 97%, 28%, .3);
      ```
  
* 图片

  * `<image>` 数据类型用于图像为有效值的任何地方
  
  ```css
  .image {
    background-image: url(star.png);
  }
  
  .gradient {
    background-image: linear-gradient(90deg, rgba(119,0,255,1) 39%, rgba(0,212,255,1) 100%);
  }
  ```

* 位置
  * `<position>` 数据类型表示一组2D坐标，用于定位一个元素，如背景图像(通过 `background-position`)。它可以使用关键字(如 `top`, `left`, `bottom`, `right`, 以及`center` )将元素与2D框的特定边界对齐，以及表示框的顶部和左侧边缘偏移量的长度。

​    

​    

​    

​    

​    

​    

​    