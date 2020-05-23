package com.sharescrow.payment.util;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

public class Util {

	public static boolean collectionEmpty(Collection collection){
		return Objects.isNull(collection) || collection.isEmpty();
	}

	public static String extractPostRequestBody(HttpServletRequest request) {
		if ("POST".equalsIgnoreCase(request.getMethod())) {
			Scanner s = null;
			try {
				s = new Scanner(request.getInputStream(), "UTF-8").useDelimiter("\\A");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return s.hasNext() ? s.next() : "";
		}
		return "";
	}
}

