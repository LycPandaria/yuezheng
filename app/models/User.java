package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by lyc08 on 2016/4/21.
 */
@Entity
public class User extends Model{
    @Id
    public Long id;

    @Constraints.Required
    String username;
    @Constraints.Required
    String password;

    //无参构造
    public User() {
    }

    //有参构造
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // 创建一个DAO
    public static Finder<Long, User> finder
            = new Finder<Long, User>(Long.class, User.class);

    // 根据传进来的参数查找用户
    public static User authenticate(String username, String password){
        return finder.where().eq("username", username)
                .eq("password", password).findUnique();
    }
}
