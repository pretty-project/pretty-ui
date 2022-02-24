
(ns x.server-ui.sample
    (:require [server-fruits.http :as http]
              [x.server-ui.api]))



;; -- HTML, HEAD és BODY elemek használata alapbeállításokkal -----------------
;; ----------------------------------------------------------------------------

(defn my-ui
  [request]
  (ui/html (ui/head request {})
           (ui/body request {})))



;; -- HTML, HEAD és BODY elemek használata egyéni beállításokkal --------------
;; ----------------------------------------------------------------------------

; - Az x.server-ui.api névtér függvényeit útvanalanként eltérő beállításokkal is lehetséges használni
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
 {:on-server-init [:router/add-route! :my-ui
                                      {:route-template "/my-ui"
                                       :get #(http/html-wrap {:body (my-ui %)})}
                                      :your-ui
                                      {:route-template "/your-ui"
                                       :get #(http/html-wrap {:body (your-ui %)})}]})
