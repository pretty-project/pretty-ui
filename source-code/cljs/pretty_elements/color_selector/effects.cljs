
(ns pretty-elements.color-selector.effects
    (:require [pretty-elements.color-selector.events :as color-selector.events]
              [re-frame.api                          :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-elements.color-selector/options-did-mount
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  (fn [{:keys [db]} [_ selector-id selector-props]]
      {:db (r color-selector.events/options-did-mount db selector-id selector-props)}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-elements.color-selector/toggle-option!
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; {:on-select (Re-Frame metamorphic-event)(opt)}
  ; @param (string) option
  (fn [{:keys [db]} [_ selector-id {:keys [on-select] :as selector-props} option]]
      {:db (r color-selector.events/toggle-color-selector-option! db selector-id selector-props option)
       :dispatch on-select}))
