

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



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
