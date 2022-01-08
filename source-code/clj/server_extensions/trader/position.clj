
(ns server-extensions.trader.position
    (:require [clj-http.client   :as client]
              [mid-fruits.candy  :refer [param return]]
              [pathom.api        :as pathom]
              [x.server-core.api :as a]
              [server-extensions.trader.engine :as engine]
              [com.wsscode.pathom3.connect.operation :as pco :refer [defresolver defmutation]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- download-position-data-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) resolver-props
  ;
  ; @return (namespaced map)
  [{:keys [request]} resolver-props])

(defmutation download-position-data
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (map)
             ; {:trader/download-position-data (map)}
             [env resolver-props]
             {:trader/download-position-data (download-position-data-f env resolver-props)})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- upload-position-data-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) env
  ; @param (map) mutation-props
  ;
  ; @return (namespaced map)
  [{:keys [request]} mutation-props])

(defmutation upload-position-data!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) mutation-props
             ;
             ; @return (namespaced map)
             [env mutation-props]
             {::pco/op-name 'trader/upload-position-data!}
             (upload-position-data-f env mutation-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [download-position-data upload-position-data!])

(pathom/reg-handlers! ::handlers HANDLERS)
