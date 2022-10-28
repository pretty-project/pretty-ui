
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-sync.query-handler.lifecycles
    (:require [x.server-core.api                  :as x.core]
              [x.server-sync.query-handler.routes :as query-handler.routes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-init [:router/add-route! :sync/process-query!
                                       {:route-template "/query"
                                        :post {:handler query-handler.routes/process-query!}}]})
