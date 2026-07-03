package model;

import com.google.gson.annotations.SerializedName;

public class DiarioAPI {

    @SerializedName("time")
    private String[] tempo;

    @SerializedName("temperature_2m_max")
    private double[] temperaturaMax;

    @SerializedName("temperature_2m_min")
    private double[] temperaturaMin;

    @SerializedName("precipitation_probability_max")
    private int[] precipitacaoMax;

    public String[] getTempo() {
        return tempo;
    }

    public void setTempo(String[] tempo) {
        this.tempo = tempo;
    }

    public double[] getTemperaturaMax() {
        return temperaturaMax;
    }

    public void setTemperaturaMax(double[] temperaturaMax) {
        this.temperaturaMax = temperaturaMax;
    }

    public double[] getTemperaturaMin() {
        return temperaturaMin;
    }

    public void setTemperaturaMin(double[] temperaturaMin) {
        this.temperaturaMin = temperaturaMin;
    }

    public int[] getPrecipitacaoMax() {
        return precipitacaoMax;
    }

    public void setPrecipitacaoMax(int[] precicipitacaoMax) {
        this.precipitacaoMax = precicipitacaoMax;
    }
}