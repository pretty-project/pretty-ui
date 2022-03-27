
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.core.helpers
    (:require [mid.plugins.item-lister.core.helpers :as core.helpers]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.plugins.item-lister.core.helpers
(def component-id       core.helpers/component-id)
(def default-items-path core.helpers/default-items-path)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn order-by-label-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (namespaced keyword) order-by
  ;
  ; @example
  ;  (core.helpers/order-by-label-f :name/ascending)
  ;  =>
  ;  :by-name-ascending
  ;
  ; @return (keyword)
  [order-by]
  (keyword (str "by-" (namespace order-by)
                "-"   (name      order-by))))
