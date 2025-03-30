drop table public.users_roles;
drop table public.users;
drop table public.roles;
drop table public.records;
drop table public.sensors;
drop table public.locations;
drop table public.companies;

--CREATE DATABASE iotdb;

create table public.roles (
	id serial4 not null,
	role_name varchar(30) not null,
	role_description varchar(100) not null,
	role_created_at BIGINT DEFAULT (EXTRACT(EPOCH FROM CURRENT_TIMESTAMP))::BIGINT NOT NULL,
	constraint role_pkey primary key (id)
);

INSERT INTO public.roles(
role_name, role_description, role_created_at)
VALUES ('ROOT', 'SYSTEM ADMINISTRATOR', EXTRACT(EPOCH FROM CURRENT_TIMESTAMP));

INSERT INTO public.roles(
role_name, role_description, role_created_at)
VALUES ('COMPANY_ADMIN', 'COMPANY ADMINISTRATOR', EXTRACT(EPOCH FROM CURRENT_TIMESTAMP));

INSERT INTO public.roles(
role_name, role_description, role_created_at)
VALUES ('COMPANY_USER', 'COMPANY USER', EXTRACT(EPOCH FROM CURRENT_TIMESTAMP));
	
create table public.companies (
	id serial4 not null,
	company_api_key varchar(100) not null,
	company_name varchar(20),	
	company_status BOOLEAN,
	company_created_at BIGINT DEFAULT (EXTRACT(EPOCH FROM CURRENT_TIMESTAMP))::BIGINT NOT NULL,
	constraint company_pkey primary key (id)
	
);
 
create table public.locations (
	id serial4 not null,
	location_name varchar(30),
	location_country varchar(50),
	location_city varchar(50),
	location_street varchar(50),
	location_number varchar(50),
	location_status BOOLEAN,
	location_created_at BIGINT DEFAULT (EXTRACT(EPOCH FROM CURRENT_TIMESTAMP))::BIGINT NOT NULL,
	company_id Int4,
	constraint location_pkey primary key (id),
	constraint company_location foreign key (company_id) references public.companies (id)
);

create table public.users (
	id serial4 not null,
	first_name varchar(20) not null,
	last_name varchar(10) not null,
	username varchar(10) not null,
	password varchar(100) not null,
	user_email varchar(100) not null,
	user_created_at BIGINT DEFAULT (EXTRACT(EPOCH FROM CURRENT_TIMESTAMP))::BIGINT NOT NULL,
	user_modified_at BIGINT DEFAULT (EXTRACT(EPOCH FROM CURRENT_TIMESTAMP))::BIGINT NOT NULL,
	user_expire_at BIGINT DEFAULT (EXTRACT(EPOCH FROM CURRENT_TIMESTAMP))::BIGINT NOT NULL,
	user_status BOOLEAN,
	user_company Int4,
	constraint user_pkey primary key (id),
	constraint user_key unique (username),
	constraint user_email_key unique (user_email),
	constraint user_company foreign key (user_company) references public.companies(id)
);

create table public.users_roles (
	user_id Int4,
	role_id Int4,
	foreign key (user_id) references users(id),
	foreign key (role_id) references roles(id),
	primary key (user_id, role_id)
);

create table public.sensors (
	id serial4 not null,
	sensor_name varchar(40) not null,
	sensor_api_key varchar(100) not null,
	sensor_location Int4,
	sensor_company Int4,
	sensor_created_at BIGINT DEFAULT (EXTRACT(EPOCH FROM CURRENT_TIMESTAMP))::BIGINT NOT NULL,
	sensor_status BOOLEAN,
	constraint sensor_pkey primary key (id),
	constraint sensor_location foreign key (sensor_location) references public.locations(id),
	constraint sensor_company foreign key (sensor_company) references public.companies (id)
);

create table public.records (
	id_record serial4 not null,
	id_sensor Int4,
	record JSONB,
	record_created_at BIGINT DEFAULT (EXTRACT(EPOCH FROM CURRENT_TIMESTAMP))::BIGINT NOT NULL,
	constraint record_sensor foreign key (id_sensor) references public.sensors(id)
);
