// Generated code from Butter Knife. Do not modify!
package br.com.mobiclub.quantoprima.ui.fragment.notification;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class NotificationItemFragment$$ViewInjector {
  public static void inject(Finder finder, final br.com.mobiclub.quantoprima.ui.fragment.notification.NotificationItemFragment target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689697, "field 'labelReference'");
    target.labelReference = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131689720, "field 'labelTime'");
    target.labelTime = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131689755, "field 'labelTitle'");
    target.labelTitle = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131689756, "field 'labelMessageContent'");
    target.labelMessageContent = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131689753, "field 'imageBullet'");
    target.imageBullet = (android.widget.ImageView) view;
  }

  public static void reset(br.com.mobiclub.quantoprima.ui.fragment.notification.NotificationItemFragment target) {
    target.labelReference = null;
    target.labelTime = null;
    target.labelTitle = null;
    target.labelMessageContent = null;
    target.imageBullet = null;
  }
}
