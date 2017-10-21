// Generated code from Butter Knife. Do not modify!
package br.com.mobiclub.quantoprima.ui.fragment.profile;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ProfileFragment$$ViewInjector {
  public static void inject(Finder finder, final br.com.mobiclub.quantoprima.ui.fragment.profile.ProfileFragment target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689733, "field 'labelName'");
    target.labelName = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131689763, "field 'labelEmail'");
    target.labelEmail = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131689765, "field 'labelBirth'");
    target.labelBirth = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131689767, "field 'labelGender'");
    target.labelGender = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131689772, "field 'labelFacebookEmail'");
    target.labelFacebookEmail = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131689774, "field 'btnFacebookConnect'");
    target.btnFacebookConnect = (com.facebook.widget.LoginButton) view;
    view = finder.findRequiredView(source, 2131689773, "field 'btnFacebookDiconnect' and method 'onFacebookDisconnect'");
    target.btnFacebookDiconnect = (android.widget.ImageButton) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onFacebookDisconnect();
        }
      });
    view = finder.findRequiredView(source, 2131689771, "field 'lytFacebookInfo'");
    target.lytFacebookInfo = (android.widget.LinearLayout) view;
    view = finder.findRequiredView(source, 2131689760, "field 'profileImage'");
    target.profileImage = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131689764, "field 'rowBirthday'");
    target.rowBirthday = (android.widget.TableRow) view;
    view = finder.findRequiredView(source, 2131689769, "field 'labelCpf'");
    target.labelCpf = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131689775, "method 'onConfigNotifications'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onConfigNotifications();
        }
      });
  }

  public static void reset(br.com.mobiclub.quantoprima.ui.fragment.profile.ProfileFragment target) {
    target.labelName = null;
    target.labelEmail = null;
    target.labelBirth = null;
    target.labelGender = null;
    target.labelFacebookEmail = null;
    target.btnFacebookConnect = null;
    target.btnFacebookDiconnect = null;
    target.lytFacebookInfo = null;
    target.profileImage = null;
    target.rowBirthday = null;
    target.labelCpf = null;
  }
}
