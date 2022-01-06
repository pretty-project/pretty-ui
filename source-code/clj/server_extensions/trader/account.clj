
(ns server-extensions.trader.account
    (:require [clj-http.client    :as client]
              [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.map     :as map]
              [mongo-db.api       :as mongo-db]
              [pathom.api         :as pathom]
              [prototypes.api     :as prototypes]
              [x.server-core.api  :as a]
              [x.server-db.api    :as db]
              [server-extensions.trader.engine       :as engine]
              [com.wsscode.pathom3.connect.operation :as pco :refer [defresolver defmutation]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn api-public-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [api-secret] :as api-details}]
  (if (some?  api-secret)
      (assoc  api-details :api-secret "************")
      (return api-details)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn query-api-key-info
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) options
  ;  {:api-key (string)
  ;   :api-secret (string)
  ;   :use-mainnet? (boolean)}
  ;
  ; @return (map)
  ;  {:api-key-info (map)
  ;   :timestamp (string)
  ;   :uri (string)}
  [options]
  (let [uri      (engine/api-key-info-uri options)
        response (client/get              uri)]
       (if (engine/response->invalid-api-details? response)
           (return {:error :invalid-api-details})
           (-> response (engine/get-response->body)
                        (map/rekey-item :result :api-key-info)
                        (assoc          :uri uri)))))

(defn query-wallet-balance
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) options
  ;  {:api-key (string)
  ;   :api-secret (string)
  ;   :use-mainnet? (boolean)}
  ;
  ; @return (map)
  ;  {:wallet-balance (map)
  ;   :timestamp (string)
  ;   :uri (string)}
  [options]
  (let [uri      (engine/wallet-balance-uri options)
        response (client/get                uri)]
       (if (engine/response->invalid-api-details? response)
           (return {:error :invalid-api-details})
           (-> response (engine/get-response->body)
                        (map/rekey-item :result :wallet-balance)
                        (assoc          :uri uri)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-api-details
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {:api-key (string)
  ;   :api-secret (string)
  ;   :use-mainnet? (boolean)}
  []
  (if-let [document (mongo-db/get-document-by-id "trader" "api-details")]
          (-> document (select-keys [:trader/api-key :trader/api-secret :trader/use-mainnet?])
                       (db/document->non-namespaced-document))))

(defn get-api-detail
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) detail-key
  ;
  ; @usage
  ;  (account/get-api-detail :use-mainnet?)
  ;
  ; @return (*)
  [detail-key]
  (if-let [document (mongo-db/get-document-by-id "trader" "api-details")]
          (get document (keyword/add-namespace :trader detail-key))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-account-data-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (map)
  ;  {:error (keyword)}
  [env response-props]
  (if-let [api-details (get-api-details)]
          (let [api-public-details (api-public-details   api-details)
                wallet-balance     (query-wallet-balance api-details)
                api-key-info       (query-api-key-info   api-details)]
               (merge api-public-details wallet-balance api-key-info))
          {:error :missing-api-details}))

(defresolver get-account-data
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (map)
             ;  {:trader/get-account-data (map)
             ;    {:api-key-info (map)
             ;     :error (keyword)
             ;     :timestamp (string)
             ;     :uri (string)
             ;     :wallet-balance (map)}}
             [env resolver-props]
             {:trader/get-account-data (get-account-data-f env resolver-props)})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- upload-api-details-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;
  ; @return (namespaced map)
  ;  {:trader/api-key (string)
  ;   :trader/api-secret (string)
  ;   :trader/use-mainnet? (boolean)}
  [{:keys [request] :as env} _]
  (let [document {:trader/api-key      (pathom/env->param env :api-key)
                  :trader/api-secret   (pathom/env->param env :api-secret)
                  :trader/use-mainnet? (pathom/env->param env :use-mainnet?)
                  :trader/id           "api-details"}]
       (dissoc (mongo-db/upsert-document! "trader" document
                                          {:prototype-f #(prototypes/updated-document-prototype request :trader %)})
               ; Removing api-secret from the response ...
               :api-secret)))

(defmutation upload-api-details!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) mutation-props
             ;
             ; @return (map)
             ;  {:trader/api-key (string)
             ;   :trader/use-mainnet? (boolean)
             ;   :trader/id (string)}}
             [env mutation-props]
             {::pco/op-name 'trader/upload-api-details!}
             (upload-api-details-f env mutation-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [get-account-data upload-api-details!])

(pathom/reg-handlers! ::handlers HANDLERS)
