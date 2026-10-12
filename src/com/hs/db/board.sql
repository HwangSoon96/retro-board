select * from board;

create table board(
	idx int not null auto_increment, -- 게시물 번호 자동 증가
	title varchar(1000) not null, -- 게시물 제목
	contents varchar(4000) not null, -- 게시물 내용
	count int, -- 게시물 조회수
	writer varchar(100) not null, -- 게시물 작성자
	indate datetime default now() not null, -- 게시물 등록일(자동으로 들어감)
	primary key(idx)
);

delete from board;

drop table board;