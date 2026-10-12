package com.hs.frontcontroller;

import java.util.HashMap;
import java.util.HashSet;

import com.hs.controller.*;

public class HandlerMapping {

	// 요청마다 필요한 권한
	public static final int PUBLIC = 0; // 로그인 없이 가능
	public static final int USER = 1;   // 로그인 필요
	public static final int ADMIN = 2;  // 관리자만 가능

	private HashMap<String, Controller> mappings;
	private HashMap<String, Integer> levels;
	private HashSet<String> postOnly; // 데이터를 바꾸는 요청: POST와 CSRF 토큰을 요구한다.

	// 생성자 메서드
	public HandlerMapping() {
		mappings = new HashMap<String, Controller>();
		levels = new HashMap<String, Integer>();
		postOnly = new HashSet<String>();

		add("/boardLogin.do", new BoardLoginController(), PUBLIC, false);
		add("/memberLogin.do", new MemberLoginController(), PUBLIC, false);
				add("/memberRegister.do", new MemberRegisterController(), PUBLIC, false);
		add("/memberInsert.do", new MemberInsertController(), PUBLIC, false);
		add("/memberDbcheck.do", new MemberDbcheckController(), PUBLIC, false);

		add("/boardList.do", new BoardListController(), USER, false);
		add("/boardWrite.do", new BoardWriteController(), USER, false);
		add("/boardInsert.do", new BoardInsertController(), USER, true);
		add("/boardContent.do", new BoardContentController(), USER, false);
		add("/boardModify.do", new BoardModifyController(), USER, false);
		add("/boardUpdate.do", new BoardUpdateController(), USER, true);
		add("/boardDelete.do", new BoardDeleteController(), USER, true);
		add("/memberContent.do", new MemberContentController(), USER, false);
		add("/memberLogout.do", new MemberLogoutController(), USER, true);

		add("/boardAdmin.do", new BoardAdminController(), ADMIN, false);
		add("/memberList.do", new MemberListController(), ADMIN, false);
		add("/memberUpdate.do", new MemberUpdateController(), ADMIN, true);
		add("/memberDelete.do", new MemberDeleteController(), ADMIN, true);
	}

	private void add(String key, Controller controller, int level, boolean changesData) {
		mappings.put(key, controller);
		levels.put(key, level);
		if (changesData) {
			postOnly.add(key);
		}
	}

	// FrontController가 key에 해당하는 POJO가 누구니 라고 요청하면 이렇게 주면 된다.
	public Controller getController(String key) { // key=/memberList.do
		return mappings.get(key); // 키에 해당하는 POJO new MemberListController() 리턴
	}

	public int getLevel(String key) {
		return levels.get(key);
	}

	public boolean isPostOnly(String key) {
		return postOnly.contains(key);
	}
}
