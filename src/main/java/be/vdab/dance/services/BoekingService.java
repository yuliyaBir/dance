package be.vdab.dance.services;

import be.vdab.dance.domain.Boeking;
import be.vdab.dance.domain.Festival;
import be.vdab.dance.exceptions.BeschikbareTicketsNietGenoeg;
import be.vdab.dance.repositories.BoekingRepository;
import be.vdab.dance.repositories.FestivalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BoekingService {
    private final FestivalRepository festivalRepository;
    private final BoekingRepository boekingRepository;
    public BoekingService(FestivalRepository festivalRepository, BoekingRepository boekingRepository) {
        this.festivalRepository = festivalRepository;
        this.boekingRepository = boekingRepository;
    }
    @Transactional
    public void boeking(Boeking boeking){
        Optional<Festival> festival = festivalRepository.findAndLockById(boeking.getFestivalId());
        var beschikbareTickets = festival.get().getTicketsBeschikbaar();
        if (boeking.getAantalTickets() > beschikbareTickets){
            throw new BeschikbareTicketsNietGenoeg("Het aantal beschikbare tickets: "
                    + festival.get().getTicketsBeschikbaar());
        }
        festivalRepository.updateBeschikbareTickets(boeking.getFestivalId(), boeking.getAantalTickets());
        boekingRepository.create(boeking);
    }
}
