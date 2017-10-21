// Generated code from Butter Knife. Do not modify!
package br.com.mobiclub.quantoprima.ui.helper;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SignupHelper$$ViewInjector {
  public static void inject(Finder finder, final br.com.mobiclub.quantoprima.ui.helper.SignupHelper target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689617, "field 'etName'");
    target.etName = (android.widget.EditText) view;
    view = finder.findOptionalView(source, 2131689618);
    target.etLastName = (android.widget.EditText) view;
    view = finder.findOptionalView(source, 2131689621);
    target.etPasswordConfirmation = (android.widget.EditText) view;
    view = finder.findOptionalView(source, 2131689620);
    target.etCpf = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131689623, "field 'etBirth'");
    target.etBirth = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131689626, "field 'spinner'");
    target.spinner = (android.widget.Spinner) view;
    view = finder.findOptionalView(source, 2131689776);
    target.scroll = (android.widget.ScrollView) view;
  }

  public static void reset(br.com.mobiclub.quantoprima.ui.helper.SignupHelper target) {
    target.etName = null;
    target.etLastName = null;
    target.etPasswordConfirmation = null;
    target.etCpf = null;
    target.etBirth = null;
    target.spinner = null;
    target.scroll = null;
  }
}
