// Generated code from Butter Knife. Do not modify!
package br.com.mobiclub.quantoprima.ui.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class CarouselFragment$$ViewInjector {
  public static void inject(Finder finder, final br.com.mobiclub.quantoprima.ui.fragment.CarouselFragment target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689740, "field 'indicator'");
    target.indicator = (com.viewpagerindicator.TitlePageIndicator) view;
    view = finder.findRequiredView(source, 2131689741, "field 'pager'");
    target.pager = (android.support.v4.view.ViewPager) view;
  }

  public static void reset(br.com.mobiclub.quantoprima.ui.fragment.CarouselFragment target) {
    target.indicator = null;
    target.pager = null;
  }
}
