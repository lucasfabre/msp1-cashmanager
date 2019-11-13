package fr.cashmanager.rpc.exception;

/**
 * JsonRpcException
 * Exception to be used inside the Json RPC implementation
 */
public class JsonRpcException extends Exception {

    private static final long serialVersionUID = 1L;
    private int code;

    public JsonRpcException(int code, String message) {
        super(message);
        this.code = code;
    }

    public JsonRpcException(int code, String message, Throwable e) {
        super(message, e);
        this.code = code;
    }

    public JsonRpcException(JsonRpcErrorCode err) {
        this(err.getCode(), err.getMessage());
    }

    public int getCode() {
        return code;
    }
    
}