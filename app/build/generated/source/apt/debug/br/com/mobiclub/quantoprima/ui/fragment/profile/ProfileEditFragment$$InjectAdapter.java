// Code generated by dagger-compiler.  Do not edit.
package br.com.mobiclub.quantoprima.ui.fragment.profile;


import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

/**
 * A {@code Binder<ProfileEditFragment>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 * 
 * Owning the dependency links between {@code ProfileEditFragment} and its
 * dependencies.
 * 
 * Being a {@code Provider<ProfileEditFragment>} and handling creation and
 * preparation of object instances.
 * 
 * Being a {@code MembersInjector<ProfileEditFragment>} and handling injection
 * of annotated fields.
 */
public final class ProfileEditFragment$$InjectAdapter extends Binding<ProfileEditFragment>
    implements Provider<ProfileEditFragment>, MembersInjector<ProfileEditFragment> {
  private Binding<br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider> serviceProvider;
  private Binding<com.squareup.otto.Bus> bus;

  public ProfileEditFragment$$InjectAdapter() {
    super("br.com.mobiclub.quantoprima.ui.fragment.profile.ProfileEditFragment", "members/br.com.mobiclub.quantoprima.ui.fragment.profile.ProfileEditFragment", NOT_SINGLETON, ProfileEditFragment.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    serviceProvider = (Binding<br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider>) linker.requestBinding("br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider", ProfileEditFragment.class);
    bus = (Binding<com.squareup.otto.Bus>) linker.requestBinding("com.squareup.otto.Bus", ProfileEditFragment.class);
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
    injectMembersBindings.add(serviceProvider);
    injectMembersBindings.add(bus);
  }

  /**
   * Returns the fully provisioned instance satisfying the contract for
   * {@code Provider<ProfileEditFragment>}.
   */
  @Override
  public ProfileEditFragment get() {
    ProfileEditFragment result = new ProfileEditFragment();
    injectMembers(result);
    return result;
  }

  /**
   * Injects any {@code @Inject} annotated fields in the given instance,
   * satisfying the contract for {@code Provider<ProfileEditFragment>}.
   */
  @Override
  public void injectMembers(ProfileEditFragment object) {
    object.serviceProvider = serviceProvider.get();
    object.bus = bus.get();
  }
}
