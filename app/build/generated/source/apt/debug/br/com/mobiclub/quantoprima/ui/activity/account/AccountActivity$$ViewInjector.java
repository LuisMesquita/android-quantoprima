// Generated code from Butter Knife. Do not modify!
package br.com.mobiclub.quantoprima.ui.activity.account;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class AccountActivity$$ViewInjector {
  public static void inject(Finder finder, final br.com.mobiclub.quantoprima.ui.activity.account.AccountActivity target, Object source) {
    View view;
    view = finder.findOptionalView(source, 2131689583);
    target.emailText = (android.widget.AutoCompleteTextView) view;
    view = finder.findOptionalView(source, 2131689584);
    target.passwordText = (android.widget.EditText) view;
    view = finder.findOptionalView(source, 2131689614);
    target.signInButton = (android.widget.Button) view;
  }

  public static void reset(br.com.mobiclub.quantoprima.ui.activity.account.AccountActivity target) {
    target.emailText = null;
    target.passwordText = null;
    target.signInButton = null;
  }
}
