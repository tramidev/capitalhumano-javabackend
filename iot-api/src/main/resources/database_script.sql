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

--
-- TOC entry 4862 (class 0 OID 33406)
-- Dependencies: 220
-- Data for Name: companies; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.companies (id, company_api_key, company_name, company_status, company_created_at) VALUES (4, 'ecxH_g1ToQVX-W8wOq4EbN3J0Zh1ADP6RBtEWHb19tc', 'Compañia 4', false, 1742173797);
INSERT INTO public.companies (id, company_api_key, company_name, company_status, company_created_at) VALUES (2, 'vNEr759dXjJcRy8PSz4OLGT9qBpgaeBUCB1jUcZzo3A', 'Compañia 1', true, 1742172013);
INSERT INTO public.companies (id, company_api_key, company_name, company_status, company_created_at) VALUES (3, 'DKVPDja9BHF9MNQCeSZhrtw4lW48pw1dqQQKcbV2-_A', 'Compañia 3', true, 1742172032);
INSERT INTO public.companies (id, company_api_key, company_name, company_status, company_created_at) VALUES (5, 'DKVPDja9BHF9MNQCeSZhrtw4lW48pw1dqQQKcbV2-_A', 'Compañia 5', false, 1743305285);


--
-- TOC entry 4864 (class 0 OID 33414)
-- Dependencies: 222
-- Data for Name: locations; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.locations (id, location_name, location_country, location_city, location_street, location_number, location_status, location_created_at, company_id) VALUES (1, 'Lugar 1', 'RM', 'Ciudad 1', 'Calle falsa', '123', true, 1742433289, 3);
INSERT INTO public.locations (id, location_name, location_country, location_city, location_street, location_number, location_status, location_created_at, company_id) VALUES (2, 'Lugar 2', 'RM', 'Ciudad 2', 'Calle falsa', '123', true, 1742433314, 2);
INSERT INTO public.locations (id, location_name, location_country, location_city, location_street, location_number, location_status, location_created_at, company_id) VALUES (3, 'Lugar 2', 'RM', 'Ciudad 2', 'Calle falsa', '123', false, 1742433320, 4);
INSERT INTO public.locations (id, location_name, location_country, location_city, location_street, location_number, location_status, location_created_at, company_id) VALUES (4, 'Lugar 4', 'RM', 'Ciudad 4', 'Calle falsa', '123', true, 1743307346, 3);
INSERT INTO public.locations (id, location_name, location_country, location_city, location_street, location_number, location_status, location_created_at, company_id) VALUES (5, 'Lugar 5', 'RM', 'Ciudad 5', 'Calle falsa', '123', true, 1743307418, 3);
INSERT INTO public.locations (id, location_name, location_country, location_city, location_street, location_number, location_status, location_created_at, company_id) VALUES (6, 'Lugar 6', 'RM', 'Ciudad 6', 'Calle falsa', '123', false, 1743307541, 3);

--
-- TOC entry 4860 (class 0 OID 33398)
-- Dependencies: 218
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.roles (id, role_name, role_description, role_created_at) VALUES (1, 'ROOT', 'SYSTEM ADMINISTRATOR', 1743281863);
INSERT INTO public.roles (id, role_name, role_description, role_created_at) VALUES (2, 'COMPANY_ADMIN', 'COMPANY ADMINISTRATOR', 1743281863);
INSERT INTO public.roles (id, role_name, role_description, role_created_at) VALUES (3, 'COMPANY_USER', 'COMPANY USER', 1743281863);

--
-- TOC entry 4866 (class 0 OID 33427)
-- Dependencies: 224
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.users (id, first_name, last_name, username, password, user_email, user_created_at, user_modified_at, user_expire_at, user_status, user_company) VALUES (3, 'Administrador', 'Compañia 3', 'admincompany3', '$2b$12$3a6TexEB23O5ShDvD50Sm.XhVqMQbhjmy.uEa4XaWSGKsaOo5S7Fi', 'root2@root.cl', 1743282966, 1743282967, 1774818966, true, 3);
INSERT INTO public.users (id, first_name, last_name, username, password, user_email, user_created_at, user_modified_at, user_expire_at, user_status, user_company) VALUES (5, 'Administrador', 'Compañia 4', 'admincompany4', '$2a$10$pqKRhI84ZU5D7TiZvseREeF/VKze4jFR4zjy7jgTpXSTceKvT2IdK', 'root3@root.cl', 1743300854, 1743300854, 1774836854, false, 4);
INSERT INTO public.users (id, first_name, last_name, username, password, user_email, user_created_at, user_modified_at, user_expire_at, user_status, user_company) VALUES (1, 'Administrador', 'Sistema', 'root', '$2b$12$1Lrge8W7loZeOOv8SQ9/hewYe9SGXkKJRgfFb173dgnpC4Frjr3Gi', 'root@root.cl', 1743282814, 1743282814, 1774818814, true, 2);


--
-- TOC entry 4867 (class 0 OID 33445)
-- Dependencies: 225
-- Data for Name: users_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.users_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO public.users_roles (user_id, role_id) VALUES (3, 2);
INSERT INTO public.users_roles (user_id, role_id) VALUES (5, 2);
