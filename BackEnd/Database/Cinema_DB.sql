--
-- PostgreSQL database dump
--

-- Dumped from database version 16.3
-- Dumped by pg_dump version 16.3

-- Started on 2024-10-05 12:11:34

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE "Cinema_web";
--
-- TOC entry 4909 (class 1262 OID 17218)
-- Name: Cinema_web; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE "Cinema_web" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Italian_Italy.1252';


ALTER DATABASE "Cinema_web" OWNER TO postgres;

\connect "Cinema_web"

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
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
-- TOC entry 221 (class 1259 OID 17247)
-- Name: utente; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.utente (
    nome character varying(50),
    cognome character varying(50),
    password character varying(50) NOT NULL,
    matricola character varying(50) DEFAULT NULL::character varying,
    numero_tessera integer NOT NULL,
    conferma_email character varying(10) DEFAULT false,
    cancellato boolean DEFAULT false NOT NULL,
    token_id character varying(50),
    email character varying(50) NOT NULL
);


ALTER TABLE public.utente OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 17445)
-- Name: Contatore_Tessere; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public."Contatore_Tessere"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 99999999
    CACHE 1;


ALTER SEQUENCE public."Contatore_Tessere" OWNER TO postgres;

--
-- TOC entry 4910 (class 0 OID 0)
-- Dependencies: 223
-- Name: Contatore_Tessere; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public."Contatore_Tessere" OWNED BY public.utente.numero_tessera;


--
-- TOC entry 215 (class 1259 OID 17219)
-- Name: abbonamento; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.abbonamento (
    abbonamento_id integer NOT NULL,
    n_accessi character varying(2) NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    utente character varying(50) NOT NULL,
    carrello_id integer,
    prezzo numeric(4,2)
);


ALTER TABLE public.abbonamento OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 17582)
-- Name: admin; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.admin (
    admin_id integer NOT NULL,
    nome character varying(50) NOT NULL,
    email character varying(50) NOT NULL,
    password character varying(50) NOT NULL,
    root boolean DEFAULT false NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE public.admin OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 17231)
-- Name: biglietto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.biglietto (
    id integer NOT NULL,
    n_colonna character varying(5) NOT NULL,
    n_riga character varying(5) NOT NULL,
    proiezione integer NOT NULL,
    comprato boolean DEFAULT false NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    utente character varying(50) NOT NULL,
    carrello_id integer
);


ALTER TABLE public.biglietto OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 17521)
-- Name: carrello; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.carrello (
    carrello_id integer NOT NULL,
    utente character varying(50) NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE public.carrello OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 17546)
-- Name: contatore_abbonamento; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.contatore_abbonamento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 99999999
    CACHE 1;


ALTER SEQUENCE public.contatore_abbonamento OWNER TO postgres;

--
-- TOC entry 4911 (class 0 OID 0)
-- Dependencies: 228
-- Name: contatore_abbonamento; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.contatore_abbonamento OWNED BY public.abbonamento.abbonamento_id;


--
-- TOC entry 232 (class 1259 OID 17587)
-- Name: contatore_admin_id; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.contatore_admin_id
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 9999999
    CACHE 1;


ALTER SEQUENCE public.contatore_admin_id OWNER TO postgres;

--
-- TOC entry 4912 (class 0 OID 0)
-- Dependencies: 232
-- Name: contatore_admin_id; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.contatore_admin_id OWNED BY public.admin.admin_id;


--
-- TOC entry 229 (class 1259 OID 17548)
-- Name: contatore_biglietto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.contatore_biglietto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999
    CACHE 1;


ALTER SEQUENCE public.contatore_biglietto OWNER TO postgres;

--
-- TOC entry 4913 (class 0 OID 0)
-- Dependencies: 229
-- Name: contatore_biglietto; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.contatore_biglietto OWNED BY public.biglietto.id;


--
-- TOC entry 227 (class 1259 OID 17544)
-- Name: contatore_carrello; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.contatore_carrello
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 99999999
    CACHE 1;


ALTER SEQUENCE public.contatore_carrello OWNER TO postgres;

--
-- TOC entry 4914 (class 0 OID 0)
-- Dependencies: 227
-- Name: contatore_carrello; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.contatore_carrello OWNED BY public.carrello.carrello_id;


