package com.oppo.transfer.sysf.wifi;

import android.net.wifi.ScanResult;
import android.os.Parcelable;
import android.os.Parcel;

/**
 * Describes information about a detected access point. In addition
 * to the attributes described here, the supplicant keeps track of
 * {@code quality}, {@code noise}, and {@code maxbitrate} attributes,
 * but does not currently report them to external clients.
 */
public class ScanResult_bak implements Parcelable {
    /** The network name. */
    public String SSID;
    /** The address of the access point. */
    public String BSSID;
    /**
     * Describes the authentication, key management, and encryption schemes
     * supported by the access point.
     */
    public String capabilities;
    /**
     * The detected signal level in dBm. At least those are the units used by
     * the TI driver.
     */
    public int level;
    /**
     * The frequency in MHz of the channel over which the client is communicating
     * with the access point.
     */
    public int frequency;

    public ScanResult_bak(String SSID, String BSSID, String caps, int level, int frequency) {
        this.SSID = SSID;
        this.BSSID = BSSID;
        this.capabilities = caps;
        this.level = level;
        this.frequency = frequency;
        //networkConfig = null;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        String none = "<none>";

        sb.append("SSID: ").
            append(SSID == null ? none : SSID).
            append(", BSSID: ").
            append(BSSID == null ? none : BSSID).
            append(", capabilities: ").
            append(capabilities == null ? none : capabilities).
            append(", level: ").
            append(level).
            append(", frequency: ").
            append(frequency);

        return sb.toString();
    }

    /** Implement the Parcelable interface {@hide} */
    public int describeContents() {
        return 0;
    }

    /** Implement the Parcelable interface {@hide} */
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(SSID);
        dest.writeString(BSSID);
        dest.writeString(capabilities);
        dest.writeInt(level);
        dest.writeInt(frequency);
    }

    /** Implement the Parcelable interface {@hide} */
    public static final Creator<ScanResult_bak> CREATOR =
        new Creator<ScanResult_bak>() {
            public ScanResult_bak createFromParcel(Parcel in) {
                return new ScanResult_bak(
                    in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readInt(),
                    in.readInt()
                );
            }

            public ScanResult_bak[] newArray(int size) {
                return new ScanResult_bak[size];
            }
        };

}
