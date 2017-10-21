// Code generated by dagger-compiler.  Do not edit.
package br.com.mobiclub.quantoprima.ui.activity;


import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

/**
 * A {@code Binder<AboutActivity>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 * 
 * Owning the dependency links between {@code AboutActivity} and its
 * dependencies.
 * 
 * Being a {@code Provider<AboutActivity>} and handling creation and
 * preparation of object instances.
 * 
 * Being a {@code MembersInjector<AboutActivity>} and handling injection
 * of annotated fields.
 */
public final class AboutActivity$$InjectAdapter extends Binding<AboutActivity>
    implements Provider<AboutActivity>, MembersInjector<AboutActivity> {
  private Binding<br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider> serviceProvider;
  private Binding<MenuActivity> supertype;

  public AboutActivity$$InjectAdapter() {
    super("br.com.mobiclub.quantoprima.ui.activity.AboutActivity", "members/br.com.mobiclub.quantoprima.ui.activity.AboutActivity", NOT_SINGLETON, AboutActivity.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    serviceProvider = (Binding<br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider>) linker.requestBinding("br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider", AboutActivity.class);
    supertype = (Binding<MenuActivity>) linker.requestBinding("members/br.com.mobiclub.quantoprima.ui.activity.MenuActivity", AboutActivity.class, false, true);
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
    injectMembersBindings.add(serviceProvider);
    injectMembersBindings.add(supertype);
  }

  /**
   * Returns the fully provisioned instance satisfying the contract for
   * {@code Provider<AboutActivity>}.
   */
  @Override
  public AboutActivity get() {
    AboutActivity result = new AboutActivity();
    injectMembers(result);
    return result;
  }

  /**
   * Injects any {@code @Inject} annotated fields in the given instance,
   * satisfying the contract for {@code Provider<AboutActivity>}.
   */
  @Override
  public void injectMembers(AboutActivity object) {
    object.serviceProvider = serviceProvider.get();
    supertype.injectMembers(object);
  }
}
