package com.teddycrane.springpractice.repository;

import com.google.cloud.firestore.Firestore;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class Repository<T extends Object> {
	public abstract void write();

	public abstract T read();
}
