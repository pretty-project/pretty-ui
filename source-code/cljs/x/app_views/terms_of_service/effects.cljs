
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.terms-of-service.effects
    (:require [x.app-core.api :as a]
              [x.app-views.terms-of-service.views :as terms-of-service.views]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :views/render-terms-of-service!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:views/render-error-page! :under-construction])
