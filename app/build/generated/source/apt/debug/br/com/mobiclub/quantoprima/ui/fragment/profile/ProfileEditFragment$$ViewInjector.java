// Generated code from Butter Knife. Do not modify!
package br.com.mobiclub.quantoprima.ui.fragment.profile;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ProfileEditFragment$$ViewInjector {
  public static void inject(Finder finder, final br.com.mobiclub.quantoprima.ui.fragment.profile.ProfileEditFragment target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689760, "field 'profileImage'");
    target.profileImage = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131689586, "field 'btnChangePassword' and method 'changePassword'");
    target.btnChangePassword = (android.widget.TextView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.changePassword();
        }
      });
    view = finder.findRequiredView(source, 2131689779, "field 'labelEditPasswordMessage'");
    target.labelEditPasswordMessage = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131689583, "field 'etEmail'");
    target.etEmail = (android.widget.AutoCompleteTextView) view;
    view = finder.findRequiredView(source, 2131689617, "field 'etName'");
    target.etName = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131689623, "field 'etBirth'");
    target.etBirth = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131689620, "field 'etCpf'");
    target.etCpf = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131689626, "field 'spinner'");
    target.spinner = (android.widget.Spinner) view;
    view = finder.findRequiredView(source, 2131689778, "method 'updateProfile'");
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.updateProfile();
        }
      });
  }

  public static void reset(br.com.mobiclub.quantoprima.ui.fragment.profile.ProfileEditFragment target) {
    target.profileImage = null;
    target.btnChangePassword = null;
    target.labelEditPasswordMessage = null;
    target.etEmail = null;
    target.etName = null;
    target.etBirth = null;
    target.etCpf = null;
    target.spinner = null;
  }
}
