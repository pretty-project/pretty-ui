
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.header.effects
    (:require [plugins.item-editor.header.events :as header.events]
              [reagent.api                       :as reagent]
              [x.app-core.api                    :as a :refer [r]]))



;; -- Header lifecycles effects -----------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/header-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) header-props
  (fn [{:keys [db]} [_ editor-id header-props]]
      {:db (r header.events/header-did-mount db editor-id header-props)}))

(a/reg-event-fx
  :item-editor/header-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id]]
      {:db (r header.events/header-will-unmount db editor-id)}))

(a/reg-event-fx
  :item-editor/header-did-update
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (?) %
  (fn [{:keys [db]} [_ editor-id %]]
      (let [[_ header-props] (reagent/arguments %)]
           {:db (r header.events/header-did-update db editor-id header-props)})))
