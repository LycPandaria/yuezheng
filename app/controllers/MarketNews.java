package controllers;

import com.avaje.ebean.Page;
import models.MarketNew;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.MarketNew.list;
import views.html.MarketNew.details;


@Security.Authenticated(Secured.class)
public class MarketNews extends Controller {

    // 统一前后台
    private static final Form<MarketNew> marketnewForm=
            Form.form(MarketNew.class);
    /**
     * 显示marketnew的列表页面
     * @param page
     * @return
     */
    public static Result list(int page){
        Page<MarketNew> marketNewPage = MarketNew.find(page);
        return ok(list.render(marketNewPage));
    }

    /**
     * 将一个新form render到页面
     * @return
     */
    public static Result newMarketNew() {
        return ok(details.render(marketnewForm));
    }

    /**
     * 根据id查询资讯，render到页面
     * 如果错误id，返回notfound
     * @param id
     * @return
     */
    public static Result details(Long id){
        MarketNew marketNew = MarketNew.findById(id);
        if(marketNew == null)
            return notFound();
        Form<MarketNew> filledForm = marketnewForm.fill(marketNew);
        return ok(details.render(filledForm));
    }

    /**
     * 根据ID删除资讯，如果ｉｄ错误，返回notfound
     * @param id
     * @return
     */
    public static Result delete(Long id){
        MarketNew marketNew = MarketNew.findById(id);
        if(marketNew == null)
            return notFound();
        marketNew.delete();
        return redirect(routes.MarketNews.list(0));
    }

    /**
     * 根据保存过来的表单进行保存
     * 如果有错，回来编辑页面
     * 成功则给予成功提示
     * @return
     */
    public static Result save(){
        Form<MarketNew> boundForm = marketnewForm.bindFromRequest();
        if(boundForm.hasErrors()){
            flash("error", "保存的时候发生了错误。");
            return badRequest(details.render(boundForm));
        }
        MarketNew marketNew = boundForm.get();
        if(marketNew.id == null){
            marketNew.save();
            flash("success", "成功添加新资讯。");
        }else {
            marketNew.update();
            flash("success", "成功修改资讯。");
        }
        return redirect(routes.MarketNews.list(0));
    }
}
