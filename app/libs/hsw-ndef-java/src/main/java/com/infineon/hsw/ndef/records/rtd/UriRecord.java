// SPDX-FileCopyrightText: 2024 Infineon Technologies AG
//
// SPDX-License-Identifier: MIT

package com.infineon.hsw.ndef.records.rtd;

import com.infineon.hsw.ndef.exceptions.NdefException;
import com.infineon.hsw.utils.annotation.NotNull;

/**
 * The NFC local type name for the action is 'U' (0x55). URI record is a record
 * that stores the URI information such as a website URL in a tag. URI is the
 * core of the smart poster, and all the records are just associated with
 * metadata about this record. Only one URI record can be present in the smart
 * poster record.
 */
public class UriRecord extends AbstractWellKnownTypeRecord {
    /**
     * The well known type for an URI record is 'U' (0x55 in the NDEF binary
     * representation).
     */
    public static final String URI_TYPE = "U";

    /**
     * Error message stating that the URI should not be empty.
     */
    public static final String
            ERR_MESSAGE_URI_NOT_EMPTY = "URI cannot be empty";

    /**
     * The rest of the URI other than URI identifier, or the entire URI (if
     * identifier code is 0x00).
     */
    private String uri;

    /**
     * URI identifier
     */
    private UriIdentifier uriIdentifier;

    /**
     * Constructor to create a new URI record. This method will internally split
     * the URI protocol prefix field (For example, http://www.) and the URI
     * value field (For example, company.com) to create the URI record.
     *
     * @param uri Uniform resource identifier (URI) (For example,
     *            <i>https://www.company.com/</i>)
     * @throws NdefException Throws an NDEF exception, if URI is null or empty.
     */
    public UriRecord(@NotNull String uri) throws NdefException {
        super(URI_TYPE);
        if (uri.isEmpty()) {
            throw new NdefException(ERR_MESSAGE_URI_NOT_EMPTY);
        }
        UriIdentifier identifier = findMatchingIdentifier(uri);
        this.uriIdentifier = identifier;
        this.uri = uri.substring(identifier.getProtocol().length());
    }

    /**
     * Check for the closely matching protocol identifier from the list of URI
     * identifiers.
     *
     * @param uriWithPrefix URI including identifier protocol prefix.
     * @return Returns the matched URI identifier.
     */
    private UriIdentifier findMatchingIdentifier(
            @NotNull String uriWithPrefix) {
        UriIdentifier matchedUriIdentifier = UriIdentifier.URI_NA;
        for (UriIdentifier tempUriIdentifier : UriIdentifier.values()) {
            if (uriWithPrefix.startsWith(tempUriIdentifier.getProtocol())) {
                matchedUriIdentifier = tempUriIdentifier;
                // Still continue to match if more closer matches available.
                // For example, urn:nfc: matches more closely than uniform
                // resource name (URN).:
            }
        }
        return matchedUriIdentifier;
    }

    /**
     * Constructor to create a new URI record with separate URI prefix and URI
     * field.
     *
     * @param identifier URI protocol prefix (For example,
     *     UriIdentifier.URI_HTTPS_WWW)
     * @param uri        URI without protocol prefix.
     *                   For example, <i>company.com/</i>
     * @throws NdefException Throws an NDEF exception if URI is null or empty.
     */
    public UriRecord(@NotNull UriIdentifier identifier, @NotNull String uri)
            throws NdefException {
        super(URI_TYPE);
        if (uri.isEmpty()) {
            throw new NdefException(ERR_MESSAGE_URI_NOT_EMPTY);
        }
        this.uriIdentifier = identifier;
        this.uri = uri;
    }

    /**
     * Constructor to create a new URI record with separate URI prefix and URI
     * field
     *
     * @param identifierCode URI protocol prefix code (For example, 0x02 for
     *     https://www.)
     * @param uri            URI. For example, <i>https://www.company.com/</i>
     * @throws NdefException Throws an NDEF exception if URI is null or empty.
     */
    public UriRecord(@NotNull int identifierCode, @NotNull String uri)
            throws NdefException {
        super(URI_TYPE);
        if (uri.isEmpty()) {
            throw new NdefException(ERR_MESSAGE_URI_NOT_EMPTY);
        }
        this.uriIdentifier = UriIdentifier.getEnumByUriIdentifierCode(
                identifierCode);
        this.uri = uri;
    }

