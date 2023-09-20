create table if not exists hiring_post (
    id int auto_increment primary key,
    title varchar(255) not null,
    category varchar(255) not null,
    content varchar(255) not null,
    views int not null,
    img_url varchar(255),
    tag1 varchar(30),
    tag2 varchar(30),
    tag3 varchar(30),
    created_by varchar(255) not null,
    created_date datetime not null,
    last_updated_date datetime
);