--
-- TOC entry 216 (class 1259 OID 17225)
-- Name: film; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.film (
    film_id integer NOT NULL,
    durata character varying(50) NOT NULL,
    regista character varying(50) NOT NULL,
    titolo character varying(50) NOT NULL,
    link_trailer character varying(100),
    deleted boolean DEFAULT false NOT NULL,
    locandina character varying,
    trama character varying(500) DEFAULT false NOT NULL,
    genere character varying(50) DEFAULT false NOT NULL
);


ALTER TABLE public.film OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 17594)
-- Name: contatore_film_id; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.contatore_film_id
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999
    CACHE 1;


ALTER SEQUENCE public.contatore_film_id OWNER TO postgres;

--
-- TOC entry 4915 (class 0 OID 0)
-- Dependencies: 233
-- Name: contatore_film_id; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.contatore_film_id OWNED BY public.film.film_id;


--
-- TOC entry 222 (class 1259 OID 17417)
-- Name: metodopagamento; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.metodopagamento (
    metodo_id integer NOT NULL,
    n_carta character varying(20) NOT NULL,
    data_scadenza character varying(6) NOT NULL,
    cvv character(3) NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    utente character varying(50) NOT NULL,
    titolare character varying(100) NOT NULL
);


ALTER TABLE public.metodopagamento OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 17550)
-- Name: contatore_metodo_pag; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.contatore_metodo_pag
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 9999999
    CACHE 1;


ALTER SEQUENCE public.contatore_metodo_pag OWNER TO postgres;

--
-- TOC entry 4916 (class 0 OID 0)
-- Dependencies: 230
-- Name: contatore_metodo_pag; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.contatore_metodo_pag OWNED BY public.metodopagamento.metodo_id;


--
-- TOC entry 218 (class 1259 OID 17236)
-- Name: proiezione; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.proiezione (
    proiezione_id integer NOT NULL,
    data character varying(50) NOT NULL,
    film_id integer NOT NULL,
    sala integer NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    prezzo numeric(4,2) DEFAULT 8.50 NOT NULL
);


ALTER TABLE public.proiezione OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 17598)
-- Name: contatore_proiezione; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.contatore_proiezione
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999
    CACHE 1;


ALTER SEQUENCE public.contatore_proiezione OWNER TO postgres;

--
-- TOC entry 4917 (class 0 OID 0)
-- Dependencies: 234
-- Name: contatore_proiezione; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.contatore_proiezione OWNED BY public.proiezione.proiezione_id;


--
-- TOC entry 219 (class 1259 OID 17239)
-- Name: recensione; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.recensione (
    recensione_id integer NOT NULL,
    data character varying(50) NOT NULL,
    voto character varying(50) NOT NULL,
    testo character varying(500),
    film integer NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    titolo character varying(50) NOT NULL,
    utente character varying(50) NOT NULL
);


ALTER TABLE public.recensione OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 17472)
-- Name: contatore_recensione; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.contatore_recensione
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999
    CACHE 1;


ALTER SEQUENCE public.contatore_recensione OWNER TO postgres;

--
-- TOC entry 4918 (class 0 OID 0)
-- Dependencies: 225
-- Name: contatore_recensione; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.contatore_recensione OWNED BY public.recensione.recensione_id;


--
-- TOC entry 220 (class 1259 OID 17244)
-- Name: sala; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sala (
    id integer NOT NULL,
    numero_sala character varying(50) NOT NULL,
    num_posti character varying(50) NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


ALTER TABLE public.sala OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 17456)
-- Name: utente_numero_tessera1_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.utente_numero_tessera1_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.utente_numero_tessera1_seq OWNER TO postgres;

--
-- TOC entry 4919 (class 0 OID 0)
-- Dependencies: 224
-- Name: utente_numero_tessera1_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.utente_numero_tessera1_seq OWNED BY public.utente.numero_tessera;


--
-- TOC entry 4679 (class 2604 OID 17547)
-- Name: abbonamento abbonamento_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.abbonamento ALTER COLUMN abbonamento_id SET DEFAULT nextval('public.contatore_abbonamento'::regclass);


--
-- TOC entry 4702 (class 2604 OID 17588)
-- Name: admin admin_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.admin ALTER COLUMN admin_id SET DEFAULT nextval('public.contatore_admin_id'::regclass);


--
-- TOC entry 4685 (class 2604 OID 17549)
-- Name: biglietto id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.biglietto ALTER COLUMN id SET DEFAULT nextval('public.contatore_biglietto'::regclass);


