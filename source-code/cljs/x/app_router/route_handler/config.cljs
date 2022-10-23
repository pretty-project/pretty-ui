
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-router.route-handler.config
    (:require [clerk.core]
              [reagent.core]
              [re-frame.api :as r]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def DEFAULT-ROUTES {:page-not-found {:client-event   [:views/render-error-screen! :page-not-found]
                                      :route-template "/page-not-found"}})

; @constant (map)
(def ACCOUNTANT-CONFIG {:nav-handler  (fn [route-string] (if-let [swap-mode? @(r/subscribe [:db/get-item [:router :route-handler/meta-items :swap-mode?]])]
                                                                 (do (r/dispatch [:db/remove-item! [:router :route-handler/meta-items :swap-mode?]]))
                                                                 (do (reagent.core/after-render clerk.core/after-render!)
                                                                     (r/dispatch [:router/handle-route! route-string])
                                                                     (clerk.core/navigate-page! route-string))))
                        :path-exists? (fn [route-string] (r/subscribed [:router/route-template-exists? route-string]))
                        :reload-same-path? false})
