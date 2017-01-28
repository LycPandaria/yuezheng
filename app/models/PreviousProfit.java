package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Page;
import controllers.PreviousProfits;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class PreviousProfit extends Model{
	@Id
	public Long id;
	
	@Required
	public Date guzhiDate;	// 估值日
	
	public String per;		// 单位净值
	public String hs_point;	// 沪深300指数
	public String incRate;	//净值增长率
	public String floatChange;	// 净值累计变动
	public String hs_inc;	// 沪深300增涨率
	
	@ManyToOne
	public Product product;

	// 无参构造
	public PreviousProfit() {
		super();
	}

	// 有参构造
	public PreviousProfit(Long id, Date guzhiDate, String per, String hs_point,
			String incRate, String floatChange, String hs_inc, Product product) {
		super();
		this.id = id;
		this.guzhiDate = guzhiDate;
		this.per = per;
		this.hs_point = hs_point;
		this.incRate = incRate;
		this.floatChange = floatChange;
		this.hs_inc = hs_inc;
		this.product = product;
	}
	
	// 定义一个finder，相当于一个DAO，通过这个find来对数据进行操作
	public static Finder<Long, PreviousProfit> find =
		new Finder<>(Long.class, PreviousProfit.class);
	
	/**
	 * 传入某个产品，返回该产品最新的一个净值
	 * @param product
	 * @return
	 */
	public static PreviousProfit findLatestByProduct(Product product){
		List<PreviousProfit> ppsList = find.where()
				.eq("product", product)
				.orderBy("guzhiDate desc")
				.findList();
		if(ppsList.size()!=0)
			return ppsList.get(0);
		else
			return null;
	}

	public Long getID() {
		return id;
	}

	public static PreviousProfit findById(Long id){
		return find.byId(id);
	}
	/**
	 * 返回所有的PreviousProfit，是一个List
     * orderby 估值日
	 * @return
     */
	public static List<PreviousProfit> findAll() {
		return find.orderBy("guzhiDate desc").findList();
	}

    /**
     * 根据产品ID来查找对应的产品业绩，按照估值日的降序排列
     * @param prodId    产品ID
     * @return
     */
	public static List<PreviousProfit> findByProduct(Long prodId){
		return find.where()
				.eq("product_id", prodId)
                .orderBy("guzhiDate desc")
                .findList();
	}

    /**
     * 查找对应的产品业绩页
     * @param page  页数
     * @param prodId    如果为0，则为在全部业绩中分页，如果不是，则在产品ID对应的产品中分页
     * @return
     */
    public static Page<PreviousProfit> find(Long prodId, int page){
        if(prodId == PreviousProfits.ALLPPS){
            return find.where().orderBy("guzhiDate desc")
                    .findPagingList(10)
                    .setFetchAhead(false)
                    .getPage(page);
        } else {
            return find.where()
                    .eq("product_id", prodId)
                    .orderBy("guzhiDate desc")
                    .findPagingList(10)
                    .setFetchAhead(false)
                    .getPage(page);
        }

    }
}
