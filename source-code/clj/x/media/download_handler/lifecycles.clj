
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.media.download-handler.lifecycles
    (:require [x.core.api                      :as x.core]
              [x.media.download-handler.routes :as download-handler.routes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-init [:x.router/add-route! :x.media/download-file
                                         {:route-template "/media/storage/:filename"
                                          :get {:handler download-handler.routes/download-file}}]})
