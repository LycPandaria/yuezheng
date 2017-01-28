package controllers;

import play.mvc.Http;
import play.mvc.Security;
import play.mvc.Result;

/**
 * Created by lyc08 on 2016/4/24.
 */
public class Secured extends Security.Authenticator{

    @Override
    public String getUsername(Http.Context ctx) {
        return ctx.session().get("user");
    }

    /**
     * 验证失败后会执行这个
     * @param ctx
     * @return
     */
    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return redirect(routes.Admin.login());
    }

}
