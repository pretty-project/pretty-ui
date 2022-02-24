
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-views.login-box.effects
    (:require [x.app-core.api :as a]
              [x.app-views.login-box.views :as login-box.views]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :app-views.login-box/render-box!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [:ui/add-popup! :app-views.login-box/view
                  {:body              #'login-box.views/body
                   :min-width         :xs
                   :render-exclusive? true
                   :user-close?       false}])
