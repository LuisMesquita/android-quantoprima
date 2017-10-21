// Generated code from Butter Knife. Do not modify!
package br.com.mobiclub.quantoprima.ui.fragment.reward;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class RescueRewardFragment$$ViewInjector {
  public static void inject(Finder finder, final br.com.mobiclub.quantoprima.ui.fragment.reward.RescueRewardFragment target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689701, "field 'imageReward'");
    target.imageReward = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131689733, "field 'labelName'");
    target.labelName = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131689920, "field 'labelLocation'");
    target.labelLocation = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131689921, "field 'labelExpiration'");
    target.labelExpiration = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131689786, "field 'btnRescue'");
    target.btnRescue = (android.widget.Button) view;
    view = finder.findRequiredView(source, 2131689719, "field 'labelCode'");
    target.labelCode = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131689720, "field 'labelTime'");
    target.labelTime = (android.widget.TextView) view;
  }

  public static void reset(br.com.mobiclub.quantoprima.ui.fragment.reward.RescueRewardFragment target) {
    target.imageReward = null;
    target.labelName = null;
    target.labelLocation = null;
    target.labelExpiration = null;
    target.btnRescue = null;
    target.labelCode = null;
    target.labelTime = null;
  }
}
