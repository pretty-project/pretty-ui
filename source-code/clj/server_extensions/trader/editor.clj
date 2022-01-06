
(ns server-extensions.trader.editor
    (:require [mid-fruits.candy  :refer [param return]]
              [mongo-db.api      :as mongo-db]
              [pathom.api        :as pathom]
              [prototypes.api    :as prototypes]
              [x.server-core.api :as a]
              [com.wsscode.pathom3.connect.operation :as pco :refer [defresolver defmutation]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- upload-source-code-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;
  ; @return (namespaced map)
  ;  {:trader/source-code (string)
  ;   :trader/id (string)}
  [{:keys [request]} mutation-props]
  (let [source-code (get mutation-props :source-code)
        document    {:trader/source-code (param source-code)
                     :trader/id          "listener-details"}]
       (mongo-db/upsert-document! "trader" document
                                  {:prototype-f #(prototypes/updated-document-prototype request :trader %)})))

(defmutation upload-source-code!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) mutation-props
             ;
             ; @return (map)
             ;  {:trader/source-code (string)
             ;   :trader/id (string)}}
             [env mutation-props]
             {::pco/op-name 'trader/upload-source-code!}
             (upload-source-code-f env mutation-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- download-editor-data-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (map)
  ;  {:source-code (string)}
  [env _]
  (let [listener-details (mongo-db/get-document-by-id "trader" "listener-details")]
       {:source-code (get listener-details :trader/source-code)}))

(defresolver download-editor-data
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (map)
             ;  {:trader/download-editor-data (map)
             ;    {:source-code (string)}}
             [env resolver-props]
             {:trader/download-editor-data (download-editor-data-f env resolver-props)})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [download-editor-data upload-source-code!])

(pathom/reg-handlers! ::handlers HANDLERS)
