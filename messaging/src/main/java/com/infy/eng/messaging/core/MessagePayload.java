package com.infy.eng.messaging.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class constructs the message of "MessagePayload" format containing both payload (actual data) and metadata
 * @author Infosys
 *
 */
public class MessagePayload implements Serializable 
{
	private static final long serialVersionUID = -5347066584860585672L;
	private Map<String, String> metadata;
	private Object payload;

	public MessagePayload(Map<String, String> metadata, Object payload) 
	{
		this.metadata = metadata;
		this.payload = payload;
	}

	public Map<String, String> getAllMetadata() 
	{
		return metadata;
	}

	public void addAllMetadata(Map<String, String> metadata) 
	{
		if (this.metadata == null) 
		{
			this.metadata = new HashMap<String, String>();
		}
		this.metadata.putAll(metadata);
	}

	public void addMetadata(String key, String value) 
	{
		if (this.metadata == null) 
		{
			this.metadata = new HashMap<String, String>();
		}
		this.metadata.put(key, value);
	}

	public Object getPayload() 
	{
		return payload;
	}

	public void setPayload(Object payload) 
	{
		this.payload = payload;
	}

	public String printMessage() 
	{
		StringBuffer messageBuffer = new StringBuffer("[");
		if (this.metadata != null) 
		{
			messageBuffer.append("UUID:").append(this.metadata.get("UUID"));
		}
		if (this.payload != null) 
		{
			messageBuffer.append(" PAYLOAD:").append(this.payload);
		}
		messageBuffer.append("]");
		return messageBuffer.toString();
	}
}