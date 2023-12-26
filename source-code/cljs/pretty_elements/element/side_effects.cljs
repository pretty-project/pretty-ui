
(ns pretty-elements.element.side-effects
    (:require [re-frame.extra.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn dispatch-event-handler!
  ; @ignore
  ;
  ; @description
  ; ...
  ;
  ; @param (function or Re-Frame metamorphic-event) event-handler
  ; @param (list of *) params
  [event-handler & params]
  ; @note (tutorials#event-functions)
  ; @note (tutorials#re-frame-event-handlers)
  (cond (fn?     event-handler) (-> (apply                             event-handler params))
        (map?    event-handler) (-> (apply r/metamorphic-event<-params event-handler params) r/dispatch)
        (vector? event-handler) (-> (apply r/metamorphic-event<-params event-handler params) r/dispatch)))

(defn dispatch-sync-event-handler!
  ; @ignore
  ;
  ; @description
  ; ...
  ;
  ; @param (function or Re-Frame metamorphic-event) event-handler
  ; @param (list of *) params
  [event-handler & params]
  ; @note (tutorials#event-functions)
  ; @note (tutorials#re-frame-event-handlers)
  (cond (fn?     event-handler) (-> (apply                             event-handler params))
        (map?    event-handler) (-> (apply r/metamorphic-event<-params event-handler params) r/dispatch-sync)
        (vector? event-handler) (-> (apply r/metamorphic-event<-params event-handler params) r/dispatch-sync)))
