-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema dungeon_rpg
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema dungeon_rpg
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `dungeon_rpg` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `dungeon_rpg` ;

-- -----------------------------------------------------
-- Table `dungeon_rpg`.`classe`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dungeon_rpg`.`classe` (
  `idpersonagem_classe` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(45) NOT NULL,
  `descricao` TEXT NOT NULL,
  PRIMARY KEY (`idpersonagem_classe`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `dungeon_rpg`.`habilidade`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dungeon_rpg`.`habilidade` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(100) NOT NULL,
  `descricao` TEXT NULL DEFAULT NULL,
  `dano_base` INT NOT NULL DEFAULT '10',
  `bonus_ataque` INT NOT NULL DEFAULT '0',
  `elemento` VARCHAR(20) NOT NULL DEFAULT 'NEUTRO',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 14
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `dungeon_rpg`.`monstro`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dungeon_rpg`.`monstro` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(50) NULL DEFAULT NULL,
  `hp` INT NULL DEFAULT NULL,
  `ataque` INT NULL DEFAULT NULL,
  `defesa` INT NULL DEFAULT NULL,
  `xp_drop` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `dungeon_rpg`.`personagem`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dungeon_rpg`.`personagem` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(100) NOT NULL,
  `nivel` INT NOT NULL DEFAULT '1',
  `xp` INT NOT NULL DEFAULT '0',
  `hp` INT NOT NULL DEFAULT '100',
  `ataque` INT NOT NULL DEFAULT '10',
  `defesa` INT NOT NULL DEFAULT '5',
  `data_criacao` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `data_atualizacao` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `classe_idpersonagem_classe` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_personagem_classe1_idx` (`classe_idpersonagem_classe` ASC) VISIBLE,
  CONSTRAINT `fk_personagem_classe1`
    FOREIGN KEY (`classe_idpersonagem_classe`)
    REFERENCES `dungeon_rpg`.`classe` (`idpersonagem_classe`))
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `dungeon_rpg`.`personagem_habilidade`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dungeon_rpg`.`personagem_habilidade` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `personagem_id` INT NULL DEFAULT NULL,
  `habilidade_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `personagem_id` (`personagem_id` ASC) VISIBLE,
  INDEX `habilidade_id` (`habilidade_id` ASC) VISIBLE,
  CONSTRAINT `personagem_habilidade_ibfk_1`
    FOREIGN KEY (`personagem_id`)
    REFERENCES `dungeon_rpg`.`personagem` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `personagem_habilidade_ibfk_2`
    FOREIGN KEY (`habilidade_id`)
    REFERENCES `dungeon_rpg`.`habilidade` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `dungeon_rpg`.`quarto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `dungeon_rpg`.`quarto` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(100) NOT NULL,
  `descricao` TEXT NULL DEFAULT NULL,
  `porta_esquerda_id` INT NULL DEFAULT NULL,
  `porta_direita_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `porta_esquerda_id` (`porta_esquerda_id` ASC) VISIBLE,
  INDEX `porta_direita_id` (`porta_direita_id` ASC) VISIBLE,
  CONSTRAINT `quarto_ibfk_1`
    FOREIGN KEY (`porta_esquerda_id`)
    REFERENCES `dungeon_rpg`.`quarto` (`id`),
  CONSTRAINT `quarto_ibfk_2`
    FOREIGN KEY (`porta_direita_id`)
    REFERENCES `dungeon_rpg`.`quarto` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
