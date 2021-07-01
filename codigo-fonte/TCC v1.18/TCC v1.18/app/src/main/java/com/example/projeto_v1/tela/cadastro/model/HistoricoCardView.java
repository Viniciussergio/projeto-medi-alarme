package com.example.projeto_v1.tela.cadastro.model;

public class HistoricoCardView {
    private String title;
    private String description;
    private String statusHistorico;
    private String modoUsoRemedio;

    public String getModoUsoRemedio() {
        return modoUsoRemedio;
    }

    public void setModoUsoRemedio(String modoUsoRemedio) {
        this.modoUsoRemedio = modoUsoRemedio;
    }

    public String getStatusHistorico() {
        return statusHistorico;
    }

    public void setStatusHistorico(String statusHistorico) {
        this.statusHistorico = statusHistorico;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
