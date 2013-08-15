
package com.douban.sdk.android;


public class DoubanException extends Exception {

	private static final long serialVersionUID = 475022994858770424L;
	private int statusCode = -1;
	
    public DoubanException(String msg) {
        super(msg);
    }

    public DoubanException(Exception cause) {
        super(cause);
    }

    public DoubanException(String msg, int statusCode) {
        super(msg);
        this.statusCode = statusCode;
    }

    public DoubanException(String msg, Exception cause) {
        super(msg, cause);
    }

    public DoubanException(String msg, Exception cause, int statusCode) {
        super(msg, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return this.statusCode;
    }
    
    
	public DoubanException() {
		super(); 
	}

	public DoubanException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public DoubanException(Throwable throwable) {
		super(throwable);
	}

	public DoubanException(int statusCode) {
		super();
		this.statusCode = statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	

}
