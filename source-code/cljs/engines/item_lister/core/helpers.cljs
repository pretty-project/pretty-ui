
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-lister.core.helpers
    (:require [iso.engines.item-lister.core.helpers :as core.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; iso.engines.item-lister.core.helpers
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
  ;  (order-by-label-f :name/ascending)
  ;  =>
  ;  :by-name-ascending
  ;
  ; @return (keyword)
  [order-by]
  ; Az order-by-label-f függvény az {:order-by ...} tulajdonság értékéből elkészíti,
  ; a hozzá tartozó címke szövegét.
  ; Pl. :name/ascending => :by-name-ascending => "Név szerint (növekvő)"
  (keyword (str "by-" (namespace order-by)
                "-"   (name      order-by))))
