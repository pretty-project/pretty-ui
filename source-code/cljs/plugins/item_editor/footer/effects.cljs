
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-editor.footer.effects
    (:require [plugins.item-editor.footer.events :as footer.events]
              [reagent.api                       :as reagent]
              [x.app-core.api                    :as a :refer [r]]))



;; -- Footer lifecycles effects -----------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-editor/footer-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) footer-props
  (fn [{:keys [db]} [_ editor-id footer-props]]
      {:db (r footer.events/footer-did-mount db editor-id footer-props)}))

(a/reg-event-fx
  :item-editor/footer-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id]]
      {:db (r footer.events/footer-will-unmount db editor-id)}))

(a/reg-event-fx
  :item-editor/footer-did-update
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (?) %
  (fn [{:keys [db]} [_ editor-id %]]
      (let [[_ footer-props] (reagent/arguments %)]
           {:db (r footer.events/footer-did-update db editor-id footer-props)})))
