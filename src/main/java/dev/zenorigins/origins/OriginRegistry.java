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
        
        // Register rare mob origins
        manager.registerOrigin(new PandaOrigin());
        manager.registerOrigin(new ParrotOrigin());
        manager.registerOrigin(new AxolotlOrigin());
        manager.registerOrigin(new OcelotOrigin());
        manager.registerOrigin(new MooshroomOrigin());
        
        // Register silly origins
        manager.registerOrigin(new ChickenOrigin());
        manager.registerOrigin(new SquidOrigin());
        manager.registerOrigin(new SheepOrigin());
    }
}
