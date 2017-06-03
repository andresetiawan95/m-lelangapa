package com.lelangapa.app.interfaces;

/**
 * Created by andre on 08/02/17.
 */

public interface AuctioneerResponseReceiver {
    void responseCancelReceived(boolean status);
    void responseWinnerChosenReceived(boolean status, String idBid);
    void responseDaftarTawaranReceived();
    void responseBlockListReceived();
}
