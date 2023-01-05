
(ns elements.slider.effects
    (:require [elements.slider.events :as slider.events]
              [re-frame.api           :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.slider/slider-did-mount
   ; WARNING! NON-PUBLIC! DO NOT USE!
   ;
   ; @param (keyword) slider-id
   ; @param (map) slider-props
   (fn [{:keys [db]} [_ slider-id slider-props]]
       {:db (r slider.events/slider-did-mount db slider-id slider-props)}))
