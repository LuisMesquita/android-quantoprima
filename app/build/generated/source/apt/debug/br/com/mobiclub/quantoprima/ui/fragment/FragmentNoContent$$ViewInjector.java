// Generated code from Butter Knife. Do not modify!
package br.com.mobiclub.quantoprima.ui.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class FragmentNoContent$$ViewInjector {
  public static void inject(Finder finder, final br.com.mobiclub.quantoprima.ui.fragment.FragmentNoContent target, Object source) {
    View view;
    view = finder.findOptionalView(source, 2131689938);
    if (view != null) {
      view.setOnClickListener(
        new android.view.View.OnClickListener() {
          @Override public void onClick(
            android.view.View p0
          ) {
            target.howToReward();
          }
        });
    }
  }

  public static void reset(br.com.mobiclub.quantoprima.ui.fragment.FragmentNoContent target) {
  }
}
