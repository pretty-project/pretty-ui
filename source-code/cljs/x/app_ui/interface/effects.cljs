

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.interface.effects
    (:require [x.app-core.api            :as a :refer [r]]
              [x.app-ui.interface.events :as interface.events]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :ui/set-interface!
  ; @param (keyword) interface
  ;  :application-ui, :website-ui
  ;
  ; @usage
  ;  [:ui/set-interface! :application-ui]
  (fn [{:keys [db]} [_ interface]]
      {:db (r interface.events/set-interface! db interface)
       :fx [:environment/set-element-attribute! "x-body-container" "data-interface" (name interface)]}))
