
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.engine.helpers
    (:require [mid.plugins.item-lister.engine.helpers :as engine.helpers]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.plugins.item-lister.engine.helpers
(def collection-name engine.helpers/collection-name)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn order-by-label-f
  ; @param (namespaced keyword) order-by
  ;
  ; @example
  ;  (order-by-label-f :name/ascending)
  ;  =>
  ;  :by-name-ascending
  ;
  ; @return (keyword)
  [order-by]
  (keyword (str "by-" (namespace order-by)
                "-"   (name      order-by))))
