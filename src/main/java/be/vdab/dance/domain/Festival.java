package be.vdab.dance.domain;

import be.vdab.dance.exceptions.GeenReclameBudgetException;

import java.math.BigDecimal;
import java.util.List;

public class Festival {
    private final long id;
    private final String naam;
    private final int ticketsBeschikbaar;
    private BigDecimal reclameBudget;

    public Festival(long id, String naam, int ticketsBeschikbaar, BigDecimal reclameBudget) {
        if (id <= 0){
            throw new IllegalArgumentException("De id moet meer dan 0 zijn");
        }
        this.id = id;
        if (naam.isEmpty() | naam.isBlank()){
            throw new IllegalArgumentException("De naam moet ingevuld zijn");
        }
        this.naam = naam;
        if (ticketsBeschikbaar < 0){
            throw new IllegalArgumentException("Tickets mag niet negatief zijn");
        }
        this.ticketsBeschikbaar = ticketsBeschikbaar;
        if (reclameBudget.compareTo(BigDecimal.ZERO) <= 0){
            throw new GeenReclameBudgetException();
        }
        this.reclameBudget = reclameBudget;
    }

    public long getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public int getTicketsBeschikbaar() {
        return ticketsBeschikbaar;
    }

    public BigDecimal getReclameBudget() {
        return reclameBudget;
    }
    public void festivalAnnuleren(List<Festival> festivals){
        if (reclameBudget == BigDecimal.ZERO){
            throw new GeenReclameBudgetException();
        }
        var toeTeVoegenBedrag =
                reclameBudget.divide(BigDecimal.valueOf(festivals.size()));
        for (var festival : festivals) {
             festival.reclameBudget = festival.reclameBudget.add(toeTeVoegenBedrag);
        }

    }
}
