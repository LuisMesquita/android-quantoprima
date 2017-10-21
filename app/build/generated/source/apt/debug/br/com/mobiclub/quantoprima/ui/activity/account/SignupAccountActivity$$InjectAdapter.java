// Code generated by dagger-compiler.  Do not edit.
package br.com.mobiclub.quantoprima.ui.activity.account;


import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

/**
 * A {@code Binder<SignupAccountActivity>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 * 
 * Owning the dependency links between {@code SignupAccountActivity} and its
 * dependencies.
 * 
 * Being a {@code MembersInjector<SignupAccountActivity>} and handling injection
 * of annotated fields.
 */
public final class SignupAccountActivity$$InjectAdapter extends Binding<SignupAccountActivity>
    implements MembersInjector<SignupAccountActivity> {
  private Binding<br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi> mobiClubService;
  private Binding<br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider> serviceProvider;
  private Binding<com.squareup.otto.Bus> bus;
  private Binding<android.content.SharedPreferences> prefs;

  public SignupAccountActivity$$InjectAdapter() {
    super(null, "members/br.com.mobiclub.quantoprima.ui.activity.account.SignupAccountActivity", NOT_SINGLETON, SignupAccountActivity.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    mobiClubService = (Binding<br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi>) linker.requestBinding("br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi", SignupAccountActivity.class);
    serviceProvider = (Binding<br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider>) linker.requestBinding("br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider", SignupAccountActivity.class);
    bus = (Binding<com.squareup.otto.Bus>) linker.requestBinding("com.squareup.otto.Bus", SignupAccountActivity.class);
    prefs = (Binding<android.content.SharedPreferences>) linker.requestBinding("android.content.SharedPreferences", SignupAccountActivity.class);
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
    injectMembersBindings.add(mobiClubService);
    injectMembersBindings.add(serviceProvider);
    injectMembersBindings.add(bus);
    injectMembersBindings.add(prefs);
  }

  /**
   * Injects any {@code @Inject} annotated fields in the given instance,
   * satisfying the contract for {@code Provider<SignupAccountActivity>}.
   */
  @Override
  public void injectMembers(SignupAccountActivity object) {
    object.mobiClubService = mobiClubService.get();
    object.serviceProvider = serviceProvider.get();
    object.bus = bus.get();
    object.prefs = prefs.get();
  }
}
