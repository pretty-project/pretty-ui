
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.core.views
    (:require [developer-tools.core.styles            :as core.styles]
              [developer-tools.docs.views             :as docs.views]
              [developer-tools.mongo-db-browser.views :as mongo-db-browser.views]
              [developer-tools.re-frame-browser.views :as re-frame-browser.views]
              [developer-tools.user-handler.views     :as user-handler.views]
              [mongo-db.api                           :as mongo-db]
              [pretty.print                           :as pretty]
              [re-frame.api                           :as r]
              [string.api                             :as string]
              [x.user.api                             :as x.user]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  [_]
  (let [menu-bar-style  (core.styles/menu-bar-style)
        menu-item-style (core.styles/menu-item-style)]
       (str "<div style=\""menu-bar-style"\">"
            "<a style=\""menu-item-style"\" href=\"/avocado-juice/docs\">Docs</a>"
            "<a style=\""menu-item-style"\" href=\"/avocado-juice/re-frame\">Re-Frame</a>"
            "<a style=\""menu-item-style"\" href=\"/avocado-juice/mongo-db\">MongoDB</a>"
            "<a style=\""menu-item-style"\" href=\"/avocado-juice/users\">Users</a>"
            "</div>")))

(defn- developer-tools
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  ;  {:uri (string)}
  [{:keys [uri] :as request}]
  (case uri "/avocado-juice" ""
            "/avocado-juice/docs"     (docs.views/view             request)
            "/avocado-juice/mongo-db" (mongo-db-browser.views/view request)
            "/avocado-juice/re-frame" (re-frame-browser.views/view request)
            "/avocado-juice/users"    (user-handler.views/view     request)))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) request
  [request]
  (if (or @(r/subscribe [:x.core/dev-mode?])
           (x.user/request->root-user? request))
      (let [body-style (core.styles/body-style)]
           (str "<html>"
                "<body style=\""body-style"\">"
                "<pre style=\"white-space: normal\">"
                (menu-bar        request)
                (developer-tools request)
                "</pre>"
                "</body>"
                "</html>"))
      (str "<html><head></head><body>Permission denied!<br/></body></html>")))