--
-- TOC entry 4700 (class 2604 OID 17545)
-- Name: carrello carrello_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.carrello ALTER COLUMN carrello_id SET DEFAULT nextval('public.contatore_carrello'::regclass);


--
-- TOC entry 4681 (class 2604 OID 17595)
-- Name: film film_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.film ALTER COLUMN film_id SET DEFAULT nextval('public.contatore_film_id'::regclass);


--
-- TOC entry 4698 (class 2604 OID 17551)
-- Name: metodopagamento metodo_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.metodopagamento ALTER COLUMN metodo_id SET DEFAULT nextval('public.contatore_metodo_pag'::regclass);


--
-- TOC entry 4688 (class 2604 OID 17599)
-- Name: proiezione proiezione_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proiezione ALTER COLUMN proiezione_id SET DEFAULT nextval('public.contatore_proiezione'::regclass);


--
-- TOC entry 4691 (class 2604 OID 17473)
-- Name: recensione recensione_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recensione ALTER COLUMN recensione_id SET DEFAULT nextval('public.contatore_recensione'::regclass);


--
-- TOC entry 4695 (class 2604 OID 17457)
-- Name: utente numero_tessera; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utente ALTER COLUMN numero_tessera SET DEFAULT nextval('public.utente_numero_tessera1_seq'::regclass);


--
-- TOC entry 4884 (class 0 OID 17219)
-- Dependencies: 215
-- Data for Name: abbonamento; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.abbonamento VALUES (10, '10', false, 'samu94223@gmail.com', NULL, 28.00);
INSERT INTO public.abbonamento VALUES (11, '5', false, 'marciboss.23112000@gmail.com', NULL, 28.00);


--
-- TOC entry 4900 (class 0 OID 17582)
-- Dependencies: 231
-- Data for Name: admin; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.admin VALUES (1, 'root', 'root@admin.it', 'root', true, false);
INSERT INTO public.admin VALUES (2, 'Marcello', 'marci@admin.it', '1', false, false);
INSERT INTO public.admin VALUES (5, 'Luca', 'root2@admin.it', 'root2', false, false);


--
-- TOC entry 4886 (class 0 OID 17231)
-- Dependencies: 217
-- Data for Name: biglietto; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.biglietto VALUES (94, '5', '2', 88, true, false, 'samu94223@gmail.com', NULL);
INSERT INTO public.biglietto VALUES (95, '16', '2', 88, true, false, 'samu94223@gmail.com', NULL);
INSERT INTO public.biglietto VALUES (96, '10', '6', 88, true, false, 'pollameacase@gmail.com', NULL);
INSERT INTO public.biglietto VALUES (93, '11', '10', 55, true, true, 'pollameacase@gmail.com', NULL);
INSERT INTO public.biglietto VALUES (97, '10', '5', 62, false, true, 'marciboss.23112000@gmail.com', NULL);
INSERT INTO public.biglietto VALUES (98, '10', '5', 62, false, true, 'marciboss.23112000@gmail.com', NULL);
INSERT INTO public.biglietto VALUES (100, '12', '5', 62, true, false, 'marciboss.23112000@gmail.com', NULL);
INSERT INTO public.biglietto VALUES (102, '9', '4', 55, true, false, 'marciboss.23112000@gmail.com', NULL);
INSERT INTO public.biglietto VALUES (72, '10', '6', 63, true, false, 'samu94223@gmail.com', NULL);
INSERT INTO public.biglietto VALUES (73, '11', '6', 63, true, false, 'samu94223@gmail.com', NULL);
INSERT INTO public.biglietto VALUES (75, '9', '7', 63, true, false, 'samu94223@gmail.com', NULL);
INSERT INTO public.biglietto VALUES (76, '11', '7', 63, true, false, 'samu94223@gmail.com', NULL);
INSERT INTO public.biglietto VALUES (77, '12', '7', 63, true, false, 'samu94223@gmail.com', NULL);
INSERT INTO public.biglietto VALUES (78, '11', '10', 61, true, false, 'samu94223@gmail.com', NULL);
INSERT INTO public.biglietto VALUES (79, '13', '4', 61, true, false, 'samu94223@gmail.com', NULL);
INSERT INTO public.biglietto VALUES (99, '11', '5', 62, true, true, 'marciboss.23112000@gmail.com', NULL);
INSERT INTO public.biglietto VALUES (101, '11', '4', 62, true, false, 'marciboss.23112000@gmail.com', NULL);
INSERT INTO public.biglietto VALUES (103, '9', '4', 59, true, false, 'marciboss.23112000@gmail.com', NULL);
INSERT INTO public.biglietto VALUES (74, '5', '10', 63, true, false, 'pollameacase@gmail.com', NULL);
INSERT INTO public.biglietto VALUES (86, '9', '6', 62, true, false, 'pollameacase@gmail.com', NULL);
INSERT INTO public.biglietto VALUES (87, '1', '10', 87, true, false, 'samu94223@gmail.com', NULL);
INSERT INTO public.biglietto VALUES (88, '2', '1', 87, true, false, 'samu94223@gmail.com', NULL);
INSERT INTO public.biglietto VALUES (89, '19', '1', 87, true, false, 'samu94223@gmail.com', NULL);
INSERT INTO public.biglietto VALUES (90, '20', '10', 87, true, false, 'samu94223@gmail.com', NULL);
INSERT INTO public.biglietto VALUES (91, '5', '10', 64, true, false, 'pollameacase@gmail.com', NULL);
INSERT INTO public.biglietto VALUES (92, '10', '10', 64, true, false, 'pollameacase@gmail.com', NULL);
INSERT INTO public.biglietto VALUES (104, '10', '4', 59, true, false, 'marciboss.23112000@gmail.com', NULL);
INSERT INTO public.biglietto VALUES (105, '11', '4', 59, true, false, 'marciboss.23112000@gmail.com', NULL);
INSERT INTO public.biglietto VALUES (106, '12', '4', 59, true, false, 'marciboss.23112000@gmail.com', NULL);
INSERT INTO public.biglietto VALUES (107, '11', '5', 62, true, false, 'marciboss.23112000@gmail.com', NULL);


