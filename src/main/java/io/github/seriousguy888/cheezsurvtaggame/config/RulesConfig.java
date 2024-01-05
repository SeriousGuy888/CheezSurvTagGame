package io.github.seriousguy888.cheezsurvtaggame.config;

import io.github.seriousguy888.cheezsurvtaggame.CheezSurvTagGame;

public class RulesConfig extends CustomConfigReader {
    public RulesConfig(CheezSurvTagGame plugin, String name) {
        super(plugin, name);
    }

    public boolean getProjectilesCanTag() {
        return config.getBoolean("projectiles-can-tag", false);
//        return this.<Boolean>get(Option.PROJECTILES_CAN_TAG);
    }

    public void setProjectilesCanTag(boolean enabled) {
        config.set("projectiles-can-tag", enabled);
    }

    public boolean getShieldsCanBlock() {
        return config.getBoolean("shields-can-block", false);
    }

    public void setShieldsCanBlock(boolean enabled) {
        config.set("shields-can-block", enabled);
    }

    public int getTagbackCooldownMs() {
        return config.getInt("tagback-cooldown-ms", 3000);
    }

    public void setTagbackCooldownMs(int milliseconds) {
        config.set("tagback-cooldown-ms", milliseconds);
    }

//    @SuppressWarnings("unchecked")
//    private <T> T get(Option option) {
//        Object val = config.get(option.key, option.default_);
//        try {
//            if (val != null) {
//                return (T) val;
//            }
//        } catch (ClassCastException ignored) {
//        }
//        return null;
//    }
//
//    private void set(Option option, Object value) {
//        config.set(option.key, value);
//    }
//
//    public enum Option {
//        PROJECTILES_CAN_TAG("projectiles-can-tag", false),
//        SHIELDS_CAN_BLOCK("shields-can-block", false),
//        TAGBACK_COOLDOWN_MILLISECONDS("tagback-cooldown-ms", 3000),
//        ;
//
//        public final String key;
//        public final Object default_;
//
//        Option(String key, Object default_) {
//            this.key = key;
//            this.default_ = default_;
//        }
//    }
}
