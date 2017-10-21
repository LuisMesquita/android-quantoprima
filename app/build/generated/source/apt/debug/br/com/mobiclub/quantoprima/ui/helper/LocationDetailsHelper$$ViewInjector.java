// Generated code from Butter Knife. Do not modify!
package br.com.mobiclub.quantoprima.ui.helper;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class LocationDetailsHelper$$ViewInjector {
  public static void inject(Finder finder, final br.com.mobiclub.quantoprima.ui.helper.LocationDetailsHelper target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689752, "field 'imageLogo'");
    target.imageLogo = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131689875, "field 'labelLocationName'");
    target.labelLocationName = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131689876, "field 'labelLocationReference'");
    target.labelLocationReference = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131689924, "field 'labelScoreValue'");
    target.labelScoreValue = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131689896, "field 'lytScore'");
    target.lytScore = (android.widget.RelativeLayout) view;
  }

  public static void reset(br.com.mobiclub.quantoprima.ui.helper.LocationDetailsHelper target) {
    target.imageLogo = null;
    target.labelLocationName = null;
    target.labelLocationReference = null;
    target.labelScoreValue = null;
    target.lytScore = null;
  }
}
