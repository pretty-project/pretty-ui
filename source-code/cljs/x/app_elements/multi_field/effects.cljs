
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.multi-field.effects
    (:require [re-frame.api                       :as r :refer [r]]
              [x.app-elements.multi-field.events  :as multi-field.events]
              [x.app-elements.multi-field.helpers :as multi-field.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.multi-field/decrease-field-count!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  (fn [{:keys [db]} [_ group-id group-props field-dex]]
      {:db (r multi-field.events/decrease-field-count! db group-id group-props field-dex)}))

(r/reg-event-fx :elements.multi-field/increase-field-count!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  (fn [{:keys [db]} [_ group-id group-props field-dex]]
      (let [field-id (multi-field.helpers/field-dex->field-id group-id group-props (inc field-dex))]
           {:db (r multi-field.events/increase-field-count! db group-id group-props field-dex)
            :dispatch-later [{:ms 100 :fx [:elements.text-field/focus-field! field-id]}]})))
