package model;

import java.time.LocalDateTime;

public class DadosHorarios {
    private int id;
    private LocalDateTime hora;
    private Double temperatura;
    private Double sensacaoTermica;
    private Double precipitacao;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getHora() {
        return hora;
    }

    public void setHora(LocalDateTime hora) {
        this.hora = hora;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public Double getSensacaoTermica() {
        return sensacaoTermica;
    }

    public void setSensacaoTermica(Double sensacaoTermica) {
        this.sensacaoTermica = sensacaoTermica;
    }

    public Double getPrecipitacao() {
        return precipitacao;
    }

    public void setPrecipitacao(Double precipitacao) {
        this.precipitacao = precipitacao;
    }

    public DadosHorarios(LocalDateTime hora, Double temperatura, Double sensacaoTermica, Double precipitacao) {
        this.hora = hora;
        this.temperatura = temperatura;
        this.sensacaoTermica = sensacaoTermica;
        this.precipitacao = precipitacao;
    }

    @Override
    public String toString() {
        return "DadosHorarios{" +
                "id=" + id +
                ", hora=" + hora +
                ", temperatura=" + temperatura +
                ", sensacaoTermica=" + sensacaoTermica +
                ", precipitacao=" + precipitacao +
                '}';
    }
}
