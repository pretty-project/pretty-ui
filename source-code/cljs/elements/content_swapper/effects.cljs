
(ns elements.content-swapper.effects
    (:require [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.content-swapper/go-fwd!
  ; @param (keyword) swapper-id
  ; @param (metamorphic-content)(opt) page
  ;
  ; @usage
  ; [:elements.content-swapper/go-fwd! :my-content-swapper [:div "My page"]]
  (fn [_ [_ swapper-id page]]
      {:fx [:elements.content-swapper/go-fwd! swapper-id page]}))

(r/reg-event-fx :elements.content-swapper/go-bwd!
  ; @param (keyword) swapper-id
  ; @param (metamorphic-content)(opt) page
  ;
  ; @usage
  ; [:elements.content-swapper/go-back! :my-content-swapper]
  (fn [_ [_ swapper-id page]]
      {:fx [:elements.content-swapper/go-bwd! swapper-id page]}))

(r/reg-event-fx :elements.content-swapper/go-home!
  ; @param (keyword) swapper-id
  ;
  ; @usage
  ; [:elements.content-swapper/go-home! :my-content-swapper]
  (fn [_ [_ swapper-id]]
      {:fx [:elements.content-swapper/go-home! swapper-id]}))
