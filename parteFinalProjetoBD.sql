DROP DATABASE IF EXISTS dungeon_rpg;
CREATE DATABASE dungeon_rpg;
USE dungeon_rpg;

-- Tabela CLASSE
CREATE TABLE classe (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    descricao TEXT
);

-- Tabela PERSONAGEM
CREATE TABLE personagem (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100),
    nivel INT,
    hp INT,
    ataque INT,
    defesa INT,
    classe_id INT
);


-- Tabela HABILIDADE
CREATE TABLE habilidade (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    dano_base INT NOT NULL,
    elemento VARCHAR(20) NOT NULL
);

-- Tabela intermediária PERSONAGEM_HABILIDADE (many-to-many)
CREATE TABLE personagem_habilidade (
    id INT AUTO_INCREMENT PRIMARY KEY,
    personagem_id INT NOT NULL,
    habilidade_id INT NOT NULL,
    FOREIGN KEY (personagem_id) REFERENCES personagem(id) ON DELETE CASCADE,
    FOREIGN KEY (habilidade_id) REFERENCES habilidade(id) ON DELETE CASCADE
);

-- Tabela COMBATE_LOG
CREATE TABLE combate_log (
    id INT AUTO_INCREMENT PRIMARY KEY,
    personagem_id INT NOT NULL,
    monstro_nome VARCHAR(100) NOT NULL,
    dano_causado INT NOT NULL,
    dano_recebido INT NOT NULL,
    resultado VARCHAR(20) NOT NULL,
    data_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (personagem_id) REFERENCES personagem(id) ON DELETE CASCADE
);