--
-- TOC entry 4895 (class 0 OID 17521)
-- Dependencies: 226
-- Data for Name: carrello; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.carrello VALUES (5, 'matteo@gmail.com', false);
INSERT INTO public.carrello VALUES (7, 'samu94223@gmail.com', false);
INSERT INTO public.carrello VALUES (8, 'pollameacase@gmail.com', false);
INSERT INTO public.carrello VALUES (9, 'marciboss.23112000@gmail.com', false);


--
-- TOC entry 4885 (class 0 OID 17225)
-- Dependencies: 216
-- Data for Name: film; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.film VALUES (4, '01:52:00', 'Jon Watts', 'Spider-Man: No Way Home', 'https://www.youtube.com/embed/rt-2cxAiPJk', false, 'locandina4.jpg', 'Con l''identità di Spider-Man ora rivelata, Peter Parker chiede aiuto al Dottor Strange. Quando un incantesimo va storto, avversari pericolosi di altri mondi iniziano ad apparire, costringendo Peter a scoprire cosa significa veramente essere Spider-Man.', 'Azione');
INSERT INTO public.film VALUES (5, '02:35:00', 'Matt Reeves', 'The Batman', 'https://www.youtube.com/embed/mqqft2x_Aa4', false, 'locandina5.jpg', 'Bruce Wayne, alias Batman, deve confrontarsi con l''Enigmista, un serial killer che minaccia Gotham City. Nel frattempo, scopre segreti oscuri sulla sua famiglia e deve decidere che tipo di eroe vuole essere.', 'Azione');
INSERT INTO public.film VALUES (1, '02:00:00', 'Christopher Nolan', 'Tenet', 'https://www.youtube.com/embed/L3pk_TBkihU', false, 'locandina1.jpg', 'Un agente segreto viene reclutato da un''organizzazione misteriosa chiamata Tenet per prevenire la Terza Guerra Mondiale. Viaggiando attraverso il tempo, deve affrontare una serie di sfide e scoprire i segreti di una tecnologia avanzata.', 'Azione');
INSERT INTO public.film VALUES (2, '02:30:00', 'Denis Villeneuve', 'Dune', 'https://www.youtube.com/embed/n9xhJrPXop4', false, 'locandina2.jpg', 'Paul Atreides, un giovane di talento e destino, deve viaggiare verso il pianeta più pericoloso dell''universo per garantire il futuro della sua famiglia e del suo popolo. Mentre forze malvagie esplodono in conflitto per la fornitura esclusiva delle risorse più preziose esistenti sul pianeta, solo coloro che possono superare la loro paura sopravvivranno.', 'Fantascienza');
INSERT INTO public.film VALUES (3, '02:10:00', 'Patty Jenkins', 'Wonder Woman 1984', 'https://www.youtube.com/embed/XW2E2Fnh52w', false, 'locandina3.jpg', 'Diana Prince, alias Wonder Woman, si trova negli anni''80 e affronta nuovi nemici: Max Lord e The Cheetah. In questo nuovo capitolo, deve imparare a vivere tra gli esseri umani e affrontare vecchie questioni irrisolte del passato.', 'Azione');
INSERT INTO public.film VALUES (6, '01:57:00', 'Chloé Zhao', 'Eternals', 'https://www.youtube.com/embed/x_me3xsvDgk', false, 'locandina6.jpg', 'Gli Eterni sono una razza di esseri immortali con poteri sovrumani che hanno vissuto sulla Terra segretamente per migliaia di anni. Quando i Deviants, i loro malvagi omologhi, tornano, gli Eterni devono riunirsi per proteggere l''umanità ancora una volta.', 'Fantascienza');
INSERT INTO public.film VALUES (13, '02:10:00', 'Richard Linklater', 'Before Sunrise', 'https://www.youtube.com/embed/9v6X-Dytlko', false, 'locandina11.jpg', 'Jesse e Céline, due giovani sconosciuti, si incontrano su un treno in Europa e decidono di trascorrere insieme una notte a Vienna. Mentre esplorano la città, sviluppano una connessione profonda e indimenticabile.', 'Romantico');
INSERT INTO public.film VALUES (11, '01:40:00', 'Lee Unkrich', 'Coco', 'https://www.youtube.com/embed/Ga6RYejo6Hk', false, 'locandina9.jpg', 'Il giovane Miguel sogna di diventare un musicista, ma la sua famiglia ha vietato la musica da generazioni. Determinato a dimostrare il suo talento, Miguel si ritrova nella Terra dei Morti, dove scoprirà il vero significato della famiglia.', 'Animazione');
INSERT INTO public.film VALUES (12, '01:45:00', 'Peter Docter', 'Inside Out', 'https://www.youtube.com/embed/seMwpP0yeu4', false, 'locandina10.jpg', 'Riley è una ragazzina di 11 anni che deve affrontare il trasloco della sua famiglia in una nuova città. Dentro la sua mente, le emozioni Gioia, Tristezza, Rabbia, Paura e Disgusto cercano di guidarla attraverso questa difficile transizione.', 'Animazione');
INSERT INTO public.film VALUES (14, '02:45:00', 'Ridley Scott', 'Gladiator', 'https://www.youtube.com/embed/owK1qxDselE', false, 'locandina12.jpg', 'Il generale romano Massimo Decimo Meridio viene tradito e la sua famiglia viene assassinata dal corrotto figlio dell''imperatore, Commodo. Ridotto in schiavitù, Massimo diventa un gladiatore e cerca vendetta contro Commodo.', 'Storico');
INSERT INTO public.film VALUES (15, '02:14:00', 'Mel Gibson', 'Braveheart', 'https://www.youtube.com/embed/wj0I8xVTV18', false, 'locandina13.jpg', 'William Wallace guida una rivolta coraggiosa contro l''oppressione inglese nella Scozia del XIII secolo. La sua determinazione e il suo sacrificio ispirano i suoi compatrioti a lottare per la libertà.', 'Storico');
INSERT INTO public.film VALUES (7, '02:12:00', 'James Gunn', 'The Suicide Squad', 'https://www.youtube.com/embed/zwxw4W9ZGWM?si=qcQ35OfwzbqUJx0G', false, 'locandina7.jpg', 'Un gruppo di supercriminali viene reclutato da Amanda Waller per una missione suicida sull''isola di Corto Maltese. Equipaggiati con le armi più avanzate, devono distruggere un laboratorio nazista e affrontare pericoli mortali.', 'Azione');
INSERT INTO public.film VALUES (29, '11:11:11', 'gj', 'ffffffffff', 'yyiyyi', false, 'locandina15.jpeg', 'gyuuuuuuuuuuuuuuuu', 'animazione');


