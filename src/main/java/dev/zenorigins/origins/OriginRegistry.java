package dev.zenorigins.origins;

import dev.zenorigins.managers.OriginManager;
import dev.zenorigins.origins.types.*;

/**
 * Registry for all default origins
 */
public class OriginRegistry {
    
    public static void registerDefaults(OriginManager manager) {
        // Register all default origins
        manager.registerOrigin(new HumanOrigin());
        manager.registerOrigin(new EndermanOrigin());
        manager.registerOrigin(new BlazeOrigin());
        manager.registerOrigin(new SpiderOrigin());
        manager.registerOrigin(new PhantomOrigin());
        manager.registerOrigin(new WolfOrigin());
        manager.registerOrigin(new CatOrigin());
        manager.registerOrigin(new BeeOrigin());
        
        // Register new fun origins
        manager.registerOrigin(new VillagerOrigin());
        manager.registerOrigin(new SlimeOrigin());
        manager.registerOrigin(new DrownedOrigin());
        manager.registerOrigin(new IronGolemOrigin());
        manager.registerOrigin(new WitchOrigin());
        manager.registerOrigin(new PiglinOrigin());
    }
}
