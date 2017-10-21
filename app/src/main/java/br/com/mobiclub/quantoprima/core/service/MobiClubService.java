
package br.com.mobiclub.quantoprima.core.service;

import retrofit.RestAdapter;

/**
 * MobiClub API service
 */
public class MobiClubService extends AbstractMobiClubService {

    /**
     * Create bootstrap service
     * Default CTOR
     */
    public MobiClubService() {
    }

    /**
     * Create bootstrap service
     *
     * @param restAdapter The RestAdapter that allows HTTP Communication.
     */
    public MobiClubService(RestAdapter restAdapter) {
        this.restAdapter = restAdapter;
    }

}