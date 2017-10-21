
package br.com.mobiclub.quantoprima.core.service.mock;

import br.com.mobiclub.quantoprima.core.service.AbstractMobiClubService;
import retrofit.RestAdapter;

/**
 * MobiClub API service
 */
public class MockMobiClubService extends AbstractMobiClubService {

    /**
     * Create bootstrap service
     * Default CTOR
     */
    public MockMobiClubService() {
    }

    /**
     * Create bootstrap service
     *
     * @param restAdapter The RestAdapter that allows HTTP Communication.
     */
    public MockMobiClubService(RestAdapter restAdapter) {
        this.restAdapter = restAdapter;
    }

}