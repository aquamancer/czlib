package com.aquamancer.czlib.api.abils.gifts;

import com.aquamancer.czlib.api.abils.AbilitySpec;

public class CallicarpasHat extends Gift {
    private final AbilitySpec spec;
    public CallicarpasHat(AbilitySpec spec) {
        super(Gifts.CALLICARPAS_POINTED_HAT);
        this.spec = spec;
    }

    public AbilitySpec getSpec() {
        return this.spec;
    }
}
