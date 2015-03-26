// IStockQuoteService.aidl
package com.stockquote.aidl;

import com.stockquote.aidl.StockQuoteRequest;
import com.stockquote.aidl.IStockQuoteServiceResponseListener;

oneway interface IStockQuoteService {
    void retrieveStockQuote(in StockQuoteRequest request, in IStockQuoteServiceResponseListener listener);
}
