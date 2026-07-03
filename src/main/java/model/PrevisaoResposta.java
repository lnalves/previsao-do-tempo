package model;

import com.google.gson.annotations.SerializedName;

public class PrevisaoResposta{
    @SerializedName("current")
    private AtualAPI atual;

    @SerializedName("daily")
    private DiarioAPI diario;


    public AtualAPI getAtual() {
        return atual;
    }

    public void setAtual(AtualAPI atual) {
        this.atual = atual;
    }

    public DiarioAPI getDiario() {
        return diario;
    }

    public void setDiario(DiarioAPI diario) {
        this.diario = diario;
    }
}