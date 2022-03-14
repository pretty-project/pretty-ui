
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns ajax.helpers
    (:require [mid-fruits.math :as math]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn progress-event->request-progress
  ; @param (object) progress-event
  ;
  ; @return (integer)
  [progress-event]
  (let [loaded (.-loaded progress-event)
        total  (.-total  progress-event)]
       (math/percent total loaded)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->local-request?
  ; @param (map) request
  ;  {:uri (string)}
  ;
  ; @return (boolean)
  [{:keys [uri]}]
  (let [uri-external? (re-find #"^\w+?://" uri)]
       (not uri-external?)))
