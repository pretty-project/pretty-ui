
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns ajax.api
    (:require [ajax.helpers      :as helpers]
              [http.side-effects :as side-effects]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; ajax.helpers
(def progress-event->request-progress helpers/progress-event->request-progress)
(def request->local-request?          helpers/request->local-request?)

; ajax.side-effects
(def send-request!  side-effects/send-request!)
(def abort-request! side-effects/abort-request!)
