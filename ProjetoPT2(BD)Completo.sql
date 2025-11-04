DROP SCHEMA IF EXISTS dungeon_rpg;
CREATE SCHEMA IF NOT EXISTS `dungeon_rpg` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `dungeon_rpg`;
SET SQL_SAFE_UPDATES=0;

CREATE TABLE IF NOT EXISTS `classe` (
  `idpersonagem_classe` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(45) NOT NULL,
  `descricao` TEXT NOT NULL,
  PRIMARY KEY (`idpersonagem_classe`)
);

CREATE TABLE IF NOT EXISTS `habilidade` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(100) NOT NULL,
  `descricao` TEXT NULL DEFAULT NULL,
  `dano_base` INT NOT NULL DEFAULT 10,
  `bonus_ataque` INT NOT NULL DEFAULT 0,
  `elemento` VARCHAR(20) NOT NULL DEFAULT 'NEUTRO',
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `monstro` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(50) NULL DEFAULT NULL,
  `hp` INT NULL DEFAULT NULL,
  `ataque` INT NULL DEFAULT NULL,
  `defesa` INT NULL DEFAULT NULL,
  `xp_drop` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `personagem` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(100) NOT NULL,
  `nivel` INT NOT NULL DEFAULT 1,
  `xp` INT NOT NULL DEFAULT 0,
  `hp` INT NOT NULL DEFAULT 100,
  `ataque` INT NOT NULL DEFAULT 10,
  `defesa` INT NOT NULL DEFAULT 5,
  `classe_idpersonagem_classe` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_personagem_classe1_idx` (`classe_idpersonagem_classe`),
  CONSTRAINT `fk_personagem_classe1`
    FOREIGN KEY (`classe_idpersonagem_classe`) REFERENCES `classe` (`idpersonagem_classe`) ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS `personagem_habilidade` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `personagem_id` INT NULL DEFAULT NULL,
  `habilidade_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `personagem_id` (`personagem_id`),
  INDEX `habilidade_id` (`habilidade_id`),
  CONSTRAINT `personagem_habilidade_ibfk_1` FOREIGN KEY (`personagem_id`) REFERENCES `personagem` (`id`) ON DELETE CASCADE,
  CONSTRAINT `personagem_habilidade_ibfk_2` FOREIGN KEY (`habilidade_id`) REFERENCES `habilidade` (`id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `status_extra` (
  `personagem_id` INT NOT NULL,
  `intensidade` VARCHAR(45) NULL,
  `efeito` VARCHAR(45) NULL,
  `duracao_turnos` INT NULL,
  PRIMARY KEY (`personagem_id`),
  INDEX `fk_status_extra_personagem1_idx` (`personagem_id`),
  CONSTRAINT `fk_status_extra_personagem1` FOREIGN KEY (`personagem_id`) REFERENCES `personagem` (`id`)
);

INSERT INTO classe (nome, descricao) VALUES
  ('Guerreiro','Especialista em combate corpo a corpo'),
  ('Mago','Mestre das artes arcanas'),
  ('Arqueiro','Ataques à distância precisos');

INSERT INTO habilidade (nome, descricao, dano_base, bonus_ataque, elemento) VALUES
  ('Espadada','Golpe corpo a corpo',15,2,'FISICO'),
  ('Bola de Fogo','Projétil flamejante',25,5,'FOGO'),
  ('Flecha Dupla','Duas flechas rápidas',18,3,'VENTO');

INSERT INTO monstro (nome, hp, ataque, defesa, xp_drop) VALUES
  ('Slime',30,5,1,10),
  ('Goblin',45,8,3,20),
  ('Orc',90,14,6,50); 

INSERT INTO personagem (nome, nivel, xp, hp, ataque, defesa, classe_idpersonagem_classe) VALUES
  ('Alice',1,20,120,14,7,1),
  ('Bruno',1,80,90,10,4,2),
  ('Carla',1,10,100,12,5,3);

INSERT INTO personagem_habilidade (personagem_id, habilidade_id) VALUES (1,1),(2,2),(3,3);

INSERT INTO status_extra (personagem_id, intensidade, efeito, duracao_turnos) VALUES
  (1,'MÉDIA','SANGRAMENTO',3),
  (2,'ALTA','QUEIMADURA',2),
  (3,'BAIXA','LENTIDAO',0);

ALTER TABLE personagem_habilidade ADD CONSTRAINT uq_ph UNIQUE (personagem_id, habilidade_id);

ALTER TABLE status_extra
  DROP PRIMARY KEY,
  MODIFY efeito VARCHAR(45) NOT NULL,
  MODIFY duracao_turnos INT NOT NULL,
  ADD PRIMARY KEY (personagem_id, efeito);

INSERT INTO status_extra (personagem_id, intensidade, efeito, duracao_turnos) VALUES (1,'BAIXA','VENENO',2);

DROP FUNCTION IF EXISTS fn_calcular_dano_total;
DELIMITER $$
CREATE FUNCTION fn_calcular_dano_total(p_dano_base INT, p_bonus INT, p_nivel INT)
RETURNS INT DETERMINISTIC
BEGIN
  RETURN p_dano_base + p_bonus + (p_nivel * 2);
END$$
DELIMITER ;

DROP VIEW IF EXISTS v_personagem_detalhe;
CREATE VIEW v_personagem_detalhe AS
SELECT
  p.id, p.nome, c.nome AS classe, p.nivel, p.xp, p.hp, p.ataque, p.defesa,
  (SELECT COUNT(*) FROM personagem_habilidade ph WHERE ph.personagem_id=p.id) AS qtd_habilidades,
  (SELECT COUNT(*) FROM status_extra se WHERE se.personagem_id=p.id) AS qtd_status
FROM personagem p
JOIN classe c ON c.idpersonagem_classe=p.classe_idpersonagem_classe;

DROP PROCEDURE IF EXISTS sp_tick_status;
DELIMITER $$
CREATE PROCEDURE sp_tick_status()
BEGIN
  UPDATE status_extra SET duracao_turnos = GREATEST(duracao_turnos - 1, 0) WHERE personagem_id IS NOT NULL;
  DELETE FROM status_extra WHERE duracao_turnos <= 0;
END$$
DELIMITER ;

DROP TRIGGER IF EXISTS trg_personagem_lvlup;
DELIMITER $$
CREATE TRIGGER trg_personagem_lvlup
BEFORE UPDATE ON personagem
FOR EACH ROW
BEGIN
  IF NEW.xp >= 100 THEN
    SET NEW.nivel = NEW.nivel + FLOOR(NEW.xp/100);
    SET NEW.xp = MOD(NEW.xp,100);
  END IF;
END$$
DELIMITER ;

UPDATE personagem SET xp = xp + 120;
DELETE FROM status_extra WHERE duracao_turnos <= 0;

DROP USER IF EXISTS 'rpg_user'@'localhost';
CREATE USER 'rpg_user'@'localhost' IDENTIFIED BY 'rpg_pass_123';
GRANT SELECT, INSERT, UPDATE, DELETE, EXECUTE, SHOW VIEW ON dungeon_rpg.* TO 'rpg_user'@'localhost';
FLUSH PRIVILEGES;

SHOW FULL TABLES IN dungeon_rpg;
