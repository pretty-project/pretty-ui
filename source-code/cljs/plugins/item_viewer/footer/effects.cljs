

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.footer.effects
    (:require [plugins.item-viewer.footer.events :as footer.events]
              [reagent.api                       :as reagent]
              [x.app-core.api                    :as a :refer [r]]))



;; -- Footer lifecycles effects -----------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-viewer/footer-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (map) footer-props
  (fn [{:keys [db]} [_ viewer-id footer-props]]
      {:db (r footer.events/footer-did-mount db viewer-id footer-props)}))

(a/reg-event-fx
  :item-viewer/footer-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  (fn [{:keys [db]} [_ viewer-id]]
      {:db (r footer.events/footer-will-unmount db viewer-id)}))

(a/reg-event-fx
  :item-viewer/footer-did-update
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) viewer-id
  ; @param (?) %
  (fn [{:keys [db]} [_ viewer-id %]]
      (let [[_ footer-props] (reagent/arguments %)]
           {:db (r footer.events/footer-did-update db viewer-id footer-props)})))
