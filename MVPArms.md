本项目主要用到`MVP`+`Dagger2`+`Retrofit`+`RxJava`


## Notice

* [MVPArms 学习项目](https://github.com/JessYanCoding/MVPArms/blob/master/CONTRIBUTING_APP.md)

* [意见收集](https://github.com/JessYanCoding/MVPArms/issues/40)

* [更新日志](https://github.com/JessYanCoding/MVPArms/wiki/UpdateLog)

* [常见 Issues](https://github.com/JessYanCoding/MVPArms/wiki/Issues)

* [我们为什么要把 Dagger2 , MVP 以及 RxJava 引入项目中?](http://www.jianshu.com/p/91c2bb8e6369)

* 看了上面的文章,对为什么使用这些技术应该比较了解了,使用这些技术对项目后期的维护和迭代特别是大型项目非常有帮助,但是在开发前期每写一个页面要多写很多  `MVP` , `Dagger2` 的类和接口,这对于开发前期确实比较头疼,现在本框架已经可以通过 [Template](https://github.com/JessYanCoding/MVPArmsTemplate) 自动生成一些 `MVP` , `Dagger2` 的模版代码,现在大家可以非常轻松的使用本框架.

* 使用此框架自带自动适配功能(可不使用)，请参考 [AutoLayout 使用方法](https://github.com/hongyangAndroid/AndroidAutoLayout).

* 作为通用框架,本框架不提供与 **UI** 有关的任何第三方库(除了 `AutoLayout` 屏幕适配方案).


## Feature

* **网络请求层**: 如今主流的网络请求框架有 `Volley` , `Okhttp` , `Retrofit` (`Android-async-http` 停止维护了) ,因为这个库是基于 `RxJava` , `Retrofit` 支持 `RxJava` ,默认使用 `Okhttp` 请求网络(`Okhttp` 使用 `Okio` , `Okio` 基于 IO 和 NIO 性能优于 `Volley` , `Volley` 内部封装有 Imageloader ,支持扩展 `Okhttp` ,封装比 `Okhttp` 好,但是比较适合频繁,数据量小的网络请求),所以此库默认使用 `Retrofit` 作为网络请求层.

* **图片加载**: 因为图片加载框架各有优缺点, `Fresco` , `Picasso` , `Glide` 这些都是现在比较主流得图片加载框架,所以为了扩展性本框架提供一个统一的管理类 Imageloader ,使用策略者模式,开发者只用实现接口,就可以动态替换图片框架,外部提供统一接口加载图片,替换图片加载框架毫无痛点,并且为了快速实现,默认提供一个 Glide 的默认实现类,有其它需求可以参照 [**Wiki**](https://github.com/JessYanCoding/MVPArms/wiki#3.4) 替换为别的图片加载框架.

* **Model层**: 优秀的数据库太多, `GreenDao` , `Realm` , `SqlBrite`（Square 公司出品，对 SQLiteOpenHelper 封装，提供响应式 Api 访问数据库）, `SqlDelight` , `Storio` , `DBFlow` ,每个框架的使用方法都不一样,本框架只提供有一个管理类 RepositoryManager 里面默认封装了 `RxCache` (此框架根据 `Retrofit` Api 实现了缓存逻辑,并提供响应式接口), `Retrofit` 等与数据相关的框架,以后有其他需求如需使用数据库,就可以直接封装进 RepositoryManager ,本框架通过 Dagger2 向 Model 层注入 RepositoryManager,来提供给开发者数据处理的能力,这样的好处的是上层（Activity/Fragment/Presenter）不需要知道数据源的细节（来自于网络、数据库、亦或是内存等等）,下层可以根据需求修改（缓存的实现细节）上下两层分离互不影响.

## Functionality & Libraries
1. [`Mvp`Google官方出品的`Mvp`架构项目，含有多个不同的架构分支(此为Dagger分支).](https://github.com/googlesamples/android-architecture/tree/todo-mvp-dagger/)
2. [`Dagger2`Google根据Square的Dagger1出品的依赖注入框架，通过Apt编译时生成代码，性能优于使用运行时反射技术的依赖注入框架.](https://github.com/google/dagger)
3. [`RxJava`提供优雅的响应式Api解决异步请求以及事件处理.](https://github.com/ReactiveX/RxJava)
4. [`RxAndroid`为Android提供响应式Api.](https://github.com/ReactiveX/RxAndroid)
5. [`Rxlifecycle`在Android上使用rxjava都知道的一个坑，就是生命周期的解除订阅，这个框架通过绑定activity和fragment的生命周期完美解决.](https://github.com/trello/RxLifecycle)
6. [`RxCache`是使用注解为Retrofit加入二级缓存(内存,磁盘)的缓存库.](https://github.com/VictorAlbertos/RxCache)
7. [`RxErroHandler` 是 `RxJava` 的错误处理库,可在出现错误后重试.](https://github.com/JessYanCoding/RxErrorHandler)
8. [`RxPermissions`用于处理Android运行时权限的响应式库.](https://github.com/tbruyelle/RxPermissions)
9. [`Retrofit`Square出品的网络请求库，极大的减少了http请求的代码和步骤.](https://github.com/square/retrofit)
10. [`Okhttp`同样Square出品，不多介绍，做Android都应该知道.](https://github.com/square/okhttp)
11. [`Autolayout`鸿洋大神的Android全尺寸适配框架.](https://github.com/hongyangAndroid/AndroidAutoLayout)
12. [`Gson`Google官方的Json Convert框架.](https://github.com/google/gson)
13. [`Butterknife`JakeWharton大神出品的view注入框架.](https://github.com/JakeWharton/butterknife)
14. [`Androideventbus`一个轻量级使用注解的Eventbus.](https://github.com/hehonghui/AndroidEventBus)
15. [`Timber`JakeWharton大神出品Log框架容器，内部代码极少，但是思想非常不错.](https://github.com/JakeWharton/timber)
16. [`Glide`此库为本框架默认封装图片加载库，可参照着例子更改为其他的库，Api和`Picasso`差不多,缓存机制比`Picasso`复杂,速度快，适合处理大型图片流，支持gfit，`Fresco`太大了！，在5.0以下优势很大，5.0以上系统默认使用的内存管理和`Fresco`类似.](https://github.com/bumptech/glide)
17. [`LeakCanary`Square出品的专门用来检测`Android`和`Java`的内存泄漏,通过通知栏提示内存泄漏信息.](https://github.com/square/leakcanary)
