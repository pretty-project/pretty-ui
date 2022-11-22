
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.user.login-handler.lifecycles
    (:require [x.core.api                  :as x.core]
              [x.user.login-handler.routes :as login-handler.routes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-init [:x.router/add-routes! {:x.user/authenticate {:route-template "/user/authenticate"
                                                                 :post {:handler login-handler.routes/authenticate}}
                                           :x.user/logout       {:route-template "/user/logout"
                                                                 :post {:handler login-handler.routes/logout}}
                                           :x.user/login        {:client-event   [:x.views/render-login-screen!]
                                                                 :js-build       :app
                                                                 :route-template "/login"}}]})
