package br.com.mobiclub.quantoprima.config.module;

import dagger.Module;

/**
 * Add all the other modules to this one.
 */
@Module(
        complete = false,
        includes = {
                AndroidModule.class,
                MobiClubModule.class,
                ServiceModule.class,
        }
)
public class RootModule {

}
