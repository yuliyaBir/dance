package be.vdab.dance.exceptions;

import org.apache.logging.log4j.message.Message;

public class BeschikbareTicketsNietGenoeg extends RuntimeException{
    private String tickets;

    public BeschikbareTicketsNietGenoeg(String tickets) {
        this.tickets = tickets;
    }
}
