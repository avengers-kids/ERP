package com.erp.erp.infrastructure.utility;

public final class StringUtils {

  public static final String EMPTY = "";
  public static final String SPACE = " ";

  private StringUtils() {

  }

  public static String leftMessage(String str, int startingIndex, int messageSize) {
    if (str == null) {
      return null;
    } else if (messageSize < 0) {
      return EMPTY;
    } else if (str.length() <= messageSize) {
      return str;
    } else {
      return str.substring(startingIndex, messageSize);
    }
  }

  public static boolean isEmpty(String str) {
    return str == null || str.length() == 0;
  }

  public static boolean isBlank(String str) {
    int length = (str == null) ? 0 : str.length();
    if (length == 0) {
      return true;
    } else {
      return str.trim().isEmpty();
    }
  }

  public static boolean isNotBlank(String str) {
    return !isBlank(str);
  }

  @SuppressWarnings({"squid:S4973", "StringEquality"})
  public static boolean equals(String str1, String str2) {
    if (str1 == str2) {
      return true;
    } else if (str1 == null || str2 == null) {
      return false;
    } else if (str1.length() != str2.length()) {
      return false;
    } else {
      return str1.equals(str2);
    }
  }

  public static boolean isNumeric(String strNum) {
    if (strNum == null || strNum.length() == 0) {
      return false;
    } else {
      int size = strNum.length();
      for (int i = 0; i < size; ++i) {
        if (!Character.isDigit(strNum.charAt(i))) {
          return false;
        }
      }
      return true;
    }
  }

  public static String defaultString(String originalString, String replaceString) {
    return originalString == null ? replaceString : originalString;
  }

  public static String initCap(String str) {
    if (str == null || str.length() == 0) {
      return str;
    }
    return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
  }
}
