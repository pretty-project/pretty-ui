
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
              [pathom.config    :as config]))



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
  ;   :uri (string)}
  [{:keys [query] :as query-props}]
  (merge {:idle-timeout config/DEFAULT-IDLE-TIMEOUT
          :uri          config/DEFAULT-URI}
         (param query-props)
         (if query {:params {:query query}})
         {:method :post}))
