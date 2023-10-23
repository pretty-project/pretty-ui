
(ns elements.content-swapper.effects
    (:require [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :elements.content-swapper/go-fwd!
  ; @param (keyword) swapper-id
  ; @param (metamorphic-content) page
  ; @param (map)(opt) options
  ; {:rerender-same? (boolean)(opt)
  ;   Default: false}
  ;
  ; @usage
  ; [:elements.content-swapper/go-fwd! :my-content-swapper [:div "My page"]]
  (fn [_ [_ swapper-id page options]]
      {:fx [:elements.content-swapper/go-fwd! swapper-id page options]}))

(r/reg-event-fx :elements.content-swapper/go-bwd!
  ; @param (keyword) swapper-id
  ; @param (metamorphic-content) page
  ; @param (map)(opt) options
  ; {:rerender-same? (boolean)(opt)
  ;   Default: false}
  ;
  ; @usage
  ; [:elements.content-swapper/go-bwd! :my-content-swapper [:div "My page"]]
  (fn [_ [_ swapper-id page options]]
      {:fx [:elements.content-swapper/go-bwd! swapper-id page options]}))
