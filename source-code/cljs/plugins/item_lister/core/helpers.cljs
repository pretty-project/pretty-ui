
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.core.helpers
    (:require [mid.plugins.item-lister.core.helpers :as core.helpers]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.plugins.item-lister.core.helpers
(def component-id core.helpers/component-id)



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

(defn default-items-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ;
  ; @example
  ;  (core.helpers/default-items-path :my-lister)
  ;  =>
  ;  [:plugins :item-lister/downloaded-items :my-lister]
  ;
  ; @return (vector)
  [lister-id]
  [:plugins :item-lister/downloaded-items lister-id])
