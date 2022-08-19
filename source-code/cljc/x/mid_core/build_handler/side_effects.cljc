
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-core.build-handler.side-effects
    (:require [x.mid-core.event-handler :as event-handler]))



;; ---------------------------------------------------------------------------
;; ---------------------------------------------------------------------------

(defn app-build
  ; @example
  ;  (a/app-build)
  ;  =>
  ;  "0.4.2"
  ;
  ; @return (string)
  []
 @(event-handler/subscribe [:core/get-app-build]))
