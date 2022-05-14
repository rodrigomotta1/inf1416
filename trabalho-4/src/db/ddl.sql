DROP TABLE IF EXISTS usuarios;

CREATE TABLE usuarios(
    login_name text primary key,
    total_acessos integer,
    total_consultas integer,
    grupo integer,
    nome text,
    certificado_digital blob,
    salt char(10),
    hex_senha text,
    bloqueado boolean,

    FOREIGN KEY (grupo) REFERENCES grupos(GID)
);

DROP TABLE IF EXISTS grupos;

CREATE TABLE grupos(
    nome text NOT NULL,
    GID integer primary key
);

DROP TABLE IF EXISTS registros;
CREATE TABLE registros(
    codigo_mensagem integer,
    data_hora timestamp,
    login_usuario text,
    nome_arquivo text,

    FOREIGN KEY (login_usuario) REFERENCES usuarios(login_name),
    FOREIGN KEY (codigo_mensagem) REFERENCES mensagens(codigo)
);

DROP TABLE IF EXISTS mensagens;
CREATE TABLE mensagens(
  mensagem text,
  codigo integer primary key
);