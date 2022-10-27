
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-views.view-handler.lifecycles
    (:require [x.server-core.api :as core]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(core/reg-lifecycles! ::lifecycles
  {:on-server-init [:router/add-route! :page-not-found {:client-event [:views/render-error-screen! :page-not-found]
                                                        :route-template "/page-not-found"}]})
