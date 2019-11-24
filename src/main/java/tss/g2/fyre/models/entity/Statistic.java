package tss.g2.fyre.models.entity;

public class Statistic {
  private String api;
  private double avg;
  private int min;
  private long max;
  private long count;

  /**
   * Constructor
   * @param api api
   * @param avg avg time
   * @param min min time
   * @param max max time
   * @param count count
   */
  public Statistic(String api, double avg, int min, long max, long count) {
    this.api = api;
    this.avg = avg;
    this.min = min;
    this.max = max;
    this.count = count;
  }

  /**
   * Method for get api.
   * @return api
   */
  public String getApi() {
    return api;
  }

  /**
   * Method for get avg time.
   * @return avg time
   */
  public double getAvg() {
    return avg;
  }

  /**
   * Method for get min time.
   * @return min time
   */
  public int getMin() {
    return min;
  }

  /**
   * Method for get max time
   * @return max time
   */
  public long getMax() {
    return max;
  }

  /**
   * Method for get count
   * @return
   */
  public long getCount() {
    return count;
  }
}
