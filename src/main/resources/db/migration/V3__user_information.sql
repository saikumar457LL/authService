
drop table if exists user_information;

create table user_profile(
    id serial primary key ,
    firstName varchar(255),
    lastName varchar(255),
    dateOfBirth date,
    joiningDate date,
    endDate date,
    gender boolean
);

alter table users add column user_profile int;
alter table users add constraint fk_user_profile foreign key (user_profile) references user_profile(id) on delete cascade ;