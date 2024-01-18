
(ns pretty-inputs.switch.effects
    (:require ;[pretty-inputs.checkbox.events :as checkbox.events]
              [re-frame.api :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-inputs.switch/switch-did-mount
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; {:initial-options (vector)(opt)
  ;  :initial-value (*)(opt)}
  (fn [{:keys [db]} [_ switch-id {:keys [initial-options initial-value] :as switch-props}]]))
      ;(if (or initial-options initial-value))))
          ;{:db (r checkbox.events/checkbox-did-mount db switch-id switch-props)})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-inputs.switch/toggle-option!
  ; @ignore
  ;
  ; @param (keyword) switch-id
  ; @param (map) switch-props
  ; @param (*) option
  (fn [{:keys [db]} [_ switch-id switch-props option]]))
      ;{:db (r checkbox.events/toggle-option! db switch-id switch-props option)
      ; :fx [:pretty-inputs.input/mark-input-as-visited! switch-id]]))
