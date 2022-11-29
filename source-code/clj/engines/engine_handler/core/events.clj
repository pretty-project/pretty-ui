
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.engine-handler.core.events)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-engine-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (map) engine-props
  ;
  ; @return (map)
  [db [_ engine-id engine-props]]
  (assoc-in db [:engines :engine-handler/engine-props engine-id] engine-props))
