

package br.com.mobiclub.quantoprima.test;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;

import br.com.mobiclub.quantoprima.service.authenticator.BootstrapAuthenticatorActivity;
import br.com.mobiclub.quantoprima.R;


/**
 * Tests of displaying the authenticator activity
 */
public class BootstrapAuthenticatorTest extends ActivityInstrumentationTestCase2<BootstrapAuthenticatorActivity> {

    /**
     * Create test for {@link br.com.mobiclub.quantoprima.service.authenticator.BootstrapAuthenticatorActivity}
     */
    public BootstrapAuthenticatorTest() {
        super(BootstrapAuthenticatorActivity.class);
    }

    /**
     * Verify activity exists
     */
    public void testActivityExists() {
        assertNotNull(getActivity());
    }

    /**
     * Verify sign in button is initially disabled
     */
    public void testSignInDisabled() {
        View view = getActivity().findViewById(R.id.b_signin);
        assertNotNull(view);
        assertTrue(view instanceof Button);
        assertFalse(((Button) view).isEnabled());
    }
}
