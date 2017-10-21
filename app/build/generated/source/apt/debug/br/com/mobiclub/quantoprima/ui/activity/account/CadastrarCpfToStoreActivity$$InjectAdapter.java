// Code generated by dagger-compiler.  Do not edit.
package br.com.mobiclub.quantoprima.ui.activity.account;


import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

/**
 * A {@code Binder<CadastrarCpfToStoreActivity>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 * 
 * Owning the dependency links between {@code CadastrarCpfToStoreActivity} and its
 * dependencies.
 * 
 * Being a {@code Provider<CadastrarCpfToStoreActivity>} and handling creation and
 * preparation of object instances.
 * 
 * Being a {@code MembersInjector<CadastrarCpfToStoreActivity>} and handling injection
 * of annotated fields.
 */
public final class CadastrarCpfToStoreActivity$$InjectAdapter extends Binding<CadastrarCpfToStoreActivity>
    implements Provider<CadastrarCpfToStoreActivity>, MembersInjector<CadastrarCpfToStoreActivity> {
  private Binding<br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi> mobiClubService;
  private Binding<br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider> serviceProvider;

  public CadastrarCpfToStoreActivity$$InjectAdapter() {
    super("br.com.mobiclub.quantoprima.ui.activity.account.CadastrarCpfToStoreActivity", "members/br.com.mobiclub.quantoprima.ui.activity.account.CadastrarCpfToStoreActivity", NOT_SINGLETON, CadastrarCpfToStoreActivity.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    mobiClubService = (Binding<br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi>) linker.requestBinding("br.com.mobiclub.quantoprima.core.service.MobiClubServiceApi", CadastrarCpfToStoreActivity.class);
    serviceProvider = (Binding<br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider>) linker.requestBinding("br.com.mobiclub.quantoprima.core.service.MobiClubServiceProvider", CadastrarCpfToStoreActivity.class);
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
    injectMembersBindings.add(mobiClubService);
    injectMembersBindings.add(serviceProvider);
  }

  /**
   * Returns the fully provisioned instance satisfying the contract for
   * {@code Provider<CadastrarCpfToStoreActivity>}.
   */
  @Override
  public CadastrarCpfToStoreActivity get() {
    CadastrarCpfToStoreActivity result = new CadastrarCpfToStoreActivity();
    injectMembers(result);
    return result;
  }

  /**
   * Injects any {@code @Inject} annotated fields in the given instance,
   * satisfying the contract for {@code Provider<CadastrarCpfToStoreActivity>}.
   */
  @Override
  public void injectMembers(CadastrarCpfToStoreActivity object) {
    object.mobiClubService = mobiClubService.get();
    object.serviceProvider = serviceProvider.get();
  }
}
