package com.hs.model;

public class MemberVO {
	
	private int num;
	private String id;
	private String pass;
	private String name;
	private int age;
	private String email;
	private String auth;
	
	public MemberVO( ) {	}
	
	// 회원가입 받을 때는 num 필요없으니 포함시키지 않는다.
	public MemberVO(String id, String pass, String name, int age, String email, String auth) { // num은 입력하지 않아도 되니 필요 X
		super();
		this.id = id;
		this.pass = pass;
		this.name = name;
		this.age = age;
		this.email = email;
		this.auth = auth;
	}

	// 반대로 db에서 데이터를 불러올때는 num 정보도 있으므로 위와 다르게 num 포함시킨 생성자 메서드도 하나 만든다.
	public MemberVO(int num, String id, String pass, String name, int age, String email, String auth) {
		super();
		this.num = num;
		this.id = id;
		this.pass = pass;
		this.name = name;
		this.age = age;
		this.email = email;
		this.auth = auth;
	}

	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}

	@Override
	public String toString() {
		return "MemberVO [num=" + num + ", id=" + id + ", pass=" + pass + ", name=" + name + ", age=" + age + ", email="
				+ email + ", auth=" + auth + "]";
	}

}
