package controllers;

import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.Admin.login;
import views.html.Admin.index;

/**
 * Created by lyc08 on 2016/4/21.
 * 主要后台的登陆
 */
public class Admin extends Controller{

    public static class Login{

        public String username;
        public String password;

    }

    // 建立一个Login表单统一前后台
    private static final Form<Login> loginForm =
            Form.form(Login.class);

    public static Result index() {
        return ok(index.render());
    }

    public static Result login() {
        Form<Login> newLogin = Form.form(Login.class);
        return ok(login.render(newLogin));
    }

    public static Result authenticate() {
        Form<Login> boundForm = loginForm.bindFromRequest();
        // 表单验证
        if(boundForm.hasErrors()){
            flash("error", "请填写管理员用户名和密码.");
            return badRequest(login.render(boundForm));
        }
        String username = boundForm.get().username;
        String password = boundForm.get().password;
        System.out.println("username: " + username +
                "    password: " + password);
        // 用户验证
        if(User.authenticate(username, password) == null){
            flash("invalid", "无效的用户名和密码.");
            return forbidden(login.render(boundForm));
        }
        // clear session
        session().clear();
        // add user`name to session
        // This indicates user is logged in
        session("user", username);

        return redirect(routes.Admin.index());
    }

    public static Result logout() {
        session().clear();
        return redirect(routes.Admin.login());
    }
}