--
-- TOC entry 4891 (class 0 OID 17417)
-- Dependencies: 222
-- Data for Name: metodopagamento; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.metodopagamento VALUES (36, '1234 5678 9012 3455', '09/28', '222', false, 'samu94223@gmail.com', 'Samuele Luciani');
INSERT INTO public.metodopagamento VALUES (37, '1111 1111 1111 1111', '11/27', '111', false, 'pollameacase@gmail.com', 'Simone Luciani');
INSERT INTO public.metodopagamento VALUES (38, '1111 1111 1111 1111', '01/25', '111', true, 'marciboss.23112000@gmail.com', 'Marcello Luciani');
INSERT INTO public.metodopagamento VALUES (39, '1111 1111 1111 1111', '06/25', '111', false, 'marciboss.23112000@gmail.com', 'marcello luciani');


--
-- TOC entry 4887 (class 0 OID 17236)
-- Dependencies: 218
-- Data for Name: proiezione; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.proiezione VALUES (53, '20/07/2024 16:00', 5, 1, false, 8.50);
INSERT INTO public.proiezione VALUES (69, '21/07/2024 16:00', 7, 4, false, 8.50);
INSERT INTO public.proiezione VALUES (70, '21/07/2024 18:00', 7, 4, false, 8.50);
INSERT INTO public.proiezione VALUES (71, '21/07/2024 20:00', 7, 4, false, 8.50);
INSERT INTO public.proiezione VALUES (75, '27/07/2024 16:00', 12, 1, false, 8.50);
INSERT INTO public.proiezione VALUES (76, '27/07/2024 16:00', 3, 1, false, 8.50);
INSERT INTO public.proiezione VALUES (55, '21/07/2024 20:00', 5, 1, false, 8.50);
INSERT INTO public.proiezione VALUES (59, '20/07/2024 14:00', 11, 2, false, 8.50);
INSERT INTO public.proiezione VALUES (60, '20/07/2024 18:00', 11, 2, false, 8.50);
INSERT INTO public.proiezione VALUES (64, '20/07/2024 22:00', 11, 1, false, 8.50);
INSERT INTO public.proiezione VALUES (61, '20/07/2024 22:00', 11, 2, false, 8.50);
INSERT INTO public.proiezione VALUES (62, '20/07/2024 14:00', 11, 1, false, 8.50);
INSERT INTO public.proiezione VALUES (63, '20/07/2024 18:00', 11, 1, false, 8.50);
INSERT INTO public.proiezione VALUES (84, '22/07/2024 16:00', 1, 1, false, 8.50);
INSERT INTO public.proiezione VALUES (85, '23/07/2024 16:00', 8, 2, false, 8.50);
INSERT INTO public.proiezione VALUES (86, '24/07/2024 16:00', 14, 3, false, 8.50);
INSERT INTO public.proiezione VALUES (87, '21/07/2024 16:00', 2, 3, false, 8.50);
INSERT INTO public.proiezione VALUES (88, '21/07/2024 18:00', 2, 3, false, 8.50);
INSERT INTO public.proiezione VALUES (89, '21/07/2024 20:00', 2, 3, false, 8.50);
INSERT INTO public.proiezione VALUES (90, '21/07/2024 18:00', 5, 1, false, 8.50);
INSERT INTO public.proiezione VALUES (54, '20/07/2024 18:00', 5, 3, false, 8.50);
INSERT INTO public.proiezione VALUES (91, '21/07/2024 16:00', 5, 2, false, 8.50);
INSERT INTO public.proiezione VALUES (92, '21/07/2024 18:00', 5, 2, false, 8.50);
INSERT INTO public.proiezione VALUES (93, '21/07/2024 20:00', 5, 2, false, 8.50);


