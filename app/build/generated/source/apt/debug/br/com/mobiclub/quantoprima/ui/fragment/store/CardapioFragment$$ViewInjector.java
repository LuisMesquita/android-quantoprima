// Generated code from Butter Knife. Do not modify!
package br.com.mobiclub.quantoprima.ui.fragment.store;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class CardapioFragment$$ViewInjector {
  public static void inject(Finder finder, final br.com.mobiclub.quantoprima.ui.fragment.store.CardapioFragment target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689730, "field 'labelCategory'");
    target.labelCategory = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131689727, "field 'navBar'");
    target.navBar = (android.widget.RelativeLayout) view;
    view = finder.findRequiredView(source, 2131689732, "field 'pager'");
    target.pager = (android.support.v4.view.ViewPager) view;
    view = finder.findRequiredView(source, 2131689728, "field 'prev' and method 'onPrev'");
    target.prev = (android.widget.ImageButton) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onPrev();
        }
      });
    view = finder.findRequiredView(source, 2131689729, "field 'next' and method 'onNext'");
    target.next = (android.widget.ImageButton) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onNext();
        }
      });
  }

  public static void reset(br.com.mobiclub.quantoprima.ui.fragment.store.CardapioFragment target) {
    target.labelCategory = null;
    target.navBar = null;
    target.pager = null;
    target.prev = null;
    target.next = null;
  }
}
