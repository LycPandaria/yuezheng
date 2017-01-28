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

@Entity
public class NewContent extends Model{
	@Id
	public Long id;
	
	@Required
	public String title;
	@Required
	public Date datenews;
	@Required
	@Lob
	public String content;
	
	@Required
	public String brief;	// 摘要

	// 无参构造
	public NewContent() {
		super();
	}

	// 有参构造
	public NewContent(Long id, String title, Date datenews, String content,
			String brief) {
		super();
		this.id = id;
		this.title = title;
		this.datenews = datenews;
		this.content = content;
		this.brief = brief;
	}

	// 定义一个finder，相当于一个DAO，通过这个find来对数据进行操作
	public static Finder<Long, NewContent> find =
			new Finder<>(Long.class, NewContent.class);

	/**
	 * 返回所用的乐正新闻
	 * @return
     */
	public static List<NewContent> findAll() {
		return find.all();
	}

	public static NewContent findById(Long id){
		return find.byId(id);
	}

	public static Page<NewContent> find(int page){
		return find.where().orderBy("datenews desc")
				.findPagingList(10)
				.setFetchAhead(false)
				.getPage(page);
	}

	public Long getID(){
		return id;
	}
}
