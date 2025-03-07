package net.corespring.csaugmentations.Augmentations.Organs.Cybernetic;

import net.corespring.csaugmentations.Augmentations.Base.Organs.SimpleLungs;
import net.corespring.csaugmentations.Utility.IOrganTiers;

public class CyberLungs extends SimpleLungs {
    public CyberLungs(IOrganTiers pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    @Override
    public int getOrganValue() {
        return 12;
    }

}

