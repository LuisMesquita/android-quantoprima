package br.com.mobiclub.quantoprima.events;

import retrofit.RetrofitError;

/**
 * The event that is posted when a network error event occurs.
 * TODO: Consume this event in the {@link br.com.mobiclub.quantoprima.ui.activity.MobiClubActionBarActivity} and
 * show a onGainMobiSuccess that something went wrong.
 */
public class NetworkErrorEvent {
    private RetrofitError cause;

    public NetworkErrorEvent(RetrofitError cause) {
        this.cause = cause;
    }

    public RetrofitError getCause() {
        return cause;
    }
}
