// Generated code from Butter Knife. Do not modify!
package br.com.mobiclub.quantoprima.ui.fragment.reward;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class RewardDetailFragment$$ViewInjector {
  public static void inject(Finder finder, final br.com.mobiclub.quantoprima.ui.fragment.reward.RewardDetailFragment target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689733, "field 'name'");
    target.name = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131689738, "field 'description'");
    target.description = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131689701, "field 'imageReward'");
    target.imageReward = (android.widget.ImageView) view;
  }

  public static void reset(br.com.mobiclub.quantoprima.ui.fragment.reward.RewardDetailFragment target) {
    target.name = null;
    target.description = null;
    target.imageReward = null;
  }
}
