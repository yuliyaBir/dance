package be.vdab.dance.services;

import be.vdab.dance.domain.Boeking;
import be.vdab.dance.domain.Festival;
import be.vdab.dance.exceptions.BeschikbareTicketsNietGenoeg;
import be.vdab.dance.exceptions.FestivalNietGevondenException;
import be.vdab.dance.repositories.BoekingRepository;
import be.vdab.dance.repositories.FestivalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoekingServiceTest {
    private BoekingService boekingService;
    @Mock
    private BoekingRepository boekingRepository;
    @Mock
    private FestivalRepository festivalRepository;

    @BeforeEach
    void BeforeEach() {
        boekingService = new BoekingService(festivalRepository, boekingRepository);
    }

    @Test
    void boekingMetTeVeelTicketsMislukt() {
        var festival = new Festival(1, "cha-cha-cha", 2, BigDecimal.TEN);
        when(festivalRepository.findAndLockById(1)).thenReturn(Optional.of(festival));
       assertThatExceptionOfType(BeschikbareTicketsNietGenoeg.class).isThrownBy(() ->
               boekingService.boeking(new Boeking("jdn", 3, 1)));
    }

    @Test
    void boekingMetOnbestaandeIdFestivalMislukt() {
        assertThatExceptionOfType(FestivalNietGevondenException.class).isThrownBy(()->
                boekingService.boeking(new Boeking("oma", 3, 1)));
    }
    @Test
    void boeking() {
        var festival = new Festival(1, "cha-cha-cha", 3, BigDecimal.TEN);
        var boeking = new Boeking("jan", 2, 1);
        when(festivalRepository.findAndLockById(1)).thenReturn(Optional.of(festival));
        boekingService.boeking(boeking);
        assertThat(festival.getTicketsBeschikbaar()).isEqualTo(1);
        verify(festivalRepository).findAndLockById(1);
        verify(boekingRepository).create(boeking);
        verify(festivalRepository).updateBeschikbareTickets(1,2);

    }
}