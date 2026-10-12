-- member(회원) table 만들기
create table member3(
	num int primary key auto_increment,
	id varchar(100) not null,
	pass varchar(1000) not null,
	name varchar(1000) not null,
	age int not null,
	email varchar(1000) not null,
	auth varchar(20) not null,
	unique key(id)
);
-- auto_increment 는 1부터 자동으로 1씩 증가
-- not null 들어가면 필수 입력 항목
-- primary key는 중복 X, null X
-- unique key는 중복 X, null O -> 아이디 공백은 프론트 단에서 자바스크립트나 제이쿼리로 필터링 해준다.

-- 검색
select * from member3;
-- member 테이블 조회

-- 저장
insert into member3(id, pass, name, age, email, auth)
values('admin', 'admin', '관리자', 26, 'admin@asd.com', 'A');
-- mysql에서 문자열은 ' ' 작은 따옴표 안에 넣어준다.

-- 수정
update member3 set auth='A' where id='admin';

-- 테이블 안에 있는 값 삭제
delete from member3;
delete from member3 where id='admin';
-- 테이블 자체를 삭제
drop table member3;