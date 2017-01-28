package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.avaje.ebean.Page;
import play.data.validation.Constraints.Max;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class MarketNew extends Model {
	@Id
	public Long id;
	
	@Required
	public String title;
	
	@Required
	public Date datenews;
	
	@Required
	public String source;
	
	@Required
	@Lob
	public String content;
	
	@Required
	public String brief;	// 摘要

	// 无参构造
	public MarketNew() {
		super();
	}

	// 有参构造
	public MarketNew(Long id, String title, Date datenews, String source,
			String content, String brief) {
		super();
		this.id = id;
		this.title = title;
		this.datenews = datenews;
		this.source = source;
		this.content = content;
		this.brief = brief;
	}

	// 定义一个finder，相当于一个DAO，通过这个find来对数据进行操作
	public static Finder<Long, MarketNew> find =
		new Finder<>(Long.class, MarketNew.class);
	
	/**
	 * 返回所有市场新闻
	 * @return 一个包含所有市场新闻的列表
	 */
	public static List<MarketNew> findAll() {
		return find.all();
	}

    /**
     * 根据id返回对应市场新闻
     * @param id
     * @return 新闻实体
     */
    public static MarketNew findById(Long id) {
        return find.byId(id);
    }

    /**
     * 根据传入的page来查找对应的一个MarketNew的页对象
     * @param page
     * @return
     */
    public static Page<MarketNew> find(int page){
        return find.where().orderBy("datenews desc")
                .findPagingList(10)
                .setFetchAhead(false)
                .getPage(page);
    }

	public Long getID() {
		return id;
	}


}
