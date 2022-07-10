# qq-pet-fight

## 一. 愿景
替代QQFight辅助, 实现 **通用/高度可定制化** 产品.

- [ ] 新增活动,可快捷接入,并进行应用
- [ ] 组合应用,自定义功能
- [ ] 定时调度
- [ ] 自动更新登录态
- [ ] 设立WEB端,使用更便捷

## 二. 功能描述
- 批量存储用户登录态.
- 发送请求: 发送请求时会偶尔提示 未登录/系统繁忙,偶尔一两次,程序会进行重试,若一会提示请想看msg信息.
- 读入runlist.txt文件内所有url进行执行.
- 读入runlist.qq号.txt, 针对各自qq号自定义执行.


## 三.使用方法

### 3.1 设置/更新登录态

#### 3.1.1 自定义登录态
- 手动更新用户的登录态
- 类: cn.chennan.qqpetfight.user.service.UserService 设置读取方式为 **UserStrategy.CUSTOM**
- 更新cn.chennan.qqpetfight.user.service.strategy.CustomUserStrategyServiceImpl下的用户登录态
  - 需网页版登录后,通过f12打开控制台. 获得到cookie中的值(Application - Cookies - fight.pet.qq.com下) skey和uin

#### 3.1.2 远程cookie文件
- 通过拉取远程文件, 并解析数据, 获得需要执行的用户登录态.
- 类: cn.chennan.qqpetfight.user.service.UserService 设置读取方式为 **UserStrategy.REMOTE_COOKIE_FILE**
- 如何生成cookie文件
  - QQFight辅助(会自动更新登录态) + fiddler(抓包)
  - 在fiddler脚本-OnBeforeRequest方法开头添加以下代码(c#代码), 文件将生成到 filename 路径下(如果没有文件, 先提前生成吧)
    - ```java
      if (oSession.fullUrl.Contains("fight.pet.qq.com")) {
              try {
                  var filename = "H:\\fiddler.txt"; 
                  // 最后分割符要与代码一致
                  var logContent = oSession.oRequest["Cookie"] + "\r\n";
                  // 文件超过1M就覆盖.
                  if ((new System.IO.FileInfo(filename)).Length < 1024*1024) {
                      System.IO.File.AppendAllText(filename,logContent);
                  } else {
                      System.IO.File.WriteAllText(filename,logContent);
                  }
              }catch (Exception){}
          }
  - 本人使用nginx的方式, 进行远程访问. (生成cookie文件是电脑A, 执行该程序是电脑B, 因此需要远程访问, 如果是同一台电脑, 可以使用读取本地文件的策略)

#### 3.1.3 本地cookie文件
- 通过本地文件, 并解析数据, 获得需要执行的用户登录态.
- 类: cn.chennan.qqpetfight.user.service.UserService 设置读取方式为 **UserStrategy.LOCAL_COOKIE_FILE**
- 如何生成cookie文件
  - 同 **3.1.2**
    
### 3.2 执行脚本
- cn.chennan.qqpetfight.http.RestTemplateTests#testRunList 使用这个test类.(暂时), 如功能描述3所说,获取读取resources/runlist.txt文件进行执行
	- #代表过滤的请求(不执行).
- cn.chennan.qqpetfight.http.RestTemplateTests#testSelfRunList 使用这个test类.(暂时), 如功能描述3所说,获取读取resources/runlist.qq号.txt文件进行执行
	- #代表过滤的请求(不执行).
