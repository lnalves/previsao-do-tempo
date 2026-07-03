package model;

public class DadosDiarios {
    private int id;
    private String data;
    private Double temperaturaMaxima;
    private Double temperaturaMinima;
    private Double precipitacao;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Double getTemperaturaMaxima() {
        return temperaturaMaxima;
    }

    public void setTemperaturaMaxima(Double temperaturaMaxima) {
        this.temperaturaMaxima = temperaturaMaxima;
    }

    public Double getTemperaturaMinima() {
        return temperaturaMinima;
    }

    public void setTemperaturaMinima(Double temperaturaMinima) {
        this.temperaturaMinima = temperaturaMinima;
    }

    public Double getPrecipitacao() {
        return precipitacao;
    }

    public void setPrecipitacao(Double precipitacao) {
        this.precipitacao = precipitacao;
    }

    public DadosDiarios(String data, Double temperaturaMaxima, Double temperaturaMinima, Double precipitacao) {
        this.data = data;
        this.temperaturaMaxima = temperaturaMaxima;
        this.temperaturaMinima = temperaturaMinima;
        this.precipitacao = precipitacao;
    }

    @Override
    public String toString() {
        return "DadosDiarios{" +
                "id=" + id +
                ", data='" + data + '\'' +
                ", temperaturaMaxima=" + temperaturaMaxima +
                ", temperaturaMinima=" + temperaturaMinima +
                ", precipitacao=" + precipitacao +
                '}';
    }
}