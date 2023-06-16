package be.vdab.dance.console;

import be.vdab.dance.domain.Boeking;
import be.vdab.dance.exceptions.BeschikbareTicketsNietGenoeg;
import be.vdab.dance.exceptions.FestivalNietGevondenException;
import be.vdab.dance.services.BoekingService;
import be.vdab.dance.services.FestivalService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class MyRunner implements CommandLineRunner {
    private final FestivalService festivalService;
    private final BoekingService boekingService;

    public MyRunner(FestivalService festivalService, BoekingService boekingService) {
        this.festivalService = festivalService;
        this.boekingService = boekingService;
    }
    @Override
    public void run(String... args){
        // FIND UITVERKOCHT
//        festivalService.findUitverkocht().forEach(festival -> System.out.println(festival.getNaam()));
        // ANNULEER FESTIVAL BY ID
//        var scanner = new Scanner(System.in);
//        System.out.print("Festival id:");
//        long id = scanner.nextLong();
//        try {
//            festivalService.annuleerFestival(id);
//            System.out.println("Festival geannuleerd.");
//        } catch (FestivalNietGevondenException ex) {
//            System.err.println("Festival " + ex.getId() + " niet gevonden.");
//        }
        // BOEKING
        var scanner = new Scanner(System.in);
        System.out.println("Geef een naam");
        var naam = scanner.nextLine();
        System.out.println("Geef aantal tickets");
        var aantalTickets = scanner.nextInt();
        System.out.println("Geef id van een festival");
        var idFestival = scanner.nextLong();
        var boeking = new Boeking(naam, aantalTickets, idFestival);
        try {
            boekingService.boeking(boeking);
        } catch (FestivalNietGevondenException | BeschikbareTicketsNietGenoeg ex){
            ex.printStackTrace();
        }
    }
}
