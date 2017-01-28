package models;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Product extends Model{
	@Id
	public Long id;
	
	@Required
	public String name;	//产品名称
	
	public String type;	// 产品类型
	public String adviser;	//投资顾问
	public String bank;		//托管人
	public String company;	// 证券公司
	public String manager;	// 投资经理
	
	@Required
	public Date date;		// 成立日期
	
	public String init;		// 初始成立规模
	public String lockup;	// 封存期
	public String xucun;	// 续存期
	public String openday;	// 开放日
	public String guzhiday;	//估值基准日
	public String trusteefee;			//托管费
	public String administrativefee;  //行政服务费
	public String floatfee;	//浮动管理费
	public String min;		//认购最低金额
	public String appday;	//认购申请提交日s
	public String rate;		// 认购费率
	public String ransomday;	//赎回申请提交日
	public String ransomfee;	//赎回费
	public String warn;			// 预警止损机制
	public String beizhu;		// 备注

	// 文件上传
	public byte[] picture;	//byte array to hold picture
	
	@OneToMany(mappedBy="product", cascade=CascadeType.ALL)
	public List<PreviousProfit> pps;
	
	// 无参构造
	public Product() {
		super();
	}

	// 有参构造
	public Product(Long id, String name, String type, String adviser, String bank,
			String company, String manager, Date date, String init,
			String lockup, String xucun, String openday, String guzhiday,
			String trusteefee, String administrativefee, String floatfee,
			String min, String appday, String rate, String ransomday,
			String ransomfee, String warn, String beizhu,
			List<PreviousProfit> pps) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.adviser = adviser;
		this.bank = bank;
		this.company = company;
		this.manager = manager;
		this.date = date;
		this.init = init;
		this.lockup = lockup;
		this.xucun = xucun;
		this.openday = openday;
		this.guzhiday = guzhiday;
		this.trusteefee = trusteefee;
		this.administrativefee = administrativefee;
		this.floatfee = floatfee;
		this.min = min;
		this.appday = appday;
		this.rate = rate;
		this.ransomday = ransomday;
		this.ransomfee = ransomfee;
		this.warn = warn;
		this.beizhu = beizhu;
		this.pps = pps;
	}
	
	// 定义一个finder，相当于一个DAO，通过这个find来对数据进行操作
	public static Finder<Long, Product> find =
			new Finder<>(Long.class, Product.class);
	
	/**
	 * 用于显示产品名称
	 */
	public String toString() {
		return name;
	}

    /**
     * type 在templete中会冲突
     * @return
     */
    public String getTYPE(){
        return this.type;
    }
	
	/**
	 * 返回所有的Product，已一个list的形式
	 */
	public static List<Product> findAll() {
		return find.all();
	}

	/**
	 * 根据ID查找对应产品
	 * @param id
	 * @return
     */
	public static Product findById(Long id){
		return find.byId(id);
	}


	/**
	 * 返回一个Map，Key为产品ID，Value为产品名字
	 * @return
     */
	public static Map<Long, String> productsMap(){
		Map<Long, String> products = new HashMap<>();
		for(Product product : Product.findAll()){
			products.put(product.id, product.name);
		}
		return products;
	}

	public Long getID(){
		return id;
	}
}
