
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.engine
    (:require [mid-plugins.item-lister.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-lister.engine
(def collection-name engine/collection-name)
(def transfer-id     engine/transfer-id)
(def route-id        engine/route-id)
(def route-template  engine/route-template)
(def component-id    engine/component-id)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn order-by-label-f
  ; @param (namespaced keyword) order-by
  ;
  ; @example
  ;  (engine/order-by-label-f :name/ascending)
  ;  =>
  ;  :by-name-ascending
  ;
  ; @return (keyword)
  [order-by]
  (keyword (str "by-" (namespace order-by)
                "-"   (name      order-by))))
