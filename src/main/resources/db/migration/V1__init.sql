create sequence students_seq start with 1 increment by 50;
create sequence university_seq start with 1 increment by 50;

create table students (
    id bigint primary key default nextval('students_seq'),
    name varchar(255) not null,
    age int not null,
    email varchar(255) not null unique
);

create table university (
    id bigint primary key default nextval('university_seq'),
    name varchar(255) not null,
    location varchar(255) not null
);

create table students_university (
    student_id bigint not null,
    university_id bigint not null,
    primary key (student_id, university_id),
    foreign key (student_id) references students(id) on delete cascade,
    foreign key (university_id) references university(id) on delete cascade
);

insert into university (name, location) values ('Harvard University', 'Cambridge, MA');
insert into university (name, location) values ('UBB', 'Cluj-Napoca, Romania');
insert into university (name, location) values ('Stanford University', 'Stanford, CA');