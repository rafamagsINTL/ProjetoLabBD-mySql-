package model;

import java.time.LocalDateTime;

public class CombateLog {
    private int id;
    private int personagemId;
    private String monstroNome;
    private int danoCausado;
    private int danoRecebido;
    private String resultado;
    private LocalDateTime dataHora;

    public CombateLog() {
    }

    public CombateLog(int personagemId, String monstroNome,
                      int danoCausado, int danoRecebido, String resultado) {
        this.personagemId = personagemId;
        this.monstroNome = monstroNome;
        this.danoCausado = danoCausado;
        this.danoRecebido = danoRecebido;
        this.resultado = resultado;
    }

    public CombateLog(int id, int personagemId, String monstroNome,
                      int danoCausado, int danoRecebido,
                      String resultado, LocalDateTime dataHora) {
        this.id = id;
        this.personagemId = personagemId;
        this.monstroNome = monstroNome;
        this.danoCausado = danoCausado;
        this.danoRecebido = danoRecebido;
        this.resultado = resultado;
        this.dataHora = dataHora;
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

    public String getMonstroNome() {
        return monstroNome;
    }

    public void setMonstroNome(String monstroNome) {
        this.monstroNome = monstroNome;
    }

    public int getDanoCausado() {
        return danoCausado;
    }

    public void setDanoCausado(int danoCausado) {
        this.danoCausado = danoCausado;
    }

    public int getDanoRecebido() {
        return danoRecebido;
    }

    public void setDanoRecebido(int danoRecebido) {
        this.danoRecebido = danoRecebido;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
