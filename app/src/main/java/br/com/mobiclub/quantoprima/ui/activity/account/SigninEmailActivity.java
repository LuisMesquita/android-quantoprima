package br.com.mobiclub.quantoprima.ui.activity.account;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import br.com.mobiclub.quantoprima.R;
import br.com.mobiclub.quantoprima.events.NetworkErrorEvent;
import br.com.mobiclub.quantoprima.events.UnAuthorizedErrorEvent;
import br.com.mobiclub.quantoprima.util.Ln;

import static android.R.layout.simple_dropdown_item_1line;
import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_ENTER;
import static android.view.inputmethod.EditorInfo.IME_ACTION_DONE;

/**
 * Activity to signin the user
 */
public class SigninEmailActivity extends AccountActivity {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        emailText.setAdapter(new ArrayAdapter<String>(this,
                simple_dropdown_item_1line, userEmailAccounts()));

        passwordText.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(final View v, final int keyCode, final KeyEvent event) {
                if (event != null && ACTION_DOWN == event.getAction()
                        && keyCode == KEYCODE_ENTER && signInButton.isEnabled()) {
                    handleSign(signInButton);
                    return true;
                }
                return false;
            }
        });

        passwordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(final TextView v, final int actionId,
                                          final KeyEvent event) {
                if (actionId == IME_ACTION_DONE && signInButton.isEnabled()) {
                    handleSign(signInButton);
                    return true;
                }
                return false;
            }
        });

        emailText.addTextChangedListener(watcher);
        passwordText.addTextChangedListener(watcher);
    }

    @Override
    protected Integer getAccountLayout() {
        return R.layout.activity_signin_email;
    }

    private List<String> userEmailAccounts() {
        final Account[] accounts = getAccountManager().getAccountsByType("com.google");
        final List<String> emailAddresses = new ArrayList<String>(accounts.length);
        for (final Account account : accounts) {
            emailAddresses.add(account.name);
        }
        return emailAddresses;
    }

    public void onLostPassword(View view) {
        Ln.d("onLostPasswordClicked");
        Intent intent = new Intent(this, LostPasswordActivity.class);
        startActivity(intent);
    }

    public void onSignup(View view) {
        Ln.d("onSignupClicked");
        Intent intent = new Intent(this, SignupActivity.class);
//        intent.putExtras(getIntent().getExtras());
        startActivity(intent);
    }

    @Subscribe
    public void onNetworkErrorEvent(NetworkErrorEvent e) {
        super.onNetworkErrorEvent(e);
    }

    @Subscribe
    public void onUnAuthorizedErrorEvent(UnAuthorizedErrorEvent unAuthorizedErrorEvent) {
        super.onUnAuthorizedErrorEvent(unAuthorizedErrorEvent);
    }

}
