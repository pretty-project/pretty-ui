
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.transfer.effects
    (:require [plugins.plugin-handler.transfer.effects]
              [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx
  :item-lister/reg-transfer-lister-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) lister-props
  (fn [_ [_ lister-id lister-props]]
      [:plugin-handler/reg-transfer-plugin-props! lister-id lister-props]))
