// Generated code from Butter Knife. Do not modify!
package br.com.mobiclub.quantoprima.ui.fragment.store;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class StoreFragment$$ViewInjector {
  public static void inject(Finder finder, final br.com.mobiclub.quantoprima.ui.fragment.store.StoreFragment target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689787, "field 'indicator'");
    target.indicator = (com.viewpagerindicator.TabPageIndicator) view;
    view = finder.findRequiredView(source, 2131689788, "field 'pager'");
    target.pager = (android.support.v4.view.ViewPager) view;
  }

  public static void reset(br.com.mobiclub.quantoprima.ui.fragment.store.StoreFragment target) {
    target.indicator = null;
    target.pager = null;
  }
}
