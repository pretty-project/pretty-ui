
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.color-selector.effects
    (:require [re-frame.api                             :as r :refer [r]]
              [x.app-elements.color-selector.prototypes :as color-selector.prototypes]
              [x.app-elements.color-selector.views      :as color-selector.views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.color-selector/render-options!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  (fn [{:keys [db]} [_ selector-id selector-props]]
      [:ui/render-popup! :elements.color-selector/options
                         {:content [color-selector.views/color-selector-options selector-id selector-props]}]))

(r/reg-event-fx :elements.color-selector/render-selector!
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;  {:options (strings in vector)(opt)
  ;   :options-label (metamorphic-content)(opt)
  ;   :options-path (vector)(opt)
  ;   :value-path (vector)(opt)}
  ;
  ; @usage
  ;  [:elements.color-selector/render-selector! {...}]
  [r/event-vector<-id]
  (fn [{:keys [db]} [_ selector-id selector-props]]
      (let [selector-props (r color-selector.prototypes/selector-props-prototype db selector-id selector-props)]
           [:elements.color-selector/render-options! selector-id selector-props])))
