
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
  [{:keys [] :as api-details}]
  (dissoc api-details :api-secret))



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

(defn get-api-details-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) detail-key
  ;
  ; @usage
  ;  (account/read-api-details-item :use-mainnet?)
  ;
  ; @return (*)
  [detail-key]
  (if-let [document (mongo-db/get-document-by-id "trader" "api-details")]
          (get document (keyword/add-namespace :trader detail-key))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-api-key-info!
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
           (-> response (engine/GET-response->body)
                        (map/rekey-item :result :api-key-info)
                        (assoc          :api-key (-> :api-key get-api-details-item))
                        (assoc          :uri uri)))))

(defn request-wallet-balance!
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
           (-> response (engine/GET-response->body)
                        (map/rekey-item :result :wallet-balance)
                        (assoc          :api-key (-> :api-key get-api-details-item))
                        (assoc          :uri uri)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-account-data-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (map)
  ;  {:error (keyword)}
  [env response-props]
  (if-let [api-details (get-api-details)]
          (let [wallet-balance (request-wallet-balance! api-details)
                api-key-info   (request-api-key-info!   api-details)]
               (merge wallet-balance api-key-info))
          {:error :missing-api-details}))

(defresolver download-account-data
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (map)
             ;  {:trader/download-account-data (map)
             ;    {:api-key-info (map)
             ;     :error (keyword)
             ;     :timestamp (string)
             ;     :uri (string)
             ;     :wallet-balance (map)}}
             [env resolver-props]
             {:trader/download-account-data (download-account-data-f env resolver-props)})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-api-details-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (map)
  ;  {:error (keyword)}
  [env response-props]
  (if-let [api-details (get-api-details)]
          (api-public-details api-details)
          {:error :missing-api-details}))

(defresolver download-api-details
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (map)
             ;  {:trader/download-api-details (map)
             [env resolver-props]
             {:trader/download-api-details (download-api-details-f env resolver-props)})



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
  [{:keys [request]} mutation-props]
  (let [document {:trader/api-key      (get mutation-props :api-key)
                  :trader/api-secret   (get mutation-props :api-secret)
                  :trader/use-mainnet? (get mutation-props :use-mainnet?)
                  :trader/id           "api-details"}]
       (dissoc (mongo-db/upsert-document! "trader" document
                                          {:prototype-f #(prototypes/updated-document-prototype request :trader %)})
               ; Removing api-secret from the response ...
               :trader/api-secret)))

(defmutation upload-api-details!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) mutation-props
             ;
             ; @return (namespaced map)
             ;  {:trader/api-key (string)
             ;   :trader/use-mainnet? (boolean)
             ;   :trader/id (string)}}
             [env mutation-props]
             {::pco/op-name 'trader/upload-api-details!}
             (upload-api-details-f env mutation-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [download-account-data download-api-details upload-api-details!])

(pathom/reg-handlers! ::handlers HANDLERS)
