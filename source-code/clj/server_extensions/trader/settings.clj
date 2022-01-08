
(ns server-extensions.trader.settings
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.keyword :as keyword]
              [mongo-db.api       :as mongo-db]
              [pathom.api         :as pathom]
              [prototypes.api     :as prototypes]
              [x.server-core.api  :as a]
              [x.server-db.api    :as db]
              [server-extensions.trader.engine       :as engine]
              [com.wsscode.pathom3.connect.operation :as pco :refer [defresolver defmutation]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def DEFAULT-SYMBOL {:label "ETH / USD" :value "ETHUSD"})

; @constant (map)
(def DEFAULT-SETTINGS {:symbol DEFAULT-SYMBOL})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-settings
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {:symbol (map)
  ;   {:label (string)
  ;    :value (string)}}
  []
  (if-let [document (mongo-db/get-document-by-id "trader" "settings")]
          (-> document (select-keys [:trader/symbol])
                       (db/document->non-namespaced-document))
          (return DEFAULT-SETTINGS)))

(defn get-settings-item
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) item-key
  [item-key]
  (if-let [document (mongo-db/get-document-by-id "trader" "settings")]
          (get document (keyword/add-namespace :trader item-key))
          (get DEFAULT-SETTINGS item-key)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn download-settings-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (map)
  [_ _]
  (get-settings))

(defresolver download-settings
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (map)
             ;  {:trader/download-settings (map)
             ;    {:symbol (map)}
             [env resolver-props]
             {:trader/download-settings (download-settings-f env resolver-props)})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- upload-settings-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;
  ; @return (namespaced map)
  ;  {:trader/symbol (map)}
  [{:keys [request]} mutation-props]
  (let [document {:trader/symbol (get mutation-props :symbol)
                  :trader/id     "settings"}]
       (mongo-db/upsert-document! "trader" document
                                  {:prototype-f #(prototypes/updated-document-prototype request :trader %)})))

(defmutation upload-settings!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) mutation-props
             ;
             ; @return (map)
             ;  {:trader/symbol (map)}
             [env mutation-props]
             {::pco/op-name 'trader/upload-settings!}
             (upload-settings-f env mutation-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [download-settings upload-settings!])

(pathom/reg-handlers! ::handlers HANDLERS)