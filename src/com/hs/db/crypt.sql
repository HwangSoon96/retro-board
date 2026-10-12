-- k2 컬럼: WAS의 k2로 암호화한 k1 (이메일 암호화 키)
create table crypt(
	k2 varchar(255) not null
);

select * from crypt;

-- 아래 값은 key.txt.example의 예시 키로 암호화한 예시 값입니다. 실제 사용 시 직접 생성한 값으로 바꿉니다.
insert into crypt(k2)
values('9ae90e4014bb65d2f4d5eba01e60ea629f45637a630e14cf9ad1f8e0dc7cfbe9');

drop table encrypt;