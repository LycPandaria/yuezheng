package controllers;

import com.google.common.io.Files;
import models.Product;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import views.html.Product.list;
import views.html.Product.details;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by lyc08 on 2016/4/19.
 */
@Security.Authenticated(Secured.class)
public class Products extends Controller{

    // 建立一个私有product form来统一表单前台后台
    private static Form<Product> prodForm = Form.form(Product.class);
    /**
     * 后台管理的产品列表页面
     * @return  读到页面上的内容是一个Map值对，key为产品id，value为产品名
     */
    public static Result list() {
        // 得到一个Map，为产品的 id--name
        Map<Long, String> prodMap = Product.productsMap();
        return ok(list.render(prodMap));
    }

    /**
     * 根据id查找到对应的product，填充到form，render到页面
     * @param id
     * @return
     */
    public static Result details(Long id) {
        Product product = Product.findById(id);
        if(product == null)
            return notFound();  //todo
        // 用查找到的product填充表单，传递到前台
        Form<Product> filledForm = prodForm.fill(product);
        return ok(details.render(filledForm));
    }

    /**
     * 新建一个产品页面，就是传一个空表单过去
     * @return
     */
    public static Result newProduct() {
        // create new product
        return ok(details.render(prodForm));
    }

    /**
     * 根据ID删除一个产品
     * @param id    产品ID
     * @return
     */
    public static Result delete(Long id) {
        final Product product = Product.findById(id);
        if(null == product)
            return notFound();  //todo
        product.delete();
        return redirect(routes.Products.list());
    }

    /**
     * 把详情页传过来的表单进行处理和保存
     * @return
     */
    public static Result save(){
        Form<Product> boundForm = prodForm.bindFromRequest();
        if(boundForm.hasErrors()){
            flash("error","请完善信息再保存。");
            return badRequest(details.render(boundForm));
        }
        Product product = boundForm.get();
        System.out.println("Product id to be save or update: " + product.id);
        //binds form as a multipart form so we can access the submitted file
        Http.MultipartFormData body = request().body().asMultipartFormData();
        // requests picture FilePart-- this should match the name attribute
        Http.MultipartFormData.FilePart part = body.getFile("picture");
        if(part != null){
            File picture = part.getFile();
            try {
                product.picture = Files.toByteArray(picture);
                System.out.println("PHOTO SIZE:" + product.picture.length);
            }catch (IOException e) {
                return internalServerError("Error reading file upload!");
            }
        }

        // 根据有没有id这个项来判断是要对它进行更新还是新建
        if(product.id == null){ //新的产品
            product.save();
            flash("success", String.format("成功新增产品：%s", product.name));
        } else {        // 已存在的产品，更新即可
            System.out.println("Operation: update");
            product.update();
            flash("success", String.format("成功保存产品：%s", product.name));
        }
        return redirect(routes.Products.list());
    }
}
