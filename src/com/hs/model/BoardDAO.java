package com.hs.model;

import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

// MyBatis
public class BoardDAO { // DAO(Data Access Object)
	
	/* MyBatis 쓰면 JDBC에서 쓰던 이러한 객체들은 이제 필요없다.
	private Connection conn;
	private PreparedStatement ps;
	private ResultSet rs;
	*/
	
	private static SqlSessionFactory sqlSessionFactory; // SqlSession들이 여러개 들어있는 메모리 공간, 같은 static이어야 참조 가능 하므로 static
														// Connection POOL 기법 이용해 db 연결 시, 부하를 줄이고, 속도를 높일 수 있다.
	
	// 초기화 블럭 - 프로그램 실행 시, 딱 한번만 실행되는 코드 영역
	static {
		String resource = "com/hs/mybatis/config.xml"; // config.xml 파일을 가져와서
		InputStream inputStream = null;
		try {
			inputStream = Resources.getResourceAsStream(resource);
		} catch (Exception e) {
			e.printStackTrace();
		}
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream); // config.xml 파일 읽어서 SqlSession 있는 메모리 공간 가리키는
																			   // SqlSessionFactory 객체 만든다.
	}
	
	// 디비에서 키 가져오기 동작
	public CryptVO dataCrypt() {
		SqlSession session = sqlSessionFactory.openSession(); // Connection POOL에서 SqlSession 꺼내온다.
		CryptVO vo = session.selectOne("dataCrypt");
		session.close(); // 반납
		return vo;
	}
	
	// 회원 전체 리스트 가져오기 동작
	public List<MemberVO> memberList() {
		// [Connection + Statement] => SqlSession
		SqlSession session = sqlSessionFactory.openSession(); // Connection POOL에서 SqlSession 꺼내온다.
		List<MemberVO> list = session.selectList("memberList"); // 맵퍼에 해당된 SQL문 실행 후 memberVO로 묶고, ArrayList에 담아준다.
		session.close(); // 반납
		return list;
	}
	
	// 회원 저장 동작
	public int memberInsert(MemberVO vo) {
		SqlSession session = sqlSessionFactory.openSession();
		int cnt = session.insert("memberInsert", vo);
		session.commit(); // 데이터베이스에 있는 내용이 변경될 경우 확실하게 commit 해줘야 한다.
		session.close();
		return cnt;
	}
	
	// 회원 삭제 동작
	public int memberDelete(int num) {
		SqlSession session = sqlSessionFactory.openSession();
		int cnt = session.delete("memberDelete", num);
		session.commit();
		session.close();
		return cnt;
	}
	
	// 회원 삭제 시 해당 회원의 모든 게시물 삭제 동작
	public int memberDeleteAll(String writer) {
		SqlSession session = sqlSessionFactory.openSession();
		int cnt = session.delete("boardDeleteAll", writer);
		session.commit();
		session.close();
		return cnt;
	}
	
	// 회원 1명 정보 상세 조회
	public MemberVO memberContent(String id) {
		SqlSession session = sqlSessionFactory.openSession();
		MemberVO vo = session.selectOne("memberContent", id); // 하나만 가져온다.
		session.close();
		return vo;
	}
	
	// 회원 정보 수정 동작
	public int memberUpdate(MemberVO vo) {
		SqlSession session = sqlSessionFactory.openSession();
		int cnt = session.update("memberUpdate", vo);
		session.commit();
		session.close();
		return cnt;
	}
	
	
	// 회원 아이디 중복 체크 동작
	public String memberDbcheck(String id) {
		SqlSession session = sqlSessionFactory.openSession();
		String dbId = session.selectOne("memberDbcheck", id); // id가 넘어오거나 중복되지 않으면 null이 넘어온다.
		session.close();
		String isDouble = "NO";
		if(dbId != null) {
			isDouble = "YES";
		}
		return isDouble; // YES(중복 O) / NO(중복 X)
	}
	
	// 게시물 전체 리스트 가져오기 동작
	public List<BoardVO> boardList() {
		// [Connection + Statement] => SqlSession
		SqlSession session = sqlSessionFactory.openSession(); // Connection POOL에서 SqlSession 꺼내온다.
		List<BoardVO> list = session.selectList("boardList"); // 맵퍼에 해당된 SQL문 실행 후 memberVO로 묶고, ArrayList에 담아준다.
		session.close(); // 반납
		return list;
	}
	
	// 게시물 등록 동작
	public int boardInsert(BoardVO vo) {
		SqlSession session = sqlSessionFactory.openSession();
		int cnt = session.insert("boardInsert", vo);
		session.commit(); // 데이터베이스에 있는 내용이 변경될 경우 확실하게 commit 해줘야 한다.
		session.close();
		return cnt;
	}
	
	// 게시물 조회 동작
	public BoardVO boardContent(int num) {
		SqlSession session = sqlSessionFactory.openSession();
		BoardVO vo = session.selectOne("boardContent", num); // 하나만 가져온다.
		session.close();
		return vo;
	}
	
	// 게시물 조회수
	public void boardCount(int num, String mode) {
		if("get".equals(mode)) {
			SqlSession session = sqlSessionFactory.openSession();
			session.update("boardCount", num);
			session.commit();
			session.close();
		}
	}
	
	// 게시물 수정 동작
	public int boardUpdate(BoardVO vo) {
		SqlSession session = sqlSessionFactory.openSession();
		int cnt = session.update("boardUpdate", vo);
		session.commit();
		session.close();
		return cnt;
	}
	
	// 게시물 삭제 동작
	public int boardDelete(int num) {
		SqlSession session = sqlSessionFactory.openSession();
		int cnt = session.delete("boardDelete", num);
		session.commit();
		session.close();
		return cnt;
	}
	
}