--
-- TOC entry 4888 (class 0 OID 17239)
-- Dependencies: 219
-- Data for Name: recensione; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.recensione VALUES (31, '20/07/2024', '1', 'Questo film non ha alcuno spessore, una delusione!', 11, true, 'Film deludente', 'marciboss.23112000@gmail.com');
INSERT INTO public.recensione VALUES (25, '19/07/2024', '5', 'BELLO BELLO', 2, true, 'BELLO', 'samu94223@gmail.com');
INSERT INTO public.recensione VALUES (22, '03/07/2024', '3', 'Il film è la stessa trama ripetuta...Una delusione :c', 11, false, 'Troppo Scontato', 'matteo@gmail.com');
INSERT INTO public.recensione VALUES (27, '19/07/2024', '3', 'pipo', 11, true, 'Le mie palle', 'pollameacase@gmail.com');
INSERT INTO public.recensione VALUES (21, '19/07/2024', '5', 'Un bellissimo film! Aspetto il secondo con ansia.', 11, true, 'Ottimo Film per bambini', 'samu94223@gmail.com');


--
-- TOC entry 4889 (class 0 OID 17244)
-- Dependencies: 220
-- Data for Name: sala; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.sala VALUES (1, '1', '200', false);
INSERT INTO public.sala VALUES (2, '2', '200', false);
INSERT INTO public.sala VALUES (3, '3', '200', false);
INSERT INTO public.sala VALUES (4, '4', '200', false);


