// Generated code from Butter Knife. Do not modify!
package br.com.mobiclub.quantoprima.ui.activity.account;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SignupActivity$$ViewInjector {
  public static void inject(Finder finder, final br.com.mobiclub.quantoprima.ui.activity.account.SignupActivity target, Object source) {
    br.com.mobiclub.quantoprima.ui.activity.account.SignupAccountActivity$$ViewInjector.inject(finder, target, source);

    View view;
    view = finder.findRequiredView(source, 2131689614, "method 'handleSignup'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.handleSignup(p0);
        }
      });
  }

  public static void reset(br.com.mobiclub.quantoprima.ui.activity.account.SignupActivity target) {
    br.com.mobiclub.quantoprima.ui.activity.account.SignupAccountActivity$$ViewInjector.reset(target);

  }
}
