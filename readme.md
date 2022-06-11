# qq-pet-fight


## 愿景
替代QQFight辅助, 实现 **通用/高度可定制化** 产品.

- [ ] 新增活动,可快捷接入,并进行应用
- [ ] 组合应用,自定义功能
- [ ] 定时调度
- [ ] 自动更新登录态
- [ ] 设立WEB端,使用更便捷

## 功能描述
- 批量存储用户登录态.
- 发送请求: 发送请求时会偶尔提示 未登录/系统繁忙,偶尔一两次,程序会进行重试,若一会提示请想看msg信息.
- 读入runlist.txt文件内所有url进行执行.


## 使用方法

### 设置/更新登录态
- 类: cn.chennan.qqpetfight.user.service.UserService 
- 需网页版登录后,通过f12打开控制台. 获得到cookie中的值(Application - Cookies - fight.pet.qq.com下) skey和uin

### 执行脚本
- cn.chennan.qqpetfight.http.RestTemplateTests#testRunList 使用这个test类.(暂时), 如功能描述3所说,获取读取resources/runlist.txt文件进行执行
	- #代表过滤的请求(不执行).
