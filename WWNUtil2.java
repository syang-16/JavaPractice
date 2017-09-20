package io.interview;

public class WWNUtil2 {
	public static void main(String args[]){
		try{
		System.out.println(validateWWN("1g:22:33:44:55:66:77:88"));
		}catch(InvalidWWNException e){
			e.printStackTrace();
		}
		
		
	}
    /**
     * Length of a WWN string in the format <tt>XX:XX:XX:XX:XX:XX:XX:XX</tt>.
     */
    public static final int WWN_LENGTH = 23;

    public static String validateWWN(final String wwn) throws InvalidWWNException {
        if (wwn == null) {
            throw new InvalidWWNException("WWN is null");
        }

        if (!isValidWWN(wwn)) {
            throw new InvalidWWNException("Malformed WWN: " + wwn);
        }

        return wwn;
    }

    /**
     * @param wwn The WWN to validate.
     * @return <tt>true</tt> if the specified WWN is valid, <tt>false</tt>
     *         otherwise.
     */
    public static boolean isValidWWN(final String wwn) {
        return isValidWWN(wwn, false, false);
    }

    /**
     * @param wwn The WWN to validate.
     * @param allowEmpty If <tt>true</tt>, allow <tt>null</tt> or empty WWN.
     * @param allowZero If <tt>true</tt>, allow zero WWN
     *        (<tt>00:00:00:00:00:00:00:00</tt>).
     * @return <tt>true</tt> if the specified WWN is valid, <tt>false</tt>
     *         otherwise.
     */
    public static boolean isValidWWN(final String wwn,
            final boolean allowEmpty, final boolean allowZero) {
        if ((wwn == null) || (wwn.length() == 0)) {
            return allowEmpty;
        }

        if (wwn.equals("00:00:00:00:00:00:00:00")) {
            return allowZero;
        }

        if (wwn.length() != WWN_LENGTH) {
            return false;
        }

        // Check that we have the proper sequence of digits and colons
        for (int i = 0; i < WWN_LENGTH; ) {
            // Check colon separator
            if ((i > 0) && (wwn.charAt(i++) != ':')) {
                return false;
            }
            // Check first hex digit
            if (Character.digit(wwn.charAt(i++), 16) < 0) {
                return false;
            }
            // Check second hex digit
            if (Character.digit(wwn.charAt(i++), 16) < 0) {
                return false;
            }
        }

        // All OK
        return true;
    }

    /**
     * Convenience method that calls
     * {@link #isValidWWN(String, boolean, boolean)}.
     *
     * @param wwn The WWN to validate.
     * @return <tt>true</tt> if the specified WWN is valid or empty,
     *         <tt>false</tt> otherwise.
     */
    public static boolean isValidWWNOrEmpty(final String wwn) {
        return isValidWWN(wwn, true, false);
    }

    /**
     *
     * @param wwn a wwn in the format XX:XX:XX:XX:XX:XX:XX:XX or XXXXXXXXXXXXXXXX
     * @return a WWN formatted as XX:XX:XX:XX:XX:XX:XX:XX
     * @throws InvalidWWNException
     */
    public static String formatWWN(final String wwn) throws InvalidWWNException {
        if (wwn == null)
            throw new InvalidWWNException("WWN is null");

        if (wwn.length() == WWN_LENGTH){
            return validateWWN(wwn.toUpperCase());
        }
        if (wwn.length() != 16){
            throw new InvalidWWNException("Malformed WWN: " + wwn);
        }
        final StringBuilder builder = new StringBuilder(WWN_LENGTH);
        String separator = "";
        for (int i = 0; i < 16; i+=2){
            builder.append(separator);
            builder.append(Character.toUpperCase(wwn.charAt(i)));
            builder.append(Character.toUpperCase(wwn.charAt(i + 1)));
            separator = ":";
        }
        return validateWWN(builder.toString());

    }

    public static class InvalidWWNException extends Exception {
        public InvalidWWNException() {
            super();
        }

        public InvalidWWNException(String message) {
            super(message);
        }

        public InvalidWWNException(String message, Throwable cause) {
            super(message, cause);
        }

        public InvalidWWNException(Throwable cause) {
            super(cause);
        }
    }
}

