

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.items.effects
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-browser/item-clicked
  ; @param (keyword) browser-id
  ; @param (integer) item-dex
  ; @param (map) item-props
  ;  {:memory-mode? (boolean)(opt)
  ;   :on-click (metamorphic-event)
  ;   :on-select (metamorphic-event)(opt)}
  ;
  ; @usage
  ;  [:item-browser/item-clicked :my-browser 0 {...}]
  (fn [{:keys [db]} [_ browser-id item-dex item-props]]
      [:item-lister/item-clicked browser-id item-dex item-props]))
