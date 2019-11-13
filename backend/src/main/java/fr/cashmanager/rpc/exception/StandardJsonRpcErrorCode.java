package fr.cashmanager.rpc.exception;

/**
 * Preferences
 */
public enum StandardJsonRpcErrorCode {

    PARSE_ERROR(-32700, "Parse error"),
    INVALID_REQUEST(-32600, "Invalid Request"),
    METHOD_NOT_FOUND(-32601, "Method not found"),
    INVALID_PARAMS(-32602, "Invalid params"),
    INTERNAL_ERROR(-32603, "Internal error");

    private int code;
    private String message;
 
    /**
     * default constructor
     * @param level the level
     */
    StandardJsonRpcErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
 
    /**
     * get the code
     * @return the code
     */
    public int getCode() {
        return this.code;
    }

    /**
     * get the Message
     * @return the message
     */
    public String getMessage() {
        return this.message;
    }
}