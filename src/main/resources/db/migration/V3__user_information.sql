
drop table if exists user_information;

create table user_profile(
    id serial primary key ,
    first_name varchar(255),
    last_name varchar(255),
    date_of_birth date,
    joining_date date,
    end_date date,
    gender boolean,
    job_title varchar(255)
);

alter table users add column user_profile int;
alter table users add constraint fk_user_profile foreign key (user_profile) references user_profile(id) on delete cascade ;