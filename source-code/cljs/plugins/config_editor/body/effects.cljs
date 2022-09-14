
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.config-editor.body.effects
    (:require [plugins.config-editor.body.events :as body.events]
              [reagent.api                       :as reagent]
              [x.app-core.api                    :as a :refer [r]]))



;; -- Body lifecycles effects -------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :config-editor/body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (map) body-props
  (fn [{:keys [db]} [_ editor-id body-props]]
      {:db       (r body.events/body-did-mount db editor-id body-props)
       :dispatch [:config-editor/load-editor! editor-id]}))

(a/reg-event-fx
  :config-editor/body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  (fn [{:keys [db]} [_ editor-id]]
      {:db (r body.events/body-will-unmount db editor-id)}))

(a/reg-event-fx
  :config-editor/body-did-update
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) editor-id
  ; @param (?) %
  (fn [{:keys [db]} [_ editor-id %]]
      (let [[_ body-props] (reagent/arguments %)]
           {:db (r body.events/body-did-update db editor-id body-props)})))
