package com.lelangapa.android.interfaces;

/**
 * Created by andre on 08/02/17.
 */

public interface AuctioneerResponseReceiver {
    void responseCancelReceived(boolean status);
    void responseWinnerChosenReceived(boolean status, String idBid);
}
