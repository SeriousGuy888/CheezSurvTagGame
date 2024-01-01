package io.github.seriousguy888.cheezsurvtaggame.config;

public class Ruleset {
    private boolean projectilesCanTag;
    private boolean shieldsCanBlock;
    private int tagbackCooldownMs;

    public Ruleset(boolean projectilesCanTag, boolean shieldsCanBlock, int tagbackCooldownMs) {
        this.projectilesCanTag = projectilesCanTag;
        this.shieldsCanBlock = shieldsCanBlock;
        this.tagbackCooldownMs = tagbackCooldownMs;
    }

    public boolean isProjectilesCanTag() {
        return projectilesCanTag;
    }

    public void setProjectilesCanTag(boolean projectilesCanTag) {
        this.projectilesCanTag = projectilesCanTag;
    }

    public boolean isShieldsCanBlock() {
        return shieldsCanBlock;
    }

    public void setShieldsCanBlock(boolean shieldsCanBlock) {
        this.shieldsCanBlock = shieldsCanBlock;
    }

    public int getTagbackCooldownMs() {
        return tagbackCooldownMs;
    }

    public void setTagbackCooldownMs(int tagbackCooldownMs) {
        this.tagbackCooldownMs = tagbackCooldownMs;
    }
}
