package spring.cloud.user.model;


import spring.cloud.user.annotation.Comment;

/**
 * 
 * @ClassName: User
 * @Description: TODO 用户实体
 * @author dh
 * @date 2019年10月28日
 *
 */
@Comment(name="用户实体")
public class User {

	@Comment(name="主键")
	private Long id;
	
	@Comment(name="邮箱")
	private String email;
	
	@Comment(name="电话")
	private String phone;
	
	@Comment(name="用户名")
	private String userName;
	
	@Comment(name="密码")
	private String password;
	
	@Comment(name="登录名称")
	private String loginName;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
}
