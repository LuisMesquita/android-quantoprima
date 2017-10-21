// Generated code from Butter Knife. Do not modify!
package br.com.mobiclub.quantoprima.ui.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class NewsActivity$$ViewInjector {
  public static void inject(Finder finder, final br.com.mobiclub.quantoprima.ui.activity.NewsActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131689933, "field 'title'");
    target.title = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131689934, "field 'content'");
    target.content = (android.widget.TextView) view;
  }

  public static void reset(br.com.mobiclub.quantoprima.ui.activity.NewsActivity target) {
    target.title = null;
    target.content = null;
  }
}
