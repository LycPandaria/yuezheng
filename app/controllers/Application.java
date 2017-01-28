package controllers;

import java.text.SimpleDateFormat;
import java.util.*;

import com.avaje.ebean.Page;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;

import play.mvc.*;
import play.libs.Json;
import com.fasterxml.jackson.databind.JsonNode;
import views.html.MarketNew.market_news;
import views.html.MarketNew.show_marketnew;
import views.html.NewContent.company_news;
import views.html.NewContent.show_companynew;
import views.html.Product.show_prod;
import views.html.Product.show_prod_pic;
import views.html.Product.show_prod_yj;
import views.html.Product.show_prod_yj_table;
import views.html.Product.show_prod_lc;

public class Application extends Controller {

	public static Result simditor() {
		return ok(views.html.simditor.render());
	}

	/**
	 * 首页
	 * @return 产品的最新业绩列表
	 */
	public static Result index() {
    	
    	List<Product> products = Product.findAll();
    	// 商品最新净值
    	List<PreviousProfit> pps = new ArrayList<>();
    	List<PreviousProfit> temp_pps = new ArrayList<>();	//临时存放历史业绩的地方
    	//遍历所有产品，分别找到产品的最新净值
    	for(Product product : products){
    		temp_pps.add(PreviousProfit.findLatestByProduct(product));
    	}
    	for(PreviousProfit pp : temp_pps){
    		if(pp != null)
    			pps.add(pp);
    	}
    	temp_pps.clear();
        return ok(views.html.index.render(pps));
    }

    /**
     * 显示产品的简介页面
     * @param id -- 产品的id
     * @return  把一个所有产品的《id，name》Map和该产品render到页面
     */
    public static Result products(Long id) {
        Map<Long, String> productsMap = Product.productsMap();
        Product product = Product.findById(id);
        return ok(show_prod.render(productsMap, product));
    }

    /**
     * 显示产品业绩页面
     * @param id  -- 产品的id
     * @return  把一个所有产品的《id，name》Map和该产品render到页面
     */
    public static Result productsYJ(Long id){
        Map<Long, String> productsMap = Product.productsMap();
        Product product = Product.findById(id);
        return ok(show_prod_yj.render(productsMap, product));
    }

    /**
     * 显示产品的净值图页面
     * @param id -- 产品的id
     * @return 把一个所有产品的《id，name》Map和该产品render到页面
     */
    public static Result productsPic(Long id){
        Map<Long, String> productsMap = Product.productsMap();
        Product product = Product.findById(id);
        return ok(show_prod_pic.render(productsMap, product));
    }

	/**
	 * 购买流程页面
	 * @param id -- 产品的id
	 * @return  把一个所有产品的《id，name》Map和该产品render到页面
     */
    public static Result productsLC(Long id) {
        Map<Long, String> productsMap = Product.productsMap();
        Product product = Product.findById(id);
        return ok(show_prod_lc.render(productsMap, product));
    }

	public static Result picture(Long id) {
		final Product product = Product.findById(id);
		if(product == null) return notFound();
		return ok(product.picture);
	}

	/**
	 * 投资目标页面
	 * @return
	 */
	public static Result investTarget() {
		return ok(views.html.Application.investTarget.render());
	}
	
	public static Result level() {
		return ok(views.html.Application.level.render());
	}
	
	public static Result strategy() {
		return ok(views.html.Application.strategy.render());
	}
	
	public static Result risk() {
		return ok(views.html.Application.risk.render());
	}
	
	/**
	 * 关于我们-特点与优势页面
	 * @return
	 */
	public static Result advantages() {
		return ok(views.html.Application.advantages.render());
	}
	
	/**
	 * 关于我们-公司简介页面
	 * @return
	 */
	public static Result companyIntro() {
		return ok(views.html.Application.company_intro.render());
	}
	
	/**
	 * 关于我们-联系我们页面
	 * @return
	 */
	public static Result contactUs() {
		return ok(views.html.Application.contactus.render());
	}
	
	/**
	 *  关于我们-乐正团队页面
	 * @return
	 */
	public static Result group() {
		return ok(views.html.Application.group.render());
	}

	/**
	 * 新闻资讯-市场资讯页面
	 * @return	把一个市场新闻list render到了页面
	 */
	public static Result marketNews(){
		List<MarketNew> mns = MarketNew.findAll();
		return ok(market_news.render(mns));
	}

	/**
	 * 新闻资讯-展示市场新闻页面
	 * @param id 新闻id
	 * @return  把特定市场新闻render到页面
	 */
	public static Result showMarketNews(Long id) {
		MarketNew marketNew = MarketNew.findById(id);
		return ok(show_marketnew.render(marketNew));
	}

    /**
     * 把一个包含所有乐正新闻的List render到页面
     * @return
     */
    public static Result companyNews() {
        List<NewContent> companyNews = NewContent.findAll();
        return ok(company_news.render(companyNews));
    }

    /**
     * 返回对应ID的乐正新闻
     * @param id
     * @return
     */
    public static Result showCompanyNew(Long id) {
        NewContent companyNew = NewContent.findById(id);
        return ok(show_companynew.render(companyNew));
    }

    /**
     * 根据产品id和页码进行查询
     * 这个页面主要是用来进行ajax操作的
     * @param id
     * @param page
     * @return  一个html中只有一个表
     */
	public static Result showPPsByProdAndPage(Long id, int page){
        Page ppPage = PreviousProfit.find(id, page);
		return ok(show_prod_yj_table.render(ppPage, id));
	}

	@BodyParser.Of(BodyParser.Json.class)
	public static Result profitsJson(Long id){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// find all PreviousProfits
		List<PreviousProfit> profitList = PreviousProfit.findByProduct(id);
		List<String> perList = new ArrayList<>();
		List<String> dateList = new ArrayList<>();
		for(PreviousProfit profit : profitList){
			perList.add(profit.per);
			dateList.add(dateFormat.format(profit.guzhiDate));
		}
		Collections.reverse(perList);
		Collections.reverse(dateList);
		ObjectNode result = Json.newObject();
		result.put("perList", Json.toJson(perList));
		result.put("dateList", Json.toJson(dateList));
		return ok(result).as("application/json");
	}
}