    /**
     * Gets the URI string without identifier.
     *
     * @return Returns the URI.
     */
    public String getUri() {
        return uri;
    }

    /**
     * Gets the URI string with prefix if present.
     *
     * @return Returns the URI.
     */
    public String getUriWithIdentifier() {
        return uriIdentifier.getProtocol() + uri;
    }

    /**
     * Gets the index to abbreviate the URI identifier including
     * prefix and value.
     *
     * @return Returns the URI identifier.
     */
    public UriIdentifier getUriIdentifier() {
        return uriIdentifier;
    }

    /**
     * Sets the URI identifier enumeration onto the record.
     *
     * @param identifier URI identifier
     */
    public void setUriIdentifier(@NotNull UriIdentifier identifier) {
        this.uriIdentifier = identifier;
    }

    /**
     * Sets the URI string onto the record.
     *
     * @param uri Sets the URI.
     */
    public void setUri(@NotNull String uri) {
        this.uri = uri;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Uri: [" + uriIdentifier.protocol + uri + "]";
    }

    /**
     * Enumeration to store all abbreviate URI's values defined in the URI RTD
     * specification.
     */
    public enum UriIdentifier {
        // cspell:ignore rtsp, btspp, btgoep, tcpobex, irdaobex
        /**
         * N/A. No prepending is done, and the URI field contains the unabridged
         * URI.
         */
        URI_NA(0x00, ""),
        /**
         * Abbreviation enumeration for https://www
         */
        URI_HTTP_WWW(0x01, "http://www."),
        /**
         * Abbreviation enumeration for https://www
         */
        URI_HTTPS_WWW(0x02, "https://www."),
        /**
         * Abbreviation enumeration for http://
         */
        URI_HTTP(0x03, "http://"),
        /**
         * Abbreviation enumeration for https://
         */
        URI_HTTPS(0x04, "https://"),
        /**
         * Abbreviation enumeration for tel:
         */
        URI_TEL(0x05, "tel:"),
        /**
         * Abbreviation enumeration for mailto:
         */
        URI_MAILTO(0x06, "mailto:"),
        /**
         * Abbreviation enumeration for mftp://anonymous:anonymous@
         */
        URI_FTP_ANONYMOUS_ANONYMOUS(0x07, "ftp://anonymous:anonymous@"),
        /**
         * Abbreviation enumeration for ftp://ftp.
         */
        URI_FTP_FTP(0x08, "ftp://ftp."),
        /**
         * Abbreviation enumeration for ftps://
         */
        URI_FTPS(0x09, "ftps://"),
        /**
         * Abbreviation enumeration for sftp://
         */
        URI_SFTP(0x0A, "sftp://"),
        /**
         * Abbreviation enumeration for smb://
         */
        URI_SMB(0x0B, "smb://"),
        /**
         * Abbreviation enumeration for nfs://
         */
        URI_NFS(0x0C, "nfs://"),
        /**
         * Abbreviation enumeration for ftp://
         */
        URI_FTP(0x0D, "ftp://"),
        /**
         * Abbreviation enumeration for dav://
         */
        URI_DAV(0x0E, "dav://"),
        /**
         * Abbreviation enumeration for news:
         */
        URI_NEWS(0x0F, "news:"),
        /**
         * Abbreviation enumeration for telnet://
         */
        URI_TELNET(0x10, "telnet://"),
        /**
         * Abbreviation enumeration for imap:
         */
        URI_IMAP(0x11, "imap:"),
        /**
         * Abbreviation enumeration for rtsp://
         */
        URI_RTSP(0x12, "rtsp://"),
        /**
         * Abbreviation enumeration for urn:
         */
        URI_URN(0x13, "urn:"),
        /**
         * Abbreviation enumeration for pop:
         */
        URI_POP(0x14, "pop:"),
        /**
         * Abbreviation enumeration for sip:
         */
        URI_SIP(0x15, "sip:"),
        /**
         * Abbreviation enumeration for sips:
         */
        URI_SIPS(0x16, "sips:"),
        /**
         * Abbreviation enumeration for tftp:
         */
        URI_TFTP(0x17, "tftp:"),
        /**
         * Abbreviation enumeration for btspp://
         */
        URI_BTSPP(0x18, "btspp://"),
        /**
         * Abbreviation enumeration for btl2cap://
         */
        URI_BT12CAP(0x19, "btl2cap://"),
        /**
         * Abbreviation enumeration for btgoep://
         */
        URI_BTGOEP(0x1A, "btgoep://"),
        /**
         * Abbreviation enumeration for tcpobex://
         */
        URI_TCPOBEX(0x1B, "tcpobex://"),
        /**
         * Abbreviation enumeration for irdaobex://
         */
        URI_IRDAOBEX(0x1C, "irdaobex://"),
        /**
         * Abbreviation enumeration for file://
         */
        URI_FILE(0x1D, "file://"),
        /**
         * Abbreviation enumeration for urn:epc:id:
         */
        URI_URN_EPC_ID(0x1E, "urn:epc:id:"),
        /**
         * Abbreviation enumeration for urn:epc:tag:
         */
        URI_URN_EPC_TAG(0x1F, "urn:epc:tag:"),
        /**
         * Abbreviation enumeration for urn:epc:pat:
         */
        URI_URN_EPC_PAT(0x20, "urn:epc:pat:"),
        /**
         * Abbreviation enumeration for urn:epc:raw:
         */
        URI_URN_EPC_RAW(0x21, "urn:epc:raw:"),
        /**
         * Abbreviation enumeration for urn:epc:
         */
        URI_URN_EPC(0x22, "urn:epc:"),
        /**
         * Abbreviation enumeration for urn:nfc:
         */
        URI_URN_NFC(0x23, "urn:nfc:");

