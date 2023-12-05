
(ns pretty-elements.checkbox.effects
    (:require [pretty-elements.checkbox.events :as checkbox.events]
              [re-frame.api                    :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-elements.checkbox/checkbox-did-mount
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; {:initial-options (vector)(opt)
  ;  :initial-value (*)(opt)}
  (fn [{:keys [db]} [_ checkbox-id {:keys [initial-options initial-value] :as checkbox-props}]]
      (if (or initial-options initial-value)
          {:db (r checkbox.events/checkbox-did-mount db checkbox-id checkbox-props)})))

(r/reg-event-fx :pretty-elements.checkbox/toggle-option!
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; @param (*) option
  (fn [{:keys [db]} [_ checkbox-id checkbox-props option]]
      {:db (r checkbox.events/toggle-option! db checkbox-id checkbox-props option)
       :fx [:pretty-elements.input/mark-input-as-visited! checkbox-id]}))
