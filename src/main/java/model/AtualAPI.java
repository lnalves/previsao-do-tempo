package model;

import com.google.gson.annotations.SerializedName;

public class AtualAPI {

    @SerializedName("time")
    private String tempo;

    @SerializedName("temperature_2m")
    private double temperatura;

    @SerializedName("apparent_temperature")
    private double sensacaoTermica;

    @SerializedName("precipitation")
    private double precipitacao;

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public double getSensacaoTermica() {
        return sensacaoTermica;
    }

    public void setSensacaoTermica(double sensacaoTermica) {
        this.sensacaoTermica = sensacaoTermica;
    }

    public double getPrecipitacao() {
        return precipitacao;
    }

    public void setPrecipitacao(double precipitacao) {
        this.precipitacao = precipitacao;
    }
}