package tss.g2.fyre.controllers.utils;

import tss.g2.fyre.models.entity.Authorization;

import java.util.Map;

/**
 * Class for get user login from token storage.
 */
public class UserLogin {
  private Map<String, Authorization> tokenStorage;
  private String token;

  /**
   * Constructor.
   * @param tokenStorage token storage
   * @param token token of session
   */
  public UserLogin(Map<String, Authorization> tokenStorage, String token) {
    this.tokenStorage = tokenStorage;
    this.token = token;
  }

  /**
   * Get user login.
   * @return login
   */
  public String get() {
    String login = "-";
    Authorization authorization = tokenStorage.get(token);
    if (token != null && authorization != null ) {
      login = authorization.getLogin();
    }
    return login;
  }
}
