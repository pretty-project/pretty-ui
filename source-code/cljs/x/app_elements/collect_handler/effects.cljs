

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.collect-handler.effects
    (:require [mid-fruits.vector                   :as vector]
              [x.app-core.api                      :as a :refer [r]]
              [x.app-db.api                        :as db]
              [x.app-elements.collect-handler.subs :as collect-handler.subs]
              [x.app-elements.engine.element       :as element]
              [x.app-elements.engine.input         :as input]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :elements/collect-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (*) option
  (fn [{:keys [db]} [_ input-id option]]
      (let [on-collect-event (r element/get-element-prop db input-id :on-collect)
            get-value-f      (r element/get-element-prop db input-id :get-value-f)
            value-path       (r element/get-element-prop db input-id :value-path)
            value            (get-value-f option)]
           {:db       (as-> db % (r db/apply-item!               % value-path vector/conj-item-once value)
                                 (r input/mark-input-as-visited! % input-id))
            :dispatch on-collect-event})))

(a/reg-event-fx
  :elements/uncollect-option!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (*) option
  (fn [{:keys [db]} [_ input-id option]]
      (let [on-uncollect-event (r element/get-element-prop db input-id :on-uncollect)
            value-path         (r element/get-element-prop db input-id :value-path)
            get-value-f        (r element/get-element-prop db input-id :get-value-f)
            value              (get-value-f option)]
           {:db       (r db/apply-item! db value-path vector/remove-item value)
            :dispatch on-uncollect-event})))

(a/reg-event-fx
  :elements/toggle-option-collection!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) input-id
  ; @param (*) option
  (fn [{:keys [db]} [_ input-id option]]
      (if (r collect-handler.subs/option-collected? db input-id option)
          [:elements/uncollect-option! input-id option]
          [:elements/collect-option!   input-id option])))
