
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.viewport-handler.effects
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :environment/viewport-resized
  ; WARNING! NON-PUBLIC! DO NOT USE!
  {:fx-n [[:environment/detect-viewport-profile!]
          [:environment/update-viewport-data!]]})
