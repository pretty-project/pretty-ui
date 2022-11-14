
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.transfer-handler.lifecycles
    (:require [x.core.transfer-handler.routes        :as transfer-handler.routes]
              [x.core.lifecycle-handler.side-effects :as lifecycle-handler.side-effects]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(lifecycle-handler.side-effects/reg-lifecycles! ::lifecycles
  {:on-server-init [:x.router/add-route! :x.core/transfer-data
                                         {:route-template "/synchronize-app"
                                          :get {:handler transfer-handler.routes/download-transfer-data}}]})
