
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.transfer-handler.lifecycles
    (:require [x.server-core.transfer-handler.routes        :as transfer-handler.routes]
              [x.server-core.lifecycle-handler.side-effects :as lifecycle-handler.side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(lifecycle-handler.side-effects/reg-lifecycles!
  ::lifecycles
  {:on-server-init [:router/add-route! :core/transfer-data
                                       {:route-template "/synchronize-app"
                                        :get {:handler transfer-handler.routes/download-transfer-data}}]})
