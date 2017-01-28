package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Page;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import play.mvc.Result;

@Entity
public class Message extends Model{
	@Id
	public Long id;

	@Required
	public String email;	//这里存储的是联系方式！
	@Required
	public String content;

	public Date msgDate;

	// 无参构造
	public Message() {
		super();
	}

	// 有参构造
	public Message(Long id, String email, String content, Date msgDate) {
		super();
		this.id = id;
		this.email = email;
		this.content = content;
		this.msgDate = msgDate;
	}
	
	// 定义一个finder，相当于一个DAO，通过这个find来对数据进行操作
	public static Finder<Long, Message> find =
		new Finder<>(Long.class, Message.class);

	public Long getID(){
		return id;
	}

	// 按页查找，返回一个页对象
	public static Page<Message> find(int page) {
		return find.where()
				.orderBy("msgDate desc")
				.findPagingList(10)
				.setFetchAhead(false)
				.getPage(page);
	}

    /**
     * 根据给的ID查找对应的留言
     * @param id
     * @return
     */
    public static Message findById(Long id){
        return find.byId(id);
    }
}
