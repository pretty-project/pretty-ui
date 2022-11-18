
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.sample
    (:require [http.api   :as http]
              [x.core.api :as x.core]
              [x.ui.api   :as x.ui]))



;; -- HTML, HEAD és BODY elemek használata alapbeállításokkal -----------------
;; ----------------------------------------------------------------------------

(defn my-ui
  [request]
  (x.ui/html (x.ui/head request {})
             (x.ui/body request {})))



;; -- HTML, HEAD és BODY elemek használata ------------------------------------
;; ----------------------------------------------------------------------------

; Az x.ui.api névtér függvényeit útvanalanként eltérő beállításokkal is lehetséges használni
; Pl.: Az egyes útvonalak kiszolgálása eltérő meta-adatokkal és egyéb beállításokkal történhet
(defn your-ui
  [request]
  (x.ui/html (x.ui/head request {:app-title        "Your title"
                                 :css-paths        [{:uri "/css/your-style.css"}]
                                 :meta-description "Your description"
                                 :meta-keywords    ["My keyword" "Your keyword"]
                                 :og-preview-path  "/your-og-preview.png"})
             (x.ui/body request {:loading-screen   [:div "My loading screen"]
                                 :plugin-js-paths  [{:uri "/js/plugins/your-plugin.js"}]})))



;; -- UI hozzáadása útvonalakhoz ----------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-server-init [:x.router/add-routes! {:my-ui   {:route-template "/my-ui"   :get #(http/html-wrap {:body (my-ui %)})}
                                           :your-ui {:route-template "/your-ui" :get #(http/html-wrap {:body (your-ui %)})}}]})