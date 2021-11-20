-- User Table : Created
create table USER(
    PID int auto_increment primary key,
    ID varchar(20) not null,
    PW varchar(200) not null,
    NAME varchar(20) not null,
    EMAIL varchar(30) not null,
    NICK varchar(25) not null,
    -- DATE DEFAULT CURRENT_TIMESTAMP, H2용
    DATE TIMESTAMP DEFAULT NOW(), -- 확인 필요 / MySQL용
    PHONE varchar(20)
);

create table ADMIN(
    ID varchar(20) not null primary key,
    PW varchar(200) not null,
    NAME varchar(20) not null,
    EMAIL varchar(30),
    NICK varchar(25) not null,
    PHONE varchar(20)
);

insert into ADMIN(ID, PW, NAME, NICK) values ('Admin','Admin','Administar','Admin');

insert into user(id,pw,name,email,nick,phone) values('test','test','test','test@mail.com','test01','000-0000-0000');


-- Board Table : Need Create
create table FREE_BOARD(
    FBOARD_NUM int auto_increment not null primary key,
    FBOARD_WRITER varchar(25) not null,
    FBOARD_TITLE varchar(50) not null,
    FBOARD_CONTENT varchar(1500) not null,
    FBOARD_VIEW_COUNT int(5) DEFAULT 0 not null,
    FBOARD_Comment_COUNT int(5) DEFAULT 0 not null, --> 댓글 카운터 추가
    FBOARD_REG_DATE datetime not null,
    FBOARD_UPDATE_DATE datetime
);
--H2용 테스트 생성
insert into free_board(fboard_writer,fboard_title,fboard_content,fboard_reg_date) values('INH2','INH2','INH2',now());

create table DEL_BOARD(
    DBOARD_NUM int auto_increment not null primary key,
    DBOARD_WRITER varchar(25) not null,
    DBOARD_TITLE varchar(50) not null,
    DBOARD_CONTENT varchar(1500) not null,
    DBOARD_VIEW_COUNT int(5) not null, --> 1000에서 5로 수정
    DBOARD_REG_DATE datetime not null,
    DBOARD_DEL_DATE datetime not null, --> 일단 보류
);

create table FREE_COMMENT(
    FCOMMENT_NUM int auto_increment not null primary key,
    Target_Board int not null, --> 어느 보드에 달리는지 명시
    FCOMMENT_WRITER varchar(25) not null,
    FCOMMENT_CONTENT varchar(500) not null,
    FCOMMENT_REG_DATE datetime not null,
    FCOMMENT_UPDATE_DATE datetime
    );

create table DEL_COMMENT(
    DCOMMENT_NUM int auto_increment not null primary key,
    Target_Board int not null,
    DCOMMENT_WRITER varchar(25) not null,
    DCOMMENT_CONTENT varchar(500) not null,
    DCOMMENT_REG_DATE datetime not null,
    DCOMMENT_UPDATE_DATE datetime
);