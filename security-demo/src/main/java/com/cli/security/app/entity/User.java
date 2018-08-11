package com.cli.security.app.entity;

import com.cli.security.app.validator.MyConstraint;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotBlank;
import java.util.Date;

/**
 * @author lc
 * @date 2018/6/13
 */
public class User {

    public interface SimpleUserView{}

    public interface DetialUserView extends SimpleUserView{}

    private Integer id;

    @MyConstraint(message = "测试自定义验证注解")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private Date birthday;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @JsonView(SimpleUserView.class)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonView(SimpleUserView.class)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonView(DetialUserView.class)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonView(SimpleUserView.class)
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}
