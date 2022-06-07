package cn.chennan.qqpetfight.config;

/**
 * @author cn
 * @date 2022-06-08 00:13
 */
public enum OnceActivityConfig {
    ZHOU_NIAN_2022("https://fight.pet.qq.com/cgi-bin/petpk?cmd=newAct&subtype=141&itemId=%s&op=3", 1, true, 0, 9),
    CHANG_AN_2022("https://fight.pet.qq.com/cgi-bin/petpk?cmd=newAct&id=%s&subtype=110&op=1", 1, true, 0, 2),
    XU_YUAN_2022("https://fight.pet.qq.com/cgi-bin/petpk?cmd=abyss_tide&op=wishexchange&id=%s", 1, true, 0, 3),
    XU_YUAN_2022_2("https://fight.pet.qq.com/cgi-bin/petpk?cmd=abyss_tide&op=wishexchange&id=%s", 300, true, 4, 11),
    XU_YUAN_2022_3("https://fight.pet.qq.com/cgi-bin/petpk?cmd=abyss_tide&op=wishexchange&id=%s", 100, true, 12, 15);

    String url;
    int count;
    boolean arg;
    int start;
    int end;

    OnceActivityConfig(String url, int count, boolean arg, int start, int end) {
        this.url = url;
        this.count = count;
        this.arg = arg;
        this.start = start;
        this.end = end;
    }

    public String getUrl() {
        return url;
    }

    public int getCount() {
        return count;
    }

    public boolean isArg() {
        return arg;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}
