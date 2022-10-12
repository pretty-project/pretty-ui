
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.slider.effects
    (:require [re-frame.api                 :as r :refer [r]]
              [x.app-elements.slider.events :as slider.events]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.slider/slider-did-mount
   ; WARNING! NON-PUBLIC! DO NOT USE!
   ;
   ; @param (keyword) slider-id
   ; @param (map) slider-props
   (fn [{:keys [db]} [_ slider-id slider-props]]
       {:db (r slider.events/slider-did-mount db slider-id slider-props)}))
