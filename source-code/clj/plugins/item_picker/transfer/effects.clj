
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-picker.transfer.effects
    (:require [plugins.plugin-handler.transfer.effects]
              [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx
  :item-picker/reg-transfer-picker-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  (fn [_ [_ picker-id picker-props]]
      [:plugin-handler/reg-transfer-plugin-props! picker-id picker-props]))
