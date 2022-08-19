
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-router.route-handler.config
    (:require [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (boolean)
(def RELOAD-SAME-PATH? true)

; @constant (map)
(def DEFAULT-ROUTES {:page-not-found {:client-event   [:views/render-error-page! :page-not-found]
                                      :route-template "/page-not-found"}})

; @constant (map)
(def ACCOUNTANT-CONFIG {:nav-handler  #(a/dispatch   [:router/handle-route!          %])
                        :path-exists? #(a/subscribed [:router/route-template-exists? %])
                        :reload-same-path? false})
