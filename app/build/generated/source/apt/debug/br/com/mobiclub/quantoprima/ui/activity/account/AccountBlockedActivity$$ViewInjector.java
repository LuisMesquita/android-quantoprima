// Generated code from Butter Knife. Do not modify!
package br.com.mobiclub.quantoprima.ui.activity.account;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class AccountBlockedActivity$$ViewInjector {
  public static void inject(Finder finder, final br.com.mobiclub.quantoprima.ui.activity.account.AccountBlockedActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689565, "field 'userMessage'");
    target.userMessage = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131689567, "field 'labelReason'");
    target.labelReason = (android.widget.TextView) view;
  }

  public static void reset(br.com.mobiclub.quantoprima.ui.activity.account.AccountBlockedActivity target) {
    target.userMessage = null;
    target.labelReason = null;
  }
}
