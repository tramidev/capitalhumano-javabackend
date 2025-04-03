--
-- PostgreSQL database dump
--

-- Dumped from database version 17.2
-- Dumped by pg_dump version 17.2

-- Started on 2025-04-03 17:53:06

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE IF EXISTS iot_db;
--
-- TOC entry 4924 (class 1262 OID 25664)
-- Name: iot_db; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE iot_db WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Spanish_Chile.1252';


ALTER DATABASE iot_db OWNER TO postgres;

\connect iot_db

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 220 (class 1259 OID 25674)
-- Name: companies; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.companies (
                                  id bigint NOT NULL,
                                  company_api_key character varying(255) NOT NULL,
                                  company_name character varying(255),
                                  company_status boolean,
                                  company_created_at bigint DEFAULT (EXTRACT(epoch FROM CURRENT_TIMESTAMP))::bigint NOT NULL
);


ALTER TABLE public.companies OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 25673)
-- Name: companies_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.companies_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.companies_id_seq OWNER TO postgres;

--
-- TOC entry 4925 (class 0 OID 0)
-- Dependencies: 219
-- Name: companies_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.companies_id_seq OWNED BY public.companies.id;


--
-- TOC entry 222 (class 1259 OID 25682)
-- Name: locations; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.locations (
                                  id bigint NOT NULL,
                                  location_name character varying(255),
                                  location_country character varying(255),
                                  location_city character varying(255),
                                  location_street character varying(255),
                                  location_number character varying(255),
                                  location_status boolean,
                                  location_created_at bigint DEFAULT (EXTRACT(epoch FROM CURRENT_TIMESTAMP))::bigint NOT NULL,
                                  company_id bigint
);


ALTER TABLE public.locations OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 25681)
-- Name: locations_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.locations_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.locations_id_seq OWNER TO postgres;

--
-- TOC entry 4926 (class 0 OID 0)
-- Dependencies: 221
-- Name: locations_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.locations_id_seq OWNED BY public.locations.id;


--
-- TOC entry 218 (class 1259 OID 25666)
-- Name: roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roles (
                              id bigint NOT NULL,
                              role_name character varying(255) NOT NULL,
                              role_description character varying(255) NOT NULL,
                              role_created_at bigint DEFAULT (EXTRACT(epoch FROM CURRENT_TIMESTAMP))::bigint NOT NULL
);


ALTER TABLE public.roles OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 25665)
-- Name: roles_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.roles_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.roles_id_seq OWNER TO postgres;

--
-- TOC entry 4927 (class 0 OID 0)
-- Dependencies: 217
-- Name: roles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.roles_id_seq OWNED BY public.roles.id;


--
-- TOC entry 229 (class 1259 OID 25747)
-- Name: sensor_data; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sensor_data (
                                    id bigint NOT NULL,
                                    id_sensor bigint,
                                    record_created_at bigint DEFAULT (EXTRACT(epoch FROM CURRENT_TIMESTAMP))::bigint NOT NULL,
                                    record character varying(255),
                                    metric character varying(255)
);


ALTER TABLE public.sensor_data OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 25746)
-- Name: sensor_data_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sensor_data_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sensor_data_id_seq OWNER TO postgres;

--
-- TOC entry 4928 (class 0 OID 0)
-- Dependencies: 228
-- Name: sensor_data_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sensor_data_id_seq OWNED BY public.sensor_data.id;


--
-- TOC entry 227 (class 1259 OID 25729)
-- Name: sensors; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sensors (
                                id bigint NOT NULL,
                                sensor_name character varying(255),
                                sensor_api_key character varying(255) NOT NULL,
                                sensor_location bigint,
                                sensor_company bigint,
                                sensor_created_at bigint DEFAULT (EXTRACT(epoch FROM CURRENT_TIMESTAMP))::bigint NOT NULL,
                                sensor_status boolean
);


ALTER TABLE public.sensors OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 25728)
-- Name: sensors_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sensors_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sensors_id_seq OWNER TO postgres;

--
-- TOC entry 4929 (class 0 OID 0)
-- Dependencies: 226
-- Name: sensors_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sensors_id_seq OWNED BY public.sensors.id;


--
-- TOC entry 224 (class 1259 OID 25695)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
                              id bigint NOT NULL,
                              first_name character varying(255) NOT NULL,
                              last_name character varying(255) NOT NULL,
                              username character varying(255) NOT NULL,
                              password character varying(255) NOT NULL,
                              user_email character varying(255) NOT NULL,
                              user_created_at bigint DEFAULT (EXTRACT(epoch FROM CURRENT_TIMESTAMP))::bigint NOT NULL,
                              user_modified_at bigint DEFAULT (EXTRACT(epoch FROM CURRENT_TIMESTAMP))::bigint NOT NULL,
                              user_expire_at bigint DEFAULT (EXTRACT(epoch FROM CURRENT_TIMESTAMP))::bigint NOT NULL,
                              user_status boolean,
                              user_company bigint
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 25694)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO postgres;

