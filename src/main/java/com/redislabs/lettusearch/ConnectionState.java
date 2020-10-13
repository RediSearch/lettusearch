package com.redislabs.lettusearch;

import java.util.List;

public class ConnectionState extends io.lettuce.core.ConnectionState {

	@Override
	public void setUserNamePassword(List<char[]> args) {
		super.setUserNamePassword(args);
	}

	@Override
	public void setDb(int db) {
		super.setDb(db);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
	}
	
	@Override
	public void setClientName(String clientName) {
		super.setClientName(clientName);
	}
	
}
