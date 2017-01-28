package controllers;

import com.avaje.ebean.Page;
import models.PreviousProfit;
import models.Product;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import play.mvc.Security;
import views.html.PreviousProfit.list;
import views.html.PreviousProfit.list_table;
import views.html.PreviousProfit.details;

/**
 * Created by lyc08 on 2016/4/19.
 */
@Security.Authenticated(Secured.class)
public class PreviousProfits  extends Controller{

    // form to render
    private static Form<PreviousProfit> ppForm =
            Form.form(PreviousProfit.class);
    // 许多页面都用的到的产品的（id，name）值对
    private static Map<Long, String> prodMap =
            Product.productsMap();

    // 当传入的产品ID为OL时，意味着要返回所有产品的所有业绩
    public final static Long ALLPPS = 0L;

    public static Result list(Long prodId, int page){
        sessionReset(prodId, page);
        return ok(list.render(prodMap, prodId, page));
    }

    public static Result details(Long id) {
        PreviousProfit pp = PreviousProfit.findById(id);
        if(pp == null)
            return notFound();  //todo
        // 用查找到的业绩填充表单，传递到前台
        Form<PreviousProfit> filledForm = ppForm.fill(pp);
        return ok(details.render(filledForm, prodMap,pp.product.id));
    }

    public static Result delete(Long id, Long prodId, int page) {
        sessionReset(prodId, page);
        PreviousProfit pp = PreviousProfit.findById(id);
        System.out.println("Pp Id=" + id + "prodId=" + prodId + "page=" + page);
        if(pp == null)
            return notFound();  //todo
        // 如果找到了，就删除该历史业绩
        pp.delete();
        // 删除完之后，根据传入的参数，找到产品和页数对应的页对象
        Page<PreviousProfit> ppPage = PreviousProfit.find(prodId, page);
        return ok(list_table.render(ppPage, prodId));
    }

    public static Result newPP() {
        // create new previousprofit
        return ok(details.render(ppForm, prodMap,0L));
    }

    public static Result getPPsTable(Long prodId, int page) {
        sessionReset(prodId, page);
        List<PreviousProfit> pps;
        // 首次进入这个页面，默认的prodId为0（并不存在）
        // 这个时候会返回所有的pp
        if(prodId == ALLPPS) {
            pps = PreviousProfit.findAll();
        } else {
            pps = PreviousProfit.findByProduct(prodId);
        }

        Page<PreviousProfit> ppPage = PreviousProfit.find(prodId, page);
        return ok(list_table.render(ppPage, prodId));
    }

    public static Result save() {
        // 从session获取参数
        Long prodId = 0L;
        if(session("prodId")!=null)
            prodId = Long.valueOf(session("prodId"));
        Form<PreviousProfit> boundForm = ppForm.bindFromRequest();
        if(boundForm.hasErrors()){
            flash("error","请先完善信息再保存");
            return badRequest(details.render(ppForm, prodMap,prodId));
        }
        PreviousProfit pp = boundForm.get();
        System.out.println("ProdId for this pp:" + pp.product.id);
        System.out.println("Pp id:" + pp.id);
        // 根据有没有id这个项来判断是要对它进行更新还是新建
        if(pp.id == null){
            pp.save();
            flash("success", "成功增加新的历史业绩。");
        } else {
            pp.update();
            flash("success", "成功修改该业绩。");
        }
        return ok(list.render(prodMap, pp.id, 0));
    }

    public static void sessionReset(Long prodId, int page){
        if(session("prodId") != null || session("page") != null){
            session().remove("prodId");
            session().remove("page");
        }
        // 用传进来的参数重置下session
        session("prodId", String.valueOf(prodId));
        session("page", String.valueOf(page));
    }
}
