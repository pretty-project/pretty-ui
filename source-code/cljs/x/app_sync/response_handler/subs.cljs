
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.response-handler.subs
    (:require [mid-fruits.mixed  :as mixed]
              [mid-fruits.reader :as reader]
              [x.app-core.api    :as a :refer [r]]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-request-response
  ; @param (keyword) request-id
  ;
  ; @return (*)
  [db [_ request-id]]
  (get-in db [:sync :response-handler/data-items request-id]))

(defn store-request-response?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (let [response-action (get-in db [:sync :request-handler/data-items request-id :response-action])]
       (= response-action :store)))

(defn save-request-response?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (boolean)
  [db [_ request-id]]
  (let [response-action (get-in db [:sync :request-handler/data-items request-id :response-action])]
       (= response-action :save)))

(defn request-response-invalid?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (string) server-response-body
  ;  "{...}"
  ;
  ; @return (boolean)
  [db [_ request-id server-response-body]]
  ; A sikeres HTTP státusz-kódtól függetlenül ha a szerver válasza a validator-f függvény szerint
  ; nem megfelelő, akkor az on-success esemény helyett az on-failure esemény fog megtörténni ...
  (boolean (if-let [validator-f (get-in db [:sync :request-handler/data-items request-id :validator-f])]
                   (-> server-response-body reader/string->mixed validator-f not))))

(defn get-invalid-server-response
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (string) server-response-body
  ;  "{...}"
  ;
  ; @return (map)
  ;  {:failure (keyword)
  ;   :response (string)
  ;   :validator-f (function)}
  [db [_ request-id server-response-body]]
  (let [validator-f (get-in db [:sync :request-handler/data-items request-id :validator-f])]
       {:failure     :invalid
        :response    server-response-body
        :validator-f validator-f}))
