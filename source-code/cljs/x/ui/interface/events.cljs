
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.interface.events)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-interface!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) interface
  ;
  ; @return (map)
  [db [_ interface]]
  (assoc-in db [:x.ui :interface/meta-items :interface] interface))
