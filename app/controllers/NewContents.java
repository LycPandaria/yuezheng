package controllers;

import com.avaje.ebean.Page;
import models.NewContent;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import play.mvc.Security;
import views.html.NewContent.list;
import views.html.NewContent.details;

/**
 * Created by lyc08 on 2016/4/19.
 */
@Security.Authenticated(Secured.class)
public class NewContents extends Controller {

    // 统一前后台
    private static final Form<NewContent> newContentForm=
            Form.form(NewContent.class);

    /**
     * 显示newContent的列表页面
     * @param page
     * @return
     */
    public static Result list(int page){
        Page<NewContent> newContentPage = NewContent.find(page);
        return ok(list.render(newContentPage));
    }

    /**
     * 将一个新form render到页面
     * @return
     */
    public static Result newContent() {
        return ok(details.render(newContentForm));
    }

    /**
     * 根据id查询资讯，render到页面
     * 如果错误id，返回notfound
     * @param id
     * @return
     */
    public static Result details(Long id){
        NewContent newContent = NewContent.findById(id);
        if(newContent == null)
            return notFound();
        Form<NewContent> filledForm = newContentForm.fill(newContent);
        return ok(details.render(filledForm));
    }

    /**
     * 根据ID删除资讯，如果ｉｄ错误，返回notfound
     * @param id
     * @return
     */
    public static Result delete(Long id){
        NewContent newContent = NewContent.findById(id);
        if(newContent == null)
            return notFound();
        newContent.delete();
        return redirect(routes.NewContents.list(0));
    }

    /**
     * 根据保存过来的表单进行保存
     * 如果有错，回来编辑页面
     * 成功则给予成功提示
     * @return
     */
    public static Result save(){
        Form<NewContent> boundForm = newContentForm.bindFromRequest();
        if(boundForm.hasErrors()){
            flash("error", "保存的时候发生了错误。");
            return badRequest();
        }
        NewContent newContent = boundForm.get();
        if(newContent.id == null){
            newContent.save();
            flash("success", "成功添加新资讯。");
        }else {
            newContent.update();
            flash("success", "成功修改资讯。");
        }
        return redirect(routes.NewContents.list(0));
    }
}
