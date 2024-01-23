
(ns pretty-elements.content-swapper.effects
    (:require [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :pretty-elements.content-swapper/go-fwd!
  ; @param (keyword) swapper-id
  ; @param (metamorphic-content) content
  ; @param (map)(opt) options
  ; {:rerender-same? (boolean)(opt)
  ;   Default: false}
  ;
  ; @usage
  ; [:pretty-elements.content-swapper/go-fwd! :my-content-swapper [:div "My content"]]
  (fn [_ [_ swapper-id content options]]
      {:fx [:pretty-elements.content-swapper/go-fwd! swapper-id content options]}))

(r/reg-event-fx :pretty-elements.content-swapper/go-bwd!
  ; @param (keyword) swapper-id
  ; @param (metamorphic-content) content
  ; @param (map)(opt) options
  ; {:rerender-same? (boolean)(opt)
  ;   Default: false}
  ;
  ; @usage
  ; [:pretty-elements.content-swapper/go-bwd! :my-content-swapper [:div "My content"]]
  (fn [_ [_ swapper-id content options]]
      {:fx [:pretty-elements.content-swapper/go-bwd! swapper-id content options]}))
