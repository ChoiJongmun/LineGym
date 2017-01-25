package health.linegym.com.linegym.object;

import java.io.Serializable;

/**
 * Created by jongmun on 2017-01-02.
 */

public class MemberInfo implements Serializable{

    String mem_no                       = "";
    String name                         = "";
    String birthday                     = "";
    String sexcd                        = "";
    String age                           = "";
    String phone                        = "";
    String start_date                  = "";
    String limit_date                  = "";
    String inbody_mem_no               = "";

    public String getMem_no() {
        return mem_no;
    }

    public void setMem_no(String mem_no) {
        this.mem_no = mem_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSexcd() {
        return sexcd;
    }

    public void setSexcd(String sexcd) {
        this.sexcd = sexcd;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getLimit_date() {
        return limit_date;
    }

    public void setLimit_date(String limit_date) {
        this.limit_date = limit_date;
    }

    public String getInbody_mem_no() {
        return inbody_mem_no;
    }

    public void setInbody_mem_no(String inbody_mem_no) {
        this.inbody_mem_no = inbody_mem_no;
    }
}
