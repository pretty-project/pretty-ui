
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.login-box.effects
    (:require [x.app-core.api              :as a]
              [x.app-views.login-box.views :as login-box.views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :views/render-login-box!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/render-surface! :views.login-box/view
                       {:content #'login-box.views/view}])
