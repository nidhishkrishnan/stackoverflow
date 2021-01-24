CREATE TABLE public.users (
      u_id SERIAL,
      u_email character varying(100) NOT NULL,
      u_first_name character varying(50) NOT NULL,
      u_last_name character varying(100) NOT NULL
);


--ALTER TABLE public.users OWNER TO postgres;

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_id__pk PRIMARY KEY (u_id);


INSERT INTO public.users (u_id, u_email, u_first_name, u_last_name) VALUES
(21, 'user1@some.com', 'Manu','Sasi'),
(22, 'user2@some.com', 'John','Duke'),
(23, 'user3@some.com', 'Susan','Bing');
