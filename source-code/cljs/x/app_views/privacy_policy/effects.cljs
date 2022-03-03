
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.privacy-policy.effects
    (:require [x.app-core.api                   :as a]
              [x.app-views.privacy-policy.views :as privacy-policy.views]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :views/render-privacy-policy!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:views/render-error-page! :under-construction])
