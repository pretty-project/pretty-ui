
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.media.thumbnail-handler.lifecycles
    (:require [x.core.api                       :as x.core]
              [x.media.thumbnail-handler.routes :as thumbnail-handler.routes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-init [:x.router/add-route! :x.media/download-thumbnail
                                         {:route-template "/media/thumbnails/:filename"
                                          :get {:handler thumbnail-handler.routes/download-thumbnail}}]})
