
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns pathom.prototypes
    (:require [mid-fruits.candy :refer [param]]
              [pathom.config    :as config]
              [pathom.events    :as events]
              [x.app-core.api   :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn query-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) query-props
  ;
  ; @return (map)
  ;  {:method (keyword)
  ;   :params (map)
  ;   :query (vector)
  ;   :response-f (function)
  ;   :uri (string)}
  [db [_ {:keys [query] :as query-props}]]
  (merge {:idle-timeout config/DEFAULT-IDLE-TIMEOUT
          :uri          config/DEFAULT-URI}
         (param query-props)
         (if query {:params {:query query}})
         {:method     :post
          :response-f #'events/target-query-answers!}))
