
(ns pretty-inputs.checkbox.effects
    (:require [pretty-inputs.checkbox.events :as checkbox.events]
              [pretty-inputs.checkbox.subs :as checkbox.subs]
              [re-frame.api :as r :refer [r]]
              [pretty-build-kit.api]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-inputs.checkbox/checkbox-did-mount
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; {:initial-options (vector)(opt)
  ;  :initial-value (*)(opt)}
  (fn [{:keys [db]} [_ checkbox-id {:keys [initial-options initial-value] :as checkbox-props}]]
      (if (or initial-options initial-value)
          {:db (r checkbox.events/checkbox-did-mount db checkbox-id checkbox-props)})))

(r/reg-event-fx :pretty-inputs.checkbox/toggle-option!
  ; @ignore
  ;
  ; @param (keyword) checkbox-id
  ; @param (map) checkbox-props
  ; {}
  ; @param (*) option
  (fn [{:keys [db]} [_ checkbox-id {:keys [on-checked on-unchecked option-value-f] :as checkbox-props} option]]
      (println option)
      (let [option-value (option-value-f option)]
           {:db       (r checkbox.events/toggle-option! db checkbox-id checkbox-props option)
            :fx       [:pretty-inputs.input/mark-input-as-visited! checkbox-id]
            :dispatch (if (r checkbox.subs/option-checked? db checkbox-id checkbox-props option)
                          [:pretty-build-kit/dispatch-event-handler! on-unchecked option-value]
                          [:pretty-build-kit/dispatch-event-handler! on-checked   option-value])})))
