
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-router.route-handler.config
    (:require [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (boolean)
(def RELOAD-SAME-PATH? true)

; @constant (map)
(def DEFAULT-ROUTES {:page-not-found {:client-event   [:views/render-error-screen! :page-not-found]
                                      :route-template "/page-not-found"}})

; @constant (map)
(def ACCOUNTANT-CONFIG {:nav-handler  #(r/dispatch   [:router/handle-route!          %])
                        :path-exists? #(r/subscribed [:router/route-template-exists? %])
                        :reload-same-path? false})
