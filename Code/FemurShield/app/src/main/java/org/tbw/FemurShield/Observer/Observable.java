package org.tbw.FemurShield.Observer;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * tale classe rappresenta un oggetto osservabile, ovvero un oggeto che può
 * essere osservato da un altro oggetto.
 * Tale classe deve essere estesa.
 * Non è possibile infatti creare un'istanza di Observable perchè la classe
 * è astratta per ovvi motivi.
 *
 * @author Alessandro Moro
 */
abstract public class Observable {
    //questa lista ha lo scopo di salvare tutti gli osservatori dell'oggetto
    private CopyOnWriteArrayList<Observer> osservatori = new CopyOnWriteArrayList<>();

    /**
     * tale metodo serve ad un osservatore per registrarsi nella lista degli
     * osservatori, ciò implica che verrà avvisato quando l'oggetto osservato
     * sarà modificato.
     *
     * @param osservatore l'osservatore da registrare
     */
    public void attach(Observer osservatore) {
        osservatori.add(osservatore);
    }

    /**
     * tale metodo serve per annullare l'effetto dell'aggiornamento all'observer
     *
     * @param osservatore l'osservatore da eliminare
     */
    public void deattach(Observer osservatore) {
        osservatori.remove(osservatore);
    }

    /**
     * tale metodo dovrà essere chiamato dalla classe che estende
     * Observable ongi qualvolta esso verrà modificato.
     * Tale metodo notificherà ogni osservatore chiamando il suo metodo update(Observable o)
     *
     * @param o L'oggetto notificato agli Osservatori
     */
    public void notifyObserver(Object o) {
        for (Observer osservatore : osservatori) {
            osservatore.update(this, o);
        }
    }
}