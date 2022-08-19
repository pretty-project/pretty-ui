
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.query-handler.prototypes
    (:require [mid-fruits.candy                :refer [param]]
              [x.app-sync.query-handler.config :as query-handler.config]))



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
  (merge {:uri query-handler.config/DEFAULT-URI}
         (param query-props)
         (if query {:params {:query query}})
         {:method :post}))