        /**
         * URI identifier code
         */
        private final int identifierCode;
        /**
         * URI identifier protocol as string
         */
        private final String protocol;

        UriIdentifier(@NotNull int identifierCode, @NotNull String protocol) {
            this.identifierCode = identifierCode;
            this.protocol = protocol;
        }

        /**
         * @return Returns the URI identifier code.
         */
        public int getIdentifierCode() {
            return this.identifierCode;
        }

        /**
         * @return Returns the URI identifier protocol as string.
         */
        public String getProtocol() {
            return this.protocol;
        }

        /**
         * Returns the URI identifier by the closely matching protocol
         * identifier.
         *
         * @param uriWithPrefix URI including identifier protocol prefix.
         * @return Matched URI identifier enumeration, null if no matching
         * 		   enumeration is found.
         */
        public static UriIdentifier getEnumByUriIdentifierPrefix(
                @NotNull String uriWithPrefix) {
            for (UriIdentifier uriIdentifier : UriIdentifier.values()) {
                if (uriIdentifier.getProtocol().equalsIgnoreCase(
                            uriWithPrefix)) {
                    return uriIdentifier;
                }
            }
            return null;
        }

        /**
         * Returns the URI identifier by the closely matching protocol
         * identifier code.
         *
         * @param identifierCode URI identifier code.
         * @return Returns the matched URI identifier enumeration, null if no
         * 		   matching enumeration is found.
         */
        public static UriIdentifier getEnumByUriIdentifierCode(
                @NotNull int identifierCode) {
            for (UriIdentifier uriIdentifier : UriIdentifier.values()) {
                if (uriIdentifier.getIdentifierCode() == identifierCode) {
                    return uriIdentifier;
                }
            }
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + ((uri == null) ? 0 : uri.hashCode());
        result = prime * result +
                 ((uriIdentifier == null) ? 0 : uriIdentifier.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof UriRecord)) {
            return false;
        }
        UriRecord other = (UriRecord) obj;
        if (uri == null) {
            return (other.uri == null);
        } else if (!uri.equals(other.uri)) {
            return false;
        } else {
            return (uriIdentifier == other.uriIdentifier);
        }
    }
}
