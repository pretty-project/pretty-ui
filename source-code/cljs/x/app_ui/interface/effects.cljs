
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.interface.effects
    (:require [re-frame.api              :as r :refer [r]]
              [x.app-ui.interface.events :as interface.events]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :ui/set-interface!
  ; @param (keyword) interface
  ;  :application-ui, :website-ui
  ;
  ; @usage
  ;  [:ui/set-interface! :application-ui]
  (fn [{:keys [db]} [_ interface]]
      {:db (r interface.events/set-interface! db interface)
       :fx [:environment/set-element-attribute! "x-body-container" "data-interface" interface]}))