--
-- TOC entry 4930 (class 0 OID 0)
-- Dependencies: 223
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- TOC entry 225 (class 1259 OID 25713)
-- Name: users_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users_roles (
                                    user_id integer NOT NULL,
                                    role_id integer NOT NULL
);


ALTER TABLE public.users_roles OWNER TO postgres;

--
-- TOC entry 4726 (class 2604 OID 25757)
-- Name: companies id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.companies ALTER COLUMN id SET DEFAULT nextval('public.companies_id_seq'::regclass);


--
-- TOC entry 4728 (class 2604 OID 25781)
-- Name: locations id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.locations ALTER COLUMN id SET DEFAULT nextval('public.locations_id_seq'::regclass);


--
-- TOC entry 4724 (class 2604 OID 25806)
-- Name: roles id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles ALTER COLUMN id SET DEFAULT nextval('public.roles_id_seq'::regclass);


--
-- TOC entry 4736 (class 2604 OID 25820)
-- Name: sensor_data id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sensor_data ALTER COLUMN id SET DEFAULT nextval('public.sensor_data_id_seq'::regclass);


--
-- TOC entry 4734 (class 2604 OID 25836)
-- Name: sensors id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sensors ALTER COLUMN id SET DEFAULT nextval('public.sensors_id_seq'::regclass);


--
-- TOC entry 4730 (class 2604 OID 25872)
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- TOC entry 4909 (class 0 OID 25674)
-- Dependencies: 220
-- Data for Name: companies; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.companies (id, company_api_key, company_name, company_status, company_created_at) VALUES (4, 'ecxH_g1ToQVX-W8wOq4EbN3J0Zh1ADP6RBtEWHb19tc', 'Compañia 4', false, 1742173797) ON CONFLICT DO NOTHING;
INSERT INTO public.companies (id, company_api_key, company_name, company_status, company_created_at) VALUES (2, 'vNEr759dXjJcRy8PSz4OLGT9qBpgaeBUCB1jUcZzo3A', 'Compañia 1', true, 1742172013) ON CONFLICT DO NOTHING;
INSERT INTO public.companies (id, company_api_key, company_name, company_status, company_created_at) VALUES (3, 'DKVPDja9BHF9MNQCeSZhrtw4lW48pw1dqQQKcbV2-_A', 'Compañia 3', true, 1742172032) ON CONFLICT DO NOTHING;
INSERT INTO public.companies (id, company_api_key, company_name, company_status, company_created_at) VALUES (5, 'DKVPDja9BHF9MNQCeSZhrtw4lW48pw1dqQQKcbV2-_A', 'Compañia 5', false, 1743305285) ON CONFLICT DO NOTHING;


--
-- TOC entry 4911 (class 0 OID 25682)
-- Dependencies: 222
-- Data for Name: locations; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.locations (id, location_name, location_country, location_city, location_street, location_number, location_status, location_created_at, company_id) VALUES (1, 'Lugar 1', 'RM', 'Ciudad 1', 'Calle falsa', '123', true, 1742433289, 3) ON CONFLICT DO NOTHING;
INSERT INTO public.locations (id, location_name, location_country, location_city, location_street, location_number, location_status, location_created_at, company_id) VALUES (2, 'Lugar 2', 'RM', 'Ciudad 2', 'Calle falsa', '123', true, 1742433314, 2) ON CONFLICT DO NOTHING;
INSERT INTO public.locations (id, location_name, location_country, location_city, location_street, location_number, location_status, location_created_at, company_id) VALUES (3, 'Lugar 2', 'RM', 'Ciudad 2', 'Calle falsa', '123', false, 1742433320, 4) ON CONFLICT DO NOTHING;
INSERT INTO public.locations (id, location_name, location_country, location_city, location_street, location_number, location_status, location_created_at, company_id) VALUES (4, 'Lugar 4', 'RM', 'Ciudad 4', 'Calle falsa', '123', true, 1743307346, 3) ON CONFLICT DO NOTHING;
INSERT INTO public.locations (id, location_name, location_country, location_city, location_street, location_number, location_status, location_created_at, company_id) VALUES (5, 'Lugar 5', 'RM', 'Ciudad 5', 'Calle falsa', '123', true, 1743307418, 3) ON CONFLICT DO NOTHING;
INSERT INTO public.locations (id, location_name, location_country, location_city, location_street, location_number, location_status, location_created_at, company_id) VALUES (6, 'Lugar 6', 'RM', 'Ciudad 6', 'Calle falsa', '123', false, 1743307541, 3) ON CONFLICT DO NOTHING;


