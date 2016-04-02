/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2014 Justin Palinkas
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package jp77.utillib.net.protocol.http.client;

public class HttpCodes {
	public static final int _CONTINUE_ = 100;
	public static final int _SWITCHING_PROTOCOL_ = 101;
	public static final int _OK_ = 200;
	public static final int _CREATED_ = 201;
	public static final int _ACCEPTED_ = 202;
	public static final int _NON_AUTHORITIVE_ = 203;
	public static final int _NO_CONTENT_ = 204;
	public static final int _RESET_CONTENT_ = 205;
	public static final int _PARTIAL_CONTENT_ = 206;
	public static final int _MULTIPLE_CHOICES_ = 300;
	public static final int _MOVED_PERMANENTLY_ = 301;
	public static final int _FOUND_ = 302;
	public static final int _SEE_OTHER_ = 303;
	public static final int _NOT_MODIFIED_ = 304;
	public static final int _USE_PROXY_ = 305;
	public static final int _UNUSED_ = 306;
	public static final int _TEMPORARY_REDIRECT_ = 307;
	public static final int _BAD_REQUEST_ = 400;
	public static final int _UNAUTHORIZED_ = 401;
	public static final int _PAYMENT_REQUIRED_ = 402;
	public static final int _FORBIDDEN_ = 403;
	public static final int _NOT_FOUND_ = 404;
	public static final int _METHOD_NOT_ALLOWED_ = 405;
	public static final int _NOT_ACCEPTABLE_ = 406;
	public static final int _PROXY_AUTHENTICATION_REQUIRED_ = 407;
	public static final int _REQUEST_TIMEOUT_ = 408;
	public static final int _CONFLICT_ = 409;
	public static final int _GONE_ = 410;
	public static final int _LENGTH_REQUIRED_ = 411;
	public static final int _PRECONDITION_FAIL_ = 412;
	public static final int _REQUEST_ENTITY_TOO_LARGE_ = 413;
	public static final int _REQUEST_URI_TOO_LARGE_ = 414;
	public static final int _UNSUPPORTED_MEDIA_TYPE_ = 415;
	public static final int _REQUEST_RANGE_NOT_SATISFIABLE_ = 416;
	public static final int _EXPECTATION_FAILDED_ = 417;
	public static final int _INTERNAL_SERVER_ERROR_ = 500;
	public static final int _NOT_IMPLEMENTED_ = 501;
	public static final int _BAD_GATEWAY_ = 502;
	public static final int _SERVICE_UNAVAILABLE_ = 503;
	public static final int _GATEWAY_TIMEOUT_ = 504;
	public static final int _HTTP_VERSION_NOT_SUPPORTED_ = 505;

//	public static final int _TYPE_INFORMATION_ = 0;
//	public static final int _TYPE_SUCCESS_ = 1;
//	public static final int _TYPE_REDIRECTION_ = 2;
//	public static final int _TYPE_CLIENT_ERROR_ = 3;
//	public static final int _TYPE_SERVER_ERROR_ = 4;

	public static enum Type {
		INFORMATION,
		SUCCESS,
		REDIRECTION,
		CLIENT_ERROR,
		SERVER_ERROR;
	};

	public static boolean validStatusCode(int value) {
		switch(value) {
			case _CONTINUE_:
			case _SWITCHING_PROTOCOL_:
			case _OK_:
			case _CREATED_:
			case _ACCEPTED_:
			case _NON_AUTHORITIVE_:
			case _NO_CONTENT_:
			case _RESET_CONTENT_:
			case _PARTIAL_CONTENT_:
			case _MULTIPLE_CHOICES_:
			case _MOVED_PERMANENTLY_:
			case _FOUND_:
			case _SEE_OTHER_:
			case _NOT_MODIFIED_:
			case _USE_PROXY_:
			case _UNUSED_:
			case _TEMPORARY_REDIRECT_:
			case _BAD_REQUEST_:
			case _UNAUTHORIZED_:
			case _PAYMENT_REQUIRED_:
			case _FORBIDDEN_:
			case _NOT_FOUND_:
			case _METHOD_NOT_ALLOWED_:
			case _NOT_ACCEPTABLE_:
			case _PROXY_AUTHENTICATION_REQUIRED_:
			case _REQUEST_TIMEOUT_:
			case _CONFLICT_:
			case _GONE_:
			case _LENGTH_REQUIRED_:
			case _PRECONDITION_FAIL_:
			case _REQUEST_ENTITY_TOO_LARGE_:
			case _REQUEST_URI_TOO_LARGE_:
			case _UNSUPPORTED_MEDIA_TYPE_:
			case _REQUEST_RANGE_NOT_SATISFIABLE_:
			case _EXPECTATION_FAILDED_:
			case _INTERNAL_SERVER_ERROR_:
			case _NOT_IMPLEMENTED_:
			case _BAD_GATEWAY_:
			case _SERVICE_UNAVAILABLE_:
			case _GATEWAY_TIMEOUT_:
			case _HTTP_VERSION_NOT_SUPPORTED_:
				return true;

			default:
				return false;
		}
	}

	public static boolean statusInformation(HttpEntry entry) {
		return status(entry, Type.INFORMATION);
	}

	public static boolean statusSuccess(HttpEntry entry) {
		return status(entry, Type.SUCCESS);
	}

	public static boolean statusRedirection(HttpEntry entry) {
		return status(entry, Type.REDIRECTION);
	}

	public static boolean statusClientError(HttpEntry entry) {
		return status(entry, Type.CLIENT_ERROR);
	}

	public static boolean statusServerError(HttpEntry entry) {
		return status(entry, Type.SERVER_ERROR);
	}

	public static boolean status(HttpEntry entry, Type type) {
		if(entry == null) {
			throw new RuntimeException("Variable[entry] - Is Null");
		}

		if(type == null) {
			throw new RuntimeException("Variable[type] - Is Null");
		}

		return (statusCodeType(entry.getStatusCode()) == type);
	}

	public static Type statusCodeType(int value) {
		if(value >= 100 && value < 200) {
			return Type.INFORMATION;
		} else if(value >= 200 && value < 300) {
			return Type.SUCCESS;
		} else if(value >= 300 && value < 400) {
			return Type.REDIRECTION;
		} else if(value >= 400 && value < 500) {
			return Type.CLIENT_ERROR;
		} else {
			return Type.SERVER_ERROR;
		}
	}
}
