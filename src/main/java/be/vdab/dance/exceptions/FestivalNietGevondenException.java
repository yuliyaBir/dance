package be.vdab.dance.exceptions;

public class FestivalNietGevondenException extends RuntimeException{
    private long id;

    public FestivalNietGevondenException(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