--
-- TOC entry 4907 (class 0 OID 25666)
-- Dependencies: 218
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.roles (id, role_name, role_description, role_created_at) VALUES (1, 'ROOT', 'SYSTEM ADMINISTRATOR', 1743281863) ON CONFLICT DO NOTHING;
INSERT INTO public.roles (id, role_name, role_description, role_created_at) VALUES (2, 'COMPANY_ADMIN', 'COMPANY ADMINISTRATOR', 1743281863) ON CONFLICT DO NOTHING;
INSERT INTO public.roles (id, role_name, role_description, role_created_at) VALUES (3, 'COMPANY_USER', 'COMPANY USER', 1743281863) ON CONFLICT DO NOTHING;


--
-- TOC entry 4918 (class 0 OID 25747)
-- Dependencies: 229
-- Data for Name: sensor_data; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.sensor_data (id, id_sensor, record_created_at, record, metric) VALUES (1, 2, 1743642108, '30.5', 'temp') ON CONFLICT DO NOTHING;
INSERT INTO public.sensor_data (id, id_sensor, record_created_at, record, metric) VALUES (2, 2, 1743642108, '30.5', 'humidity') ON CONFLICT DO NOTHING;
INSERT INTO public.sensor_data (id, id_sensor, record_created_at, record, metric) VALUES (3, 3, 1743642108, '30.5', 'temp') ON CONFLICT DO NOTHING;
INSERT INTO public.sensor_data (id, id_sensor, record_created_at, record, metric) VALUES (4, 3, 1743642108, '30.5', 'humidity') ON CONFLICT DO NOTHING;
INSERT INTO public.sensor_data (id, id_sensor, record_created_at, record, metric) VALUES (5, 4, 1743642108, '30.5', 'temp') ON CONFLICT DO NOTHING;
INSERT INTO public.sensor_data (id, id_sensor, record_created_at, record, metric) VALUES (6, 4, 1743642108, '30.5', 'humidity') ON CONFLICT DO NOTHING;
INSERT INTO public.sensor_data (id, id_sensor, record_created_at, record, metric) VALUES (7, 2, 1743712656, '24.4', 'temp') ON CONFLICT DO NOTHING;
INSERT INTO public.sensor_data (id, id_sensor, record_created_at, record, metric) VALUES (8, 2, 1743712656, '0.5', 'humidity') ON CONFLICT DO NOTHING;
INSERT INTO public.sensor_data (id, id_sensor, record_created_at, record, metric) VALUES (9, 2, 1743712656, '22.1', 'temp') ON CONFLICT DO NOTHING;
INSERT INTO public.sensor_data (id, id_sensor, record_created_at, record, metric) VALUES (10, 2, 1743712656, '0.6', 'humidity') ON CONFLICT DO NOTHING;
INSERT INTO public.sensor_data (id, id_sensor, record_created_at, record, metric) VALUES (11, 2, 1743712961, '37.4', 'temp') ON CONFLICT DO NOTHING;
INSERT INTO public.sensor_data (id, id_sensor, record_created_at, record, metric) VALUES (12, 2, 1743712961, '0.5', 'humidity') ON CONFLICT DO NOTHING;


--
-- TOC entry 4916 (class 0 OID 25729)
-- Dependencies: 227
-- Data for Name: sensors; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.sensors (id, sensor_name, sensor_api_key, sensor_location, sensor_company, sensor_created_at, sensor_status) VALUES (2, 'Plant on bedroom 1', '82ba1908-96c7-4a7b-854c-969a5e389909', 1, 2, 1743642108, true) ON CONFLICT DO NOTHING;
INSERT INTO public.sensors (id, sensor_name, sensor_api_key, sensor_location, sensor_company, sensor_created_at, sensor_status) VALUES (3, 'Plant on living', '82ba1908-96c7-4a7b-854c-82ba1908', 1, 2, 1743642108, true) ON CONFLICT DO NOTHING;
INSERT INTO public.sensors (id, sensor_name, sensor_api_key, sensor_location, sensor_company, sensor_created_at, sensor_status) VALUES (4, 'Entrance sensor', '969a5e38-96c7-4a7b-854c-969a5e389909', 1, 2, 1743642108, true) ON CONFLICT DO NOTHING;


