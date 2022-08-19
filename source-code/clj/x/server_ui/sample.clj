
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.sample
    (:require [server-fruits.http :as http]
              [x.server-core.api  :as a]
              [x.server-ui.api    :as ui]))



;; -- HTML, HEAD és BODY elemek használata alapbeállításokkal -----------------
;; ----------------------------------------------------------------------------

(defn my-ui
  [request]
  (ui/html (ui/head request {})
           (ui/body request {})))



;; -- HTML, HEAD és BODY elemek használata ------------------------------------
;; ----------------------------------------------------------------------------

; Az x.server-ui.api névtér függvényeit útvanalanként eltérő beállításokkal is lehetséges használni
; Pl. az egyes útvonalak kiszolgálása eltérő meta-adatokkal és egyéb beállításokkal történhet
(defn your-ui
  [request]
  (ui/html (ui/head request {:app-title "Your title"
                             :css-paths [{:uri "/css/your-style.css"}]
                             :meta-description "Your description"
                             :meta-keywords ["My keyword" "Your keyword"]
                             :og-preview-path "/your-og-preview.png"})
           (ui/body request {:plugin-js-paths [{:uri "/js/plugins/your-plugin.js"}]})))



;; -- Útvonal hozzáadása ------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
 ::lifecycles
 {:on-server-init [:router/add-routes! {:my-ui   {:route-template "/my-ui"   :get #(http/html-wrap {:body (my-ui %)})}
                                        :your-ui {:route-template "/your-ui" :get #(http/html-wrap {:body (your-ui %)})}}]})
