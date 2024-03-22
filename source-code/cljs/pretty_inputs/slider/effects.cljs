
(ns pretty-inputs.slider.effects
    (:require [pretty-inputs.slider.events :as slider.events]
              [re-frame.extra.api          :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-inputs.slider/slider-did-mount
   ; @ignore
   ;
   ; @param (keyword) slider-id
   ; @param (map) slider-props
   (fn [{:keys [db]} [_ slider-id slider-props]]
       {:db (r slider.events/slider-did-mount db slider-id slider-props)}))
