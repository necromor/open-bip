package net.jewczuk.openbip.to;

import java.time.LocalDateTime;

public class HistoryTO {

	private String action;
	private String createdBy;
	private LocalDateTime createdAt;
	
	public static class Builder {
		private String action;
		private String createdBy;
		private LocalDateTime createdAt;
		
		public Builder action(String action) {
			this.action = action;
			return this;
		}
		
		public Builder createdBy(String createdBy) {
			this.createdBy = createdBy;
			return this;
		}
		
		public Builder createdAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
			return this;
		}
		
		public HistoryTO build() {
			return new HistoryTO(this);
		}
		
	}
	
	private HistoryTO(Builder builder) {
		action = builder.action;
		createdBy = builder.createdBy;
		createdAt = builder.createdAt;
	}
	
	public HistoryTO() {
		
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
}
