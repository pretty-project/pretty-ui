
(ns server-extensions.trader.order
    (:require [clj-http.client   :as client]
              [mid-fruits.candy  :refer [param return]]
              [x.server-core.api :as a]
              [server-extensions.trader.engine       :as engine]
              [com.wsscode.pathom3.connect.operation :refer [defresolver]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name Market order
;  Azonnali vétel / eladás az elérhető legjobb piaci áron.
;
; @name Limit order
;  Vétel / eladás a meghatározott vagy annál kedvezőbb áron.
;
; @name Stop order
;  Ha a piaci ár eléri a meghatározott mértéket, akkor átvált market order típusra
;  és vesz / elad az elérhető legjobb piaci áron.
;
; @name Order quantity
;  This parameter indicates the quantity of perpetual contracts you want to buy or sell.
;  All USD markets have a contract size of 1 USD.
;
; @name Good till cancel
;  A megbízás visszavonásig, vagy az ügylet teljesüléséig aktív.
;
; @name Immediate or cancel
;  A megbízás azonnal nem teljesíthető része visszavonásra kerül.
;
; @name Fill or kill
;  A megbízás vagy teljesül egy meghatározott áron azonnal, vagy visszavonásra kerül.
;
; @name Post only
;  Post only order will not be executed immediately in the market.
;  It will exist as a maker order on the order book, but never match with orders
;  that are already on the book.
;
; @name Taker fee / maker fee
;  When you place an order that gets partially matched immediately,
;  you pay a taker fee for that portion.
;  The remainder of the order is placed on the order book and, when matched,
;  is considered a maker order.
;  You pay a maker fee for this remaining portion of the total order.
;
; @name Unrealized P&L
;  The unrealized P&L is a reflection of what profit or loss could be realized if
;  the position were closed at that time.
;  The P&L does not become realized until the position is closed.
;
; @name Leverage
;  Pl.: 10x leverage: 1 saját + 9 kölcsön
;
; @name Cross / isolated
;  Cross:    elérhetővé teszi az account teljes egyenlegét az egyes megbízások számára (NE HASZNÁLD!)
;  Isolated: ...
;
; @name Positions
;  "Sell" - short position
;  "Buy"  - long position
;
; @name Short position
;  A befektető kölcsönvesz a brókertől egy adott részvényt, majd azt eladja a piacon.
;  Később a befektető visszavásárolja a részvényt, és visszaadja a brókernek.
;  Ha a részvény ára esett, a befektető nyereségre tesz szert.
;
; @name Long position
;  A befektetők akkor vesznek fel long pozíciót a részvénytőzsdén, amikor részvényeket vásárolnak,
;  és tartják a részvényeket abban a reményben, hogy az áruk emelkedni fog.
;
; @name Buy
;  - Veszel ETH-t, mert azt hiszed, hogy magasabb lesz később az ára
;  - Mikor lenn van, akkor BUY
;
; @name Sell
;  - Eladsz ETH-t, mert azt hiszed, hogy alacsonyabb lesz később az ára és később visszaveszed
;  - Mikor fenn van, akkor SELL



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn create-order!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (map)
  [env _]
  (let [;api-key      (pathom/env->param env :api-key)
        ;api-secret   (pathom/env->param env :api-secret)
        ;use-mainnet? (pathom/env->param env :use-mainnet?)
        uri          (engine/create-order-uri) ;{:use-mainnet? use-mainnet?})
        form-params (engine/post-form-params {:api-key  ;  api-key
                                              :api-secret ; api-secret
                                              :order-type    "Limit"
                                              :price         "3792"
                                              :side          "Sell"
                                              :symbol        "ETHUSD"
                                              :time-in-force "GoodTillCancel"
                                              :qty           10000})
        response     (client/post uri {:form-params form-params
                                       :as :x-www-form-urlencoded})]

       (println "trader/order: post data to" uri)
       (println (str (get response :body)))
       {:body (get response :body)}))
;      (-> response (engine/post-response->body response)
;                   (assoc :uri uri))
