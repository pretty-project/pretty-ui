
(ns pretty-elements.element.effects
    (:require [re-frame.extra.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx
  :pretty-elements.element/dispatch-event-handler!
  ; @ignore
  ;
  ; @description
  ; ...
  ;
  ; @param (function or Re-Frame metamorphic-event) event-handler
  ; @param (list of *) params
  (fn [_ [_ event-handler & params]]
      ; @note (tutorials#event-functions)
      ; @note (tutorials#re-frame-event-handlers)
      (cond (fn?     event-handler) {:dispatch-f (fn [] (apply event-handler params))}
            (map?    event-handler) {:dispatch   (apply r/metamorphic-event<-params event-handler params)}
            (vector? event-handler) {:dispatch   (apply r/metamorphic-event<-params event-handler params)})))
