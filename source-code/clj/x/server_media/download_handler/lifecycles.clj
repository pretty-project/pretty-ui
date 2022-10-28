
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-media.download-handler.lifecycles
    (:require [x.server-core.api                      :as x.core]
              [x.server-media.download-handler.routes :as download-handler.routes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-init [:router/add-route! :media/download-file
                                       {:route-template "/media/storage/:filename"
                                        :get {:handler download-handler.routes/download-file}}]})
