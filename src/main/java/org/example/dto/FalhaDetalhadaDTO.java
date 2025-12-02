package org.example.dto;

import org.example.model.Equipamento;
import org.example.model.Falha;

import java.util.List;

public class FalhaDetalhadaDTO {
    private Falha falha;
    private Equipamento equipamento;
    private List<String> acoesCorretivas;

    public FalhaDetalhadaDTO(Falha falha, Equipamento equipamento, List<String> acoesCorretivas) {
        this.falha = falha;
        this.equipamento = equipamento;
        this.acoesCorretivas = acoesCorretivas;
    }

    public FalhaDetalhadaDTO() {

    }

    public Falha getFalha() { return falha; }
    public Equipamento getEquipamento() { return equipamento; }
    public List<String> getAcoesCorretivas() { return acoesCorretivas; }

    public void setFalha(Falha falha) {
        this.falha = falha;
    }

    public void setEquipamento(Equipamento equipamento) {
        this.equipamento = equipamento;
    }

    public void setAcoesCorretivas(List<String> acoesCorretivas) {
        this.acoesCorretivas = acoesCorretivas;
    }
}
