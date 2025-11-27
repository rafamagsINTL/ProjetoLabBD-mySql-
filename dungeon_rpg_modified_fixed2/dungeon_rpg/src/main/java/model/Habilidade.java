package model;

public class Habilidade {
    private int id;
    private String nome;
    private String descricao;
    private int bonusAtaque;
    private int danoBase;
    private Elemento elemento;

    public Habilidade(String nome, String descricao, int bonusAtaque, int danoBase, Elemento elemento) {
        this.nome = nome;
        this.descricao = descricao;
        this.bonusAtaque = bonusAtaque;
        this.danoBase = danoBase;
        this.elemento = elemento;
    }

    // ===== GETTERS =====

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getBonusAtaque() {
        return bonusAtaque;
    }

    public int getDanoBase() {
        return danoBase;
    }

    public Elemento getElemento() {
        return elemento;
    }

    public String getNomeCompleto() {
        return nome + " " + elemento.getEmoji();
    }

    // ===== SETTERS =====

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setBonusAtaque(int bonusAtaque) {
        this.bonusAtaque = bonusAtaque;
    }

    public void setDanoBase(int danoBase) {
        this.danoBase = danoBase;
    }

    public void setElemento(Elemento elemento) {
        this.elemento = elemento;
    }
}
