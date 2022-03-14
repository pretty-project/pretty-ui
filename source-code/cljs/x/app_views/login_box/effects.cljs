
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
  [:ui/add-popup! :views.login-box/view
                  {:body              #'login-box.views/body
                   :min-width         :xs
                   :render-exclusive? true
                   :user-close?       false}])
