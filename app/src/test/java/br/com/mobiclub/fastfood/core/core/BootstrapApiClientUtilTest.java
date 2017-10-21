

package br.com.mobiclub.quantoprima.domain.core;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import br.com.mobiclub.quantoprima.core.service.MobiClubService;
import br.com.mobiclub.quantoprima.core.service.api.entity.User;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Unit tests of client API
 */
public class BootstrapApiClientUtilTest {

    @Test
    @Ignore("Requires the API to use basic authentication. Parse.com api does not. See MobiClubService for more info.")
    public void shouldCreateClient() throws Exception {
        List<User> users = new MobiClubService("demo@androidbootstrap.com", "foobar").getUsers();

        assertThat(users.get(0).getName(), notNullValue());
    }
}
