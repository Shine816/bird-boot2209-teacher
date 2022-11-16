实现处理业务
步骤:
1:新建包com.webserver.controller
2:在controller包中新建类UserController并定义reg方法(与SpringBoot项目一致)
3:在DispatcherServlet中修改逻辑
  3.1:废弃原有的通过request.getUri()方式获取请求路径(因为包含参数，不适用判断具体请求内容)
  3.2:改用request.getRequestURI()作为请求路径判断用户请求
  3.3:添加分支，首先判断请求路径是否为请求注册，如果是则实例化UserController并调用reg方法
      如果不是请求注册则走原有的分支判断是否请求static目录下的文件或404



实现重定向
当浏览器提交一个请求时，比如注册，那么请求会带着表单信息请求注册功能。而注册功能处理
完毕后直接设置一个页面给浏览器，这个过程是内部跳转。
即:浏览器上的地址栏中地址显示的是提交表单的请求，而实际看到的是注册结果的提示页面。
这有一个问题，如果此时浏览器刷新，会重复上一次的请求，即:再次提交表单请求注册业务。

为了解决这个问题，我们可以使用重定向。
重定向是当我们处理完请求后，不直接响应一个页面，而是给浏览器回复一个路径，让其再次根据
该路径发起请求。这样一来，无论用户如何刷新，请求的都是该路径。避免表单的重复提交。

实现:
在HttpServletResponse中定义一个方法:sendRedirect()
该方法中设置状态代码为302，并在响应头中包含Location指定需要浏览器重新发起请求的路径
将原来Controller中内部跳转页面的操作全部改为重定向。



独立练习部分:
按照注册的流程实现用户登录功能
1:在static下准备登录所需页面(与SpringBoot一致)
2:在UserController中定义方法login
3:实现登录逻辑(与SpringBoot项目一致)
4:在DispatcherServlet中添加分支，判断请求路径是否为登录，
  如果是则调用UserController的login方法






