package model;

public class PersonagemHabilidade {
    private int id;
    private int personagemId;
    private int habilidadeId;

    public PersonagemHabilidade() {
    }

    public PersonagemHabilidade(int personagemId, int habilidadeId) {
        this.personagemId = personagemId;
        this.habilidadeId = habilidadeId;
    }

    public PersonagemHabilidade(int id, int personagemId, int habilidadeId) {
        this.id = id;
        this.personagemId = personagemId;
        this.habilidadeId = habilidadeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPersonagemId() {
        return personagemId;
    }

    public void setPersonagemId(int personagemId) {
        this.personagemId = personagemId;
    }

    public int getHabilidadeId() {
        return habilidadeId;
    }

    public void setHabilidadeId(int habilidadeId) {
        this.habilidadeId = habilidadeId;
    }
}
