package model;

import java.sql.Date;

public class Localizacao {
    private int id;
    private String cidade;
    private Double latitude;
    private Double longitude;
    private Date dataHora;

    public Localizacao(int id, String cidade, Double latitude, Double longitude, Date dataHora) {
        this.id = id;
        this.cidade = cidade;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dataHora = dataHora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    @Override
    public String toString() {
        return cidade + " - " + " (" + latitude + ", " + longitude + ")";
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Localizacao)) return false;
        Localizacao other = (Localizacao) obj;
        return id == other.id &&
               cidade.equals(other.cidade) &&
               latitude.equals(other.latitude) &&
               longitude.equals(other.longitude) &&
               dataHora.equals(other.dataHora);
    }

}

