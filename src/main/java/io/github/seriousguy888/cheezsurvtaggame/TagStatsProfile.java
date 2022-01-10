package io.github.seriousguy888.cheezsurvtaggame;

public class TagStatsProfile {
  private int tagsTaken;
  private int tagsGiven;

  public TagStatsProfile(int tagsTaken, int tagsGiven) {
    this.tagsTaken = tagsTaken;
    this.tagsGiven = tagsGiven;
  }

  public int getTagsTaken() {
    return tagsTaken;
  }
  public void incrementTagsTaken() {
    tagsTaken++;
  }
  public int getTagsGiven() {
    return tagsGiven;
  }
  public void incrementTagsGiven() {
    tagsGiven++;
  }
}
