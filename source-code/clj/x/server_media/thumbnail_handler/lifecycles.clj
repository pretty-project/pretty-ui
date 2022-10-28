
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-media.thumbnail-handler.lifecycles
    (:require [x.server-core.api                       :as x.core]
              [x.server-media.thumbnail-handler.routes :as thumbnail-handler.routes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-init [:router/add-route! :media/download-thumbnail
                                       {:route-template "/media/thumbnails/:filename"
                                        :get {:handler thumbnail-handler.routes/download-thumbnail}}]})
