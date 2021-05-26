package hr.fer.oprpp1.gui.prim;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The {@code PrimListModel} class represents a model for the {@link PrimDemo}. It implements {@link ListModel}.
 *
 * @author mirtamoslavac
 * @version 1.0
 */
public class PrimListModel implements ListModel<Integer> {
    /**
     * List containing all generated primes.
     */
    private final List<Integer> primes;
    /**
     * List containing all listeners of the current {@code PrimListModel}.
     */
    private final List<ListDataListener> listeners;

    /**
     * Creates a new {@code PrimListModel} instance with a set first prime.
     */
    public PrimListModel() {
        this.primes = new ArrayList<>(List.of(1));
        this.listeners = new ArrayList<>();
    }

    /**
     * Generates the next prime and notifies all registered listeners about the event.
     */
    public void next() {
        this.primes.add(this.findNextPrime());
        int position = this.getSize() - 1;

        ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, position, position);
        this.listeners.forEach(listener -> listener.intervalAdded(event));
    }

    /**
     * Finds the first prime following the last known prime in the {@code primes} list.
     *
     * @return next prime.
     */
    private Integer findNextPrime() {
        for(int prime = this.primes.get(this.primes.size() - 1) + 1; ; prime++) if (isPrime(prime)) return prime;
    }

    /**
     * Determines whether the given number {@code prime} is prime.
     *
     * @param prime the number to be checked.
     * @return {@code true} if the given number is prime, {@code false} otherwise.
     */
    private boolean isPrime(int prime) {
        for(int i = 2; i <= prime / 2; i++) if (prime % i == 0) return false;
        return true;
    }

    @Override
    public int getSize() {
        return this.primes.size();
    }

    @Override
    public Integer getElementAt(int index) {
        return this.primes.get(index);
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException when the given {@code l} is {@code null}.
     */
    @Override
    public void addListDataListener(ListDataListener l) {
        this.listeners.add(Objects.requireNonNull(l, "The given listener cannot be null!"));
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException when the given {@code l} is {@code null}.
     */
    @Override
    public void removeListDataListener(ListDataListener l) {
        this.listeners.remove(Objects.requireNonNull(l, "The given listener cannot be null!"));
    }
}
