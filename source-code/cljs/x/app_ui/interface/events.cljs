

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.interface.events)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-interface!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) interface
  ;
  ; @return (map)
  [db [_ interface]]
  (assoc-in db [:ui :interface/meta-items :interface] interface))
