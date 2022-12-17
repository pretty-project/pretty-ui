
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-browser.body.effects
    (:require [engines.item-browser.body.events :as body.events]
              [engines.item-browser.core.subs   :as core.subs]
              [re-frame.api                     :as r :refer [r]]
              [reagent.api                      :as reagent]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-browser/body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) body-props
  (fn [{:keys [db]} [_ browser-id body-props]]
      {:db       (r body.events/body-did-mount db browser-id body-props)
       :dispatch [:item-browser/load-browser! browser-id]}))

(r/reg-event-fx :item-browser/body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  (fn [{:keys [db]} [_ browser-id]]
      {:db (r body.events/body-will-unmount db browser-id)}))

(r/reg-event-fx :item-browser/body-did-update
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (?) %
  (fn [{:keys [db]} [_ browser-id %]]
      ; XXX#1249 (source-code/cljs/engines/item_lister/body/effects.cljs)
      (let [[_ body-props] (reagent/arguments %)]
           ; According to the XXX#1249 the item-browser has to reload when
           ; the body component get updated!
           ; (if (r core.subs/reload-item? db browser-id body-props) ...)
           {:dispatch [:item-browser/reload-browser! browser-id]
            :db       (r body.events/body-did-update db browser-id body-props)})))
