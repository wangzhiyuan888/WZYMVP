# MVPArms 
[ ![Bintray](https://img.shields.io/badge/bintray-v2.3.1-brightgreen.svg) ](https://bintray.com/jessyancoding/maven/MVPArms/2.3.1/link)
[ ![Build Status](https://travis-ci.org/JessYanCoding/MVPArms.svg?branch=master) ](https://travis-ci.org/JessYanCoding/MVPArms)
[ ![API](https://img.shields.io/badge/API-15%2B-blue.svg?style=flat-square) ](https://developer.android.com/about/versions/android-4.0.3.html)
[ ![License](http://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square) ](http://www.apache.org/licenses/LICENSE-2.0)
[ ![QQGroup](https://img.shields.io/badge/QQ群-301733278-ff69b4.svg) ](https://shang.qq.com/wpa/qunwpa?idkey=1a5dc5e9b2e40a780522f46877ba243eeb64405d42398643d544d3eec6624917)

## A common Architecture for Android Applications developing based on MVP，integrates many Open Source Projects( like Dagger2,RxJava,Retrofit... ),to make your developing quicker and easier.

[中文说明](MVPArms.md)

## Architectural
<img src="https://github.com/JessYanCoding/MVPArms/raw/master/image/Architecture.png" width="80%" height="80%">

## Usage
> New Project
>> If you are building a new project, directly to the entire project **clone** (or download), as **app** as the main **Module** (It is recommended to remove the **arms Module** and use **Gradle** to [depend](https://github.com/JessYanCoding/MVPArms/wiki#1.1) on this framework remotely for easy updates), then the package name into their own package name , **app Module** contains the package structure can be used directly, a mainstream `MVP` +` Dagger2` + `Retrofit` +` RxJava` framework so easy to build successful, and now you refer **Mvp** Package under the **UserActivity** format,[Use Template to automatically generate MVP, Dagger2 related classes](https://github.com/JessYanCoding/MVPArmsTemplate),With access to [Wiki documents](https://github.com/JessYanCoding/MVPArms/wiki) slowly grasp the framework to see more articles as soon as possible in the project to use it, in practice, learning is the fastest

> Old Project
>> [Old projects would like to introduce this framework, you can refer to the Wiki documentation, written in great detail](https://github.com/JessYanCoding/MVPArms/wiki)

## Wiki
[Detailed usage reference Wiki (**Must see!!!**)](https://github.com/JessYanCoding/MVPArms/wiki)


## Notice

* [MVPArms Learning Project](https://github.com/JessYanCoding/MVPArms/blob/master/CONTRIBUTING_APP.md)

* [Collection Box](https://github.com/JessYanCoding/MVPArms/issues/40)

* [Update Log](https://github.com/JessYanCoding/MVPArms/wiki/UpdateLog)

* [Common Issues](https://github.com/JessYanCoding/MVPArms/wiki/Issues)

* The use of these technologies for the latter part of the project maintenance and iterative, especially large projects is very helpful, but is to develop a pre-write a page to write a lot of `MVP`,` Dagger2` class and interface, which is indeed a headache for the development of pre- Now the framework has been able to [Template](https://github.com/JessYanCoding/MVPArmsTemplate) automatically generate some `MVP`,` Dagger2` template code, and now we can very easily use the framework.

* Use this frame comes with automatic adaptation function, please refer to [AndroidAutoLayout](https://github.com/hongyangAndroid/AndroidAutoLayout).

* This framework does not provide any third-party libraries associated with the **UI**(except for the [`AndroidAutoLayout`](https://github.com/hongyangAndroid/AndroidAutoLayout) screen adaptation scheme).


## Functionality & Libraries
1. [`Mvp` Google's official` Mvp` architecture project, which contains several different schema branches (this is the Dagger branch).](https://github.com/googlesamples/android-architecture/tree/todo-mvp-dagger/)
2. [`Dagger2`](https://github.com/google/dagger)
3. [`RxJava`](https://github.com/ReactiveX/RxJava)
4. [`RxAndroid`](https://github.com/ReactiveX/RxAndroid)
5. [`Rxlifecycle`](https://github.com/trello/RxLifecycle)
6. [`RxCache`](https://github.com/VictorAlbertos/RxCache)
7. [`RxPermissions`](https://github.com/tbruyelle/RxPermissions)
8. [`RxErroHandler`](https://github.com/JessYanCoding/RxErrorHandler)
9. [`Retrofit`](https://github.com/square/retrofit)
10. [`Okhttp`](https://github.com/square/okhttp)
11. [`Autolayout`](https://github.com/hongyangAndroid/AndroidAutoLayout)
12. [`Gson`](https://github.com/google/gson)
13. [`Butterknife`](https://github.com/JakeWharton/butterknife)
14. [`Androideventbus`](https://github.com/hehonghui/AndroidEventBus)
15. [`Timber`](https://github.com/JakeWharton/timber)
16. [`Glide`](https://github.com/bumptech/glide)
17. [`LeakCanary`](https://github.com/square/leakcanary)

Fragment 
 Two Fragments should never communicate directly
 android2.3出现Fragemnt， 2.3后平板越来越多，为了屏幕适配，谷歌开发了Fragment，
Fragment 好处
 1.Fragment优势是系统开销小，碎片化
 2.FragmentManager，可以方便管理Fragment
 3.Fragment界面耦合度低

常见的通讯方式
1.EventBus， rxBus
网精油一样，
	优点：方便，快捷，简单
	缺点
	  1.放射，性能大打折扣，效率低
	  2.代码维护困难
	  3.数据无法返回，单向传递

2.handler
	优点: 可以解决问题

	缺点：1.耦合 2. 无法获取activity返回结果 3. 很容易内存泄漏

3.static
	静态变量，很占用内存，android分配给该app的内存只有63M左右，static太多，导致内存严重被占用。

3.广播
  广播是一个非常庞大的通讯系统，它出现的目的是监听系统级的方法，大多数情况下充电、重启、联网、WIFI连接、蓝牙连接、短信， 单元发送元，多处接收。
	缺点：
	1.新能差，延迟
	2.通信体系，重，一个发生，多个接收
	3.传播的数据有限
	4.代码冗余


4.接口
	优点：简单，效率高，方便，解耦合
	缺点：
	  1.代码冗余，每个需要通信的Fragment 都必须定义自己独一无二的接口
