package model;

import java.util.ArrayList;
import java.util.List;

public class Personagem {
    // ===== Integração com o banco =====
    private int id;
    private Classe classe;

    // ===== Atributos principais do jogo =====
    private String nome;
    private int nivel;
    private int xp;
    private int xpParaProximoNivel;
    private int hp;
    private int hpMaximo;
    private int ataque;
    private int defesa;
    private int pocoes;
    private List<Habilidade> habilidades;
    private HabilidadeCallback habilidadeCallback;

    public Personagem(String nome) {
        this.nome = nome;
        this.nivel = 1;
        this.xp = 0;
        this.xpParaProximoNivel = 50;
        this.hpMaximo = 100;
        this.hp = this.hpMaximo;
        this.ataque = 10;
        this.defesa = 5;
        this.pocoes = 1;
        this.habilidades = new ArrayList<>();

        // Habilidade inicial
        Habilidade basico = new Habilidade(
                "Golpe Básico",
                "Um ataque simples, mas confiável.",
                0,
                10,
                Elemento.NEUTRO
        );
        this.habilidades.add(basico);
    }

    // ===== Integração com o banco =====

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setHpMaximo(int hpMaximo) {
        this.hpMaximo = hpMaximo;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public void setDefesa(int defesa) {
        this.defesa = defesa;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void setXpParaProximoNivel(int xpParaProximoNivel) {
        this.xpParaProximoNivel = xpParaProximoNivel;
    }

    public void setPocoes(int pocoes) {
        this.pocoes = pocoes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    // ===== Getters usados pelo jogo / Main =====
    public String getNome() {
        return nome;
    }

    public int getNivel() {
        return nivel;
    }

    public int getXp() {
        return xp;
    }

    public int getXpParaProximoNivel() {
        return xpParaProximoNivel;
    }

    public int getHp() {
        return hp;
    }

    public int getHpMaximo() {
        return hpMaximo;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getDefesa() {
        return defesa;
    }

    public int getPocoes() {
        return pocoes;
    }

    public List<Habilidade> getHabilidades() {
        return habilidades;
    }

    public void setHabilidadeCallback(HabilidadeCallback callback) {
        this.habilidadeCallback = callback;
    }

    // ===== Lógica de combate =====
    public void usarHabilidade(Habilidade habilidade, Monstro monstro) {
        int danoBase = habilidade.getDanoBase() + this.ataque;

        // vantagem/desvantagem elemental
        double multiplicador = habilidade.getElemento().getMultiplicadorContra(monstro.getElemento());
        int dano = (int) Math.round(danoBase * multiplicador - monstro.getDefesa());
        if (dano < 1) {
            dano = 1;
        }

        monstro.receberDano(dano);

        String mensagem = nome + " usou " + habilidade.getNomeCompleto()
                + " em " + monstro.getNomeCompleto()
                + " causando " + dano + " de dano!";

        if (multiplicador > 1.0) {
            mensagem += " ✅ Super eficaz!";
        } else if (multiplicador < 1.0) {
            mensagem += " ❌ Pouco eficaz...";
        }

        System.out.println(mensagem);
    }

    public void receberDano(int dano) {
        int danoReal = Math.max(1, dano - this.defesa);
        this.hp -= danoReal;
        if (this.hp < 0) {
            this.hp = 0;
        }
        System.out.println(nome + " recebeu " + danoReal + " de dano!");
    }

    public boolean estaVivo() {
        return this.hp > 0;
    }

    public void usarPocao() {
        if (pocoes <= 0) {
            System.out.println("Você não tem poções!");
            return;
        }

        pocoes--;
        int cura = 50;
        int hpAntes = this.hp;
        this.hp = Math.min(this.hp + cura, this.hpMaximo);

        System.out.println("Você usou uma poção! HP: " + hpAntes + " → " + this.hp);
        System.out.println("Poções restantes: " + pocoes);
    }

    public void curar(int valor) {
        int hpAntes = this.hp;
        this.hp = Math.min(this.hp + valor, this.hpMaximo);
        System.out.println(nome + " foi curado: " + hpAntes + " → " + this.hp);
    }

    public void ganharPocao() {
        this.pocoes++;
        System.out.println("Você ganhou uma poção de vida! Total: " + pocoes);
    }

    // ===== XP e nível =====
    public void ganharXp(int xpGanho) {
        this.xp += xpGanho;
        System.out.println("+" + xpGanho + " XP! Total: " + this.xp + "/" + this.xpParaProximoNivel);

        while (this.xp >= this.xpParaProximoNivel) {
            this.xp -= this.xpParaProximoNivel;
            subirNivel();
        }
    }

    private void subirNivel() {
        this.nivel++;
        this.xpParaProximoNivel += 50;

        int hpAntigo = this.hpMaximo;
        int atkAntigo = this.ataque;
        int defAntigo = this.defesa;

        this.hpMaximo += 20;
        this.ataque += 4;
        this.defesa += 2;
        this.hp = this.hpMaximo;

        System.out.println("\n*** PARABÉNS! Você subiu para o nível " + nivel + "! ***");
        System.out.println("HP Máx: " + hpAntigo + " → " + hpMaximo);
        System.out.println("ATK: " + atkAntigo + " → " + ataque);
        System.out.println("DEF: " + defAntigo + " → " + defesa);
        System.out.println("XP para o próximo nível: " + xpParaProximoNivel);

        if (habilidadeCallback != null) {
            habilidadeCallback.oferecerEscolhaHabilidade(this);
        }
    }

    // ===== Habilidades =====
    public void aprenderHabilidade(Habilidade habilidade) {
        habilidades.add(habilidade);
        if (habilidade.getBonusAtaque() > 0) {
            this.ataque += habilidade.getBonusAtaque();
        }

        System.out.println("\n✨ Nova habilidade aprendida: " + habilidade.getNome() + " ✨");
        System.out.println(habilidade.getDescricao());
        if (habilidade.getBonusAtaque() > 0) {
            System.out.println("Ataque +" + habilidade.getBonusAtaque());
        }
    }

    @Override
    public String toString() {
        return nome + " - Nível " + nivel +
                " (HP: " + hp + "/" + hpMaximo +
                ", ATK: " + ataque +
                ", DEF: " + defesa +
                ", Poções: " + pocoes +
                ", XP: " + xp + "/" + xpParaProximoNivel + ")";
    }
}
