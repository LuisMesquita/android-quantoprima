// Code generated by dagger-compiler.  Do not edit.
package br.com.mobiclub.quantoprima.ui.activity.notification;


import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

/**
 * A {@code Binder<PushNotificationsActivity>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 * 
 * Owning the dependency links between {@code PushNotificationsActivity} and its
 * dependencies.
 * 
 * Being a {@code Provider<PushNotificationsActivity>} and handling creation and
 * preparation of object instances.
 * 
 * Being a {@code MembersInjector<PushNotificationsActivity>} and handling injection
 * of annotated fields.
 */
public final class PushNotificationsActivity$$InjectAdapter extends Binding<PushNotificationsActivity>
    implements Provider<PushNotificationsActivity>, MembersInjector<PushNotificationsActivity> {
  private Binding<br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider> serviceProvider;
  private Binding<br.com.mobiclub.quantoprima.ui.activity.FullScreenMobiClubActivity> supertype;

  public PushNotificationsActivity$$InjectAdapter() {
    super("br.com.mobiclub.quantoprima.ui.activity.notification.PushNotificationsActivity", "members/br.com.mobiclub.quantoprima.ui.activity.notification.PushNotificationsActivity", NOT_SINGLETON, PushNotificationsActivity.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    serviceProvider = (Binding<br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider>) linker.requestBinding("br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider", PushNotificationsActivity.class);
    supertype = (Binding<br.com.mobiclub.quantoprima.ui.activity.FullScreenMobiClubActivity>) linker.requestBinding("members/br.com.mobiclub.quantoprima.ui.activity.FullScreenMobiClubActivity", PushNotificationsActivity.class, false, true);
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
   * {@code Provider<PushNotificationsActivity>}.
   */
  @Override
  public PushNotificationsActivity get() {
    PushNotificationsActivity result = new PushNotificationsActivity();
    injectMembers(result);
    return result;
  }

  /**
   * Injects any {@code @Inject} annotated fields in the given instance,
   * satisfying the contract for {@code Provider<PushNotificationsActivity>}.
   */
  @Override
  public void injectMembers(PushNotificationsActivity object) {
    object.serviceProvider = serviceProvider.get();
    supertype.injectMembers(object);
  }
}
