
drop table if exists ocean.user_information;

create table ocean.user_profile(
    id serial primary key ,
    first_name varchar(255),
    last_name varchar(255),
    job_title varchar(255),
    gender boolean,
    date_of_birth date,
    joining_date date,
    end_date date,
    line_manager int
);


alter table ocean.users add column user_profile int;
alter table ocean.users add constraint fk_user_profile foreign key (user_profile) references ocean.user_profile(id) on delete cascade ;
alter table ocean.user_profile add constraint fk_line_manager foreign key (line_manager) references ocean.users(id) on delete set null ;