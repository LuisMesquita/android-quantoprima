// Generated code from Butter Knife. Do not modify!
package br.com.mobiclub.quantoprima.ui.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class HomeActivity$$ViewInjector {
  public static void inject(Finder finder, final br.com.mobiclub.quantoprima.ui.activity.HomeActivity target, Object source) {
    View view;
    view = finder.findOptionalView(source, 2131689594);
    if (view != null) {
      view.setOnClickListener(
        new android.view.View.OnClickListener() {
          @Override public void onClick(
            android.view.View p0
          ) {
            target.onCloseGPSBar();
          }
        });
    }
  }

  public static void reset(br.com.mobiclub.quantoprima.ui.activity.HomeActivity target) {
  }
}
