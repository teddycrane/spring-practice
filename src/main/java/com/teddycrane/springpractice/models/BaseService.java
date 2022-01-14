package com.teddycrane.springpractice.models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class BaseService {
  protected final Logger logger;

  public BaseService() { this.logger = LogManager.getLogger(this.getClass()); }
}
