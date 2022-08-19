

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-user.account-handler.lifecycles
    (:require [x.server-core.api                    :as a]
              [x.server-user.account-handler.routes :as account-handler.routes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-init [:router/add-routes! {:user/authenticate {:route-template "/user/authenticate"
                                                             :post {:handler account-handler.routes/authenticate}}
                                         :user/logout       {:route-template "/user/logout"
                                                             :post {:handler account-handler.routes/logout}}}]})
