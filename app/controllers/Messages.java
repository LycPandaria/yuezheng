package controllers;

import com.avaje.ebean.Page;
import models.Message;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.Message.leave_msg;
import views.html.Message.list;
import play.libs.mailer.Email;
import play.libs.mailer.MailerPlugin;

import java.util.Date;

/**
 * Created by lyc08 on 2016/4/20.
 */
public class Messages extends Controller{
    // 新建一个Product的Form，用来统一网页和后台的表单
    private static final Form<Message> msgForm =
            Form.form(Message.class);

    public static Result leaveMsg(){
        Form<Message> newMsg = Form.form(Message.class);
        return ok(leave_msg.render(newMsg));
    }
    public static Result saveMsg() {
        // save message
        // 通过这个bindFromRequest中获取到提交的Form，然后再运用get方法得到具体的实体类
        // create a Form object from the request
        Form<Message> boundfForm = msgForm.bindFromRequest();
        if(boundfForm.hasErrors()){
            System.out.println(boundfForm.errors());
            //detect error
            //检查从网页提交过来的form有没有错
            //如果有错，回传一个badRequest。并将提交过来的form（boundForm）送回去显示
            flash("error", "请输入您的常用联系方式和意见。");
            return badRequest(leave_msg.render(boundfForm));
        }
        // extract object from the form
        Message message = boundfForm.get();
        message.msgDate = new Date();
        message.save();
        flash("success", "多谢您对乐正资本的支持，我们会尽快联系您。");

        // Email
        Email email = new Email();
        email.setSubject("有新的用户留言了！");
        email.setFrom("691632600@qq.com");
        email.addTo("lyc0873@163.com");
        email.setBodyText("联系方式：" + message.email + "\n" +
            "留言内容为：" + message.content);
        MailerPlugin.send(email);
        return redirect(routes.Messages.leaveMsg());
    }

    /**
     * 根据页数返回一个对应的页对象
     * @param page
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result list(int page) {
        Page<Message> messagePage = Message.find(page);
        return ok(list.render(messagePage));
    }

    /**
     * 根据ID删除留言
     * @param id
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result delete(Long id){
        final Message message = Message.findById(id);
        if(null == message){
            return notFound();      // todo
        }
        message.delete();
        return redirect(routes.Messages.list(0));
    }
}