--
-- TOC entry 4913 (class 0 OID 25695)
-- Dependencies: 224
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.users (id, first_name, last_name, username, password, user_email, user_created_at, user_modified_at, user_expire_at, user_status, user_company) VALUES (3, 'Administrador', 'Compañia 3', 'admincompany3', '$2b$12$3a6TexEB23O5ShDvD50Sm.XhVqMQbhjmy.uEa4XaWSGKsaOo5S7Fi', 'root2@root.cl', 1743282966, 1743282967, 1774818966, true, 3) ON CONFLICT DO NOTHING;
INSERT INTO public.users (id, first_name, last_name, username, password, user_email, user_created_at, user_modified_at, user_expire_at, user_status, user_company) VALUES (5, 'Administrador', 'Compañia 4', 'admincompany4', '$2a$10$pqKRhI84ZU5D7TiZvseREeF/VKze4jFR4zjy7jgTpXSTceKvT2IdK', 'root3@root.cl', 1743300854, 1743300854, 1774836854, false, 4) ON CONFLICT DO NOTHING;
INSERT INTO public.users (id, first_name, last_name, username, password, user_email, user_created_at, user_modified_at, user_expire_at, user_status, user_company) VALUES (1, 'Administrador', 'Sistema', 'root', '$2b$12$1Lrge8W7loZeOOv8SQ9/hewYe9SGXkKJRgfFb173dgnpC4Frjr3Gi', 'root@root.cl', 1743282814, 1743282814, 1774818814, true, 2) ON CONFLICT DO NOTHING;


--
-- TOC entry 4914 (class 0 OID 25713)
-- Dependencies: 225
-- Data for Name: users_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.users_roles (user_id, role_id) VALUES (1, 1) ON CONFLICT DO NOTHING;
INSERT INTO public.users_roles (user_id, role_id) VALUES (3, 2) ON CONFLICT DO NOTHING;
INSERT INTO public.users_roles (user_id, role_id) VALUES (5, 2) ON CONFLICT DO NOTHING;


--
-- TOC entry 4931 (class 0 OID 0)
-- Dependencies: 219
-- Name: companies_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.companies_id_seq', 1, false);


--
-- TOC entry 4932 (class 0 OID 0)
-- Dependencies: 221
-- Name: locations_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.locations_id_seq', 1, false);


--
-- TOC entry 4933 (class 0 OID 0)
-- Dependencies: 217
-- Name: roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.roles_id_seq', 1, false);


--
-- TOC entry 4934 (class 0 OID 0)
-- Dependencies: 228
-- Name: sensor_data_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sensor_data_id_seq', 12, true);


--
-- TOC entry 4935 (class 0 OID 0)
-- Dependencies: 226
-- Name: sensors_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sensors_id_seq', 1, false);


--
-- TOC entry 4936 (class 0 OID 0)
-- Dependencies: 223
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 1, false);


--
-- TOC entry 4741 (class 2606 OID 25759)
-- Name: companies company_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.companies
    ADD CONSTRAINT company_pkey PRIMARY KEY (id);


--
-- TOC entry 4743 (class 2606 OID 25783)
-- Name: locations location_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.locations
    ADD CONSTRAINT location_pkey PRIMARY KEY (id);


--
-- TOC entry 4739 (class 2606 OID 25808)
-- Name: roles role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- TOC entry 4753 (class 2606 OID 25838)
-- Name: sensors sensor_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sensors
    ADD CONSTRAINT sensor_pkey PRIMARY KEY (id);


--
-- TOC entry 4745 (class 2606 OID 25889)
-- Name: users user_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_email_key UNIQUE (user_email);


--
-- TOC entry 4747 (class 2606 OID 25891)
-- Name: users user_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_key UNIQUE (username);


--
-- TOC entry 4749 (class 2606 OID 25874)
-- Name: users user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- TOC entry 4751 (class 2606 OID 25717)
-- Name: users_roles users_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT users_roles_pkey PRIMARY KEY (user_id, role_id);


--
-- TOC entry 4754 (class 2606 OID 25795)
-- Name: locations company_location; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.locations
    ADD CONSTRAINT company_location FOREIGN KEY (company_id) REFERENCES public.companies(id);


--
-- TOC entry 4760 (class 2606 OID 25839)
-- Name: sensor_data record_sensor; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sensor_data
    ADD CONSTRAINT record_sensor FOREIGN KEY (id_sensor) REFERENCES public.sensors(id);


--
-- TOC entry 4758 (class 2606 OID 25850)
-- Name: sensors sensor_company; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sensors
    ADD CONSTRAINT sensor_company FOREIGN KEY (sensor_company) REFERENCES public.companies(id);


--
-- TOC entry 4759 (class 2606 OID 25861)
-- Name: sensors sensor_location; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sensors
    ADD CONSTRAINT sensor_location FOREIGN KEY (sensor_location) REFERENCES public.locations(id);


--
-- TOC entry 4755 (class 2606 OID 25892)
-- Name: users user_company; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_company FOREIGN KEY (user_company) REFERENCES public.companies(id);


--
-- TOC entry 4756 (class 2606 OID 25809)
-- Name: users_roles users_roles_role_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT users_roles_role_id_fkey FOREIGN KEY (role_id) REFERENCES public.roles(id);


--
-- TOC entry 4757 (class 2606 OID 25875)
-- Name: users_roles users_roles_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT users_roles_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


-- Completed on 2025-04-03 17:53:07

--
-- PostgreSQL database dump complete
--

