package com.brookfieldpropertiesprogram.core.services.http;

public final class Constants {

    public static final String IP_ADDRESS = "IPAddress";
    public static final String MACHINE_NAME = "MachineName";
    public static final String VENDOR_ID = "1";
    public static final int RETRY_COUNT = 3;
    public static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss.SSS";
    public static final String DATE_FORMAT_ENCODED = "yyyySSS$MM#,dd.HHmm-ss";
    public static final String METHOD_NAME_SEND_REQUEST = "sendRequest";
    public static final String MESSAGE = "message";
    public static final String EXCEPTION_TYPE = "exceptionType";
    public static final String REQUEST_AND_SESSION_ID = "requestAndSessionId";
    public static final String EXCEPTION_MESSAGE = "exceptionMessage";
    public static final String QUERYSTRING = "querystring";
    public static final String COOKIESTRING = "cookiestring";
    public static final String REQUEST_OBJECT_STRING = "request";
    public static final String RESPONSE_OBJECT_STRING = "Response";
    public static final String ERROR_CODE = "errorcode";
    public static final String ADDITIONAL_INFO = "additionalInfo";
    public static final String CART_EXPERIENCE = "cartexperience";
    public static final String DATA_SECURITY = "datasecurity";
    public static final String STACK_TRACE = "stacktrace";
    public static final String ACTION_NAME = "ActionName";
    public static final String ADD_PRODUCT_TO_CART = "AddProductToCart";
    public static final String PURCHASE_COMPLETE = "PurchaseComplete";
    public static final String UNIT_OF_MEASUREMENT = "UnitOfMeasurement";
    public static final int THRESHOLD_TIME = 500;

    public static final String METHOD_NAME_CALL_EXECUTE = "callExecuteMethod";
    public static final String ENCODING_UTF_8 = "UTF-8";
    public static final int MAX_CONNECTIONS = 100;
    public static final String HTTP_PROTOCOL_EXPECT_CONTINUE = "http.protocol.expect-continue";
    public static final int CONNECTION_TIME_OUT = 30000;
    public static final String ENTERING = "ENTERING - ";
    public static final String EXITING = "EXITING - ";
    public static final String ACTIVATE_METHOD = "ACTIVATE METHOD - ";
    public static final String DEACTIVATE_METHOD = "DEACTIVATE METHOD - ";
    public static final int SO_TIME_OUT = 1000;

    public static final String COMPONENT_NAME_PREFIX = "BrookfieldCore-";

    private Constants() {

    }
}
