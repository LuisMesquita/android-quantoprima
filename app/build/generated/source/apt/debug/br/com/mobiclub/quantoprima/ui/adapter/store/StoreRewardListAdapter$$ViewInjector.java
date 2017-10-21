// Generated code from Butter Knife. Do not modify!
package br.com.mobiclub.quantoprima.ui.adapter.store;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class StoreRewardListAdapter$$ViewInjector {
  public static void inject(Finder finder, final br.com.mobiclub.quantoprima.ui.adapter.store.StoreRewardListAdapter target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689904, "field 'buttonBuy' and method 'buyReward'");
    target.buttonBuy = (android.widget.ImageButton) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.buyReward((android.widget.ImageButton) p0);
        }
      });
  }

  public static void reset(br.com.mobiclub.quantoprima.ui.adapter.store.StoreRewardListAdapter target) {
    target.buttonBuy = null;
  }
}