--
-- TOC entry 4890 (class 0 OID 17247)
-- Dependencies: 221
-- Data for Name: utente; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.utente VALUES ('Matteo', 'Luciani', '1', NULL, 26, 'false', false, '2WUKG', 'matteo@gmail.com');
INSERT INTO public.utente VALUES ('Samuele', 'Luciani', '0309wppi', NULL, 33, 'true', false, 'H1uQz', 'samu94223@gmail.com');
INSERT INTO public.utente VALUES ('Simone', 'Luciani', 'CanCasa44', NULL, 32, 'true', false, '2tczV', 'pollameacase@gmail.com');
INSERT INTO public.utente VALUES ('gino', 'gino', '1', NULL, 24, 'true', true, 'kNejo', 'gino@gmail.com');
INSERT INTO public.utente VALUES ('Marcello', 'Luciani', '1234567', NULL, 34, 'true', false, 'Szvkw', 'marciboss.23112000@gmail.com');


--
-- TOC entry 4920 (class 0 OID 0)
-- Dependencies: 223
-- Name: Contatore_Tessere; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public."Contatore_Tessere"', 1, false);


--
-- TOC entry 4921 (class 0 OID 0)
-- Dependencies: 228
-- Name: contatore_abbonamento; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.contatore_abbonamento', 11, true);


--
-- TOC entry 4922 (class 0 OID 0)
-- Dependencies: 232
-- Name: contatore_admin_id; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.contatore_admin_id', 5, true);


--
-- TOC entry 4923 (class 0 OID 0)
-- Dependencies: 229
-- Name: contatore_biglietto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.contatore_biglietto', 107, true);


--
-- TOC entry 4924 (class 0 OID 0)
-- Dependencies: 227
-- Name: contatore_carrello; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.contatore_carrello', 10, true);


--
-- TOC entry 4925 (class 0 OID 0)
-- Dependencies: 233
-- Name: contatore_film_id; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.contatore_film_id', 29, true);


--
-- TOC entry 4926 (class 0 OID 0)
-- Dependencies: 230
-- Name: contatore_metodo_pag; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.contatore_metodo_pag', 39, true);


--
-- TOC entry 4927 (class 0 OID 0)
-- Dependencies: 234
-- Name: contatore_proiezione; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.contatore_proiezione', 93, true);


--
-- TOC entry 4928 (class 0 OID 0)
-- Dependencies: 225
-- Name: contatore_recensione; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.contatore_recensione', 31, true);


--
-- TOC entry 4929 (class 0 OID 0)
-- Dependencies: 224
-- Name: utente_numero_tessera1_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.utente_numero_tessera1_seq', 36, true);


--
-- TOC entry 4718 (class 2606 OID 17351)
-- Name: utente MATRICOLA_UNICA; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utente
    ADD CONSTRAINT "MATRICOLA_UNICA" UNIQUE (matricola);


--
-- TOC entry 4706 (class 2606 OID 17353)
-- Name: abbonamento abbonamento_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.abbonamento
    ADD CONSTRAINT abbonamento_pkey PRIMARY KEY (abbonamento_id);


--
-- TOC entry 4728 (class 2606 OID 17586)
-- Name: admin admin_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.admin
    ADD CONSTRAINT admin_pkey PRIMARY KEY (admin_id);


--
-- TOC entry 4710 (class 2606 OID 17361)
-- Name: biglietto biglietto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.biglietto
    ADD CONSTRAINT biglietto_pkey PRIMARY KEY (id);


--
-- TOC entry 4726 (class 2606 OID 17525)
-- Name: carrello carrello_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.carrello
    ADD CONSTRAINT carrello_pkey PRIMARY KEY (carrello_id);


