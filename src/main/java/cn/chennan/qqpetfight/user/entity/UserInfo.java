package cn.chennan.qqpetfight.user.entity;

/**
 * @author cn
 * @date 2022-06-08 23:00
 */
public class UserInfo {
    private String name;
    private String skey;
    private String uin;

    public UserInfo() {
    }

    public UserInfo(String name, String skey, String uin) {
        this.name = name;
        this.skey = skey;
        this.uin = uin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSkey() {
        return skey;
    }

    public void setSkey(String skey) {
        this.skey = skey;
    }

    public String getUin() {
        return uin;
    }

    public void setUin(String uin) {
        this.uin = uin;
    }

    @Override
    public String toString() {
        return "UserCookie{" +
                "name='" + name + '\'' +
                ", skey='" + skey + '\'' +
                ", uin='" + uin + '\'' +
                '}';
    }
}
