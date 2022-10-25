
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.engine-handler.core.subs)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-engine-prop
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ; @param (keyword) item-key
  ;
  ; @return (map)
  [db [_ engine-id item-key]]
  (get-in db [:engines :engine-handler/engine-props engine-id item-key]))