--
-- TOC entry 4720 (class 2606 OID 17498)
-- Name: utente email_unica; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utente
    ADD CONSTRAINT email_unica UNIQUE (email);


--
-- TOC entry 4730 (class 2606 OID 17590)
-- Name: admin email_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.admin
    ADD CONSTRAINT email_unique UNIQUE (email);


--
-- TOC entry 4708 (class 2606 OID 17357)
-- Name: film film_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.film
    ADD CONSTRAINT film_pkey PRIMARY KEY (film_id);


--
-- TOC entry 4724 (class 2606 OID 17421)
-- Name: metodopagamento metodopagamento_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.metodopagamento
    ADD CONSTRAINT metodopagamento_pkey PRIMARY KEY (metodo_id);


--
-- TOC entry 4712 (class 2606 OID 17363)
-- Name: proiezione proiezione_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proiezione
    ADD CONSTRAINT proiezione_pkey PRIMARY KEY (proiezione_id);


--
-- TOC entry 4714 (class 2606 OID 17365)
-- Name: recensione recensione_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recensione
    ADD CONSTRAINT recensione_pkey PRIMARY KEY (recensione_id);


--
-- TOC entry 4716 (class 2606 OID 17367)
-- Name: sala sala_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sala
    ADD CONSTRAINT sala_pkey PRIMARY KEY (id);


--
-- TOC entry 4722 (class 2606 OID 17500)
-- Name: utente utente_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_pkey PRIMARY KEY (email);


--
-- TOC entry 4737 (class 2606 OID 17386)
-- Name: recensione FK_RECENSIONE_FILM; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recensione
    ADD CONSTRAINT "FK_RECENSIONE_FILM" FOREIGN KEY (film) REFERENCES public.film(film_id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 4738 (class 2606 OID 17501)
-- Name: recensione FK_RECENSIONE_UTENTE; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.recensione
    ADD CONSTRAINT "FK_RECENSIONE_UTENTE" FOREIGN KEY (utente) REFERENCES public.utente(email) ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;


--
-- TOC entry 4731 (class 2606 OID 17536)
-- Name: abbonamento fk_abbonamento_carrello; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.abbonamento
    ADD CONSTRAINT fk_abbonamento_carrello FOREIGN KEY (carrello_id) REFERENCES public.carrello(carrello_id) ON UPDATE CASCADE ON DELETE SET NULL NOT VALID;


--
-- TOC entry 4732 (class 2606 OID 17511)
-- Name: abbonamento fk_abbonamento_utente; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.abbonamento
    ADD CONSTRAINT fk_abbonamento_utente FOREIGN KEY (utente) REFERENCES public.utente(email) ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;


--
-- TOC entry 4733 (class 2606 OID 17531)
-- Name: biglietto fk_biglietto_carrello; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.biglietto
    ADD CONSTRAINT fk_biglietto_carrello FOREIGN KEY (carrello_id) REFERENCES public.carrello(carrello_id) ON UPDATE CASCADE ON DELETE SET NULL NOT VALID;


--
-- TOC entry 4734 (class 2606 OID 17411)
-- Name: biglietto fk_biglietto_proiezione; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.biglietto
    ADD CONSTRAINT fk_biglietto_proiezione FOREIGN KEY (proiezione) REFERENCES public.proiezione(proiezione_id) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- TOC entry 4735 (class 2606 OID 17506)
-- Name: biglietto fk_biglietto_utente; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.biglietto
    ADD CONSTRAINT fk_biglietto_utente FOREIGN KEY (utente) REFERENCES public.utente(email) ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;


--
-- TOC entry 4740 (class 2606 OID 17526)
-- Name: carrello fk_carrello_utente; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.carrello
    ADD CONSTRAINT fk_carrello_utente FOREIGN KEY (utente) REFERENCES public.utente(email) ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;


--
-- TOC entry 4736 (class 2606 OID 17954)
-- Name: proiezione fk_film_film_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.proiezione
    ADD CONSTRAINT fk_film_film_id FOREIGN KEY (film_id) REFERENCES public.film(film_id) ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;


--
-- TOC entry 4739 (class 2606 OID 17516)
-- Name: metodopagamento fk_metodo_utente; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.metodopagamento
    ADD CONSTRAINT fk_metodo_utente FOREIGN KEY (utente) REFERENCES public.utente(email) ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;


-- Completed on 2024-10-05 12:11:34

--
-- PostgreSQL database dump complete
--

