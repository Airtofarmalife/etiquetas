CREATE TABLE empresa (
    id       INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nome     VARCHAR(30) NOT NULL,
    validade DATE not null,
    PRIMARY KEY (id)
);

CREATE TABLE kit (
    id                INTEGER NOT NULL PRIMARY KEY,
    descricao         VARCHAR(30) NOT NULL,
    codbarras         VARCHAR NOT NULL,
    quantidade        INTEGER,
    tamanho           VARCHAR NULL,
    envolucro         VARCHAR(50) NULL,
    total_envolucro   INTEGER NOT NULL,
    anexo             VARCHAR NULL,
    tipo_envolucro    VARCHAR NOT NULL
 );

CREATE TABLE PUBLIC.USUARIO (
	ID INTEGER NOT NULL AUTO_INCREMENT,
	TIPO CHARACTER VARYING NOT NULL,
	NOME CHARACTER VARYING NOT NULL,
	NOME_COMPLEMENTAR CHARACTER VARYING NOT NULL,
	SENHA CHARACTER VARYING NOT NULL,
	CONSTRAINT CONSTRAINT_2 PRIMARY KEY (ID)
);

INSERT INTO PUBLIC.usuario (nome,nome_complementar,senha,tipo) values ('Suldesc','','N2KKcryPwVbztVgHghDdoQ==','PADRAO');
INSERT INTO PUBLIC.usuario (nome,nome_complementar,senha,tipo) values ('Administrador','','9lXZXQUQtiOdeRAdpsGk7Q==','ADMINISTRADOR');
INSERT INTO PUBLIC.usuario (nome,nome_complementar,senha,tipo) values ('Gestor','','YXJn7/lTnRz1JNlKutawKw==','GESTOR');

CREATE TABLE etiqueta (
       id        			     INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
       id_kit                    INTEGER NOT NULL,
       empresa   			     VARCHAR(28) NOT NULL,
       nome_kit		  	         VARCHAR(30) NOT NULL,
       cod_barras 			     VARCHAR NOT NULL,
       tipo				         VARCHAR NOT NULL,
       lote	  			         VARCHAR(8) NOT NULL,
       ciclo	  			     VARCHAR(8) NOT NULL,
       esterelizacao		     DATE NOT NULL,
       validade			         DATE NOT NULL,
       responsavel_esterelizacao VARCHAR NOT NULL,
       temperatura			     VARCHAR,
       temperatura_tipo	         VARCHAR(9),
       temperatura_valor	     VARCHAR(2),
       outras                    VARCHAR,
       responsavel_preparo	     VARCHAR NOT NULL,
       quantidade			     INTEGER,
       observacao			     VARCHAR(28),
       horario_geracao		     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       envolucro                 VARCHAR(50) NOT NULL,
       total_envolucro           INTEGER NOT NULL,
       tipo_envolucro            VARCHAR NOT NULL,
       tamanho                   VARCHAR NULL
 );

CREATE TABLE periodo  (
    id       	 INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nome     	 VARCHAR NOT NULL,
    hora_inicial TIME NOT NULL,
    hora_final	 TIME NOT NULL
);

INSERT INTO PUBLIC.periodo (NOME, HORA_INICIAL, HORA_FINAL) VALUES('Manhã', '07:01:00', '13:00:00');
INSERT INTO PUBLIC.periodo (NOME, HORA_INICIAL, HORA_FINAL) VALUES('Tarde', '13:01:00', '19:00:00');
INSERT INTO PUBLIC.periodo (NOME, HORA_INICIAL, HORA_FINAL) VALUES('Noite', '19:01:00', '07:00:00');



CREATE SEQUENCE "KIT_SEQUENCE"
MINVALUE 1
MAXVALUE 999999999
INCREMENT BY 1
START WITH 1
NOCACHE
NOCYCLE;


CREATE VIEW RELATORIO AS
SELECT *
FROM
	(
		SELECT e.*,
			CASE
				WHEN FORMATDATETIME(HORARIO_GERACAO,'HH:mm:ss') BETWEEN p.HORA_INICIAL AND p.HORA_FINAL THEN p.NOME
				WHEN (
						(p.HORA_INICIAL > p.HORA_FINAL)
							AND (
								(
									FORMATDATETIME(HORARIO_GERACAO,'HH:mm:ss') between p.HORA_INICIAL AND '23:59:59'
									or
								 	FORMATDATETIME(HORARIO_GERACAO,'HH:mm:ss') between '00:00:00' AND p.HORA_FINAL
								 )
						)
					)THEN p.NOME
			END AS turno
		FROM ETIQUETA e, PERIODO p
	)

WHERE turno IS not NULL
ORDER BY NOME_KIT;