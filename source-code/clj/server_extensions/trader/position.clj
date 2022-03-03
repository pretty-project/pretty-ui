
(ns server-extensions.trader.position
    (:require [com.wsscode.pathom3.connect.operation :as pathom.co :refer [defresolver defmutation]]
              [clj-http.client                       :as client]
              [mid-fruits.candy                      :refer [param return]]
              [pathom.api                            :as pathom]
              [server-extensions.trader.engine       :as engine]
              [x.server-core.api                     :as a]))



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

(defresolver download-position-data
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) resolver-props
             ;
             ; @return (namespaced map)
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
  ; @return (?)
  [{:keys [request]} mutation-props])

(defmutation upload-position-data!
             ; WARNING! NON-PUBLIC! DO NOT USE!
             ;
             ; @param (map) env
             ; @param (map) mutation-props
             ;
             ; @return (?)
             [env mutation-props]
             {::pathom.co/op-name 'trader/upload-position-data!}
             (upload-position-data-f env mutation-props))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [download-position-data upload-position-data!])

(pathom/reg-handlers! ::handlers HANDLERS)
