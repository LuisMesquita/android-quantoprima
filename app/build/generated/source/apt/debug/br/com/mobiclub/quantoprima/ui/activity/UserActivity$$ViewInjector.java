// Generated code from Butter Knife. Do not modify!
package br.com.mobiclub.quantoprima.ui.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class UserActivity$$ViewInjector {
  public static void inject(Finder finder, final br.com.mobiclub.quantoprima.ui.activity.UserActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689974, "field 'avatar'");
    target.avatar = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131689658, "field 'name'");
    target.name = (android.widget.TextView) view;
  }

  public static void reset(br.com.mobiclub.quantoprima.ui.activity.UserActivity target) {
    target.avatar = null;
    target.name = null;
  }
}
